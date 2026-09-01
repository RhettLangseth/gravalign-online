package io.github.rhettlangseth.gravalignonline.puzzle.service.impl;

import io.github.rhettlangseth.gravalignonline.game.Board;
import io.github.rhettlangseth.gravalignonline.player.domain.entity.PlayerProfile;
import io.github.rhettlangseth.gravalignonline.player.repository.PlayerProfileRepository;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.entity.Puzzle;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.entity.PuzzleAttempt;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.entity.PuzzleAttemptStatus;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.model.NextPuzzle;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.model.PuzzleAttemptResult;
import io.github.rhettlangseth.gravalignonline.puzzle.exception.PuzzleNotFoundException;
import io.github.rhettlangseth.gravalignonline.puzzle.mapper.PuzzleMapper;
import io.github.rhettlangseth.gravalignonline.puzzle.repository.PuzzleAttemptRepository;
import io.github.rhettlangseth.gravalignonline.puzzle.repository.PuzzleRepository;
import io.github.rhettlangseth.gravalignonline.puzzle.service.PuzzleService;
import io.github.rhettlangseth.gravalignonline.rating.RatingCalculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PuzzleServiceImpl implements PuzzleService {

    private static final UUID DEMO_PLAYER_ID = UUID.fromString("00000000-0000-0000-0000-000000000101");

    private final PlayerProfileRepository playerProfileRepository;
    private final PuzzleRepository puzzleRepository;
    private final PuzzleAttemptRepository puzzleAttemptRepository;
    private final PuzzleMapper puzzleMapper;

    public PuzzleServiceImpl(
            PlayerProfileRepository playerProfileRepository,
            PuzzleRepository puzzleRepository,
            PuzzleAttemptRepository puzzleAttemptRepository,
            PuzzleMapper puzzleMapper) {

        this.playerProfileRepository = playerProfileRepository;
        this.puzzleRepository = puzzleRepository;
        this.puzzleAttemptRepository = puzzleAttemptRepository;
        this.puzzleMapper = puzzleMapper;

    }

    @Override
    public NextPuzzle getNextPuzzle() {

        PlayerProfile playerProfile = playerProfileRepository.findById(DEMO_PLAYER_ID)
                .orElseThrow(() -> new RuntimeException("Demo player profile not found."));

        List<Puzzle> puzzles = puzzleRepository.findUnattemptedByPlayerProfileId(playerProfile.getId());

        if (puzzles.isEmpty()) {

            return null;

        }

        Puzzle puzzle = puzzles.getFirst();

        return puzzleMapper.toNextPuzzle(puzzle, playerProfile);

    }

    @Override
    @Transactional
    public PuzzleAttemptResult submitAttempt(UUID puzzleId, int column, int moveIndex) {

        Puzzle puzzle = puzzleRepository.findById(puzzleId).orElseThrow(() -> new PuzzleNotFoundException(puzzleId));
        PlayerProfile playerProfile = playerProfileRepository.findLockedById(DEMO_PLAYER_ID)
                .orElseThrow(() -> new RuntimeException("Demo player profile not found."));
        Optional<PuzzleAttempt> existingAttempt = puzzleAttemptRepository.findByPlayerProfileIdAndPuzzleId(
                playerProfile.getId(),
                puzzle.getId()
        );
        boolean isFirstAttempt = existingAttempt.isEmpty();
        boolean isInProgressAttempt = existingAttempt.isPresent()
                && existingAttempt.get().getStatus() == PuzzleAttemptStatus.IN_PROGRESS;
        boolean isRatedAttempt = isFirstAttempt || isInProgressAttempt;
        int nextMoveIndex = moveIndex;

        if (isRatedAttempt) {

            nextMoveIndex = 0;

        }

        if (isInProgressAttempt) {

            nextMoveIndex = existingAttempt.get().getNextMoveIndex();

        }

        if (!isValidUserMoveIndex(puzzle, nextMoveIndex)) {

            Board board = new Board(puzzle.getBoard(), puzzle.getPlayerToMove());

            return new PuzzleAttemptResult(
                    false,
                    false,
                    false,
                    board.toPositionString(),
                    0,
                    "Could not continue this puzzle. Please try again.",
                    playerProfile.getRating(),
                    playerProfile.getRating(),
                    puzzle.getRating(),
                    puzzle.getRating()
            );

        }

        Board board = buildBoardAtMoveIndex(puzzle, nextMoveIndex);

        if (!board.makeMove(column)) {

            return new PuzzleAttemptResult(
                    false,
                    false,
                    false,
                    board.toPositionString(),
                    nextMoveIndex,
                    "That move is not legal.",
                    playerProfile.getRating(),
                    playerProfile.getRating(),
                    puzzle.getRating(),
                    puzzle.getRating()
            );

        }

        int correctColumn = Character.getNumericValue(puzzle.getSolutionColumns().charAt(nextMoveIndex));
        boolean correctMove = column == correctColumn;
        int oldPlayerRating = playerProfile.getRating();
        int oldPuzzleRating = puzzle.getRating();

        if (correctMove) {

            nextMoveIndex = advanceAfterCorrectUserMove(board, puzzle, nextMoveIndex);

        }

        boolean solved = correctMove && nextMoveIndex == puzzle.getSolutionColumns().length();
        boolean complete = !correctMove || solved;
        int newPlayerRating;
        int newPuzzleRating;

        newPlayerRating = oldPlayerRating;
        newPuzzleRating = oldPuzzleRating;

        if (isRatedAttempt && complete) {

            newPlayerRating = RatingCalculator.calculateNewRating(solved, oldPlayerRating, oldPuzzleRating);
            newPuzzleRating = RatingCalculator.calculateNewRating(!solved, oldPuzzleRating, oldPlayerRating);

            playerProfile.updateRating(newPlayerRating);
            puzzle.updateRating(newPuzzleRating);

        }

        PuzzleAttemptStatus attemptStatus = PuzzleAttemptStatus.IN_PROGRESS;

        if (complete) {

            attemptStatus = solved ? PuzzleAttemptStatus.SOLVED : PuzzleAttemptStatus.FAILED;

        }

        if (isFirstAttempt) {

            int attemptNumber = (int)puzzleAttemptRepository.countByPlayerProfileId(playerProfile.getId());

            puzzleAttemptRepository.save(
                    new PuzzleAttempt(
                            UUID.randomUUID(),
                            playerProfile.getId(),
                            puzzle.getId(),
                            attemptStatus,
                            nextMoveIndex,
                            attemptNumber,
                            complete ? newPlayerRating : null,
                            complete ? newPuzzleRating : null
                    )
            );

        } else if (isInProgressAttempt) {

            PuzzleAttempt attempt = existingAttempt.get();

            if (complete) {

                if (solved) {

                    attempt.markSolved(newPlayerRating, newPuzzleRating);

                } else {

                    attempt.markFailed(newPlayerRating, newPuzzleRating);

                }

            } else {

                attempt.advanceToMoveIndex(nextMoveIndex);

            }

        }

        return new PuzzleAttemptResult(
                solved,
                complete,
                isRatedAttempt,
                board.toPositionString(),
                nextMoveIndex,
                "Column " + column + (correctMove
                        ? " is correct!" + (complete ? "" : " Choose the next column.")
                        : " is incorrect."),
                oldPlayerRating,
                newPlayerRating,
                oldPuzzleRating,
                newPuzzleRating
        );

    }

    private Board buildBoardAtMoveIndex(Puzzle puzzle, int nextMoveIndex) {

        Board board = new Board(puzzle.getBoard(), puzzle.getPlayerToMove());

        for (int moveIndex = 0; moveIndex < nextMoveIndex; moveIndex++) {

            int solutionColumn = Character.getNumericValue(puzzle.getSolutionColumns().charAt(moveIndex));
            board.makeMove(solutionColumn);

        }

        return board;

    }

    private int advanceAfterCorrectUserMove(Board board, Puzzle puzzle, int nextMoveIndex) {

        nextMoveIndex++;

        if (nextMoveIndex < puzzle.getSolutionColumns().length()) {

            int opponentReplyColumn = Character.getNumericValue(puzzle.getSolutionColumns().charAt(nextMoveIndex));
            board.makeMove(opponentReplyColumn);
            nextMoveIndex++;

        }

        return nextMoveIndex;

    }

    private boolean isValidUserMoveIndex(Puzzle puzzle, int moveIndex) {

        return moveIndex >= 0
                && moveIndex < puzzle.getSolutionColumns().length()
                && moveIndex % 2 == 0;

    }

}
