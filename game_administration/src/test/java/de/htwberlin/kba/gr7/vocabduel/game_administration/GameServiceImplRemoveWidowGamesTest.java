package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.dao.*;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.InternalGameModuleException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.FinishedVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.RunningVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceImplRemoveWidowGamesTest {

    private GameServiceImpl gameService;
    @Mock
    private VocabularyService vocabularyService;
    @Mock
    private UserService userService;
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
        gameService = new GameServiceImpl(userService, vocabularyService, runningVocabduelGameDAO, vocabduelRoundDAO, finishedVocabduelGameDAO);

        Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryMock);
    }

    @Test
    public void shouldRemoveWidowGamesWhenZeroGamesToRemove() throws InternalGameModuleException {
        Mockito.when(queryMock.getResultList()).thenThrow(NoResultException.class);
        final int statusCode = gameService.removeWidowGames();
        Assert.assertEquals(0, statusCode);
    }

    @Test
    public void shouldRemoveWidowGamesWhenFinishedAndRunningGameToRemove() throws InternalGameModuleException {
        final RunningVocabduelGame runningGame = new RunningVocabduelGame();
        final FinishedVocabduelGame finishedGame = new FinishedVocabduelGame(
                new RunningVocabduelGame(1L, null, null, null, null, null)
        );
        Mockito.when(queryMock.getResultList()).thenReturn(Stream.of(runningGame).collect(Collectors.toList()),
                Stream.of(finishedGame).collect(Collectors.toList()));
        final int statusCode = gameService.removeWidowGames();
        Assert.assertEquals(0, statusCode);
    }

    @Test
    public void shouldRemoveWidowGamesWhenFinishedGameToRemove() throws InternalGameModuleException {
        final FinishedVocabduelGame finishedGame = new FinishedVocabduelGame(
                new RunningVocabduelGame(1L, null, null, null, null, null)
        );
        Mockito.when(queryMock.getResultList()).thenThrow(NoResultException.class).thenReturn(
                Stream.of(finishedGame).collect(Collectors.toList()));
        final int statusCode = gameService.removeWidowGames();
        Assert.assertEquals(0, statusCode);
    }

    @Test
    public void shouldRemoveWidowGamesWhenRunningGameToRemove() throws InternalGameModuleException {
        final RunningVocabduelGame runningGame = new RunningVocabduelGame();
        Mockito.when(queryMock.getResultList()).thenReturn(
                Stream.of(runningGame).collect(Collectors.toList())).thenThrow(NoResultException.class);
        final int statusCode = gameService.removeWidowGames();
        Assert.assertEquals(0, statusCode);
    }

}
