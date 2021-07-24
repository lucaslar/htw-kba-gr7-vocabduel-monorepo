package de.htwberlin.kba.gr7.vocabduel.user_administration;

import at.favre.lib.crypto.bcrypt.BCrypt;
import de.htwberlin.kba.gr7.vocabduel.user_administration.dao.LoginDataDAO;
import de.htwberlin.kba.gr7.vocabduel.user_administration.dao.LoginDataDAOImpl;
import de.htwberlin.kba.gr7.vocabduel.user_administration.dao.StoredRefreshTokenDAO;
import de.htwberlin.kba.gr7.vocabduel.user_administration.dao.StoredRefreshTokenDAOImpl;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.AuthService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.LoginData;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import javax.naming.InvalidNameException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.Arrays;

@RunWith(Parameterized.class)
public class ValidPwdsTest {

    @Parameterized.Parameters(name = "{index}: registration and pwd update test with valid password \"{0}\"")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"UpperAndLowerCase"}, // upper- and lowercase
                {"UPPERANDNR123"}, // uppercase and numbers
                {"UPPERANDSC...!"}, // uppercase and special characters
                {"lowercaseandnr123"}, // lowercase and numbers
                {"lowercaseandsc...!"}, // lowercase and special characters
                {"123...123!!..123"}, // numbers and special characters
                {"lowerUpper123"}, // Numbers, lower- and uppercase
                {"lowerUpper...!"}, // Special characters, lower- and uppercase
                {"lowercase...123"}, // Lowercase, special characters, numbers
                {"UPPERCASE...123"}, // Uppercase, special characters, numbers
                {"PR€T7Y_5TR0NG_P@S$W0RD"}, // numbers, special characters, upper- and lowercase
        });
    }

    // Rule instead of @RunWith since one class can only have one @RunWith,
    // see: https://stackoverflow.com/a/12606503/8228988
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private UserService userService;
    @Mock
    private AuthService auth;
    @Mock
    private EntityManager entityManager;
    @Mock
    private EntityTransaction entityTransaction;
    @Mock
    private Query queryMock;
    private User existingUser;
    private final String PWD;

    private final String PREVIOUS_PWD = "ThisWasTheUser'sPreviousPwd";

    public ValidPwdsTest(final String pwd) {
        this.PWD = pwd;
    }

    @Before
    public void setup() {
//        final StoredRefreshTokenDAO storedRefreshTokenDAO = new StoredRefreshTokenDAOImpl(entityManager);
//        final LoginDataDAO loginDataDAO = new LoginDataDAOImpl(entityManager);
//
//        auth = new AuthServiceImpl(userService, loginDataDAO, storedRefreshTokenDAO);

        existingUser = new User(42L,
                "existinguser",
                "existinguser@user.de",
                "Existing",
                "User");

        Mockito.when(entityManager.getTransaction()).thenReturn(entityTransaction);
        Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryMock);
        Mockito.when(queryMock.setParameter(Mockito.anyString(), Mockito.any())).thenReturn(queryMock);
        Mockito.when(queryMock.getSingleResult()).thenReturn(new LoginData(existingUser, BCrypt.withDefaults().hashToString(12, PREVIOUS_PWD.toCharArray())));
    }

    @Test
    public void shouldNotThrowPwdTooWeakInRegistration() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PasswordsDoNotMatchException, PwTooWeakException, IncompleteUserDataException, InvalidNameException {
        auth.registerUser("newuser", "newuser@user.de", "New", "User", PWD, PWD);
    }

    @Test
    public void shouldNotThrowPwdTooWeakInUpdate() throws PasswordsDoNotMatchException, PwTooWeakException, InvalidFirstPwdException, InvalidUserException {
        auth.updateUserPassword(existingUser, PREVIOUS_PWD, PWD, PWD);
    }
}
