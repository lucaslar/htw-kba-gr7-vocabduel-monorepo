package de.htwberlin.kba.gr7.vocabduel.game_administration;

import de.htwberlin.kba.gr7.vocabduel.game_administration.export.ScoreService;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.UnfinishedGameException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.exceptions.UserIsNotPlayerException;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.FinishedVocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.PersonalFinishedGame;
import de.htwberlin.kba.gr7.vocabduel.game_administration.export.model.VocabduelGame;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.UserService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

@Service
public class ScoreServiceImpl implements ScoreService {

    private UserService userService;

    // TODO: In the future, replace by db
    private List<FinishedVocabduelGame> allFinishedGames;

    private final EntityManager ENTITY_MANAGER;

    public ScoreServiceImpl() {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("VocabduelJPA_PU_game");
        ENTITY_MANAGER = emf.createEntityManager();
    }

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
    public PersonalFinishedGame finishGame(User player, VocabduelGame game) throws UnfinishedGameException, UserIsNotPlayerException {
        return null;
    }
}
