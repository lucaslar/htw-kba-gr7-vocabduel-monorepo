package de.htwberlin.kba.gr7.vocabduel.game_administration.export.dao;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.RunningVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelRound;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import java.util.List;

public interface VocabduelRoundDAO {

    boolean updateVocabduelRound(VocabduelRound round);

    boolean deleteVocabduelRoundByGame(VocabduelGame game);

    VocabduelRound selectVocabduelRoundByGameAndUser(RunningVocabduelGame game, User player);

}
