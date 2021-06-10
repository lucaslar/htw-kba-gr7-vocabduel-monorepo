package de.htwberlin.kba.gr7.vocabduel.user_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.AuthService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.AuthTokens;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.LoggedInUser;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class AuthServiceImpl implements AuthService {

    private UserServiceImpl userService;

    @Override
    public LoggedInUser registerUser(User user, String password, String confirmPassword) throws PasswordsDoNotMatchException, PwTooWeakException, InvalidOrRegisteredMailException, AlreadyRegisteredUsernameException, IncompleteUserDataException {
        final boolean isComplete = user.getEmail() == null || user.getUsername() == null || user.getFirstName() == null || user.getLastName() == null || password == null || confirmPassword == null;
        if (isComplete) throw new IncompleteUserDataException();
        else if (!password.equals(confirmPassword)) throw new PasswordsDoNotMatchException();
        else if (!isValidPassword(password)) {
            throw new PwTooWeakException("Pwd too weak. Expected: at least two of four: [lower case, upper case, digit, special character] and min. 8 characters length");
        } else if (!Pattern.compile("^(.+)@(.+)$").matcher(user.getEmail()).matches()) {
            throw new InvalidOrRegisteredMailException("Invalid mail format");
        } else if (userService.getUserDataByEmail(user.getEmail()) != null) {
            throw new InvalidOrRegisteredMailException("Email is already registered");
        } else if (userService.getUserDataByUsername(user.getUsername()) != null) {
            throw new AlreadyRegisteredUsernameException("");
        }

        // TODO registration

        return null;
    }

    @Override
    public LoggedInUser loginUser(String email, String password) {
        // TODO: remove mock data
        final LoggedInUser user = new LoggedInUser(42L);
        user.setEmail(email);
        user.setFirstName("Arnold");
        user.setLastName("Schwarzenegger");
        user.setUsername("arnie1947");
        user.setAuthTokens(new AuthTokens());
        user.getAuthTokens().setToken("123");
        user.getAuthTokens().setRefreshToken("123");
        return user;
    }

    @Override
    public User fetchUser(String token) {
        return null;
    }

    @Override
    public AuthTokens refreshAuthTokens(String refreshToken) {
        return null;
    }

    @Override
    public boolean hasAccessRights(String token) {
        return false;
    }

    public UserServiceImpl getUserService() {
        return userService;
    }

    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    private boolean isValidPassword(final String password) {
        int matches = 0;
        if (Pattern.compile("\\d+").matcher(password).find()) matches++;
        if (Pattern.compile("[a-z]+").matcher(password).find()) matches++;
        if (Pattern.compile("[A-Z]+").matcher(password).find()) matches++;
        if (Pattern.compile("(?=.*[-+_!@#$%^&*., ?]).+").matcher(password).find()) matches++;
        return password.length() >= 8 && matches >= 2;
    }
}
