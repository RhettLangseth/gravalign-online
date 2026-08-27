package io.github.rhettlangseth.gravalignonline.puzzle.exception;

import java.util.UUID;

public class PuzzleNotFoundException extends RuntimeException {

  private final UUID id;

  public PuzzleNotFoundException(UUID id) {

    super(String.format("Puzzle with ID '%s' does not exist.", id));
    this.id = id;

  }

  public UUID getId() {

    return id;

  }

}
