package io.github.rhettlangseth.gravalignonline.puzzle.domain.entity;

import jakarta.persistence.*;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PuzzleAttemptStatus status;

    @Column(name = "next_move_index", nullable = false)
    private int nextMoveIndex;

    @Column(name = "attempt_number", nullable = false, updatable = false)
    private int attemptNumber;

    @Column(name = "new_player_rating")
    private Integer newPlayerRating;

    @Column(name = "new_puzzle_rating")
    private Integer newPuzzleRating;

    protected PuzzleAttempt() {

    }

    public PuzzleAttempt(
            UUID id,
            UUID playerProfileId,
            UUID puzzleId,
            PuzzleAttemptStatus status,
            int nextMoveIndex,
            int attemptNumber,
            Integer newPlayerRating,
            Integer newPuzzleRating
    ) {

        this.id = id;
        this.playerProfileId = playerProfileId;
        this.puzzleId = puzzleId;
        this.status = status;
        this.nextMoveIndex = nextMoveIndex;
        this.attemptNumber = attemptNumber;
        this.newPlayerRating = newPlayerRating;
        this.newPuzzleRating = newPuzzleRating;

    }

    public void advanceToMoveIndex(int nextMoveIndex) {

        this.nextMoveIndex = nextMoveIndex;

    }

    public void markSolved(int newPlayerRating, int newPuzzleRating) {

        this.status = PuzzleAttemptStatus.SOLVED;
        this.newPlayerRating = newPlayerRating;
        this.newPuzzleRating = newPuzzleRating;

    }

    public void markFailed(int newPlayerRating, int newPuzzleRating) {

        this.status = PuzzleAttemptStatus.FAILED;
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

    public PuzzleAttemptStatus getStatus() {

        return status;

    }

    public boolean isSolved() {

        return status == PuzzleAttemptStatus.SOLVED;

    }

    public int getNextMoveIndex() {

        return nextMoveIndex;

    }

    public int getAttemptNumber() {

        return attemptNumber;

    }

    public Integer getNewPlayerRating() {

        return newPlayerRating;

    }

    public Integer getNewPuzzleRating() {

        return newPuzzleRating;

    }

}
