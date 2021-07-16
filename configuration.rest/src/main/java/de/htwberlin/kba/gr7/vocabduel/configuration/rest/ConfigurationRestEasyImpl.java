package de.htwberlin.kba.gr7.vocabduel.configuration.rest;

import de.htwberlin.kba.gr7.vocabduel.game_administration.rest.GameServiceRestAdapter;
import de.htwberlin.kba.gr7.vocabduel.game_administration.rest.ScoreServiceRestAdapter;
import de.htwberlin.kba.gr7.vocabduel.user_administration.rest.AuthServiceRestAdapter;
import de.htwberlin.kba.gr7.vocabduel.user_administration.rest.UserServiceRestAdapter;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.rest.VocabularyServiceRestAdapter;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class ConfigurationRestEasyImpl extends Application {
    // TODO: Load application context
    // private final static ConfigurableApplicationContext CONTEXT = new AnnotationConfigApplicationContext("de.htwberlin.kba.gr7.vocabduel");
    private final Set<Object> SINGLETONS = new HashSet<>();

    public ConfigurationRestEasyImpl() {
        SINGLETONS.add(new UserServiceRestAdapter());
        SINGLETONS.add(new AuthServiceRestAdapter());
        SINGLETONS.add(new VocabularyServiceRestAdapter());
        SINGLETONS.add(new GameServiceRestAdapter());
        SINGLETONS.add(new ScoreServiceRestAdapter());
    }

    @Override
    public Set<Object> getSingletons() {
        return SINGLETONS;
    }
}
