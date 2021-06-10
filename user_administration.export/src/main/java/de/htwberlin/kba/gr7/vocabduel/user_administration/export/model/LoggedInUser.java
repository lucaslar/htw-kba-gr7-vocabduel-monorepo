package de.htwberlin.kba.gr7.vocabduel.user_administration.export.model;

import javax.persistence.*;

@Entity
@PrimaryKeyJoinColumn(name = "LoggedInUser_Id")
public class LoggedInUser extends User {
    @OneToOne
    @JoinColumns({
            @JoinColumn(name="AuthTokens_Token", referencedColumnName = "token"),
            @JoinColumn(name="AuthTokens_refreshToken", referencedColumnName = "refreshToken")

    })
    private AuthTokens authTokens;

    public LoggedInUser(Long id) {
        super(id);
    }

    public AuthTokens getAuthTokens() {
        return authTokens;
    }

    // TODO: Keep setter?
    public void setAuthTokens(AuthTokens authTokens) {
        this.authTokens = authTokens;
    }
}
