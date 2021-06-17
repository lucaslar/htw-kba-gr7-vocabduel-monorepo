package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.assets.EntityTransactionMock;
import de.htwberlin.kba.gr7.vocabduel.game_administration.assets.GameDataMock;
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
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceImplStartRoundTest {

    private GameServiceImpl gameAdministration;
    private VocabduelRound newRoundRes;
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
        gameAdministration = new GameServiceImpl(userService, vocabularyService, entityManager);
        mock = new GameDataMock();
        Mockito.when(entityManager.getTransaction()).thenReturn(new EntityTransactionMock());
        Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryMock);
        Mockito.when(queryMock.setParameter(Mockito.anyString(), Mockito.anyObject())).thenReturn(queryMock);
        Mockito.when(queryMock.setMaxResults(Mockito.anyInt())).thenReturn(queryMock);
        Mockito.when(queryMock.getSingleResult()).thenReturn(mock.mockVocabduelRound());
    }

    @Test()
    public void shouldHaveData() throws NoAccessException {

        newRoundRes = gameAdministration.startRound(
                mock.mockSampleUser(), mock.mockVocabduelGame().getId()
        );
        Assert.assertNotNull(newRoundRes);

        // check given Input
//        Assert.assertEquals(mock.mockVocabduelGame().getId(), newRoundRes.getGameId());
        Assert.assertTrue(newRoundRes.getAnswers().size() >= 2);
        Assert.assertNotNull(newRoundRes.getQuestion());
    }

//    @Test(expected = RoundAlreadyFinishedException.class)
    public void shouldNotStartFinishedRound() throws NoAccessException {
        newRoundRes = gameAdministration.startRound(
                mock.mockSampleUser(), mock.mockVocabduelGame().getId()
        );
    }
}
