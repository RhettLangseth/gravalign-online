package io.github.rhettlangseth.gravalignonline.puzzle.service.impl;

import io.github.rhettlangseth.gravalignonline.game.Board;
import io.github.rhettlangseth.gravalignonline.player.domain.entity.PlayerProfile;
import io.github.rhettlangseth.gravalignonline.player.repository.PlayerProfileRepository;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.entity.Puzzle;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.entity.PuzzleAttempt;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.model.NextPuzzle;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.model.PuzzleAttemptResult;
import io.github.rhettlangseth.gravalignonline.puzzle.exception.PuzzleNotFoundException;
import io.github.rhettlangseth.gravalignonline.puzzle.mapper.PuzzleMapper;
import io.github.rhettlangseth.gravalignonline.puzzle.repository.PuzzleAttemptRepository;
import io.github.rhettlangseth.gravalignonline.puzzle.repository.PuzzleRepository;
import io.github.rhettlangseth.gravalignonline.puzzle.service.PuzzleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PuzzleServiceImpl implements PuzzleService {

    private static final UUID DEMO_PLAYER_ID = UUID.fromString("00000000-0000-0000-0000-000000000101");
    private static final int ASSUMED_RATING_GAME_COUNT = 50;
    private static final int RATING_FLOOR = 100;

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

    private int calculateNewRating(boolean result, int rating, int otherRating) {

        double actualScore = result ? 1.0 : 0.0;
        double expectedScore = calculateExpectedScore(rating, otherRating);
        double kFactor = calculateKFactor(rating);
        int ratingChange = (int) Math.round(kFactor * (actualScore - expectedScore));

        return Math.max(RATING_FLOOR, rating + ratingChange);

    }

    private double calculateExpectedScore(int rating, int otherRating) {

        return 1.0 / (1.0 + Math.pow(10.0, (otherRating - rating) / 400.0));

    }

    private double calculateKFactor(int rating) {

        double ratingBasedGameCap;

        if (rating <= 2355) {
            ratingBasedGameCap = 50.0 / Math.sqrt(0.662 + 0.00000739 * Math.pow(2569.0 - rating, 2.0));
        } else {
            ratingBasedGameCap = 50.0;
        }

        double effectiveGameCount = Math.min(ASSUMED_RATING_GAME_COUNT, ratingBasedGameCap);

        return 800.0 / (effectiveGameCount + 1.0);

    }

    @Override
    @Transactional
    public PuzzleAttemptResult submitAttempt(UUID puzzleId, int column) {

        Puzzle puzzle = puzzleRepository.findById(puzzleId).orElseThrow(() -> new PuzzleNotFoundException(puzzleId));
        PlayerProfile playerProfile = playerProfileRepository.findLockedById(DEMO_PLAYER_ID)
                .orElseThrow(() -> new RuntimeException("Demo player profile not found."));
        boolean isFirstAttempt = !puzzleAttemptRepository.existsByPlayerProfileIdAndPuzzleId(
                playerProfile.getId(),
                puzzle.getId()
        );
        Board board = new Board(puzzle.getBoard(), puzzle.getPlayerToMove());

        if (!board.makeMove(column)) {

            return new PuzzleAttemptResult(
                    false,
                    false,
                    board.toPositionString(),
                    "That move is not legal.",
                    playerProfile.getRating(),
                    playerProfile.getRating(),
                    puzzle.getRating(),
                    puzzle.getRating()
            );

        }

        boolean solved = column == puzzle.getCorrectColumn();
        int oldPlayerRating = playerProfile.getRating();
        int oldPuzzleRating = puzzle.getRating();
        int newPlayerRating;
        int newPuzzleRating;

        if (isFirstAttempt) {

            int attemptNumber = (int)puzzleAttemptRepository.countByPlayerProfileId(playerProfile.getId());

            newPlayerRating = calculateNewRating(solved, oldPlayerRating, oldPuzzleRating);
            newPuzzleRating = calculateNewRating(!solved, oldPuzzleRating, oldPlayerRating);

            playerProfile.updateRating(newPlayerRating);
            puzzle.updateRating(newPuzzleRating);

            puzzleAttemptRepository.save(
                    new PuzzleAttempt(
                            UUID.randomUUID(),
                            playerProfile.getId(),
                            puzzle.getId(),
                            solved,
                            attemptNumber,
                            newPlayerRating,
                            newPuzzleRating
                    )
            );

        } else {

            newPlayerRating = oldPlayerRating;
            newPuzzleRating = oldPuzzleRating;

        }

        return new PuzzleAttemptResult(
                solved,
                isFirstAttempt,
                board.toPositionString(),
                "Column " + column + (solved ? " is correct!" : " is incorrect."),
                oldPlayerRating,
                newPlayerRating,
                oldPuzzleRating,
                newPuzzleRating
        );

    }

}
