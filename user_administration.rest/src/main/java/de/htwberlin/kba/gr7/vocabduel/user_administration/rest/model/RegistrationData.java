package de.htwberlin.kba.gr7.vocabduel.user_administration.rest.model;

import de.htwberlin.kba.gr7.vocabduel.shared_logic.rest.model.NoNullableProperty;

import java.util.ArrayList;
import java.util.List;

public class RegistrationData extends SignInData implements NoNullableProperty {
    private String username;
    private String firstName;
    private String lastName;
    private String confirm;

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getConfirm() {
        return confirm;
    }

    @Override
    public List<String> missingProperties() {
        List<String> missing = super.missingProperties();
        if (missing == null) missing = new ArrayList<>();
        if (username == null) missing.add("username");
        if (firstName == null) missing.add("firstName");
        if (lastName == null) missing.add("lastName");
        if (confirm == null) missing.add("confirm");
        return missing.isEmpty() ? null : missing;
    }
}
