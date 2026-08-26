package io.github.rhettlangseth.gravalignonline.puzzle.domain.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PuzzleAttemptRequestDto(
        @NotNull
        @Min(0)
        @Max(6)
        Integer column
) {

}
