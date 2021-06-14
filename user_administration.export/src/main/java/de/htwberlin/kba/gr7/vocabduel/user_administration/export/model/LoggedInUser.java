package de.htwberlin.kba.gr7.vocabduel.user_administration.export.model;

public class LoggedInUser extends User {
    private AuthTokens authTokens;

    public LoggedInUser(User user) {
        super(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName());
    }

    public static User asUser(final LoggedInUser loggedInUser) {
        return new User(
                loggedInUser.getId(),
                loggedInUser.getUsername(),
                loggedInUser.getEmail(),
                loggedInUser.getFirstName(),
                loggedInUser.getLastName()
        );
    }

    public AuthTokens getAuthTokens() {
        return authTokens;
    }

    public void setAuthTokens(AuthTokens authTokens) {
        this.authTokens = authTokens;
    }
}
