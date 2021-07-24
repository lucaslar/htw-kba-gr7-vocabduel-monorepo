package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.assets.GameDataMock;
import de.htwberlin.kba.gr7.vocabduel.game_administration.dao.*;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.RunningVocabduelGame;
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
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

        final RunningVocabduelGameDAOImpl runningVocabduelGameDAO = new RunningVocabduelGameDAOImpl();
        runningVocabduelGameDAO.setEntityManager(entityManager);
        final VocabduelRoundDAOImpl vocabduelRoundDAO = new VocabduelRoundDAOImpl();
        vocabduelRoundDAO.setEntityManager(entityManager);
        final FinishedVocabduelGameDAOImpl finishedVocabduelGameDAO = new FinishedVocabduelGameDAOImpl();
        finishedVocabduelGameDAO.setEntityManager(entityManager);

        gameAdministration = new GameServiceImpl(userService, vocabularyService, runningVocabduelGameDAO, vocabduelRoundDAO, finishedVocabduelGameDAO);

        mock = new GameDataMock();
        Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryMock);
        Mockito.when(queryMock.setParameter(Mockito.anyString(), Mockito.any())).thenReturn(queryMock);

    }

    @Test()
    public void shouldGetNullIfNoUnfinishedGames() {
        Mockito.when(queryMock.getResultList()).thenThrow(NoResultException.class);
        final List<RunningVocabduelGame> unfinishedGames = gameAdministration.getPersonalChallengedGames(new User(4711L));
        Assert.assertNull(unfinishedGames);
    }

    @Test()
    public void shouldGetUnfinishedGamesListWithEveryGameOnce() {
        Mockito.when(queryMock.getResultList()).thenReturn(mock.mockUnfinishedGameList());
        final List<RunningVocabduelGame> unfinishedGames = gameAdministration.getPersonalChallengedGames(mock.mockSampleUser());
        Assert.assertNotNull(unfinishedGames);
        Assert.assertTrue(unfinishedGames.size() > 1);
        List<VocabduelGame> uniques = unfinishedGames.stream().distinct().collect(Collectors.toList());
        Assert.assertEquals(uniques.size(), unfinishedGames.size());
    }

    @Test()
    public void shouldGetUnfinishedGamesTheUserIsPlayerOf() {
        Mockito.when(queryMock.getResultList()).thenReturn(mock.mockUnfinishedGameList());
        final User user = mock.mockSampleUser();
        final List<RunningVocabduelGame> unfinishedGames = gameAdministration.getPersonalChallengedGames(user);
        Assert.assertNotNull(unfinishedGames);
        unfinishedGames.forEach(g -> Assert.assertTrue(g.getPlayerA().equals(user) || g.getPlayerB().equals(user)));
    }
}
