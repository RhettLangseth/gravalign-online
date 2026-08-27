package io.github.rhettlangseth.gravalignonline.puzzle.service.impl;

import io.github.rhettlangseth.gravalignonline.puzzle.domain.entity.Puzzle;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.model.PuzzleAttemptResult;
import io.github.rhettlangseth.gravalignonline.puzzle.service.PuzzleService;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class PuzzleServiceImpl implements PuzzleService {

    public PuzzleServiceImpl() {

    }

    @Override
    public Puzzle getNextPuzzle() {

        return new Puzzle(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                "000000000000120000000000120000000000000000",
                1,
                3,
                900);

    }

    private int calculateNewRating(boolean result, int rating, int otherRating) {

        return result ? rating + 100 : rating - 100;

    }

    @Override
    public PuzzleAttemptResult submitAttempt(UUID puzzleId, int column) {

        Puzzle puzzle = getNextPuzzle();
        boolean solved = column == puzzle.getCorrectColumn();
        return new PuzzleAttemptResult(
                solved,
                puzzleId + (solved ? " is correct!" : " is incorrect."),
                1200,
                calculateNewRating(solved, 1200, puzzle.getRating()),
                puzzle.getRating(),
                calculateNewRating(!solved, puzzle.getRating(), 1200)
        );

    }

}
