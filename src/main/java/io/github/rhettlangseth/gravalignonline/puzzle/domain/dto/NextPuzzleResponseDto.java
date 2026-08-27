package io.github.rhettlangseth.gravalignonline.puzzle.domain.dto;

import java.util.UUID;

public record NextPuzzleResponseDto(
        UUID puzzleId,
        String board,
        int playerToMove,
        int playerRating
) {

}
