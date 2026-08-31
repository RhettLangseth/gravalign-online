package io.github.rhettlangseth.gravalignonline.player.repository;

import io.github.rhettlangseth.gravalignonline.player.domain.entity.PlayerProfile;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;
import java.util.UUID;

public interface PlayerProfileRepository extends JpaRepository<PlayerProfile, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<PlayerProfile> findLockedById(UUID id);

}
