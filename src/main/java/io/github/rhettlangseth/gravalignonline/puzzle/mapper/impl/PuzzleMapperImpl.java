package io.github.rhettlangseth.gravalignonline.puzzle.mapper.impl;

import io.github.rhettlangseth.gravalignonline.player.domain.entity.PlayerProfile;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.dto.NextPuzzleResponseDto;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.dto.PuzzleAttemptResultDto;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.entity.Puzzle;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.model.NextPuzzle;
import io.github.rhettlangseth.gravalignonline.puzzle.domain.model.PuzzleAttemptResult;
import io.github.rhettlangseth.gravalignonline.puzzle.mapper.PuzzleMapper;
import org.springframework.stereotype.Component;

@Component
public class PuzzleMapperImpl implements PuzzleMapper {

    @Override
    public NextPuzzleResponseDto toNextPuzzleResponseDto(NextPuzzle nextPuzzle) {

        return new NextPuzzleResponseDto(
                nextPuzzle.puzzle().getId(),
                nextPuzzle.puzzle().getBoard(),
                nextPuzzle.puzzle().getPlayerToMove(),
                nextPuzzle.playerRating()
        );

    }

    @Override
    public PuzzleAttemptResultDto toPuzzleAttemptResultDto(PuzzleAttemptResult puzzleAttemptResult) {

        return new PuzzleAttemptResultDto(
                puzzleAttemptResult.solved(),
                puzzleAttemptResult.rated(),
                puzzleAttemptResult.board(),
                puzzleAttemptResult.message(),
                puzzleAttemptResult.oldPlayerRating(),
                puzzleAttemptResult.newPlayerRating(),
                puzzleAttemptResult.oldPuzzleRating(),
                puzzleAttemptResult.newPuzzleRating()
        );

    }

    @Override
    public NextPuzzle toNextPuzzle(Puzzle puzzle, PlayerProfile playerProfile) {

        return new NextPuzzle(
                puzzle,
                playerProfile.getRating()
        );

    }

}
