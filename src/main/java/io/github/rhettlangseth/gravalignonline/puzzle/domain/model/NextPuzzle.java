package io.github.rhettlangseth.gravalignonline.puzzle.domain.model;

import io.github.rhettlangseth.gravalignonline.puzzle.domain.entity.Puzzle;

public record NextPuzzle(
        Puzzle puzzle,
        int playerRating
) {

}
