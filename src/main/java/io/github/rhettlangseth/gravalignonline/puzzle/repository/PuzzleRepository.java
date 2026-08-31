package io.github.rhettlangseth.gravalignonline.puzzle.repository;

import io.github.rhettlangseth.gravalignonline.puzzle.domain.entity.Puzzle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PuzzleRepository extends JpaRepository<Puzzle, UUID> {

    @Query("""
            select puzzle
            from Puzzle puzzle
            where puzzle.id not in (
                select puzzleAttempt.puzzleId
                from PuzzleAttempt puzzleAttempt
                where puzzleAttempt.playerProfileId = :playerProfileId
            )
            order by puzzle.rating
            """)

    List<Puzzle> findUnattemptedByPlayerProfileId(@Param("playerProfileId") UUID playerProfileId);

}
