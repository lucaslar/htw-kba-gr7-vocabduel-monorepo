package de.htwberlin.kba.gr7.vocabduel.configuration;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Component
public class ConfigurationMySQLImpl {
    private EntityManager entityManager;

    public void init() throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("VocabduelJPA_PU");
        entityManager = emf.createEntityManager();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
