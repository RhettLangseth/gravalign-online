package io.github.rhettlangseth.gravalignonline.puzzle.config;

import io.github.rhettlangseth.gravalignonline.puzzle.domain.entity.Puzzle;
import io.github.rhettlangseth.gravalignonline.puzzle.repository.PuzzleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PuzzleDataLoader implements CommandLineRunner {

    private final PuzzleRepository puzzleRepository;

    public PuzzleDataLoader(PuzzleRepository puzzleRepository) {

        this.puzzleRepository = puzzleRepository;

    }

    @Override
    public void run(String... args) {

        if (puzzleRepository.count() > 0) {
            return;
        }

        puzzleRepository.save(
                new Puzzle(
                        UUID.fromString("00000000-0000-0000-0000-000000000001"),
                        "000000000000120000000000120000000000000000",
                        1,
                        4,
                        900
                )
        );

    }

}
