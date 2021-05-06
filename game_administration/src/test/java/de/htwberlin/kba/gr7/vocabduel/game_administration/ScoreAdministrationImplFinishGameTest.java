package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.PersonalFinishedGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.junit.Before;
import org.mockito.Mockito;

public class ScoreAdministrationImplFinishGameTest {

    // Fixture/to be added to mocking framework:
    // - User with id 2020 (exists)
    // - User with id 42:
    //      - Record: 2 - 1
    //      - no games the user has been challenged to
    // - User with id 4711:
    //      - No finished game yet
    //      - Challenged to game by #42 without open questions, i.e. game can be finished

    private User user2020;
    private User user4711;
    private ScoreAdministrationImpl scoreAdministration;
    private PersonalFinishedGame personalFinishedGame;
    private VocabduelGame unfinishedGame;

    @Before
    public void setup() {
        scoreAdministration = new ScoreAdministrationImpl();
        user2020 = new User(2020L);
        user4711 = new User(4711L);

        // Stubbing
        VocabduelGame game = new VocabduelGame();
        Mockito.when(scoreAdministration.finishGame(user4711, game)).thenReturn(null); // TODO Continue with implementation.
    }
}
