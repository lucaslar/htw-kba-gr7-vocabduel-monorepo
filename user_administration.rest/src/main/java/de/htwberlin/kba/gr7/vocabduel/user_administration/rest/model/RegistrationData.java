package de.htwberlin.kba.gr7.vocabduel.user_administration.rest.model;

public class RegistrationData {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String confirm;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirm() {
        return confirm;
    }
}
