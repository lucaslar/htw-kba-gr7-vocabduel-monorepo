package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.PersonalFinishedGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.Result;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(Parameterized.class)
public class ScoreAdministrationImplFinishededGamesTest {

    // Fixture/to be added to mocking framework:
    // - User with id 2020 (exists)
    // - User with id 42:
    //      - Record: 2 - 1, all against #2020
    //      - no games the user has been challenged to
    // - User with id 4711:
    //      - No finished game yet
    //      - Challenged to game by #42 without open questions, i.e. game can be finished

    private static final User USER_42 = new User(42L);
    private static final User USER_4711 = new User(4711L);
    private static final User USER_2020 = new User(2020L);

    @Parameterized.Parameters(name = "{index}: getPersonalFinishedGames(\"{0}\")")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {USER_42, mockedFinishedGamesFor42()},
                {USER_4711, Collections.emptyList()},
        });
    }

    // TODO: Long term: Store object of type finished vocabduel game in mock db instead of using personalized
    //  (=> finished game is personalized by service!)
    private static List<PersonalFinishedGame> mockedFinishedGamesFor42() {
        AtomicInteger i = new AtomicInteger();
        return Stream.of(new PersonalFinishedGame(), new PersonalFinishedGame(), new PersonalFinishedGame())
                .peek((game) -> {
                    game.setOpponent(USER_2020);
                    game.setOwnPoints(3 - i.get());
                    game.setOpponentPoints(i.get());
                    game.setGameResult(game.getOwnPoints() > 1 ? Result.WIN : Result.LOSS);
                    // TODO: Add more?
                    i.getAndIncrement();
                })
                .collect(Collectors.toList());
    }

    private final User user;
    private final List<PersonalFinishedGame> finishedGamesExp;
    private ScoreAdministrationImpl scoreAdministration;
    private List<PersonalFinishedGame> finishedGames;

    public ScoreAdministrationImplFinishededGamesTest(final User user, final List<PersonalFinishedGame> finishedGamesExp) {
        this.user = user;
        this.finishedGamesExp = finishedGamesExp;
    }

    @Before
    public void setup() {
        scoreAdministration = new ScoreAdministrationImpl();
        finishedGames = scoreAdministration.getPersonalFinishedGames(user);
    }

    @Test()
    public void testAmountOfFinishedGames() {
        Assert.assertEquals(finishedGames.size(), finishedGamesExp.size());
    }

    @Test
    public void testCorrectResultsDetermined() {
        finishedGames.forEach(game -> {
            final boolean isMorePoints = game.getOpponentPoints() < game.getOwnPoints();
            if (game.getGameResult() == Result.WIN) {
                Assert.assertTrue("Expected to have more points than opponent", isMorePoints);
            } else Assert.assertFalse("Expected to have less points than opponent", isMorePoints);
        });
    }
}
