package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.VocabularyLib;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

public class GameAdministrationImplStartGameTest {

    // Fixture/to be added to mocking framework:
    // - User with id 2020 (exists)
    // - User with id 4711:
    //      - Game can get started

    private static final User USER_2020 = new User(2020L);
    private static final User USER_4711 = new User(4711L);

    private GameAdministrationImpl gameAdministration;
    private VocabduelGame newGame;
    private VocabduelGame newGameRes;

    @Before
    public void setup() {
        // Mocks
        VocabularyLib vocabService = Mockito.mock(VocabularyLib.class);
        Mockito.when(vocabService.getVocableListsOfUser(USER_2020)).thenReturn(new ArrayList<>());
        Mockito.doReturn((new String[]{"DE", "EN"})).when(vocabService).getAllSupportedLanguages();

        // create sample VocabduelGame
        gameAdministration = new GameAdministrationImpl();
        newGame = new VocabduelGame();
        newGame.setKnownLanguage(vocabService.getAllSupportedLanguages().get(0));
        newGame.setLearntLanguage(vocabService.getAllSupportedLanguages().get(1));
        newGame.setPlayerA(USER_2020);
        newGame.setPlayerB(USER_4711);
        newGame.setVocableLists(vocabService.getVocableListsOfUser(USER_2020));

    }
    @Test()
    public void testGetStartedGameAsInput() {

        newGameRes = gameAdministration.startGame(
                newGame.getPlayerA(),
                newGame.getPlayerB(),
                newGame.getVocableLists(),
                newGame.getKnownLanguage(),
                newGame.getLearntLanguage()
        );
        Assert.assertNotNull(newGameRes);

        // check given Input
        Assert.assertEquals(newGameRes.getKnownLanguage(), newGame.getKnownLanguage());
        Assert.assertEquals(newGameRes.getLearntLanguage(), newGame.getLearntLanguage());
        Assert.assertEquals(newGameRes.getPlayerA(), newGame.getPlayerA());
        Assert.assertEquals(newGameRes.getPlayerB(), newGame.getPlayerB());
        Assert.assertEquals(newGameRes.getVocableLists(), newGame.getVocableLists());
    }

    @Test()
    public void testNewGameWithEmptyRoundList(){
        newGameRes = gameAdministration.startGame(
                newGame.getPlayerA(),
                newGame.getPlayerB(),
                newGame.getVocableLists(),
                newGame.getKnownLanguage(),
                newGame.getLearntLanguage()
        );
        Assert.assertNotNull(newGameRes);
        Assert.assertTrue(newGame.getRounds().isEmpty());
    }

    @Test()
    public void testNewGameWithNewId(){
        newGameRes = gameAdministration.startGame(
                newGame.getPlayerA(),
                newGame.getPlayerB(),
                newGame.getVocableLists(),
                newGame.getKnownLanguage(),
                newGame.getLearntLanguage()
        );
        Assert.assertNotNull(newGameRes);
        Assert.assertNotNull(newGame.getId());

        // TODO: check if gameId already exists?? -> new interface List<VocabduelGame> getGameById()
    }


}
