package de.htwberlin.kba.gr7.vocabduel.user_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.AuthService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.AuthTokens;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.LoggedInUser;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.Validation;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService USER_SERVICE;

    private final EntityManager ENTITY_MANAGER;

    public AuthServiceImpl(final UserService userService) {
        USER_SERVICE = userService;

        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("VocabduelJPA_PU_user");
        ENTITY_MANAGER = emf.createEntityManager();
    }

    @Override
    public LoggedInUser registerUser(String username, String email, String firstname, String lastname, String password, String confirmPassword)
            throws PasswordsDoNotMatchException, PwTooWeakException, InvalidOrRegisteredMailException, AlreadyRegisteredUsernameException, IncompleteUserDataException {
        Validation.completeDataValidation(username, email, firstname, lastname, password, confirmPassword);
        Validation.uniqueUserDataValidation(username, email, USER_SERVICE);
        Validation.passwordValidation(password, confirmPassword);

        // TODO implement actual registration

        final LoggedInUser user = new LoggedInUser(42L);
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setAuthTokens(new AuthTokens());
        user.getAuthTokens().setToken("123");
        user.getAuthTokens().setRefreshToken("123");
        return user;
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
        final AuthTokens tokens = new AuthTokens();
        tokens.setToken("valid token");
        tokens.setRefreshToken("some refresh token");
        return tokens; // TODO Implement correctly using db
    }

    @Override
    public boolean hasAccessRights(String token) {
        return token.equals("valid token"); // TODO Implement correctly using db
    }
}
