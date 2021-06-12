package de.htwberlin.kba.gr7.vocabduel.user_administration.export.model;

import java.io.Serializable;

public class AuthTokens implements Serializable {
    private String token;
    private String refreshToken;

    public AuthTokens(final String refreshToken, final String token) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    // TODO Keep setters?

    public void setToken(String token) {
        this.token = token;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
