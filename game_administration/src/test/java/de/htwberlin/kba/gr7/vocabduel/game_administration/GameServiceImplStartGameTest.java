package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.assets.GameDataMock;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.*;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.RunningVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.InvalidUserException;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.VocabularyService;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.LanguageSet;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.SupportedLanguage;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceImplStartGameTest {

    private GameServiceImpl gameAdministration;
    private RunningVocabduelGame newGame;
    private RunningVocabduelGame newGameRes;
    private GameDataMock mock;
    @Mock
    private UserService userService; // to be mocked
    @Mock
    private EntityManager entityManager;
    @Mock
    private EntityTransaction entityTransaction;
    @Mock
    private VocabularyService vocabularyService;

    @Before
    public void setup() {
//        Mockito.when(userService.getUserDataById(12L)).thenReturn(mock.mockOpponent());
        // mock sample VocabduelGame
        gameAdministration = new GameServiceImpl(userService, vocabularyService, entityManager);
        mock = new GameDataMock();
        newGame = mock.mockVocabduelGame();

        List<SupportedLanguage> langList = new ArrayList<>();
        langList.add(mock.mockKnownLanguage());
        langList.add(mock.mockLearntLanguage());
        List<LanguageSet> langset = new ArrayList<>();
        VocableUnit unit = new VocableUnit("new Title");
        unit.setVocableLists(mock.mockVocableLists());
        LanguageSet mySet = new LanguageSet(mock.mockLearntLanguage(), mock.mockKnownLanguage());
        mySet.setVocableUnits(Stream.of(unit).collect(Collectors.toList()));
        langset.add(mySet);

        Mockito.when(userService.getUserDataById(Mockito.anyLong())).thenReturn(mock.mockSampleUser());
        Mockito.when(userService.getUserDataById(12L)).thenReturn(mock.mockOpponent());
        Mockito.when(vocabularyService.getAllLanguageSets()).thenReturn(langset);
        Mockito.when(entityManager.getTransaction()).thenReturn(entityTransaction);
  //      Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryMock);
    //    Mockito.when(queryMock.setParameter(Mockito.anyString(), Mockito.anyObject())).thenReturn(queryMock);
    }

    @Test()
    public void shouldGetStartedGameAsInput() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException {
      //  Mockito.when(queryMock.getResultList()).thenReturn(Stream.of(languageSet).collect(Collectors.toList()));
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockVocableLists()
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
    public void shouldNewGameWithFixedRoundList() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException  {

        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockVocableLists()
        );
        Assert.assertNotNull(newGameRes);
        Assert.assertEquals(newGameRes.getRounds().size(), GameServiceImpl.getFixNumberOfRoundsPerGame());
    }

    @Test()
    public void shouldNewGameWithEnoughVocablesInMultipleLists() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException  {

        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockMultipleVocableListsWithEnoughVocabulary()
        );
        Assert.assertNotNull(newGameRes);
        Assert.assertEquals(newGameRes.getRounds().size(), GameServiceImpl.getFixNumberOfRoundsPerGame());
    }

    @Test()
    public void shouldStartGameWithUniqueRounds() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException  {
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockVocableLists()
        );
        Assert.assertNotNull(newGameRes);
        List<VocabduelRound> uniques = newGameRes.getRounds().stream().distinct().collect(Collectors.toList());
        Assert.assertEquals(uniques.size(), newGameRes.getRounds().size());
    }

    @Test
    public void shouldStartGameWithUniqueQuestions() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException  {
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockVocableLists()
        );
        Assert.assertNotNull(newGameRes);
        newGameRes.getRounds().stream().map(r -> r.getQuestion().getVocable())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .forEach((key, value) -> Assert.assertEquals(1, value.intValue()));
    }

    @Test()
    public void shouldStartGameWithCorrectRoundIds() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException  {
        // setup for this method
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockVocableLists()
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
    public void shouldNotStartGameInSinglePlayerMode() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException  {
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockSampleUser(),
                mock.mockVocableLists()
        );
    }

    @Test(expected = NotEnoughVocabularyException.class)
    public void shouldNotStartGameWithoutVocabulary() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException  {
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockVocableListsWithEmptyVocabulary()
        );
    }

    @Test(expected = NotEnoughVocabularyException.class)
    public void shouldNotStartGameWithoutEnoughVocabulary() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException  {
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockVocableListsWithoutEnoughVocabulary()
        );
    }

    @Test(expected = NotEnoughVocabularyException.class)
    public void shouldNotStartGameWithoutEnoughVocabularyInMultipleVocableLists() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException  {
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockMultipleVocableListsWithoutEnoughVocabulary()
        );
    }

    @Test(expected = NotEnoughVocabularyException.class)
    public void shouldNotStartGameWithEmptyVocableLists() throws NotEnoughVocabularyException, InvalidGameSetupException, InvalidUserException  {
        newGameRes = gameAdministration.startGame(
                mock.mockSampleUser(),
                mock.mockOpponent(),
                mock.mockEmptyVocableLists()
        );
    }

    Comparator<VocabduelRound> compareById = Comparator.comparingInt(VocabduelRound::getRoundNr);

}
