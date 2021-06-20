package de.htwberlin.kba.gr7.vocabduel.user_administration.export.model;

import java.io.Serializable;

public class AuthTokens implements Serializable {
    private final String TOKEN;
    private final String REFRESH_TOKEN;

    public AuthTokens(final String refreshToken, final String token) {
        this.TOKEN = token;
        this.REFRESH_TOKEN = refreshToken;
    }

    public String getToken() {
        return TOKEN;
    }

    public String getRefreshToken() {
        return REFRESH_TOKEN;
    }
}
