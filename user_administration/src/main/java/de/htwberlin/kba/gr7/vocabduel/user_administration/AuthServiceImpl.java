package de.htwberlin.kba.gr7.vocabduel.user_administration;

import at.favre.lib.crypto.bcrypt.BCrypt;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.AuthService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.AuthTokens;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.LoggedInUser;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.LoginData;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.StoredRefreshToken;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.Validation;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService USER_SERVICE;

    private final EntityManager ENTITY_MANAGER;

    private final SecretKeySpec TOKEN_KEY;

    public AuthServiceImpl(final UserService userService) {
        USER_SERVICE = userService;

        ENTITY_MANAGER = EntityFactoryManagement.getEntityFactory().createEntityManager();

        final String secret = "SuperSecretKey123HtwBerlinVocabduel2021";
        final byte[] encoded = (Base64.getEncoder().encode(secret.getBytes(StandardCharsets.UTF_8)));
        TOKEN_KEY = new SecretKeySpec(Base64.getDecoder().decode(encoded), SignatureAlgorithm.HS256.getJcaName());
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
            return manageUserTokens(loginData.getUser());
        } else return null;
    }

    @Override
    public User fetchUser(String token) {
        return null;
    }

    @Override
    public AuthTokens refreshAuthTokens(String refreshToken) {
        return new AuthTokens("some refresh token", "valid token");
        // TODO Implement correctly using db
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

    private LoggedInUser manageUserTokens(final User foundUser) {
        final LoggedInUser user = new LoggedInUser(foundUser);
        final String refreshToken = generateRefreshToken(user);
        final String token = generateAuthToken(user);

        ENTITY_MANAGER.getTransaction().begin();
        try {
            final List<StoredRefreshToken> storedRefreshTokens = (List<StoredRefreshToken>) ENTITY_MANAGER
                    .createQuery("from StoredRefreshToken where user = :user")
                    .setParameter("user", user)
                    .getResultList();
            if (storedRefreshTokens.size() > 4) storedRefreshTokens.forEach(ENTITY_MANAGER::remove);
        } catch (NoResultException ignored) {
        }

        ENTITY_MANAGER.persist(new StoredRefreshToken(user, refreshToken));
        ENTITY_MANAGER.getTransaction().commit();

        user.setAuthTokens(new AuthTokens(refreshToken, token));
        return user;
    }

    private String generateRefreshToken(final User user) {
        final Date expiration = Date.from(Instant.now().plus(30, ChronoUnit.DAYS));
        return generateToken(user.getEmail(), expiration);
    }

    private String generateAuthToken(final User user) {
        final Date expiration = Date.from(Instant.now().plus(5, ChronoUnit.MINUTES));
        return generateToken(user.getEmail(), expiration);
    }

    private String generateToken(final String email, final Date expiration) {
        final Instant now = Instant.now();
        return Jwts.builder()
                .claim("email", email)
                .setIssuedAt(Date.from(now))
                .setExpiration(expiration)
                .signWith(TOKEN_KEY)
                .compact();
    }
}
