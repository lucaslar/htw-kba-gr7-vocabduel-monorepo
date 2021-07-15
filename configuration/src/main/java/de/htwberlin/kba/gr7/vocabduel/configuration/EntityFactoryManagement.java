package de.htwberlin.kba.gr7.vocabduel.configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntityFactoryManagement {
    @Bean
    public EntityManager getEntityManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("VocabduelJPA_PU");
        return factory.createEntityManager();
    }
}
