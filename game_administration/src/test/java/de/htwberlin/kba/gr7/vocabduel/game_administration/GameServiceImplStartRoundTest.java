package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.assets.GameDataMock;
import de.htwberlin.kba.gr7.vocabduel.game_administration.dao.*;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.GameOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.NoAccessException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelRound;
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

@RunWith(MockitoJUnitRunner.class)
public class GameServiceImplStartRoundTest {

    private GameServiceImpl gameAdministration;
    private GameDataMock mock;
    @Mock
    private Query queryMock;
    @Mock
    private UserService userService;
    @Mock
    private VocabularyService vocabularyService;
    @Mock
    private EntityManager entityManager;

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
        Mockito.when(queryMock.setMaxResults(Mockito.anyInt())).thenReturn(queryMock);
        Mockito.when(queryMock.getSingleResult()).thenReturn(mock.mockVocabduelRound());
    }

    @Test()
    public void shouldHaveRoundData() throws NoAccessException, GameOptimisticLockException {
        VocabduelRound newRoundRes = gameAdministration.startRound(
                mock.mockSampleUser(), mock.mockVocabduelGame().getId()
        );
        Assert.assertNotNull(newRoundRes);
        Assert.assertTrue(newRoundRes.getAnswers().size() >= 2);
        Assert.assertNotNull(newRoundRes.getQuestion());
    }

    @Test(expected = NoAccessException.class)
    public void shouldThrowNoAccessExceptionIfNoRoundFound() throws NoAccessException, GameOptimisticLockException {
        Mockito.when(queryMock.getSingleResult()).thenThrow(NoResultException.class);
        gameAdministration.startRound(mock.mockSampleUser(), mock.mockVocabduelGame().getId());
    }
}
