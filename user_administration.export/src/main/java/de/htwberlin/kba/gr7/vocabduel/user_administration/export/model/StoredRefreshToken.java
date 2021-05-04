package de.htwberlin.kba.gr7.vocabduel.user_administration.export.model;

public class StoredRefreshToken {
    private int userId;
    private String refreshToken;

    public int getUserId() {
        return userId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
