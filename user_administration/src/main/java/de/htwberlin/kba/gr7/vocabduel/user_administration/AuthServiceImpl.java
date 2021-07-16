package de.htwberlin.kba.gr7.vocabduel.user_administration;

import at.favre.lib.crypto.bcrypt.BCrypt;
import de.htwberlin.kba.gr7.vocabduel.user_administration.dao.LoginDataDAOImpl;
import de.htwberlin.kba.gr7.vocabduel.user_administration.dao.StoredRefreshTokenDAOImpl;
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
import javax.naming.InvalidNameException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService USER_SERVICE;

    private final LoginDataDAOImpl loginDataDAO;
    private final StoredRefreshTokenDAOImpl storedRefreshTokenDAO;

    private final SecretKeySpec TOKEN_KEY;

    public AuthServiceImpl(final UserService userService, final EntityManager entityManager) {
        USER_SERVICE = userService;
        loginDataDAO = new LoginDataDAOImpl(entityManager);
        storedRefreshTokenDAO = new StoredRefreshTokenDAOImpl(entityManager);
        TOKEN_KEY = initializeTokenkey();
    }

    private SecretKeySpec initializeTokenkey() {
        final String secret = "SuperSecretKey123HtwBerlinVocabduel2021";
        final byte[] encoded = (Base64.getEncoder().encode(secret.getBytes(StandardCharsets.UTF_8)));
        return new SecretKeySpec(Base64.getDecoder().decode(encoded), SignatureAlgorithm.HS256.getJcaName());
    }

    @Override
    public LoggedInUser registerUser(String username, String email, String firstname, String lastname, String password, String confirmPassword) throws PasswordsDoNotMatchException, PwTooWeakException, InvalidOrRegisteredMailException, AlreadyRegisteredUsernameException, IncompleteUserDataException, InvalidNameException {
        Validation.completeDataValidation(username, email, firstname, lastname, password, confirmPassword);
        Validation.uniqueUserDataValidation(username, email, USER_SERVICE);
        Validation.nameValidation(firstname);
        Validation.nameValidation(lastname);
        Validation.passwordValidation(password, confirmPassword);

        final User user = new User(username, email, firstname, lastname);

        loginDataDAO.insertLoginData(new LoginData(user, hashPassword(password)));

        return loginUser(email, password);
    }

    @Override
    public LoggedInUser loginUser(String email, String password) {
        LoginData loginData = loginDataDAO.selectLoginDataByUserEmail(email);

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
                return USER_SERVICE.getUserDataById(claims.get("id", Long.class));
            } catch (JwtException ignored) {
            }
        }
        return null;
    }

    @Override
    public AuthTokens refreshAuthTokens(final String refreshToken) {
        try {
            final User user = fetchUser(refreshToken);
            if (user != null) {
                StoredRefreshToken foundToken = storedRefreshTokenDAO.selectStoredRefreshTokenByUserAndToken(user, refreshToken);
                if (validateToken(foundToken.getRefreshToken())) {
                    storedRefreshTokenDAO.deleteStoredRefreshToken(foundToken);
                    return insertNewUserTokens(user);
                }
            }
        } catch (NoResultException ignored) {
        }
        return null;
    }

    @Override
    public int updateUserPassword(final User user, final String currentPassword, final String password, final String confirmPassword) throws InvalidUserException, InvalidFirstPwdException, PasswordsDoNotMatchException, PwTooWeakException {
        if (user == null) throw new InvalidUserException("Invalid user");

        LoginData loginData = loginDataDAO.selectLoginDataByUser(user);

        if (loginData == null) throw new InvalidUserException("User could not be found");
        else if (!validatePassword(loginData.getPasswordHash(), currentPassword)) {
            throw new InvalidFirstPwdException("Invalid current password.");
        }

        Validation.passwordValidation(password, confirmPassword);

        loginData.setPasswordHash(hashPassword(password));
        loginDataDAO.insertLoginData(loginData);

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
        storedRefreshTokenDAO.insertStoredRefreshTokenByUserAndToken(user, refreshToken);
        return new AuthTokens(refreshToken, token);
    }

    private String generateRefreshToken(final User user) {
        final Date expiration = Date.from(Instant.now().plus(30, ChronoUnit.DAYS));
        return generateToken(user.getId(), expiration);
    }

    private String generateAuthToken(final User user) {
        final Date expiration = Date.from(Instant.now().plus(5, ChronoUnit.MINUTES));
        return generateToken(user.getId(), expiration);
    }

    private String generateToken(final long id, final Date expiration) {
        final Instant now = Instant.now();
        return Jwts.builder()
                .claim("id", id)
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
