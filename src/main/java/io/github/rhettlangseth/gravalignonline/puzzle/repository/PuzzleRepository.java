package io.github.rhettlangseth.gravalignonline.puzzle.repository;

import io.github.rhettlangseth.gravalignonline.puzzle.domain.entity.Puzzle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PuzzleRepository extends JpaRepository<Puzzle, UUID> {

}
