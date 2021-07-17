package de.htwberlin.kba.gr7.vocabduel.user_administration.rest.model;

public class RegistrationData extends SignInData{
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
}
