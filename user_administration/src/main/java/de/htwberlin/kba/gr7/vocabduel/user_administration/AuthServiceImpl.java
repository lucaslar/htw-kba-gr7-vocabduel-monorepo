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
import io.jsonwebtoken.*;
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
            final LoggedInUser user = new LoggedInUser(loginData.getUser());
            user.setAuthTokens(insertNewUserTokens(user));
            return user;
        }
        return null;
    }

    @Override
    public User fetchUser(final String token) {
        if (token != null) {
            try {
                final Claims claims = parseToken(token);
                return USER_SERVICE.getUserDataByEmail(claims.get("email", String.class));
            } catch (JwtException ignored) {
            }
        }
        return null;
    }

    @Override
    public AuthTokens refreshAuthTokens(final String refreshToken) {
        try {
            ENTITY_MANAGER.getTransaction().begin();
            final User user = fetchUser(refreshToken);
            if (user != null) {
                final StoredRefreshToken foundToken = (StoredRefreshToken) ENTITY_MANAGER
                        .createQuery("from StoredRefreshToken where user = :user and refreshToken = :token")
                        .setParameter("user", user)
                        .setParameter("token", refreshToken)
                        .getSingleResult();
                if (validateToken(foundToken.getRefreshToken())) {
                    ENTITY_MANAGER.remove(foundToken);
                    ENTITY_MANAGER.getTransaction().commit();
                    return insertNewUserTokens(user);
                }
            }
        } catch (NoResultException ignored) {
        }
        ENTITY_MANAGER.getTransaction().commit();
        return null;
    }

    @Override
    public int updateUserPassword(User user, String currentPassword, String password, String confirmPassword) throws InvalidFirstPwdException, PasswordsDoNotMatchException, PwTooWeakException {
        // TODO check current pwd
        Validation.passwordValidation(password, confirmPassword);
        // TODO Implement
        return 0;
    }

    @Override
    public boolean hasAccessRights(final String token) {
        return validateToken(token);
    }

    private String hashPassword(final String pwd) {
        return BCrypt.withDefaults().hashToString(12, pwd.toCharArray());
    }

    private boolean validatePassword(final String hashedPwd, final String pwd) {
        return BCrypt.verifyer().verify(pwd.toCharArray(), hashedPwd).verified;
    }

    private AuthTokens insertNewUserTokens(final User user) {
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
        return new AuthTokens(refreshToken, token);
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

    private boolean validateToken(final String token) {
        try {
            if (token != null) {
                parseToken(token);
                return true;
            }
        } catch (JwtException ignored) {
        }
        return false;
    }

    private Claims parseToken(final String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(TOKEN_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
