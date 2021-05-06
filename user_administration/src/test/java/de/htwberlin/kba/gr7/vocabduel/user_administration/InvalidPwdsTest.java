package de.htwberlin.kba.gr7.vocabduel.user_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.InvalidOrRegisteredMailException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.AlreadyRegisteredUsernameException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.PasswordsDoNotMatchException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.PwTooWeakException;
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

    // Rule instead of @RunWith since one class can only have one @RunWith,
    // see: https://stackoverflow.com/a/12606503/8228988
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

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


    @Mock
    private UserAdministrationImpl userAdministration;
    private AuthImpl auth;
    private User newUser;
    private User existingUser;
    private final String PWD;

    public InvalidPwdsTest(final String pwd) {
        this.PWD = pwd;
    }

    @Before
    public void setup() throws PasswordsDoNotMatchException, PwTooWeakException {
        auth = new AuthImpl();

        newUser = new User(null);
        newUser.setEmail("newuser@user.de");
        newUser.setUsername("newuser");
        newUser.setFirstName("New");
        newUser.setLastName("User");

        existingUser = new User(42L);
        existingUser.setEmail("existinguser@user.de");
        existingUser.setUsername("existinguser");
        existingUser.setFirstName("Existing");
        existingUser.setLastName("User");

        // Don't mock updateUserPassword function
        Mockito.when(userAdministration.getUserDataByEmail(existingUser.getEmail())).thenReturn(existingUser);
        Mockito.when(userAdministration.updateUserPassword(existingUser, PWD, PWD)).thenCallRealMethod();
    }

    @Test(expected = PwTooWeakException.class)
    public void shouldThrowPwdTooWeakInRegistration() throws AlreadyRegisteredUsernameException, InvalidOrRegisteredMailException, PasswordsDoNotMatchException, PwTooWeakException {
        auth.registerUser(newUser, PWD, PWD);
    }

    @Test(expected = PwTooWeakException.class)
    public void shouldThrowPwdTooWeakInUpdate() throws PasswordsDoNotMatchException, PwTooWeakException {
        userAdministration.updateUserPassword(existingUser, PWD, PWD);
    }
}
