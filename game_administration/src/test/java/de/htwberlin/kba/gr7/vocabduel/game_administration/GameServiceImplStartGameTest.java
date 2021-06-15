package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.assets.GameDataMock;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.InvalidUserException;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.VocabularyService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GameServiceImplStartGameTest {

    private GameServiceImpl gameAdministration;
    private VocabduelGame newGame;
    private VocabduelGame newGameRes;
    private GameDataMock mock;
    private UserService userService; // to be mocked
    private VocabularyService vocabularyService;

    @Before
    public void setup() {

        // mock sample VocabduelGame
        gameAdministration = new GameServiceImpl(userService, vocabularyService);
        mock = new GameDataMock();
        newGame = mock.mockVocabduelGame();
    }

    @Test()
    public void shouldGetStartedGameAsInput() throws KnownLangEqualsLearntLangException, NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException {

        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockVocableLists(),
                mock.mockKnownLanguage(),
                mock.mockLearntLanguage()
        );
        Assert.assertNotNull(newGameRes);

        // check given Input
        Assert.assertEquals(newGameRes.getKnownLanguage(), mock.mockKnownLanguage());
        Assert.assertEquals(newGameRes.getLearntLanguage(), mock.mockLearntLanguage());
        Assert.assertEquals(newGameRes.getPlayerA(), mock.mockSampleUser());
        Assert.assertEquals(newGameRes.getPlayerB(), mock.mockOpponent());
        Assert.assertEquals(newGameRes.getVocableLists(), mock.mockVocableLists());
        Assert.assertNotNull(newGame.getId());
    }

    @Test()
    public void shouldNewGameWithFixedRoundList() throws KnownLangEqualsLearntLangException, NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException  {

        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockVocableLists(),
                mock.mockKnownLanguage(),
                mock.mockLearntLanguage()
        );
        Assert.assertNotNull(newGameRes);
        Assert.assertEquals(newGameRes.getRounds().size(), GameServiceImpl.getFixNumberOfRoundsPerGame());
    }

    @Test()
    public void shouldNewGameWithEnoughVocablesInMultipleLists() throws KnownLangEqualsLearntLangException, NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException  {

        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockMultipleVocableListsWithEnoughVocabulary(),
                mock.mockKnownLanguage(),
                mock.mockLearntLanguage()
        );
        Assert.assertNotNull(newGameRes);
        Assert.assertEquals(newGameRes.getRounds().size(), GameServiceImpl.getFixNumberOfRoundsPerGame());
    }

    @Test()
    public void shouldStartGameWithUniqueRounds() throws KnownLangEqualsLearntLangException, NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException  {
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockVocableLists(),
                mock.mockKnownLanguage(),
                mock.mockLearntLanguage()
        );
        Assert.assertNotNull(newGameRes);
        List<VocabduelRound> uniques = newGameRes.getRounds().stream().distinct().collect(Collectors.toList());
        Assert.assertEquals(uniques.size(), newGameRes.getRounds().size());
    }

    @Test
    public void shouldStartGameWithUniqueQuestions() throws KnownLangEqualsLearntLangException, NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException  {
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockVocableLists(),
                mock.mockKnownLanguage(),
                mock.mockLearntLanguage()
        );
        Assert.assertNotNull(newGameRes);
        newGameRes.getRounds().stream().map(r -> r.getQuestion().getVocable())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .forEach((key, value) -> Assert.assertEquals(1, value.intValue()));
    }

    @Test()
    public void shouldStartGameWithCorrectRoundIds() throws KnownLangEqualsLearntLangException, NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException  {
        // setup for this method
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockVocableLists(),
                mock.mockKnownLanguage(),
                mock.mockLearntLanguage()
        );
        Assert.assertNotNull(newGameRes);
        newGameRes.getRounds().sort(compareById);

        VocabduelRound[] rounds = new VocabduelRound[newGameRes.getRounds().size()];
        newGameRes.getRounds().toArray(rounds);

        //test round ids start from 1 to GameAdministration.NR_OF_ROUNDS
        for (int i = 1; i <= newGameRes.getRounds().size(); i++) {
            Assert.assertNotNull(newGameRes.getRounds().get(i - 1));
            Assert.assertEquals(i, newGameRes.getRounds().get(i - 1).getRoundNr());
        }
    }

    @Test(expected = InvalidGameSetupException.class)
    public void shouldNotStartGameInSinglePlayerMode() throws KnownLangEqualsLearntLangException, NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException  {
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockSampleUser(),
                mock.mockVocableLists(),
                mock.mockKnownLanguage(),
                mock.mockLearntLanguage()
        );
    }

    @Test(expected = KnownLangEqualsLearntLangException.class)
    public void shouldNotStartGameWithSameLanguageToLearnAsToLearnFrom() throws KnownLangEqualsLearntLangException, NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException  {
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockVocableLists(),
                mock.mockKnownLanguage(),
                mock.mockKnownLanguage()
        );
    }

    @Test(expected = NotEnoughVocabularyException.class)
    public void shouldNotStartGameWithoutVocabulary() throws KnownLangEqualsLearntLangException, NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException  {
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockSampleUser(),
                mock.mockVocableListsWithEmptyVocabulary(),
                mock.mockKnownLanguage(),
                mock.mockLearntLanguage()
        );
    }

    @Test(expected = NotEnoughVocabularyException.class)
    public void shouldNotStartGameWithoutEnoughVocabulary() throws KnownLangEqualsLearntLangException, NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException  {
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockSampleUser(),
                mock.mockVocableListsWithoutEnoughVocabulary(),
                mock.mockKnownLanguage(),
                mock.mockLearntLanguage()
        );
    }

    @Test(expected = NotEnoughVocabularyException.class)
    public void shouldNotStartGameWithoutEnoughVocabularyInMultipleVocableLists() throws KnownLangEqualsLearntLangException, NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException  {
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockSampleUser(),
                mock.mockMultipleVocableListsWithoutEnoughVocabulary(),
                mock.mockKnownLanguage(),
                mock.mockLearntLanguage()
        );
    }

    @Test(expected = NotEnoughVocabularyException.class)
    public void shouldNotStartGameWithEmptyVocableLists() throws KnownLangEqualsLearntLangException, NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException  {
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockSampleUser(),
                mock.mockEmptyVocableLists(),
                mock.mockKnownLanguage(),
                mock.mockLearntLanguage()
        );
    }

    Comparator<VocabduelRound> compareById = Comparator.comparingInt(VocabduelRound::getRoundNr);

}
