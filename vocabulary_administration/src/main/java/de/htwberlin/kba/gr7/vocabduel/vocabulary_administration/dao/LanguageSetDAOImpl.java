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
    private EntityManager entityManager;

    @Override
    public void insertLanguageSet(LanguageSet languageSet) {
        entityManager.getTransaction().begin();
        entityManager.persist(languageSet);
        entityManager.getTransaction().commit();
    }

    @Override
    public LanguageSet selectOrInsertLanguageSetBySupportedLanguages(SupportedLanguage learnt, SupportedLanguage known) throws PersistenceException {
        LanguageSet languageSet = null;
        entityManager.getTransaction().begin();
        try {
            final String query = "from LanguageSet as l where l.learntLanguage like :learntLanguage and l.knownLanguage like :knownLanguage";
            languageSet = (LanguageSet) entityManager
                    .createQuery(query)
                    .setParameter("learntLanguage", learnt)
                    .setParameter("knownLanguage", known)
                    .getSingleResult();
        } catch (NoResultException ignored){
            languageSet = new LanguageSet(learnt, known);
            entityManager.persist(languageSet);
        } catch (PersistenceException e){
            entityManager.getTransaction().rollback();
            throw e;
        }
        entityManager.getTransaction().commit();
        return languageSet;
    }

    @Override
    public LanguageSet selectLanguageSetByVocableUnit(VocableUnit unit) {
        entityManager.getTransaction().begin();
        final LanguageSet languageSet = (LanguageSet) entityManager
                .createQuery("select l from LanguageSet l inner join l.vocableUnits u where u = :unit")
                .setParameter("unit", unit)
                .getSingleResult();
        entityManager.getTransaction().commit();
        return languageSet;
    }

    @Override
    public List<LanguageSet> selectLanguageSets() {
        List<LanguageSet> languageSets = null;
        try {
            entityManager.getTransaction().begin();
            languageSets = (List<LanguageSet>) entityManager
                    .createQuery("from LanguageSet")
                    .getResultList();
        } catch (NoResultException ignored) {
        }
        entityManager.getTransaction().commit();
        return languageSets;
    }

    @Override
    public boolean deleteLanguageSet(LanguageSet languageSet) throws PersistenceException{
        try {
            entityManager.remove(languageSet);
        } catch (PersistenceException e){
            entityManager.getTransaction().rollback();
            throw e;
        }
        return true;
    }
}
