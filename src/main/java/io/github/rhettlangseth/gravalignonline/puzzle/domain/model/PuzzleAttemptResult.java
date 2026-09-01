package io.github.rhettlangseth.gravalignonline.puzzle.domain.model;

public record PuzzleAttemptResult(
        boolean solved,
        boolean complete,
        boolean rated,
        String board,
        int nextMoveIndex,
        String message,
        int oldPlayerRating,
        int newPlayerRating,
        int oldPuzzleRating,
        int newPuzzleRating
) {

}
