package de.htwberlin.kba.gr7.vocabduel.game_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.RunningVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import java.util.List;

public interface RunningVocabduelGameDAO {

    void insertRunningVocabduelGame(RunningVocabduelGame game);

    List<RunningVocabduelGame> selectRunningVocabduelGamesByUser(User player);

    RunningVocabduelGame selectRunningVocabduelGameByGameIdAndUser(User user, Long gameId);

    boolean deleteRunningVocabduelGameWhereUserDoesntExist();

}
