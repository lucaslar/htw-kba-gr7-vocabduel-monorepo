package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Comparator;
import java.util.List;


public class GameAdministrationImplUnfinishedGamesTest {

    private List<VocabduelGame> unfinishedGames;

    @Before
    public void setup() {
        User user = new User(2020L);
        GameAdministrationImpl gameAdministration = Mockito.mock(GameAdministrationImpl.class);
        Mockito.when(gameAdministration.getPersonalChallengedGames(
                user)).thenReturn(null); // TODO: Continue with implementation - Mock should return server response for unfinished game
        unfinishedGames = gameAdministration.getPersonalChallengedGames(user);
        Assert.assertNotNull(unfinishedGames);
    }

    @Test()
    public void shouldGetEmptyChallengedGames(){
        Assert.assertTrue(unfinishedGames.isEmpty());
    }

    @Test()
    public void shouldGetEveryGameOnce() {
        unfinishedGames.sort(compareById);
        final long[] gameId = {0};
        unfinishedGames.forEach(game -> {
            if (gameId[0] == 0 || gameId[0] != game.getId()) gameId[0] = game.getId();
            else Assert.fail();
        });
    }

    Comparator<VocabduelGame> compareById = Comparator.comparing(VocabduelGame::getId);

}
