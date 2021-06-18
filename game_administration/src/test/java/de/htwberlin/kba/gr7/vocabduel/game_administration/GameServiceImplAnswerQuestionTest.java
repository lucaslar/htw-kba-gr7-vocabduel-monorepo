package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.assets.EntityTransactionMock;
import de.htwberlin.kba.gr7.vocabduel.game_administration.assets.GameDataMock;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.InvalidAnswerNrException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.NoAccessException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.CorrectAnswerResult;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.Result;
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
public class GameServiceImplAnswerQuestionTest {

    private GameServiceImpl gameAdministration;
    @Mock
    private UserService userService;
    @Mock
    private VocabularyService vocabularyService;
    @Mock
    private EntityManager entityManager;
    @Mock
    private Query queryMock;
    private GameDataMock mock;

    @Before
    public void setup(){
        gameAdministration = new GameServiceImpl(userService, vocabularyService, entityManager);
        mock = new GameDataMock();

        Mockito.when(entityManager.getTransaction()).thenReturn(new EntityTransactionMock());
        Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryMock);
        Mockito.when(queryMock.setParameter(Mockito.anyString(), Mockito.anyObject())).thenReturn(queryMock);
        Mockito.when(queryMock.setMaxResults(Mockito.anyInt())).thenReturn(queryMock);
        Mockito.when(queryMock.getSingleResult()).thenReturn(mock.mockVocabduelRound());
    }

    @Test()
    public void shouldGetLossWithIncorrectAnswer() throws InvalidAnswerNrException, NoAccessException {
        CorrectAnswerResult result = gameAdministration.answerQuestion(
                mock.mockSampleUser(),
                mock.mockVocabduelRound().getGame().getId(),
                mock.mockVocabduelRound().getRoundNr(),
                2);
        Assert.assertNotNull(result);
        Assert.assertEquals(Result.LOSS, result.getResult());
        Assert.assertNotNull(result.getCorrectAnswer());
        Assert.assertEquals(result.getCorrectAnswer().getId(), mock.mockVocabduelRound().getAnswers().get(1).getId());
    }

    @Test()
    public void shouldGetWinWithCorrectAnswer() throws InvalidAnswerNrException, NoAccessException {
        CorrectAnswerResult result = gameAdministration.answerQuestion(
                mock.mockSampleUser(),
                mock.mockVocabduelRound().getGame().getId(),
                mock.mockVocabduelRound().getRoundNr(),
                1);
        Assert.assertNotNull(result);
        Assert.assertEquals(Result.WIN, result.getResult());
        Assert.assertNull(result.getCorrectAnswer());
    }
}
