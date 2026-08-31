package io.github.rhettlangseth.gravalignonline.puzzle.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.UUID;

@Entity
@Table(
        name = "puzzle_attempts",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_puzzle_attempt_player_profile_puzzle",
                columnNames = {"player_profile_id", "puzzle_id"}
        )
)
public class PuzzleAttempt {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "player_profile_id", nullable = false, updatable = false)
    private UUID playerProfileId;

    @Column(name = "puzzle_id", nullable = false, updatable = false)
    private UUID puzzleId;

    @Column(name = "solved", nullable = false, updatable = false)
    private boolean solved;

    @Column(name = "attempt_number", nullable = false, updatable = false)
    private int attemptNumber;

    @Column(name = "new_player_rating", nullable = false, updatable = false)
    private int newPlayerRating;

    @Column(name = "new_puzzle_rating", nullable = false, updatable = false)
    private int newPuzzleRating;

    protected PuzzleAttempt() {

    }

    public PuzzleAttempt(
            UUID id,
            UUID playerProfileId,
            UUID puzzleId,
            boolean solved,
            int attemptNumber,
            int newPlayerRating,
            int newPuzzleRating
    ) {

        this.id = id;
        this.playerProfileId = playerProfileId;
        this.puzzleId = puzzleId;
        this.solved = solved;
        this.attemptNumber = attemptNumber;
        this.newPlayerRating = newPlayerRating;
        this.newPuzzleRating = newPuzzleRating;

    }

    public UUID getId() {

        return id;

    }

    public UUID getPlayerProfileId() {

        return playerProfileId;

    }

    public UUID getPuzzleId() {

        return puzzleId;

    }

    public boolean isSolved() {

        return solved;

    }

    public int getAttemptNumber() {

        return attemptNumber;

    }

    public int getNewPlayerRating() {

        return newPlayerRating;

    }

    public int getNewPuzzleRating() {

        return newPuzzleRating;

    }

}
