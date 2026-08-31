package io.github.rhettlangseth.gravalignonline.puzzle.domain.model;

public record PuzzleAttemptResult(
        boolean solved,
        boolean rated,
        String board,
        String message,
        int oldPlayerRating,
        int newPlayerRating,
        int oldPuzzleRating,
        int newPuzzleRating
) {

}
