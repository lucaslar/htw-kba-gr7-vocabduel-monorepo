package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.stream.Collectors;


public class GameAdministrationImplUnfinishedGamesTest {

    private List<VocabduelGame> unfinishedGames;
    private User user;
    private GameAdministrationImpl gameAdministration;

    @Before
    public void setup() {
        user = new User(2020L);
        gameAdministration = new GameAdministrationImpl();
    }

    @Test()
    public void shouldGetEmptyChallengedGames(){
        unfinishedGames = gameAdministration.getPersonalChallengedGames(user);
        Assert.assertNotNull(unfinishedGames);
        Assert.assertTrue(unfinishedGames.isEmpty());
    }

    @Test()
    public void shouldGetEveryGameOnce() {
        unfinishedGames = gameAdministration.getPersonalChallengedGames(user);
        Assert.assertNotNull(unfinishedGames);
        List<VocabduelGame> uniques =unfinishedGames.stream().distinct().collect(Collectors.toList());
        Assert.assertEquals(uniques.size(), unfinishedGames.size());
    }

    // newTest: user im game playerA?
    // newTest: user im game playerB?
}
