package io.github.rhettlangseth.gravalignonline.player.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "player_profiles")
public class PlayerProfile {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "rating", nullable = false)
    private int rating;

    protected PlayerProfile() {

    }

    public PlayerProfile(UUID id, String displayName, int rating) {
        this.id = id;
        this.displayName = displayName;
        this.rating = rating;
    }

    public void updateRating(int rating) {

        this.rating = rating;

    }

    public UUID getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getRating() {
        return rating;
    }

}
