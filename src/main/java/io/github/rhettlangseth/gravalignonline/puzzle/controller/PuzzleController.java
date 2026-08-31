package io.github.rhettlangseth.gravalignonline.puzzle.controller;

import io.github.rhettlangseth.gravalignonline.puzzle.domain.dto.NextPuzzleResponseDto;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.dto.PuzzleAttemptRequestDto;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.dto.PuzzleAttemptResultDto;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.model.NextPuzzle;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.model.PuzzleAttemptResult;
import io.github.rhettlangseth.gravalignonline.puzzle.mapper.PuzzleMapper;
import io.github.rhettlangseth.gravalignonline.puzzle.service.PuzzleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/puzzles")
public class PuzzleController {

    private final PuzzleService puzzleService;
    private final PuzzleMapper puzzleMapper;

    public PuzzleController(PuzzleService puzzleService, PuzzleMapper puzzleMapper) {

        this.puzzleService = puzzleService;
        this.puzzleMapper = puzzleMapper;

    }

    @GetMapping(path = "/next")
    public ResponseEntity<NextPuzzleResponseDto> getNextPuzzle() {

        NextPuzzle nextPuzzle = puzzleService.getNextPuzzle();

        if (nextPuzzle == null) {

            return ResponseEntity.noContent().build();

        }

        NextPuzzleResponseDto nextPuzzleResponseDto = puzzleMapper.toNextPuzzleResponseDto(nextPuzzle);

        return ResponseEntity.ok(nextPuzzleResponseDto);

    }

    @PostMapping(path = "/{puzzleId}/attempts")
    public ResponseEntity<PuzzleAttemptResultDto> submitAttempt(
            @PathVariable UUID puzzleId,
            @Valid @RequestBody PuzzleAttemptRequestDto puzzleAttemptRequestDto
    ) {

        PuzzleAttemptResult puzzleAttemptResult = puzzleService.submitAttempt(puzzleId, puzzleAttemptRequestDto.column());
        PuzzleAttemptResultDto puzzleAttemptResultDto = puzzleMapper.toPuzzleAttemptResultDto(puzzleAttemptResult);

        return ResponseEntity.ok(puzzleAttemptResultDto);

    }

}
