package io.github.rhettlangseth.gravalignonline.puzzle.service.impl;

import io.github.rhettlangseth.gravalignonline.game.Board;
import io.github.rhettlangseth.gravalignonline.player.domain.entity.PlayerProfile;
import io.github.rhettlangseth.gravalignonline.player.repository.PlayerProfileRepository;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.entity.Puzzle;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.model.NextPuzzle;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.model.PuzzleAttemptResult;
import io.github.rhettlangseth.gravalignonline.puzzle.exception.PuzzleNotFoundException;
import io.github.rhettlangseth.gravalignonline.puzzle.mapper.PuzzleMapper;
import io.github.rhettlangseth.gravalignonline.puzzle.repository.PuzzleRepository;
import io.github.rhettlangseth.gravalignonline.puzzle.service.PuzzleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PuzzleServiceImpl implements PuzzleService {

    private static final UUID DEMO_PLAYER_ID = UUID.fromString("00000000-0000-0000-0000-000000000101");

    private final PlayerProfileRepository playerProfileRepository;
    private final PuzzleRepository puzzleRepository;
    private final PuzzleMapper puzzleMapper;

    public PuzzleServiceImpl(
            PlayerProfileRepository playerProfileRepository,
            PuzzleRepository puzzleRepository,
            PuzzleMapper puzzleMapper) {

        this.playerProfileRepository = playerProfileRepository;
        this.puzzleRepository = puzzleRepository;
        this.puzzleMapper = puzzleMapper;

    }

    @Override
    public NextPuzzle getNextPuzzle() {

        List<Puzzle> puzzles = puzzleRepository.findAll();

        if (puzzles.isEmpty()) {
            throw new RuntimeException("No puzzles found.");
        }

        Puzzle puzzle = puzzles.getFirst();
        PlayerProfile playerProfile = playerProfileRepository.findById(DEMO_PLAYER_ID)
                .orElseThrow(() -> new RuntimeException("Demo player profile not found."));

        return puzzleMapper.toNextPuzzle(puzzle, playerProfile);

    }

    private int calculateNewRating(boolean result, int rating, int otherRating) {

        return result ? rating + 100 : rating - 100;

    }

    @Override
    public PuzzleAttemptResult submitAttempt(UUID puzzleId, int column) {

        Puzzle puzzle = puzzleRepository.findById(puzzleId).orElseThrow(() -> new PuzzleNotFoundException(puzzleId));
        PlayerProfile playerProfile = playerProfileRepository.findById(DEMO_PLAYER_ID)
                .orElseThrow(() -> new RuntimeException("Demo player profile not found."));
        Board board = new Board(puzzle.getBoard(), puzzle.getPlayerToMove());

        if (!board.makeMove(column)) {

            return new PuzzleAttemptResult(
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

        return new PuzzleAttemptResult(
                solved,
                board.toPositionString(),
                "Column " + column + (solved ? " is correct!" : " is incorrect."),
                playerProfile.getRating(),
                calculateNewRating(solved, playerProfile.getRating(), puzzle.getRating()),
                puzzle.getRating(),
                calculateNewRating(!solved, puzzle.getRating(), playerProfile.getRating())
        );

    }

}
