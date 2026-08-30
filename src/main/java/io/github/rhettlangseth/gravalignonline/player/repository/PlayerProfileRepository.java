package io.github.rhettlangseth.gravalignonline.player.repository;

import io.github.rhettlangseth.gravalignonline.player.domain.entity.PlayerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PlayerProfileRepository extends JpaRepository<PlayerProfile, UUID> {



}
