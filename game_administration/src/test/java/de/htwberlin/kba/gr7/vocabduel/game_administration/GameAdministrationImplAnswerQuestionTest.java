package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.NoPlannedAnswerResultException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.CorrectAnswerResult;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.Result;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.TranslationGroup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class GameAdministrationImplAnswerQuestionTest {

    private GameAdministrationImpl gameAdministration;
    private User user;
    private VocabduelRound newRound;
    private TranslationGroup answer;

    @Before
    public void setup(){
        gameAdministration = new GameAdministrationImpl();
        user = new User(2020L);
        // set VocabduelRound
        newRound = new VocabduelRound();
        TranslationGroup poss1 = new TranslationGroup();
        poss1.setSynonyms(Arrays.asList("three"));
        TranslationGroup poss2 = new TranslationGroup();
        poss2.setSynonyms(Arrays.asList("tree"));
        TranslationGroup poss3 = new TranslationGroup();
        poss3.setSynonyms(Arrays.asList("tre"));
        TranslationGroup poss4 = new TranslationGroup();
        poss4.setSynonyms(Arrays.asList("stock"));
        newRound.setAnswers(Arrays.asList(poss1, poss2, poss3, poss4));

    }

    @Test()
    public void shouldGetNotNullObject(){
        CorrectAnswerResult result = gameAdministration.answerQuestion(
                user, newRound, newRound.getAnswers().get(1));
        Assert.assertNotNull(result);
    }

    @Test()
    public void shouldGetWinOrLossWithCorrectAnswer() throws NoPlannedAnswerResultException {
        CorrectAnswerResult result = gameAdministration.answerQuestion(
                user, newRound, newRound.getAnswers().get(1));
        if (result.getResult().equals(Result.WIN))
            Assert.assertNull(result.getCorrectAnswer());
        else if (result.getResult().equals(Result.LOSS))
            Assert.assertNotNull(result.getCorrectAnswer());
        else throw new NoPlannedAnswerResultException();
    }
}
