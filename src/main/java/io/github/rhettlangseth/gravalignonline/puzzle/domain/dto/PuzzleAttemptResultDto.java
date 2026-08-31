package io.github.rhettlangseth.gravalignonline.puzzle.domain.dto;

public record PuzzleAttemptResultDto(
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
