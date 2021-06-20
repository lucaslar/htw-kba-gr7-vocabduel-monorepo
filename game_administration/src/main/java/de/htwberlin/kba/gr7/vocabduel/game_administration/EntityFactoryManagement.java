package de.htwberlin.kba.gr7.vocabduel.game_administration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityFactoryManagement {
    private static EntityManagerFactory factory;

    public static EntityManager getManager () {
        if (factory == null) factory = Persistence.createEntityManagerFactory("VocabduelJPA_PU_game");
        return factory.createEntityManager();
    }
}
