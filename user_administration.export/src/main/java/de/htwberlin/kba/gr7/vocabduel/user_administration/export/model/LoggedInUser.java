package de.htwberlin.kba.gr7.vocabduel.user_administration.export.model;

public class LoggedInUser extends User {
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
