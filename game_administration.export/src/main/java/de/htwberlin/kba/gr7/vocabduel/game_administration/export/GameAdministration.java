package de.htwberlin.kba.gr7.vocabduel.game_administration.export;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.CorrectAnswerResult;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import java.util.List;

public interface GameAdministration {

    /**
     * start a new game
     * @param playerA <code>User</code>
     * @param playerB <code>User</code>
     * @return new <code>VocabduelGame</code>
     */
    VocabduelGame startGame(User playerA, User playerB, List<VocableList> vocableLists, SupportedLanguage knownLanguage, SupportedLanguage learntLanguage);

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
