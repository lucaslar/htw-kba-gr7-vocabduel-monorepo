package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.ScoreAdministration;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.PersonalFinishedGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import java.util.List;

public class ScoreAdministrationImpl implements ScoreAdministration {
    @Override
    public List<PersonalFinishedGame> getPersonalFinishedGames(User user) {
        return null;
    }

    @Override
    public int getTotalWinsOfUser(User user) {
        return 0;
    }

    @Override
    public int getTotalLossesOfUser(User user) {
        return 0;
    }

    @Override
    public PersonalFinishedGame finishGame(User player, VocabduelGame game) {
        return null;
    }
}