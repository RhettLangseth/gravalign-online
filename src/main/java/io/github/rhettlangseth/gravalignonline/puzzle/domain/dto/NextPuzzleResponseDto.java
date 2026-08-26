package io.github.rhettlangseth.gravalignonline.puzzle.domain.dto;

import java.util.UUID;

public record NextPuzzleResponseDto(
        UUID id,
        String board,
        int playerToMove
) {

}
