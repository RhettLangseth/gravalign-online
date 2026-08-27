package io.github.rhettlangseth.gravalignonline.puzzle.domain.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PuzzleAttemptRequestDto(
        @NotNull
        @Min(1)
        @Max(7)
        Integer column
) {

}
