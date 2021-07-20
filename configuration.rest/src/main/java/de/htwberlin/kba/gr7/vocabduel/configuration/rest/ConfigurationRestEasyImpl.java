package de.htwberlin.kba.gr7.vocabduel.configuration.rest;

import de.htwberlin.kba.gr7.vocabduel.shared_logic.rest.AuthInterceptor;
import de.htwberlin.kba.gr7.vocabduel.game_administration.rest.GameServiceRestAdapter;
import de.htwberlin.kba.gr7.vocabduel.game_administration.rest.ScoreServiceRestAdapter;
import de.htwberlin.kba.gr7.vocabduel.user_administration.rest.AuthServiceRestAdapter;
import de.htwberlin.kba.gr7.vocabduel.user_administration.rest.UserServiceRestAdapter;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.rest.VocabularyServiceRestAdapter;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class ConfigurationRestEasyImpl extends Application {
    private final static ConfigurableApplicationContext CONTEXT = new AnnotationConfigApplicationContext("de.htwberlin.kba.gr7.vocabduel");
    private final Set<Object> SINGLETONS = new HashSet<>();

    public ConfigurationRestEasyImpl() {
        // For local development only (if not set, requests to the same server are not prohibited):
        // TODO Comment out when in production in order to support every port
        CorsFilter corsFilter = new CorsFilter();
        corsFilter.getAllowedOrigins().add("http://localhost:4200"); // local dev
        corsFilter.getAllowedOrigins().add("http://localhost:8080"); // productive application
        corsFilter.setAllowedMethods("OPTIONS, GET, POST, DELETE, PUT, PATCH");
        SINGLETONS.add(corsFilter);

        // Auth interceptor:
        SINGLETONS.add(CONTEXT.getBean(AuthInterceptor.class));

        // Adapter for the landing page:
        SINGLETONS.add(CONTEXT.getBean(LandingPageAdapter.class));

        // Services:
        SINGLETONS.add(CONTEXT.getBean(UserServiceRestAdapter.class));
        SINGLETONS.add(CONTEXT.getBean(AuthServiceRestAdapter.class));
        SINGLETONS.add(CONTEXT.getBean(VocabularyServiceRestAdapter.class));
        SINGLETONS.add(CONTEXT.getBean(GameServiceRestAdapter.class));
        SINGLETONS.add(CONTEXT.getBean(ScoreServiceRestAdapter.class));
    }

    @Override
    public Set<Object> getSingletons() {
        return SINGLETONS;
    }
}
