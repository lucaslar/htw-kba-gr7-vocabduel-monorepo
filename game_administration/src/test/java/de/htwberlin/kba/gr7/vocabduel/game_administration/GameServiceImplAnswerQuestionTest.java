package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.assets.GameDataMock;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.CorrectAnswerResult;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.Result;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.VocabularyService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GameServiceImplAnswerQuestionTest {

    private GameServiceImpl gameAdministration;
    private UserService userService; // to be mocked
    private VocabularyService vocabularyService;
    private GameDataMock mock;

    @Before
    public void setup(){
        gameAdministration = new GameServiceImpl(userService, vocabularyService);
        mock = new GameDataMock();
    }

    @Test()
    public void shouldGetLossWithIncorrectAnswer(){
        CorrectAnswerResult result = gameAdministration.answerQuestion(
                mock.mockSampleUser(), mock.mockVocabduelRound(), mock.mockVocabduelRound().getAnswers().get(2));
        Assert.assertNotNull(result);
        Assert.assertEquals(Result.LOSS, result.getResult());
        Assert.assertNotNull(result.getCorrectAnswer());
        Assert.assertEquals(result.getCorrectAnswer(), mock.mockVocabduelRound().getAnswers().get(1));
    }

    @Test()
    public void shouldGetWinWithCorrectAnswer(){
        CorrectAnswerResult result = gameAdministration.answerQuestion(
                mock.mockSampleUser(), mock.mockVocabduelRound(), mock.mockVocabduelRound().getAnswers().get(1));
        Assert.assertNotNull(result);
        Assert.assertEquals(Result.WIN, result.getResult());
        Assert.assertNull(result.getCorrectAnswer());
    }
}
