package de.htwberlin.kba.gr7.vocabduel.user_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.AlreadyRegisteredMailException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.AlreadyRegisteredUsernameException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.PasswordsDoNotMatchException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.PwTooWeakException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class AuthImplInvalidPwdsTest {

    @Parameterized.Parameters(name = "{index}: getPersonalFinishedGames(\"{0}\")")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"onlylowercase"}, // only lowercase
                {"onlyuppercase"}, // only lowercase
                {"1234567890"}, // only numbers
                {"Abc"} // too short
        });
    }

    private final String pwd;
    private AuthImpl auth;

    public AuthImplInvalidPwdsTest(final String pwd) {
        this.pwd = pwd;
    }

    @Before
    public void setup() {
        auth = new AuthImpl();
    }

    @Test(expected = PwTooWeakException.class)
    public void shouldThrowPwdTooWeakIfOnlyOneCharType() throws AlreadyRegisteredUsernameException, AlreadyRegisteredMailException, PasswordsDoNotMatchException, PwTooWeakException {
        auth.registerUser(new User(null), pwd, pwd);
    }
}
