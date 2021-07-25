package de.htwberlin.kba.gr7.vocabduel.user_administration;

import at.favre.lib.crypto.bcrypt.BCrypt;
import de.htwberlin.kba.gr7.vocabduel.user_administration.dao.LoginDataDAOImpl;
import de.htwberlin.kba.gr7.vocabduel.user_administration.dao.StoredRefreshTokenDAOImpl;
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
import org.mockito.junit.MockitoJUnitRunner;

import javax.crypto.spec.SecretKeySpec;
import javax.naming.InvalidNameException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceImplTest {

    @Mock
    private UserService userService;
    @Mock
    private EntityManager entityManager;
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
    public void setup() throws UserOptimisticLockException {
        final StoredRefreshTokenDAOImpl storedRefreshTokenDAO = new StoredRefreshTokenDAOImpl();
        storedRefreshTokenDAO.setEntityManager(entityManager);
        final LoginDataDAOImpl loginDataDAO = new LoginDataDAOImpl();
        loginDataDAO.setEntityManager(entityManager);

        auth = new AuthServiceImpl(userService, loginDataDAO, storedRefreshTokenDAO);

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

        Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryMock);
        Mockito.when(queryMock.setParameter(Mockito.anyString(), Mockito.any())).thenReturn(queryMock);
        Mockito.when(userService.getUserDataByEmail(existingUser.getEmail())).thenReturn(existingUser);
        Mockito.when(userService.getUserDataById(existingUser.getId())).thenReturn(existingUser);
        Mockito.when(userService.getUserDataById(UNKNOWN_ID)).thenReturn(null);
        Mockito.when(userService.getUserDataByUsername(existingUser.getUsername())).thenReturn(existingUser);
        Mockito.when(userService.getUserDataByUsername(newUserName)).thenReturn(newUser);
    }

    @Test(expected = PasswordsDoNotMatchException.class)
    public void shouldThrowPwdsDoNotMatch() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PasswordsDoNotMatchException, PwTooWeakException, IncompleteUserDataException, InvalidNameException, UserOptimisticLockException {
        auth.registerUser("username", "mail@mail.de", "Arnold", "Schwarzenegger", STRONG_PWD, STRONG_PWD + "thisSuffixWillCauseTrouble!");
    }

    @Test(expected = AlreadyRegisteredUsernameException.class)
    public void shouldThrowUsernameAlreadyRegistered() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PasswordsDoNotMatchException, PwTooWeakException, IncompleteUserDataException, InvalidNameException, UserOptimisticLockException {
        auth.registerUser(existingUser.getUsername(), "mail@mail.de", "Arnold", "Schwarzenegger", STRONG_PWD, STRONG_PWD);
    }

    @Test(expected = InvalidOrRegisteredMailException.class)
    public void shouldThrowMailAlreadyRegistered() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PasswordsDoNotMatchException, PwTooWeakException, IncompleteUserDataException, InvalidNameException, UserOptimisticLockException {
        auth.registerUser("username", existingUser.getEmail(), "Arnold", "Schwarzenegger", STRONG_PWD, STRONG_PWD);
    }

    @Test(expected = InvalidOrRegisteredMailException.class)
    public void shouldThrowMailInvalid() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PwTooWeakException, PasswordsDoNotMatchException, IncompleteUserDataException, InvalidNameException, UserOptimisticLockException {
        auth.registerUser("username", "invalidmail", "Arnold", "Schwarzenegger", STRONG_PWD, STRONG_PWD);
    }

    @Test(expected = InvalidNameException.class)
    public void shouldNotRegisterUserWithEmptyTrimmedData() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PwTooWeakException, PasswordsDoNotMatchException, IncompleteUserDataException, InvalidNameException, UserOptimisticLockException {
        auth.registerUser("username", "invalidmail@gmail.com", "    ", "Schwarzenegger", STRONG_PWD, STRONG_PWD);
    }

    @Test(expected = IncompleteUserDataException.class)
    public void shouldNotRegisterUserWithIncompleteDataUsername() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PasswordsDoNotMatchException, PwTooWeakException, IncompleteUserDataException, InvalidNameException, UserOptimisticLockException {
        auth.registerUser(null, "mail@mail.de", "Arnold", "Schwarzenegger", STRONG_PWD, STRONG_PWD);
    }

    @Test(expected = IncompleteUserDataException.class)
    public void shouldNotRegisterUserWithIncompleteDataEmail() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PasswordsDoNotMatchException, PwTooWeakException, IncompleteUserDataException, InvalidNameException, UserOptimisticLockException {
        auth.registerUser("username", null, "Arnold", "Schwarzenegger", STRONG_PWD, STRONG_PWD);
    }

    @Test(expected = IncompleteUserDataException.class)
    public void shouldNotRegisterUserWithIncompleteDataFirstname() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PasswordsDoNotMatchException, PwTooWeakException, IncompleteUserDataException, InvalidNameException, UserOptimisticLockException {
        auth.registerUser("username", "mail@mail.de", null, "Schwarzenegger", STRONG_PWD, STRONG_PWD);
    }

    @Test(expected = IncompleteUserDataException.class)
    public void shouldNotRegisterUserWithIncompleteDataLastname() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PasswordsDoNotMatchException, PwTooWeakException, IncompleteUserDataException, InvalidNameException, UserOptimisticLockException {
        auth.registerUser("username", "mail@mail.de", "Arnold", null, STRONG_PWD, STRONG_PWD);
    }

    @Test(expected = IncompleteUserDataException.class)
    public void shouldNotRegisterUserWithIncompleteDataPwd() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PasswordsDoNotMatchException, PwTooWeakException, IncompleteUserDataException, InvalidNameException, UserOptimisticLockException {
        auth.registerUser("username", "mail@mail.de", "Arnold", "Schwarzenegger", null, STRONG_PWD);
    }

    @Test(expected = IncompleteUserDataException.class)
    public void shouldNotRegisterUserWithIncompleteDataConfirmPwd() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PasswordsDoNotMatchException, PwTooWeakException, IncompleteUserDataException, InvalidNameException, UserOptimisticLockException {
        auth.registerUser("username", "mail@mail.de", "Arnold", "Schwarzenegger", STRONG_PWD, null);
    }

    @Test
    public void shouldRegisterUser() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PasswordsDoNotMatchException, PwTooWeakException, IncompleteUserDataException, InvalidNameException, UserOptimisticLockException {
        Mockito.when(queryMock.getSingleResult()).thenReturn(
                new LoginData(new User(21L, newUser.getUsername(), newUser.getEmail(), newUser.getFirstName(), newUser.getLastName()),
                        BCrypt.withDefaults().hashToString(12, STRONG_PWD.toCharArray())));
        Mockito.when(queryMock.getResultList()).thenThrow(NoResultException.class);
        final LoggedInUser loggedInUser = auth.registerUser("username", "mail@mail.de", "Arnold", "Schwarzenegger", STRONG_PWD, STRONG_PWD);
        final User registeredUser = userService.getUserDataByUsername(newUser.getUsername());

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
    public void shouldNotLoginNotExistingUser() throws UserOptimisticLockException {
        Assert.assertNull(auth.loginUser(UNKNOWN_MAIL, STRONG_PWD));
    }

    @Test
    public void shouldNotLoginIfPwdIsWrong() throws UserOptimisticLockException {
        Assert.assertNull(auth.loginUser(existingUser.getEmail(), STRONG_PWD + "thisSuffixWillCauseTrouble!"));
    }

    @Test
    public void shouldNotLoginIfNOTfOUND() throws UserOptimisticLockException {
        Mockito.when(queryMock.getSingleResult()).thenThrow(NoResultException.class);
        Assert.assertNull(auth.loginUser("does not matter anyway", STRONG_PWD + "thisSuffixWillCauseTrouble!"));
    }

    @Test
    public void shouldLoginUser() throws UserOptimisticLockException {
        Mockito.when(queryMock.getSingleResult()).thenReturn(existingUser);
        final User expected = userService.getUserDataByEmail(existingUser.getEmail());
        Mockito.when(queryMock.getSingleResult()).thenReturn(new LoginData(existingUser, BCrypt.withDefaults().hashToString(12, STRONG_PWD.toCharArray())));
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
    public void shouldNotFetchUserDueToNoToken() throws UserOptimisticLockException {
        Assert.assertNull(auth.fetchUser(null));
    }

    @Test
    public void shouldNotFetchUserDueToExpiredToken() throws UserOptimisticLockException {
        Assert.assertNull(auth.fetchUser(expiredAuthToken));
    }

    @Test
    public void shouldNotFetchUserDueToNoMatchingUser() throws UserOptimisticLockException {
        Assert.assertNull(auth.fetchUser(authTokenOfUnknownUser));
    }

    @Test
    public void shouldFetchUser() throws UserOptimisticLockException {
        final User fetchedUser = auth.fetchUser(validAuthToken);
        Assert.assertNotNull(fetchedUser);
        Assert.assertEquals(existingUser.toString(), fetchedUser.toString());
    }

    @Test
    public void shouldNotRefreshTokensIfRefreshIsNull() throws UserOptimisticLockException {
        Assert.assertNull(auth.refreshAuthTokens(null));
    }

    @Test
    public void shouldNotRefreshTokensIfNotFound() throws UserOptimisticLockException {
        Mockito.when(queryMock.getSingleResult()).thenThrow(NoResultException.class);
        Assert.assertNull(auth.refreshAuthTokens(validRefreshToken));
    }

    @Test
    public void shouldNotRefreshTokensIfRefreshTokenExpired() throws UserOptimisticLockException {
        Assert.assertNull(auth.refreshAuthTokens(expiredRefreshToken));
    }

    @Test
    public void shouldNotRefreshTokensIfNoUserIsFoundForToken() throws UserOptimisticLockException {
        Assert.assertNull(auth.refreshAuthTokens(refreshTokenOfUnknownUser));
    }

    @Test
    public void shouldRefreshTokens() throws UserOptimisticLockException {
        try {
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
    public void updatingPasswordShouldFailIfPrevPwdWrong() throws PasswordsDoNotMatchException, PwTooWeakException, InvalidFirstPwdException, InvalidUserException, UserOptimisticLockException {
        final String falsePwd = "123thisWasNotMyPrevPwd";
        final String dbPwd = "PR€T7Y_5TR0NG_P@S$W0RD_2";
        Mockito.when(queryMock.getSingleResult()).thenReturn(new LoginData(existingUser, BCrypt.withDefaults().hashToString(12, dbPwd.toCharArray())));
        final String newPwd = "PR€T7Y_5TR0NG_P@S$W0RD";
        auth.updateUserPassword(existingUser, falsePwd, newPwd, newPwd);
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowInvalidUserExceptionIfUserNotFound() throws PasswordsDoNotMatchException, PwTooWeakException, InvalidFirstPwdException, InvalidUserException, UserOptimisticLockException {
        Mockito.when(queryMock.getSingleResult()).thenThrow(NoResultException.class);
        auth.updateUserPassword(existingUser, "does", "not", "matter");
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowInvalidUserExceptionIfUserIsNull() throws PasswordsDoNotMatchException, PwTooWeakException, InvalidFirstPwdException, InvalidUserException, UserOptimisticLockException {
        auth.updateUserPassword(existingUser, "does", "not", "matter");
    }

    @Test(expected = PasswordsDoNotMatchException.class)
    public void updatingPasswordShouldFailIfPwdsDoNotMatch() throws PasswordsDoNotMatchException, PwTooWeakException, InvalidFirstPwdException, InvalidUserException, UserOptimisticLockException {
        final String currentPwd = "123thisWasNotMyPrevPwd";
        final String newPwd = "PR€T7Y_5TR0NG_P@S$W0RD";
        final String newPwd2 = "PR€T7Y_5TR0NG_P@S$W0RD2";
        LoginData data = new LoginData();
        data.setPasswordHash(BCrypt.withDefaults().hashToString(12, currentPwd.toCharArray()));
        Mockito.when(queryMock.getSingleResult()).thenReturn(data);
        auth.updateUserPassword(existingUser, currentPwd, newPwd, newPwd2);
    }

    @Test
    public void updatingPasswordShouldHaveDbStatus0() throws PasswordsDoNotMatchException, PwTooWeakException, InvalidFirstPwdException, InvalidUserException, UserOptimisticLockException {
        final String newPwd = "PR€T7Y_5TR0NG_P@S$W0RD";
        final String currentPwd = "pr€V10U5PwD";
        Mockito.when(queryMock.getSingleResult()).thenReturn(new LoginData(existingUser, BCrypt.withDefaults().hashToString(12, currentPwd.toCharArray())));
        final int statusCode = auth.updateUserPassword(existingUser, currentPwd, newPwd, newPwd);
        Assert.assertEquals(0, statusCode);
    }

    @Test
    public void shouldNotHaveAccessRightsIfAuthTokenNull() throws UserOptimisticLockException {
        Assert.assertFalse(auth.hasAccessRights(null));
    }

    @Test
    public void shouldNotHaveAccessRightsIfAuthTokenExpired() throws UserOptimisticLockException {
        Assert.assertFalse(auth.hasAccessRights(expiredAuthToken));
    }

    @Test
    public void shouldHaveAccessRightsIfAuthTokenIsValid() throws UserOptimisticLockException {
        Assert.assertTrue(auth.hasAccessRights(validAuthToken));
    }

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
