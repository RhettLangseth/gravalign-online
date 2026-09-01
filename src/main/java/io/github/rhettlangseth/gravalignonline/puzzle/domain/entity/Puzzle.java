package io.github.rhettlangseth.gravalignonline.puzzle.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "puzzles")
public class Puzzle {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "board", nullable = false)
    private String board;

    @Column(name = "player_to_move", nullable = false)
    private int playerToMove;

    @Column(name = "solution_columns", nullable = false)
    private String solutionColumns;

    @Column(name = "rating", nullable = false)
    private int rating;

    protected Puzzle() {

    }

    public Puzzle(
            UUID id,
            String board,
            int playerToMove,
            String solutionColumns,
            int rating
    ) {

        this.id = id;
        this.board = board;
        this.playerToMove = playerToMove;
        this.solutionColumns = solutionColumns;
        this.rating = rating;

    }

    public void updateRating(int rating) {

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

    public String getSolutionColumns() {

        return solutionColumns;

    }

    public int getRating() {

        return rating;

    }

}
