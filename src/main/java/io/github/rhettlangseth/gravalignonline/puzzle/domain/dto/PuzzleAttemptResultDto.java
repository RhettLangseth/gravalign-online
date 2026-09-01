package io.github.rhettlangseth.gravalignonline.puzzle.domain.dto;

public record PuzzleAttemptResultDto(
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
