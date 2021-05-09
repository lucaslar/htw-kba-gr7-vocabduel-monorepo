package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.CorrectAnswerResult;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.Result;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.TranslationGroup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;

public class GameAdministrationImplAnswerQuestionTest {

    private GameAdministrationImpl gameAdministration;

    private User user;
    private VocabduelRound newRound;

    @Before
    public void setup(){
        gameAdministration = Mockito.mock(GameAdministrationImpl.class);
        user = new User(2020L);

        // set object VocabduelRound
        newRound = new VocabduelRound();
        newRound.setAnswers(Arrays.asList(
                new TranslationGroup(Collections.singletonList("wrongAnswer1")),
                new TranslationGroup(Collections.singletonList("rightAnswer")),
                new TranslationGroup(Collections.singletonList("wrongAnswer2")),
                new TranslationGroup(Collections.singletonList("wrongAnswer3"))
        ));
    }

    @Test()
    public void shouldGetLossWithIncorrectAnswer(){
        CorrectAnswerResult result = gameAdministration.answerQuestion(
                user, newRound, newRound.getAnswers().get(2));
        Assert.assertNotNull(result);
        Assert.assertEquals(Result.LOSS, result.getResult());
        Assert.assertNotNull(result.getCorrectAnswer());
        Assert.assertEquals(result.getCorrectAnswer(), newRound.getAnswers().get(1));
    }

    @Test()
    public void shouldGetWinWithCorrectAnswer(){
        CorrectAnswerResult result = gameAdministration.answerQuestion(
                user, newRound, newRound.getAnswers().get(1));
        Assert.assertNotNull(result);
        Assert.assertEquals(Result.WIN, result.getResult());
        Assert.assertNull(result.getCorrectAnswer());
    }
}
