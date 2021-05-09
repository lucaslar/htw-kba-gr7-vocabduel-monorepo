package de.htwberlin.kba.gr7.vocabduel.user_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.AuthTokens;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.LoggedInUser;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class AuthImplTest {

    @Mock
    private UserAdministrationImpl userAdministration;
    private AuthImpl auth;
    private User existingUser;
    private User newUser;

    private SecretKeySpec secretKey;

    private String validRefreshToken;
    private String expiredRefreshToken;
    private String refreshTokenOfUnknownUser;
    private String validAuthToken;
    private String expiredAuthToken;
    private String authTokenOfUnknownUser;

    private final String STRONG_PWD = "PR€T7Y_5TR0NG_P@S$W0RD";
    private final String NEW_USER_NAME = "newuser";
    private final String UNKNOWN_MAIL = "unknown@mail.de";

    @Before
    public void setup() {
        auth = new AuthImpl();
        auth.setUserService(userAdministration);

        final String secret = "SuperSecretKey123HtwBerlinVocabduel2021";
        final byte[] encoded = (Base64.getEncoder().encode(secret.getBytes(StandardCharsets.UTF_8)));
        secretKey = new SecretKeySpec(Base64.getDecoder().decode(encoded), SignatureAlgorithm.HS256.getJcaName());

        existingUser = new User(42L);
        existingUser.setEmail("existinguser@user.de");
        existingUser.setUsername("existinguser");
        existingUser.setFirstName("Existing");
        existingUser.setLastName("User");

        newUser = new User(null);
        newUser.setEmail("newuser@user.de");
        newUser.setUsername(NEW_USER_NAME);
        newUser.setFirstName("New");
        newUser.setLastName("User");

        validRefreshToken = generateRefreshToken(false, existingUser.getEmail());
        expiredRefreshToken = generateRefreshToken(true, existingUser.getEmail());
        refreshTokenOfUnknownUser = generateRefreshToken(false, UNKNOWN_MAIL);
        validAuthToken = generateAuthToken(false, existingUser.getEmail());
        expiredAuthToken = generateAuthToken(true, existingUser.getEmail());
        authTokenOfUnknownUser = generateAuthToken(false, UNKNOWN_MAIL);

        Mockito.when(userAdministration.getUserDataByEmail(existingUser.getEmail())).thenReturn(existingUser);
        Mockito.when(userAdministration.getUserDataByEmail(UNKNOWN_MAIL)).thenReturn(null);
        Mockito.when(userAdministration.getUserDataByUsername(existingUser.getUsername())).thenReturn(existingUser);
        Mockito.when(userAdministration.getUserDataByUsername(NEW_USER_NAME)).thenReturn(newUser);
    }

    @Test(expected = PasswordsDoNotMatchException.class)
    public void shouldThrowPwdsDoNotMatch() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PasswordsDoNotMatchException, PwTooWeakException, IncompleteUserDataException {
        auth.registerUser(new User(null), STRONG_PWD, STRONG_PWD + "thisSuffixWillCauseTrouble!");
    }

    @Test(expected = AlreadyRegisteredUsernameException.class)
    public void shouldThrowUsernameAlreadyRegistered() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PasswordsDoNotMatchException, PwTooWeakException, IncompleteUserDataException {
        final User user = new User(null);
        user.setUsername(existingUser.getUsername());
        auth.registerUser(user, STRONG_PWD, STRONG_PWD);
    }

    @Test(expected = InvalidOrRegisteredMailException.class)
    public void shouldThrowMailAlreadyRegistered() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PasswordsDoNotMatchException, PwTooWeakException, IncompleteUserDataException {
        final User user = new User(null);
        user.setEmail(existingUser.getEmail());
        auth.registerUser(user, STRONG_PWD, STRONG_PWD);
    }

    @Test(expected = InvalidOrRegisteredMailException.class)
    public void shouldThrowMailInvalid() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PwTooWeakException, PasswordsDoNotMatchException, IncompleteUserDataException {
        final User user = new User(null);
        String INVALID_MAIL = "invalidmail";
        user.setEmail(INVALID_MAIL);
        auth.registerUser(user, STRONG_PWD, STRONG_PWD);
    }

    @Test
    public void shouldRegisterUser() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PasswordsDoNotMatchException, PwTooWeakException, IncompleteUserDataException {
        final LoggedInUser loggedInUser = auth.registerUser(newUser, STRONG_PWD, STRONG_PWD);
        final User registeredUser = userAdministration.getUserDataByUsername(newUser.getUsername());

        Assert.assertNotNull(registeredUser);
        Assert.assertNotNull(loggedInUser);
        Assert.assertNotNull(loggedInUser.getAuthTokens());
        Assert.assertEquals(newUser.toString(), registeredUser.toString());

        Stream.of(loggedInUser.getAuthTokens().getToken(), loggedInUser.getAuthTokens().getRefreshToken()).forEach(token -> {
            final Object emailClaim = Jwts
                    .parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(loggedInUser.getAuthTokens().getToken())
                    .getBody()
                    .get("email");
            Assert.assertEquals(emailClaim, newUser.getEmail());
        });

    }

    @Test
    public void shouldNotLoginNotExistingUser() {
        Assert.assertNull(auth.loginUser(UNKNOWN_MAIL, STRONG_PWD));
    }

    @Test
    public void shouldNotLoginIfPwdIsWrong() {
        Assert.assertNull(auth.loginUser(existingUser.getEmail(), STRONG_PWD + "thisSuffixWillCauseTrouble!"));
    }

    @Test
    public void shouldLoginUser() {
        final User expected = userAdministration.getUserDataByEmail(existingUser.getEmail());
        final LoggedInUser loggedInUser = auth.loginUser(existingUser.getEmail(), STRONG_PWD);
        Assert.assertNotNull(loggedInUser);
        Assert.assertNotNull(loggedInUser.getAuthTokens());
        Assert.assertEquals(expected.getUsername(), loggedInUser.getUsername());
        Assert.assertEquals(expected.getFirstName(), loggedInUser.getFirstName());
        Assert.assertEquals(expected.getLastName(), loggedInUser.getLastName());

        Stream.of(loggedInUser.getAuthTokens().getToken(), loggedInUser.getAuthTokens().getRefreshToken()).forEach(token -> {
            final Object emailClaim = Jwts
                    .parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(loggedInUser.getAuthTokens().getToken())
                    .getBody()
                    .get("email");
            Assert.assertEquals(emailClaim, existingUser.getEmail());
        });
    }

    @Test
    public void shouldNotFetchUserDueToNoToken() {
        Assert.assertNull(auth.fetchUser(null));
    }

    @Test
    public void shouldNotFetchUserDueToExpiredToken() {
        Assert.assertNull(auth.fetchUser(expiredAuthToken));
    }

    @Test
    public void shouldNotFetchUserDueToNoMatchingUser() {
        Assert.assertNull(auth.fetchUser(authTokenOfUnknownUser));
    }

    @Test
    public void shouldFetchUser() {
        final User fetchedUser = auth.fetchUser(validAuthToken);
        Assert.assertNotNull(fetchedUser);
        Assert.assertEquals(existingUser.toString(), fetchedUser.toString());
    }

    @Test
    public void shouldNotRefreshTokensIfRefreshIsNull() {
        Assert.assertNull(auth.refreshAuthTokens(null));
    }

    @Test
    public void shouldNotRefreshTokensIfRefreshTokenExpired() {
        Assert.assertNull(auth.refreshAuthTokens(expiredRefreshToken));
    }

    @Test
    public void shouldNotRefreshTokensIfNoUserIsFoundForToken() {
        Assert.assertNull(auth.refreshAuthTokens(refreshTokenOfUnknownUser));
    }

    @Test
    public void shouldRefreshTokens() {
        final AuthTokens newTokens = auth.refreshAuthTokens(validRefreshToken);
        Assert.assertNotNull(newTokens);
        Assert.assertNotEquals(newTokens.getRefreshToken(), validRefreshToken);
    }

    @Test
    public void shouldNotHaveAccessRightsIfAuthTokenNull() {
        Assert.assertFalse(auth.hasAccessRights(null));
    }

    @Test
    public void shouldNotHaveAccessRightsIfAuthTokenIsOfUnknownUser() {
        Assert.assertFalse(auth.hasAccessRights(authTokenOfUnknownUser));
    }

    @Test
    public void shouldNotHaveAccessRightsIfAuthTokenExpired() {
        Assert.assertFalse(auth.hasAccessRights(expiredAuthToken));
    }

    @Test
    public void shouldHaveAccessRightsIfAuthTokenIsValid() {
        Assert.assertTrue(auth.hasAccessRights(validAuthToken));
    }

    // TODO: in the future, use component's secret / move token generation

    private String generateToken(boolean isExpired, String email, Date expiration) {
        final Instant now = Instant.now();
        return Jwts.builder()
                .claim("email", email)
                .setIssuedAt(Date.from(now.minus(isExpired ? 100 : 0, ChronoUnit.DAYS)))
                .setExpiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    private String generateRefreshToken(boolean isExpired, String email) {
        final Date expiration = Date.from(Instant.now().plus(30, ChronoUnit.DAYS));
        return generateToken(isExpired, email, expiration);
    }

    private String generateAuthToken(boolean isExpired, String email) {
        final Date expiration = Date.from(Instant.now().plus(5, ChronoUnit.MINUTES));
        return generateToken(isExpired, email, expiration);
    }
}
