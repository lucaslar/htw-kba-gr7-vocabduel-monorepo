package de.htwberlin.kba.gr7.vocabduel.vocabduel_ui;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.UserOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.AuthTokens;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.LoggedInUser;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.springframework.stereotype.Component;

@Component
public class CliSessionStorage {
    private UserService userService;

    private Long userId;
    private AuthTokens authTokens;

    public void setLoggedInUser(LoggedInUser loggedInUser) {
        this.userId = loggedInUser.getId();
        authTokens = loggedInUser.getAuthTokens();
    }

    public void rmLoggedInUser() {
        userId = null;
        authTokens = null;
    }

    public User getLoggedInUser() {
        try {
            return userId == null ? null : userService.getUserDataById(userId);
        } catch (UserOptimisticLockException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AuthTokens getAuthTokens() {
        return authTokens;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setAuthTokens(AuthTokens tokens) {
        authTokens = tokens;
    }
}
