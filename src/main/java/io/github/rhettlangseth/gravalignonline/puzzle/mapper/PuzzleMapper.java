package io.github.rhettlangseth.gravalignonline.puzzle.mapper;

import io.github.rhettlangseth.gravalignonline.puzzle.domain.dto.NextPuzzleResponseDto;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.dto.PuzzleAttemptResultDto;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.model.NextPuzzle;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.model.PuzzleAttemptResult;

public interface PuzzleMapper {

    NextPuzzleResponseDto toNextPuzzleResponseDto(NextPuzzle nextPuzzle);

    PuzzleAttemptResultDto toPuzzleAttemptResultDto(PuzzleAttemptResult puzzleAttemptResult);

}
