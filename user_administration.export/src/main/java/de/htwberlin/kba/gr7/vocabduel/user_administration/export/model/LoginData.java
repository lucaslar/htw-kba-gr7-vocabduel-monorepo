package de.htwberlin.kba.gr7.vocabduel.user_administration.export.model;

public class LoginData {
    private Long id;
    private int passwordHash;

    public Long getId() {
        return id;
    }

    public int getPasswordHash() {
        return passwordHash;
    }
}
