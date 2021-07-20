package de.htwberlin.kba.gr7.vocabduel.user_administration.model;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class LoginData implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(targetEntity = User.class, cascade = CascadeType.ALL)
    @JoinColumn(name="user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private String passwordHash;

    @Version
    private Integer version;

    public LoginData() {}

    public LoginData(final User user, final String passwordHash) {
        this.user = user;
        this.passwordHash = passwordHash;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Integer getVersion() {
        return version;
    }
}
