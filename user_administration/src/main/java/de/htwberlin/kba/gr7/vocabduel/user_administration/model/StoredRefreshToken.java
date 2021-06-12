package de.htwberlin.kba.gr7.vocabduel.user_administration.model;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
public class StoredRefreshToken implements Serializable {
    @Id
    @ManyToOne
    private User user;

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
}
