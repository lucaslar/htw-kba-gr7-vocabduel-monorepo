package de.htwberlin.kba.gr7.vocabduel.user_administration.model;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Entity
public class LoginData implements Serializable {
    @Id
    @OneToOne
    private User user;
    private String passwordHash;

    public LoginData(final User user, final String passwordHash) {
        this.user = user;
        this.passwordHash = passwordHash;
    }

    public User getUser() {
        return user;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
