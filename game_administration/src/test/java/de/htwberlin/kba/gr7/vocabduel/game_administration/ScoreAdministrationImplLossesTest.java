package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class ScoreAdministrationImplLossesTest {

    // Fixture/to be added to mocking framework:
    // - User with id 2020 (exists)
    // - User with id 42:
    //      - Record: 2 - 1
    //      - no games the user has been challenged to
    // - User with id 4711:
    //      - No finished game yet
    //      - Challenged to game by #42 without open questions, i.e. game can be finished

    private static final User USER_42 = new User(42L);
    private static final User USER_4711 = new User(4711L);

    @Parameterized.Parameters(name = "{index}: getTotalLossesOfUser(\"{0}\")")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {USER_42, 1},
                {USER_4711, 0},
        });
    }

    private ScoreAdministrationImpl scoreAdministration;
    private final User user;
    private final int wins;

    public ScoreAdministrationImplLossesTest(final User user, final int losses) {
        this.user = user;
        this.wins = losses;
    }

    @Before
    public void setup() {
        scoreAdministration = new ScoreAdministrationImpl();
    }

    @Test()
    public void testLosses() {
        Assert.assertEquals(scoreAdministration.getTotalLossesOfUser(user), wins);
    }
}
