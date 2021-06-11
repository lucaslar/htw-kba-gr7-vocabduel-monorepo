package de.htwberlin.kba.gr7.vocabduel.user_administration;

import at.favre.lib.crypto.bcrypt.BCrypt;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.AuthService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.AuthTokens;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.LoggedInUser;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.LoginData;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.Validation;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService USER_SERVICE;

    private final EntityManager ENTITY_MANAGER;

    public AuthServiceImpl(final UserService userService) {
        USER_SERVICE = userService;

        ENTITY_MANAGER = EntityFactoryManagement.getEntityFactory().createEntityManager();
    }

    @Override
    public LoggedInUser registerUser(String username, String email, String firstname, String lastname, String password, String confirmPassword)
            throws PasswordsDoNotMatchException, PwTooWeakException, InvalidOrRegisteredMailException, AlreadyRegisteredUsernameException, IncompleteUserDataException {
        Validation.completeDataValidation(username, email, firstname, lastname, password, confirmPassword);
        Validation.uniqueUserDataValidation(username, email, USER_SERVICE);
        Validation.passwordValidation(password, confirmPassword);

        final User user = new User(username, email, firstname, lastname);

        ENTITY_MANAGER.getTransaction().begin();
        ENTITY_MANAGER.persist(new LoginData(user, hashPassword(password)));
        ENTITY_MANAGER.getTransaction().commit();

        return loginUser(email, password);
    }

    @Override
    public LoggedInUser loginUser(String email, String password) {

        ENTITY_MANAGER.getTransaction().begin();
        LoginData loginData = null;
        try {
            loginData = (LoginData) ENTITY_MANAGER
                    .createQuery("SELECT l FROM LoginData l INNER JOIN l.user u WHERE u.email LIKE :email")
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException ignored) {
        }

        ENTITY_MANAGER.getTransaction().commit();
        if (loginData != null && validatePassword(loginData.getPasswordHash(), password)) {
            final LoggedInUser user = new LoggedInUser(loginData.getUser());
            user.setAuthTokens(new AuthTokens());
            user.getAuthTokens().setToken("123");
            user.getAuthTokens().setRefreshToken("123");
            // TODO Generate real tokens
            return user;
        } else return null;
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

    private String hashPassword(final String pwd) {
        return BCrypt.withDefaults().hashToString(12, pwd.toCharArray());
    }

    private boolean validatePassword(final String hashedPwd, final String pwd) {
        return BCrypt.verifyer().verify(pwd.toCharArray(), hashedPwd).verified;
    }
}
