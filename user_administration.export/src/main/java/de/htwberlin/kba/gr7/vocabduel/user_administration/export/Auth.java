package de.htwberlin.kba.gr7.vocabduel.user_administration.export;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.AlreadyRegisteredMailException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.AlreadyRegisteredUsernameException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.PasswordsDoNotMatchException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.AuthTokens;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.LoggedInUser;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

public interface Auth {

    /**
     * <p>Register and add a new user, who can take actions in this application.</p>
     *
     * @param user            Dataset of the new user
     * @param password        Password for the new user. it has to be stored in a different way than the other user data
     * @param confirmPassword The password confirmed
     * @return The user object of the new user. password will never be given
     * @throws PasswordsDoNotMatchException Password and confirmPassword do not match
     * @throws AlreadyRegisteredException   The user to be added already existis
     */
    LoggedInUser registerUser(User user, String password, String confirmPassword) throws PasswordsDoNotMatchException, AlreadyRegisteredMailException, AlreadyRegisteredUsernameException;

    /**
     * <p>Login an existing user, so he can take actions in this application.</p>
     *
     * @param email    User can login with his unique email address
     * @param password The user's password
     * @return The user object
     */
    LoggedInUser loginUser(String email, String password);

    User fetchUser(String token);

    AuthTokens refreshAuthToken(String refreshToken);

    boolean hasAccessRights(String token);
}
