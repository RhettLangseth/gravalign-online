package io.github.rhettlangseth.gravalignonline.puzzle.domain.dto;

public record PuzzleAttemptResultDto(
        boolean solved,
        String message,
        int oldPlayerRating,
        int newPlayerRating,
        int oldPuzzleRating,
        int newPuzzleRating
) {

}
