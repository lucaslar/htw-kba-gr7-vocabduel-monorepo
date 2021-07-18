package de.htwberlin.kba.gr7.vocabduel.user_administration.rest.model;

import de.htwberlin.kba.gr7.vocabduel.shared_logic.rest.model.NoNullableProperty;

import java.util.ArrayList;
import java.util.List;

public class SignInData implements NoNullableProperty {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public List<String> missingProperties() {
        final ArrayList<String> missing = new ArrayList<>();
        if (email == null) missing.add("email");
        if (password == null) missing.add("password");
        return missing.isEmpty() ? null : missing;
    }
}
