package de.htwberlin.kba.gr7.vocabduel.user_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.AuthTokens;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.LoggedInUser;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.LoginData;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.StoredRefreshToken;
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
import javax.naming.InvalidNameException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceImplTest {

    @Mock
    private UserServiceImpl userAdministration;
    @Mock
    private EntityManager entityManager;
    @Mock
    private EntityTransaction entityTransaction;
    @Mock
    private Query queryMock;
    private AuthServiceImpl auth;
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
    private final String UNKNOWN_MAIL = "unknown@mail.de";
    private final long UNKNOWN_ID = 1234567L;

    @Before
    public void setup() {
        auth = new AuthServiceImpl(userAdministration, entityManager);

        final String secret = "SuperSecretKey123HtwBerlinVocabduel2021";
        final byte[] encoded = (Base64.getEncoder().encode(secret.getBytes(StandardCharsets.UTF_8)));
        secretKey = new SecretKeySpec(Base64.getDecoder().decode(encoded), SignatureAlgorithm.HS256.getJcaName());

        existingUser = new User(42L);
        existingUser.setEmail("existinguser@user.de");
        existingUser.setUsername("existinguser");
        existingUser.setFirstName("Existing");
        existingUser.setLastName("User");

        final String newUserName = "newuser";
        newUser = new User(21L);
        newUser.setEmail("newuser@user.de");
        newUser.setUsername(newUserName);
        newUser.setFirstName("New");
        newUser.setLastName("User");

        validRefreshToken = generateRefreshToken(false, existingUser.getId());
        expiredRefreshToken = generateRefreshToken(true, existingUser.getId());
        refreshTokenOfUnknownUser = generateRefreshToken(false, UNKNOWN_ID);
        validAuthToken = generateAuthToken(false, existingUser.getId());
        expiredAuthToken = generateAuthToken(true, existingUser.getId());
        authTokenOfUnknownUser = generateAuthToken(false, UNKNOWN_ID);

        Mockito.when(entityManager.getTransaction()).thenReturn(entityTransaction);
        Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryMock);
        Mockito.when(queryMock.setParameter(Mockito.anyString(), Mockito.anyObject())).thenReturn(queryMock);
        Mockito.when(userAdministration.getUserDataByEmail(existingUser.getEmail())).thenReturn(existingUser);
        Mockito.when(userAdministration.getUserDataById(existingUser.getId())).thenReturn(existingUser);
        Mockito.when(userAdministration.getUserDataByEmail(UNKNOWN_MAIL)).thenReturn(null);
        Mockito.when(userAdministration.getUserDataById(UNKNOWN_ID)).thenReturn(null);
        Mockito.when(userAdministration.getUserDataByUsername(existingUser.getUsername())).thenReturn(existingUser);
        Mockito.when(userAdministration.getUserDataByUsername(newUserName)).thenReturn(newUser);
    }

    @Test(expected = PasswordsDoNotMatchException.class)
    public void shouldThrowPwdsDoNotMatch() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PasswordsDoNotMatchException, PwTooWeakException, IncompleteUserDataException, InvalidNameException {
        auth.registerUser("username", "mail@mail.de", "Arnold", "Schwarzenegger", STRONG_PWD, STRONG_PWD + "thisSuffixWillCauseTrouble!");
    }

    @Test(expected = AlreadyRegisteredUsernameException.class)
    public void shouldThrowUsernameAlreadyRegistered() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PasswordsDoNotMatchException, PwTooWeakException, IncompleteUserDataException, InvalidNameException {
        auth.registerUser(existingUser.getUsername(), "mail@mail.de", "Arnold", "Schwarzenegger", STRONG_PWD, STRONG_PWD);
    }

    @Test(expected = InvalidOrRegisteredMailException.class)
    public void shouldThrowMailAlreadyRegistered() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PasswordsDoNotMatchException, PwTooWeakException, IncompleteUserDataException, InvalidNameException {
        auth.registerUser("username", existingUser.getEmail(), "Arnold", "Schwarzenegger", STRONG_PWD, STRONG_PWD);
    }

    @Test(expected = InvalidOrRegisteredMailException.class)
    public void shouldThrowMailInvalid() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PwTooWeakException, PasswordsDoNotMatchException, IncompleteUserDataException, InvalidNameException {
        auth.registerUser("username","invalidmail", "Arnold", "Schwarzenegger", STRONG_PWD, STRONG_PWD);
    }

    // The following test could be implemented for each potentially null/empty trimmed field
    // using a parameterized test. However, this would lead to an unnecessarily large number of tests.

    @Test(expected = InvalidNameException.class)
    public void shouldNotRegisterUserWithEmptyTrimmedData() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PwTooWeakException, PasswordsDoNotMatchException, IncompleteUserDataException, InvalidNameException {
        final User user = new User(null);
        auth.registerUser("username","invalidmail@gmail.com", "    ", "Schwarzenegger", STRONG_PWD, STRONG_PWD);
    }

    @Test
    public void shouldRegisterUser() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PasswordsDoNotMatchException, PwTooWeakException, IncompleteUserDataException, InvalidNameException {
        Mockito.when(queryMock.getSingleResult()).thenReturn(
                new LoginData(new User(21L, newUser.getUsername(), newUser.getEmail(), newUser.getFirstName(), newUser.getLastName()),
                              auth.hashPassword(STRONG_PWD)));
        Mockito.when(queryMock.getResultList()).thenReturn(new ArrayList<>());
        final LoggedInUser loggedInUser = auth.registerUser("username", "mail@mail.de", "Arnold", "Schwarzenegger", STRONG_PWD, STRONG_PWD);
        Mockito.when(queryMock.getSingleResult()).thenReturn(new User());
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
                    .get("id");
            Assert.assertEquals(Long.valueOf(emailClaim.toString()), newUser.getId());
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
        Mockito.when(queryMock.getSingleResult()).thenReturn(existingUser);
        final User expected = userAdministration.getUserDataByEmail(existingUser.getEmail());
        Mockito.when(queryMock.getSingleResult()).thenReturn(new LoginData(existingUser, auth.hashPassword(STRONG_PWD)));
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
                    .get("id");
            Assert.assertEquals(Long.valueOf(emailClaim.toString()), existingUser.getId());
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
    public void shouldRefreshTokens() { // TODO: wenn alle Tests laufen, schlägt dieser fehl
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        StoredRefreshToken stored = new StoredRefreshToken(new User(), generateRefreshToken(false, existingUser.getId()));
        Mockito.when(queryMock.getSingleResult()).thenReturn(stored);
        final AuthTokens newTokens = auth.refreshAuthTokens(validRefreshToken);
        Assert.assertNotNull(newTokens);
        Assert.assertNotEquals(newTokens.getRefreshToken(), validRefreshToken);
    }

    // Tests concerning valid (= should not throw exception)/invalid passwords => ValidPwdsTest / InvalidPwdsTest

    @Test(expected = InvalidFirstPwdException.class)
    public void updatingPasswordShouldFailIfPrevPwdWrong() throws PasswordsDoNotMatchException, PwTooWeakException, InvalidFirstPwdException, InvalidUserException {
        final String falsePwd = "123thisWasNotMyPrevPwd";
        final String dbPwd = "PR€T7Y_5TR0NG_P@S$W0RD_2";
        Mockito.when(queryMock.getSingleResult()).thenReturn(new LoginData(existingUser, auth.hashPassword(dbPwd)));
        final String newPwd = "PR€T7Y_5TR0NG_P@S$W0RD";
        auth.updateUserPassword(existingUser, falsePwd, newPwd, newPwd);
    }

    @Test(expected = PasswordsDoNotMatchException.class)
    public void updatingPasswordShouldFailIfPwdsDoNotMatch() throws PasswordsDoNotMatchException, PwTooWeakException, InvalidFirstPwdException, InvalidUserException {
        final String currentPwd = "123thisWasNotMyPrevPwd";
        final String newPwd = "PR€T7Y_5TR0NG_P@S$W0RD";
        final String newPwd2 = "PR€T7Y_5TR0NG_P@S$W0RD2";
        LoginData data = new LoginData();
        data.setPasswordHash(auth.hashPassword(currentPwd));
        Mockito.when(queryMock.getSingleResult()).thenReturn(data);
        auth.updateUserPassword(existingUser, currentPwd, newPwd, newPwd2);
    }

    @Test
    public void updatingPasswordShouldHaveDbStatus0() throws PasswordsDoNotMatchException, PwTooWeakException, InvalidFirstPwdException, InvalidUserException {
        final String newPwd = "PR€T7Y_5TR0NG_P@S$W0RD";
        final String currentPwd = "pr€V10U5PwD";
        Mockito.when(queryMock.getSingleResult()).thenReturn(new LoginData(existingUser, auth.hashPassword(currentPwd)));
        final int statusCode = auth.updateUserPassword(existingUser, currentPwd, newPwd, newPwd);
        Assert.assertEquals(0, statusCode);
    }

    @Test
    public void shouldNotHaveAccessRightsIfAuthTokenNull() {
        Assert.assertFalse(auth.hasAccessRights(null));
    }

    @Test
    public void shouldNotHaveAccessRightsIfAuthTokenIsOfUnknownUser() {
        // TODO: bei hasAccessRights DB Abfrage getUserByEmail ??
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

    private String generateToken(boolean isExpired, long id, Date expiration) {
        final Instant now = Instant.now();
        return Jwts.builder()
                .claim("id", id)
                .setIssuedAt(Date.from(now.minus(isExpired ? 100 : 0, ChronoUnit.DAYS)))
                .setExpiration(isExpired ? Date.from(now) : expiration)
                .signWith(secretKey)
                .compact();
    }

    private String generateRefreshToken(boolean isExpired, long id) {
        final Date expiration = Date.from(Instant.now().plus(30, ChronoUnit.DAYS));
        return generateToken(isExpired, id, expiration);
    }

    private String generateAuthToken(boolean isExpired, long id) {
        final Date expiration = Date.from(Instant.now().plus(5, ChronoUnit.MINUTES));
        return generateToken(isExpired, id, expiration);
    }
}
