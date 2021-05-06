package de.htwberlin.kba.gr7.vocabduel.user_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.Auth;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.AlreadyRegisteredMailException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.AlreadyRegisteredUsernameException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.PasswordsDoNotMatchException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.AuthTokens;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.LoggedInUser;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

public class AuthImpl implements Auth {
    @Override
    public LoggedInUser registerUser(User user, String password, String confirmPassword) throws PasswordsDoNotMatchException, AlreadyRegisteredMailException, AlreadyRegisteredUsernameException {
        return null;
    }

    @Override
    public LoggedInUser loginUser(String email, String password) {
        return null;
    }

    @Override
    public User fetchUser(String token) {
        return null;
    }

    @Override
    public AuthTokens refreshAuthToken(String refreshToken) {
        return null;
    }

    @Override
    public boolean hasAccessRights(String token) {
        return false;
    }
}
