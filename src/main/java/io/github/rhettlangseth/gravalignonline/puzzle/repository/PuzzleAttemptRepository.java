package io.github.rhettlangseth.gravalignonline.puzzle.repository;

import io.github.rhettlangseth.gravalignonline.puzzle.domain.entity.PuzzleAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PuzzleAttemptRepository extends JpaRepository<PuzzleAttempt, UUID> {

    Optional<PuzzleAttempt> findByPlayerProfileIdAndPuzzleId(UUID playerProfileId, UUID puzzleId);

    long countByPlayerProfileId(UUID playerProfileId);

}
