package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.assets.EntityTransactionMock;
import de.htwberlin.kba.gr7.vocabduel.game_administration.assets.GameDataMock;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.VocabularyService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceImplUnfinishedGamesTest {

    private GameServiceImpl gameAdministration;
    private GameDataMock mock;
    @Mock
    private UserService userService;
    @Mock
    private VocabularyService vocabularyService;
    @Mock
    private EntityManager entityManager;
    @Mock
    private Query queryMock;

    @Before
    public void setup() {
        gameAdministration = new GameServiceImpl(userService, vocabularyService, entityManager);
        mock = new GameDataMock();
        Mockito.when(entityManager.getTransaction()).thenReturn(new EntityTransactionMock());
        Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryMock);
        Mockito.when(queryMock.setParameter(Mockito.anyString(), Mockito.anyObject())).thenReturn(queryMock);

    }

    @Test()
    public void shouldGetEmptyUnfinishedGamesList() {
        Mockito.when(queryMock.getResultList()).thenReturn(mock.mockEmptyUnfinishedGameList());
        final List<VocabduelGame> unfinishedGames = gameAdministration.getPersonalChallengedGames(new User(4711L));
        Assert.assertNotNull(unfinishedGames);
        Assert.assertTrue(unfinishedGames.isEmpty());
    }

    @Test()
    public void shouldGetUnfinishedGamesListWithEveryGameOnce() {
        Mockito.when(queryMock.getResultList()).thenReturn(mock.mockUnfinishedGameList());
        final List<VocabduelGame> unfinishedGames = gameAdministration.getPersonalChallengedGames(mock.mockSampleUser());
        Assert.assertNotNull(unfinishedGames);
        Assert.assertTrue(unfinishedGames.size() > 1);
        List<VocabduelGame> uniques = unfinishedGames.stream().distinct().collect(Collectors.toList());
        Assert.assertEquals(uniques.size(), unfinishedGames.size());
    }

    @Test()
    public void shouldGetUnfinishedGamesTheUserIsPlayerOf() {
        Mockito.when(queryMock.getResultList()).thenReturn(mock.mockUnfinishedGameList());
        final User user = mock.mockSampleUser();
        final List<VocabduelGame> unfinishedGames = gameAdministration.getPersonalChallengedGames(user);
        Assert.assertNotNull(unfinishedGames);
        unfinishedGames.forEach(g -> Assert.assertTrue(g.getPlayerA().equals(user) || g.getPlayerB().equals(user)));
    }
}
