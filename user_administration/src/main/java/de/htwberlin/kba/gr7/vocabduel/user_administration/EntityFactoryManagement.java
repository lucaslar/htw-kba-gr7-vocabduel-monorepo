package de.htwberlin.kba.gr7.vocabduel.user_administration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityFactoryManagement {
    private static EntityManagerFactory factory;

    public static EntityManager getManager () {
        if (factory == null) factory = Persistence.createEntityManagerFactory("VocabduelJPA_PU_user");
        return factory.createEntityManager();
    }
}
