package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.notFinishedGameGivenMoreThanOnceException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class GameAdministrationImplUnfinishedGamesTest {

    private User user;
    private GameAdministrationImpl gameAdministration;
    private List<VocabduelGame> unfinishedGames;

    @Before
    public void setup() {
        // TODO: mocking, so tests about isEmpty() and data in personalChallengedGames can be tested??
        user = new User(2020L);
        gameAdministration = new GameAdministrationImpl();
        unfinishedGames = gameAdministration.getPersonalChallengedGames(user);
    }

    @Test()
    public void shouldGetEmptyChallengedGames(){
        unfinishedGames = gameAdministration.getPersonalChallengedGames(user);
        Assert.assertTrue(unfinishedGames.isEmpty());
    }

    // TODO: exception richtig gesetzt?
    @Test()
    public void shouldGetEveryGameOnce() throws notFinishedGameGivenMoreThanOnceException {
        Collections.sort(unfinishedGames, compareById);
        final long[] gameId = {0};
        unfinishedGames.forEach(game -> {
            if (gameId[0] == 0 || gameId[0] != game.getId()) gameId[0] = game.getId();
            else new notFinishedGameGivenMoreThanOnceException();
        });
    }

    Comparator<VocabduelGame> compareById = (VocabduelGame game1, VocabduelGame game2) -> game1.getId().compareTo(game2.getId());

}
