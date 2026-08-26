package io.github.rhettlangseth.gravalignonline.puzzle.domain.entity;

import java.util.UUID;

public class Puzzle {

    private UUID id;
    private String board;
    private int playerToMove;
    private int correctColumn;
    private int rating;

    public Puzzle(
            UUID id,
            String board,
            int playerToMove,
            int correctColumn,
            int rating
    ) {

        this.id = id;
        this.board = board;
        this.playerToMove = playerToMove;
        this.correctColumn = correctColumn;
        this.rating = rating;

    }

    public UUID getId() {
        return id;
    }

    public String getBoard() {
        return board;
    }

    public int getPlayerToMove() {
        return playerToMove;
    }

    public int getCorrectColumn() {
        return correctColumn;
    }

    public int getRating() {
        return rating;
    }

}
