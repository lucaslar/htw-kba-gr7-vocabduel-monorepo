package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.FinishedVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.RunningVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.VocabularyService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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
    private EntityTransaction entityTransaction;
    @Mock
    private Query queryMock;

    @Before
    public void setup(){
        try (MockedStatic<EntityFactoryManagement> emf = Mockito.mockStatic(EntityFactoryManagement.class)) {
            emf.when(EntityFactoryManagement::getManager).thenReturn(entityManager);
            gameService = new GameServiceImpl(userService, vocabularyService);
        }

        Mockito.when(entityManager.getTransaction()).thenReturn(entityTransaction);
        Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryMock);
    }

    @Test
    public void shouldRemoveWidowGamesWhenZeroGamesToRemove(){
        Mockito.when(queryMock.getResultList()).thenThrow(NoResultException.class);
        final int statusCode = gameService.removeWidowGames();
        Assert.assertEquals(0, statusCode);
    }

    @Test
    public void shouldRemoveWidowGamesWhenFinishedAndRunningGameToRemove(){
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
    public void shouldRemoveWidowGamesWhenFinishedGameToRemove(){
        final FinishedVocabduelGame finishedGame = new FinishedVocabduelGame(
                new RunningVocabduelGame(1L, null, null, null, null, null)
        );
        Mockito.when(queryMock.getResultList()).thenThrow(NoResultException.class).thenReturn(
                Stream.of(finishedGame).collect(Collectors.toList()));
        final int statusCode = gameService.removeWidowGames();
        Assert.assertEquals(0, statusCode);
    }

    @Test
    public void shouldRemoveWidowGamesWhenRunningGameToRemove(){
        final RunningVocabduelGame runningGame = new RunningVocabduelGame();
        Mockito.when(queryMock.getResultList()).thenReturn(
                Stream.of(runningGame).collect(Collectors.toList())).thenThrow(NoResultException.class);
        final int statusCode = gameService.removeWidowGames();
        Assert.assertEquals(0, statusCode);
    }

}
