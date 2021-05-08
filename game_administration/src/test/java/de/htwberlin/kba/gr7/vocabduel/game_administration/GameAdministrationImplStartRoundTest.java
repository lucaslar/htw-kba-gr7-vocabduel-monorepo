package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.TooManyRoundsException;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GameAdministrationImplStartRoundTest {
    // Fixture/to be added to mocking framework:
    // - User with id 2020 (exists)
    //      - starts new Round

    private static final User USER_2020 = new User(2020L);

    private GameAdministrationImpl gameAdministration;
    private VocabduelGame newGame;
    private VocabduelRound newRoundRes;


    @Before
    public void setup() {
        VocableList myList;
        gameAdministration = new GameAdministrationImpl();

        // new VocabduelGame
        newGame = new VocabduelGame();
        // new VocableList
        myList = new VocableList();
        myList.setAuthor(USER_2020);
        myList.setTimestamp(new Timestamp(System.currentTimeMillis()));
        myList.setTitle("myTitle");
        // new Vocable
        Vocable myVocable = new Vocable(
                new TranslationGroup(Collections.singletonList("Baum")),
                Collections.singletonList(new TranslationGroup(Collections.singletonList("tree"))));
        myList.setVocables(Collections.singletonList(myVocable));

    }
    @Test()
    public void shouldHaveData() throws TooManyRoundsException {

        newRoundRes = gameAdministration.startRound(
                USER_2020, newGame
        );
        Assert.assertNotNull(newRoundRes);

        // check given Input
        Assert.assertEquals(newGame.getId(), newRoundRes.getGameId());
        Assert.assertFalse(newRoundRes.getAnswers().isEmpty());
        Assert.assertNotNull(newRoundRes.getQuestion());
    }

    @Test()
    public void shouldHaveCorrectRoundIds() throws TooManyRoundsException{
        // setup for this method
        for (int i = 0; i<5; i++){
            newRoundRes = gameAdministration.startRound(
                    USER_2020, newGame
            );
            Assert.assertNotNull(newRoundRes); // otherwise compare at line 81 will get NullPointerException
            if (newGame.getRounds() == null) newGame.setRounds(Collections.singletonList(newRoundRes));
            else {
                ArrayList<VocabduelRound> temp1 = new ArrayList<>(newGame.getRounds());
                temp1.add(newRoundRes);
                newGame.setRounds(temp1);
            }
            i++;
        }
        newGame.getRounds().sort(compareById);

        VocabduelRound[] rounds = new VocabduelRound[newGame.getRounds().size()];
        newGame.getRounds().toArray(rounds);

        //test
        for (int i = 1; i <= newGame.getRounds().size(); i++){
            Assert.assertEquals(i, newGame.getRounds().get(i - 1).getRoundNr());
        }
    }

    @Test(expected = TooManyRoundsException.class)
    public void shouldThrowExceptionOnStartingOneRoundTooMuch() throws TooManyRoundsException {
        for (int i = 0; i <= 10; i++){
            newRoundRes = gameAdministration.startRound(
                    USER_2020, newGame
            );
            if (newGame.getRounds() == null) newGame.setRounds(Collections.singletonList(newRoundRes));
            else {
                ArrayList<VocabduelRound> temp1 = new ArrayList<>(newGame.getRounds());
                temp1.add(newRoundRes);
                newGame.setRounds(temp1);
            }
        }
    }

    Comparator<VocabduelRound> compareById = Comparator.comparingInt(VocabduelRound::getRoundNr);

}
