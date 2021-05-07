package de.htwberlin.kba.gr7.vocabduel.game_administration.export;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.CorrectAnswerResult;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.SupportedLanguage;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.TranslationGroup;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;

import java.util.List;

/**
 * Service for managing current games and their rounds.
 *
 * @author Sebastian Kehl, Lucas Larisch
 * @version 1.0, May 2021
 */
public interface GameAdministration {

    /**
     * Starts a new game as <code>playerA</code> against <code>playerB</code> with the given parameters
     * (languages/vocable lists) and returns an instance of that newly started match.
     *
     * @param playerA        Player initiating the game.
     * @param playerB        Opponent to be invited to play with <code>playerA</code>.
     * @param vocableLists   List of vocable lists to be used in the game. The questions will randomly selected by these lists.
     * @param knownLanguage  Known language, i.e. the language in which the questions are to be asked in.
     * @param learntLanguage Learnt language, i.e. the language the answers are displayed in.
     * @return New <code>{@link VocabduelGame}</code> instance based on the given params.
     */
    VocabduelGame startGame(User playerA, User playerB, List<VocableList> vocableLists, SupportedLanguage knownLanguage, SupportedLanguage learntLanguage);

    /**
     * Collects all pending, i.e. not finished, games a given user has been challenged to and returns them.
     *
     * @param user User the pending/challenged matches of are to be returned.
     * @return List of all unfinished games the given <code>user</code> has been challenged to.
     */
    List<VocabduelGame> getPersonalChallengedGames(User user);

    /**
     * Starts the next round of a given game as a given player, i.e. returns the respective next round object.
     *
     * @param player Player the next round is to be returned for.
     * @param game   Game the next round of is to be returned for the given <code>user</code>.
     * @return New <code>{@link VocabduelRound}</code> of a given game for a given user.
     */
    VocabduelRound startRound(User player, VocabduelGame game);

    /**
     * Checks and stores the result for an answer submitted in a given <code>round</code> by a given
     * <code>player</code> and returns feedback for that answer incl. the correct answer in case of
     * having submitted a wrong one.
     *
     * @param player Player who has answered the question.
     * @param round  Round the answer has been submitted for.
     * @param answer Answer submitted by the given <code>player</code>.
     * @return Result for the given <code>round</code> from the perspective of the given <code>player</code> incl. the correct answer in case of having submitted a wrong one.
     */
    CorrectAnswerResult answerQuestion(User player, VocabduelRound round, TranslationGroup answer);
}
