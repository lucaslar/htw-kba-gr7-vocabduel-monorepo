package de.htwberlin.kba.gr7.model;

public class LoggedInUser extends User {
    private AuthTokens authTokens;

    public AuthTokens getAuthTokens() {
        return authTokens;
    }
}
