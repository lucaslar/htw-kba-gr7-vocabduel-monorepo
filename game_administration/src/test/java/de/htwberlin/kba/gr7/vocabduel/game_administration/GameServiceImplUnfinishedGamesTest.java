package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.assets.GameDataMock;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.VocabularyService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;


public class GameServiceImplUnfinishedGamesTest {

    private GameServiceImpl gameAdministration;
    private GameDataMock mock;
    private UserService userService;
    private VocabularyService vocabularyService;

    @Before
    public void setup() {
        gameAdministration = new GameServiceImpl(userService, vocabularyService);
        mock = new GameDataMock();
    }

    @Test()
    public void shouldGetEmptyUnfinishedGamesList() {
        final List<VocabduelGame> unfinishedGames = gameAdministration.getPersonalChallengedGames(new User(4711L));
        Assert.assertNotNull(unfinishedGames);
        Assert.assertTrue(unfinishedGames.isEmpty());
    }

    @Test()
    public void shouldGetUnfinishedGamesListWithEveryGameOnce() {
        final List<VocabduelGame> unfinishedGames = gameAdministration.getPersonalChallengedGames(mock.mockSampleUser());
        Assert.assertNotNull(unfinishedGames);
        Assert.assertTrue(unfinishedGames.size() > 1);
        List<VocabduelGame> uniques = unfinishedGames.stream().distinct().collect(Collectors.toList());
        Assert.assertEquals(uniques.size(), unfinishedGames.size());
    }

    @Test()
    public void shouldGetUnfinishedGamesTheUserIsPlayerOf() {
        final User user = mock.mockSampleUser();
        final List<VocabduelGame> unfinishedGames = gameAdministration.getPersonalChallengedGames(user);
        Assert.assertNotNull(unfinishedGames);
        unfinishedGames.forEach(g -> Assert.assertTrue(g.getPlayerA().equals(user) || g.getPlayerB().equals(user)));
    }
}
