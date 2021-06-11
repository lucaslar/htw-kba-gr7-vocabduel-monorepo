package de.htwberlin.kba.gr7.vocabduel.user_administration;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityFactoryManagement {
    private static EntityManagerFactory factory;

    public static EntityManagerFactory getEntityFactory() {
        if (factory == null) factory = Persistence.createEntityManagerFactory("VocabduelJPA_PU_user");
        return factory;
    }
}
