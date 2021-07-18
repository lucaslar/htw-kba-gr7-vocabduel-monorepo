package de.htwberlin.kba.gr7.vocabduel.user_administration.rest.model;

import de.htwberlin.kba.gr7.vocabduel.shared_logic.rest.model.NoNullableProperty;

import java.util.ArrayList;
import java.util.List;

public class PasswordData implements NoNullableProperty {
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
    @Override
    public List<String> missingProperties() {
        final ArrayList<String> missing = new ArrayList<>();
        if (currentPassword == null) missing.add("currentPassword");
        if (newPassword == null) missing.add("newPassword");
        if (confirm == null) missing.add("confirm");
        return missing.isEmpty() ? null : missing;
    }
}
