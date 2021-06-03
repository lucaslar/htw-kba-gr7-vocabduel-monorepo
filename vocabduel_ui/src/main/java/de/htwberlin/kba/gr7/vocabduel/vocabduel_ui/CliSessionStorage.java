package de.htwberlin.kba.gr7.vocabduel.vocabduel_ui;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.AuthTokens;

public class CliSessionStorage {
    private AuthTokens authTokens;

    public AuthTokens getAuthTokens() {
        return authTokens;
    }

    public void setAuthTokens(AuthTokens authTokens) {
        this.authTokens = authTokens;
    }
}
