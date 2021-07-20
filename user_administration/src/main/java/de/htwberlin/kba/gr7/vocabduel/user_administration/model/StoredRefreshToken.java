package de.htwberlin.kba.gr7.vocabduel.user_administration.model;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class StoredRefreshToken implements Serializable {
    @ManyToOne(optional = false)
    private User user;

    @Version
    private Integer version;

    @Id
    private String refreshToken;

    public StoredRefreshToken() {
    }

    public StoredRefreshToken(final User user, final String refreshToken) {
        this.user = user;
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Integer getVersion() {
        return version;
    }
}
