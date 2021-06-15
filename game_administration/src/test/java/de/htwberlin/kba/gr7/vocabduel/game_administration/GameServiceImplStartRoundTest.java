package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.assets.GameDataMock;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.RoundAlreadyFinishedException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.VocabularyService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class GameServiceImplStartRoundTest {

    private GameServiceImpl gameAdministration;
    private VocabduelRound newRoundRes;
    private GameDataMock mock;
    private UserService userService; // to be mocked
    private VocabularyService vocabularyService;

    @Before
    public void setup() {
        gameAdministration = new GameServiceImpl(userService, vocabularyService);
        mock = new GameDataMock();
    }

    @Test()
    public void shouldHaveData() throws RoundAlreadyFinishedException {

        newRoundRes = gameAdministration.startRound(
                mock.mockSampleUser(), mock.mockVocabduelGame()
        );
        Assert.assertNotNull(newRoundRes);

        // check given Input
        Assert.assertEquals(mock.mockVocabduelGame().getId(), newRoundRes.getGameId());
        Assert.assertTrue(newRoundRes.getAnswers().size() >= 2);
        Assert.assertNotNull(newRoundRes.getQuestion());
    }

    @Test(expected = RoundAlreadyFinishedException.class)
    public void shouldNotStartFinishedRound() throws RoundAlreadyFinishedException {
        newRoundRes = gameAdministration.startRound(
                mock.mockSampleUser(), mock.mockVocabduelGame()
        );
    }
}
