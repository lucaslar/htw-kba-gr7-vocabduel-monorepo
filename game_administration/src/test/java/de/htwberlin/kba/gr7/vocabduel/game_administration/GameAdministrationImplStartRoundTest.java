package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.tooManyRoundsException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.TranslationGroup;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.Vocable;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Arrays;

public class GameAdministrationImplStartRoundTest {
    // Fixture/to be added to mocking framework:
    // - User with id 2020 (exists)
    //      - starts new Round

    private static final User USER_2020 = new User(2020L);

    private GameAdministrationImpl gameAdministration;
    private VocabduelGame newGame;
    private VocabduelRound newRound;
    private VocabduelRound newRoundRes;
    private VocableList myList;

    @Before
    public void setup() {

        gameAdministration = new GameAdministrationImpl();
        newGame = new VocabduelGame();
        // new VocavleList
        myList = new VocableList();
        myList.setAuthor(USER_2020);
        myList.setTimestamp(new Timestamp(System.currentTimeMillis()));
        myList.setTitle("myTitle");
        // new Vocable
        Vocable myVocable = new Vocable();
        myVocable.setVocable(new TranslationGroup());
        myVocable.setTranslations(Arrays.asList(new TranslationGroup()));

        myVocable.getVocable().setSynonyms(Arrays.asList("Baum"));
        myVocable.getTranslations().add(new TranslationGroup());
        myVocable.getTranslations().get(0).setSynonyms(Arrays.asList("tree"));

        myList.setVocables(Arrays.asList(myVocable));

        // create sample VocabduelRound
        newRound.setAnswers(myList.getVocables().get(0).getTranslations());
        newRound.setQuestion(myList.getVocables().get(0));
        newRound.setRoundNr(newGame.getRounds().size() + 1);

    }
    @Test()
    public void shouldStartNewRound() throws tooManyRoundsException{

        newRoundRes = gameAdministration.startRound(
                USER_2020, newGame
        );
        Assert.assertNotNull(newRoundRes);
    }

    @Test()
    public void shouldCheckDataOfNewRound() {

        // check given Input
        Assert.assertEquals(newGame.getRounds().size() + 1, newRoundRes.getRoundNr());
        Assert.assertEquals(newGame.getId(), newRoundRes.getGameId());
    }

    @Test(expected = tooManyRoundsException.class)
    public void shouldThrowExceptionOnStartingOneRoundTooMuch() throws tooManyRoundsException {
        for (int i = 0; i <= 10; i++){
            newRoundRes = gameAdministration.startRound(
                    USER_2020, newGame
            );
            Assert.assertNotNull(newRoundRes);
        }
    }
}
