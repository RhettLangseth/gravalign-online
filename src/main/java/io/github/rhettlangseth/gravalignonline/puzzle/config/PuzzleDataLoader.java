package io.github.rhettlangseth.gravalignonline.puzzle.config;

import io.github.rhettlangseth.gravalignonline.puzzle.domain.entity.Puzzle;
import io.github.rhettlangseth.gravalignonline.puzzle.repository.PuzzleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.jspecify.annotations.NonNull;
import java.util.UUID;

@Component
public class PuzzleDataLoader implements CommandLineRunner {

    private final PuzzleRepository puzzleRepository;

    public PuzzleDataLoader(PuzzleRepository puzzleRepository) {

        this.puzzleRepository = puzzleRepository;

    }

    @Override
    public void run(String @NonNull ... args) {

        if (puzzleRepository.count() > 0) {
            return;
        }

        puzzleRepository.save(
                new Puzzle(
                        UUID.fromString("00000000-0000-0000-0000-000000000001"),
                        "000000000000212000121000212100000000000000",
                        1,
                        2,
                        700
                )
        );

        puzzleRepository.save(
                new Puzzle(
                        UUID.fromString("00000000-0000-0000-0000-000000000002"),
                        "000000000000120000000000120000000000000000",
                        1,
                        4,
                        900
                )
        );

        puzzleRepository.save(
                new Puzzle(
                        UUID.fromString("00000000-0000-0000-0000-000000000003"),
                        "000000000000212000121200112000000000000000",
                        1,
                        6,
                        900
                )
        );

        puzzleRepository.save(
                new Puzzle(
                        UUID.fromString("00000000-0000-0000-0000-000000000004"),
                        "220000000000212000111200100000000000000000",
                        1,
                        6,
                        1200
                )
        );

        puzzleRepository.save(
                new Puzzle(
                        UUID.fromString("00000000-0000-0000-0000-000000000005"),
                        "000000112120222112121212110000212000000000",
                        1,
                        5,
                        1800
                )
        );

    }

}
