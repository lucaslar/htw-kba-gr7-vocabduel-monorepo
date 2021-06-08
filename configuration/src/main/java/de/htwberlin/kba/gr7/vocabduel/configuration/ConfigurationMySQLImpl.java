package de.htwberlin.kba.gr7.vocabduel.configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ConfigurationMySQLImpl {

    public static void main (String[] args ) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("VocabduelJPA_PU");
            EntityManager em = emf.createEntityManager();
        }catch (Exception exc){
            exc.printStackTrace();
        }
    }
}
