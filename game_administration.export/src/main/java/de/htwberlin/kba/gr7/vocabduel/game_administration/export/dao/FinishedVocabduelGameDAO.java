package de.htwberlin.kba.gr7.vocabduel.game_administration.export.dao;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.FinishedVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.RunningVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import java.util.List;

public interface FinishedVocabduelGameDAO {

    FinishedVocabduelGame insertFinishedVocabduelGame(RunningVocabduelGame game);

    List<FinishedVocabduelGame> selectFinishedVocabduelGamesByUser(User player);

    boolean deleteFinishedVocabduelGamesWhereUserDoesntExist();

}
