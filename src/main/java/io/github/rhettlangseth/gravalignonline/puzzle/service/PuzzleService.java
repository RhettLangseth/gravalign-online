package io.github.rhettlangseth.gravalignonline.puzzle.service;

import io.github.rhettlangseth.gravalignonline.puzzle.domain.model.NextPuzzle;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.model.PuzzleAttemptResult;

import java.util.UUID;

public interface PuzzleService {

    NextPuzzle getNextPuzzle();

    PuzzleAttemptResult submitAttempt(UUID puzzleId, int column, int moveIndex);

}
