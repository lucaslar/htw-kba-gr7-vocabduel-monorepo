package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.model.CorrectAnswerResult;
import de.htwberlin.kba.gr7.vocabduel.game_administration.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.model.VocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.user_administration.model.User;

import java.util.List;

public interface GameAdministration {

    /**
     * start a new game
     * @param playerA <code>User</code>
     * @param playerB <code>User</code>
     * @return new <code>VocabduelGame</code>
     */
    VocabduelGame startGame(User playerA, User playerB);

    /**
     * get list of challenged games from one user
     * @param user <code>User</code>
     * @return
     */
    List<VocabduelGame> getPersonalChallengedGames(User user);

    /**
     * start next round of a game.
     * @param player
     * @param game
     * @return
     */
    VocabduelRound startRound(User player, VocabduelGame game);

    /**
     * answer question in a round of a game
     * @param player <code>User</code>
     * @param game <code>VocabduelGame</code>
     * @return <code>CorrectAnswerResult</code>
     */
    CorrectAnswerResult answerQuestion(User player, VocabduelGame game);
}
