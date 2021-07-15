package de.htwberlin.kba.gr7.vocabduel.configuration.rest;

import de.htwberlin.kba.gr7.vocabduel.game_administration.rest.ScoreServiceRestAdapter;
import de.htwberlin.kba.gr7.vocabduel.user_administration.rest.UserServiceRestAdapter;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/vocabduel")
public class ConfigurationRestEasyImpl extends Application {
    // All services are initialized automatically
//    UserServiceRestAdapter;
//    ScoreServiceRestAdapter;
}
