package io.github.rhettlangseth.gravalignonline.player.config;

import io.github.rhettlangseth.gravalignonline.player.domain.entity.PlayerProfile;
import io.github.rhettlangseth.gravalignonline.player.repository.PlayerProfileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PlayerProfileDataLoader implements CommandLineRunner {

    private final PlayerProfileRepository playerProfileRepository;

    public PlayerProfileDataLoader(PlayerProfileRepository playerProfileRepository) {

        this.playerProfileRepository = playerProfileRepository;

    }

    @Override
    public void run(String... args) {

        if (playerProfileRepository.count() > 0) {
            return;
        }

        playerProfileRepository.save(
                new PlayerProfile(
                        UUID.fromString("00000000-0000-0000-0000-000000000101"),
                        "Demo Player",
                        1200
                )
        );

    }

}
