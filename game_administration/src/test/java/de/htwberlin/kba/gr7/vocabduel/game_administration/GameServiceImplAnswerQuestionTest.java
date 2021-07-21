package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.assets.GameDataMock;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.InvalidVocabduelGameNrException;
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
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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
    private EntityTransaction entityTransaction;
    @Mock
    private Query queryMock;
    private GameDataMock mock;

    @Before
    public void setup() {
        gameAdministration = new GameServiceImpl(userService, vocabularyService, entityManager);
        mock = new GameDataMock();

        Mockito.when(entityManager.getTransaction()).thenReturn(entityTransaction);
        Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryMock);
        Mockito.when(queryMock.setParameter(Mockito.anyString(), Mockito.any())).thenReturn(queryMock);
        Mockito.when(queryMock.setMaxResults(Mockito.anyInt())).thenReturn(queryMock);
        Mockito.when(queryMock.getSingleResult()).thenReturn(mock.mockVocabduelRound());
    }

    @Test()
    public void shouldGetLossWithIncorrectAnswer() throws InvalidVocabduelGameNrException, NoAccessException {
        CorrectAnswerResult result = gameAdministration.answerQuestion(
                mock.mockSampleUser(),
                mock.mockVocabduelRound().getGame().getId(),
                mock.mockVocabduelRound().getRoundNr(),
                2);
        Assert.assertNotNull(result);
        Assert.assertEquals(Result.LOSS, result.getResult());
        Assert.assertEquals(Result.LOSS, mock.mockVocabduelRound().getResultPlayerA());
        Assert.assertNotNull(result.getCorrectAnswer());
        Assert.assertEquals(result.getCorrectAnswer().getId(), mock.mockVocabduelRound().getAnswers().get(1).getId());
    }

    @Test()
    public void shouldGetWinWithCorrectAnswer() throws InvalidVocabduelGameNrException, NoAccessException {
        CorrectAnswerResult result = gameAdministration.answerQuestion(
                mock.mockSampleUser(),
                mock.mockVocabduelRound().getGame().getId(),
                mock.mockVocabduelRound().getRoundNr(),
                1);
        Assert.assertNotNull(result);
        Assert.assertEquals(Result.WIN, result.getResult());
        Assert.assertEquals(Result.WIN, mock.mockVocabduelRound().getResultPlayerA());
        Assert.assertNull(result.getCorrectAnswer());
    }

    @Test()
    public void shouldAnswerQuestionAsSecondPlayer() throws InvalidVocabduelGameNrException, NoAccessException {
        CorrectAnswerResult result = gameAdministration.answerQuestion(
                mock.mockOpponent(),
                mock.mockVocabduelRound().getGame().getId(),
                mock.mockVocabduelRound().getRoundNr(),
                1);
        Assert.assertNotNull(result);
        Assert.assertEquals(Result.WIN, result.getResult());
        Assert.assertEquals(Result.WIN, mock.mockVocabduelRound().getResultPlayerB());
        Assert.assertNull(result.getCorrectAnswer());
    }

    @Test()
    public void shouldNotAlterRoundDataIfPlayerIsNull() throws InvalidVocabduelGameNrException, NoAccessException {
        CorrectAnswerResult result = gameAdministration.answerQuestion(
                null,
                mock.mockVocabduelRound().getGame().getId(),
                mock.mockVocabduelRound().getRoundNr(),
                1);
        Assert.assertNull(result);
        Assert.assertNull(mock.mockVocabduelRound().getResultPlayerA());
        Assert.assertNull(mock.mockVocabduelRound().getResultPlayerB());
    }

    @Test(expected = InvalidVocabduelGameNrException.class)
    public void shouldThrowInvalidAnswerExceptionIfAnswerNrIsSmaller0() throws NoAccessException, InvalidVocabduelGameNrException {
        gameAdministration.answerQuestion(
                mock.mockSampleUser(),
                mock.mockVocabduelRound().getGame().getId(),
                mock.mockVocabduelRound().getRoundNr(),
                -4711
        );
    }

    @Test(expected = InvalidVocabduelGameNrException.class)
    public void shouldThrowInvalidAnswerExceptionIfAnswerNrIsGreater3() throws NoAccessException, InvalidVocabduelGameNrException {
        gameAdministration.answerQuestion(
                mock.mockSampleUser(),
                mock.mockVocabduelRound().getGame().getId(),
                mock.mockVocabduelRound().getRoundNr(),
                4711
        );
    }

    @Test(expected = InvalidVocabduelGameNrException.class)
    public void shouldThrowInvalidAnswerExceptionIfRoundNrIsSmaller1() throws NoAccessException, InvalidVocabduelGameNrException {
        gameAdministration.answerQuestion(
                mock.mockSampleUser(),
                mock.mockVocabduelRound().getGame().getId(),
                0,
                1
        );
    }

    @Test(expected = InvalidVocabduelGameNrException.class)
    public void shouldThrowInvalidAnswerExceptionIfRoundNrIsGreater9() throws NoAccessException, InvalidVocabduelGameNrException {
        gameAdministration.answerQuestion(
                mock.mockSampleUser(),
                mock.mockVocabduelRound().getGame().getId(),
                10,
                1
        );
    }

    @Test(expected = NoAccessException.class)
    public void shouldThrowNoAccessExceptionIfRoundIsNotNext() throws NoAccessException, InvalidVocabduelGameNrException {
        gameAdministration.answerQuestion(
                mock.mockSampleUser(),
                mock.mockVocabduelRound().getGame().getId(),
                8,
                1
        );
    }
}
