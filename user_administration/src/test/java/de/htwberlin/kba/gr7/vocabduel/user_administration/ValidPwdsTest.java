package de.htwberlin.kba.gr7.vocabduel.user_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

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
    private UserServiceImpl userAdministration;
    private AuthServiceImpl auth;
    private User newUser;
    private User existingUser;
    private final String PWD;

    private final String PREVIOUS_PWD = "ThisWasTheUser'sPreviousPwd"; // TODO: Mock => this pwd was indeed the user's previous pwd

    public ValidPwdsTest(final String pwd) {
        this.PWD = pwd;
    }

    @Before
    public void setup() throws PasswordsDoNotMatchException, PwTooWeakException, InvalidFirstPwdException {
        auth = new AuthServiceImpl();

        existingUser = new User(42L);
        existingUser.setEmail("existinguser@user.de");
        existingUser.setUsername("existinguser");
        existingUser.setFirstName("Existing");
        existingUser.setLastName("User");

        // In the future, this method will be called in `updateUserPassword` => mock it in tests
        Mockito.when(userAdministration.getUserDataByEmail(existingUser.getEmail())).thenReturn(existingUser);

        // Don't mock updateUserPassword function
        Mockito.when(userAdministration.updateUserPassword(existingUser, PREVIOUS_PWD, PWD, PWD)).thenCallRealMethod();
    }

    @Test(expected = Test.None.class)
    public void shouldNotThrowPwdTooWeakInRegistration() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PasswordsDoNotMatchException, PwTooWeakException, IncompleteUserDataException {
        auth.registerUser("newuser", "newuser@user.de", "New", "User", PWD, PWD);
    }

    @Test(expected = Test.None.class)
    public void shouldNotThrowPwdTooWeakInUpdate() throws PasswordsDoNotMatchException, PwTooWeakException, InvalidFirstPwdException {
        userAdministration.updateUserPassword(existingUser, PREVIOUS_PWD, PWD, PWD);
    }
}
