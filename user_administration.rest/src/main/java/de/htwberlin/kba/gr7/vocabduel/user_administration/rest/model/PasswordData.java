package de.htwberlin.kba.gr7.vocabduel.user_administration.rest.model;

public class PasswordData {
    private String currentPassword;
    private String newPassword;
    private String confirm;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getConfirm() {
        return confirm;
    }
}
