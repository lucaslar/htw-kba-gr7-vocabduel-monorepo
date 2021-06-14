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
public class InvalidPwdsTest {

    @Parameterized.Parameters(name = "{index}: registration and pwd update test with invalid password \"{0}\"")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"onlylowercase"}, // only lowercase
                {"onlyuppercase"}, // only lowercase
                {"1234567890"}, // only numbers
                {"._<-|->_."}, // only special characters
                {"Abc"} // too short
        });
    }

    // Rule instead of @RunWith since one class can only have one @RunWith,
    // see: https://stackoverflow.com/a/12606503/8228988
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private UserServiceImpl userAdministration;
    private AuthServiceImpl auth;
    private User existingUser;
    private final String PWD;

    private final String PREVIOUS_PWD = "ThisWasTheUser'sPreviousPwd"; // TODO: Mock => this pwd was indeed the user's previous pwd

    public InvalidPwdsTest(final String pwd) {
        this.PWD = pwd;
    }

    @Before
    public void setup() throws PasswordsDoNotMatchException, PwTooWeakException, InvalidFirstPwdException {
        auth = new AuthServiceImpl(new UserServiceImpl());

        existingUser = new User(42L);
        existingUser.setEmail("existinguser@user.de");
        existingUser.setUsername("existinguser");
        existingUser.setFirstName("Existing");
        existingUser.setLastName("User");

        // In the future, this method will be called in `updateUserPassword` => mock it in tests
        Mockito.when(userAdministration.getUserDataByEmail(existingUser.getEmail())).thenReturn(existingUser);

        // Don't mock updateUserPassword function
        Mockito.when(auth.updateUserPassword(existingUser, PREVIOUS_PWD, PWD, PWD)).thenCallRealMethod();
    }

    @Test(expected = PwTooWeakException.class)
    public void shouldThrowPwdTooWeakInRegistration() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PasswordsDoNotMatchException, PwTooWeakException, IncompleteUserDataException {
        auth.registerUser("newuser", "newuser@user.de", "New", "User", PWD, PWD);
    }

    @Test(expected = PwTooWeakException.class)
    public void shouldThrowPwdTooWeakInUpdate() throws PasswordsDoNotMatchException, PwTooWeakException, InvalidFirstPwdException {
        auth.updateUserPassword(existingUser, PREVIOUS_PWD, PWD, PWD);
    }
}
