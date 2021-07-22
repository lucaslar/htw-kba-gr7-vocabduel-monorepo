package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.LanguageSet;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.SupportedLanguage;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableUnit;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;

@Repository
public class LanguageSetDAOImpl implements LanguageSetDAO{

    @PersistenceContext
    private final EntityManager ENTITY_MANAGER;

    public LanguageSetDAOImpl(EntityManager entityManager){
        ENTITY_MANAGER = entityManager;
    }

    @Override
    public void insertLanguageSet(LanguageSet languageSet) {
        ENTITY_MANAGER.getTransaction().begin();
        ENTITY_MANAGER.persist(languageSet);
        ENTITY_MANAGER.getTransaction().commit();
    }

    @Override
    public LanguageSet selectOrInsertLanguageSetBySupportedLanguages(SupportedLanguage learnt, SupportedLanguage known) throws PersistenceException {
        LanguageSet languageSet = null;
        ENTITY_MANAGER.getTransaction().begin();
        try {
            final String query = "from LanguageSet as l where l.learntLanguage like :learntLanguage and l.knownLanguage like :knownLanguage";
            languageSet = (LanguageSet) ENTITY_MANAGER
                    .createQuery(query)
                    .setParameter("learntLanguage", learnt)
                    .setParameter("knownLanguage", known)
                    .getSingleResult();
        } catch (NoResultException ignored){
            languageSet = new LanguageSet(learnt, known);
            ENTITY_MANAGER.persist(languageSet);
        } catch (PersistenceException e){
            ENTITY_MANAGER.getTransaction().rollback();
            throw e;
        }
        ENTITY_MANAGER.getTransaction().commit();
        return languageSet;
    }

    @Override
    public LanguageSet selectLanguageSetByVocableUnit(VocableUnit unit) {
        ENTITY_MANAGER.getTransaction().begin();
        final LanguageSet languageSet = (LanguageSet) ENTITY_MANAGER
                .createQuery("select l from LanguageSet l inner join l.vocableUnits u where u = :unit")
                .setParameter("unit", unit)
                .getSingleResult();
        ENTITY_MANAGER.getTransaction().commit();
        return languageSet;
    }

    @Override
    public List<LanguageSet> selectLanguageSets() {
        List<LanguageSet> languageSets = null;
        try {
            ENTITY_MANAGER.getTransaction().begin();
            languageSets = (List<LanguageSet>) ENTITY_MANAGER
                    .createQuery("from LanguageSet")
                    .getResultList();
        } catch (NoResultException ignored) {
        }
        ENTITY_MANAGER.getTransaction().commit();
        return languageSets;
    }

    @Override
    public boolean deleteLanguageSet(LanguageSet languageSet) throws PersistenceException{
        try {
            ENTITY_MANAGER.remove(languageSet);
        } catch (PersistenceException e){
            ENTITY_MANAGER.getTransaction().rollback();
            throw e;
        }
        return true;
    }
}
