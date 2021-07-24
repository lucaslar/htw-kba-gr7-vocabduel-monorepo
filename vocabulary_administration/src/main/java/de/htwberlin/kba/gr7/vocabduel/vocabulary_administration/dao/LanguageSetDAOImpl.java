package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.LanguageSet;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.SupportedLanguage;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableUnit;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;

@Repository
public class LanguageSetDAOImpl implements LanguageSetDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void insertLanguageSet(LanguageSet languageSet) {
        entityManager.persist(languageSet);
    }

    @Override
    public LanguageSet selectOrInsertLanguageSetBySupportedLanguages(SupportedLanguage learnt, SupportedLanguage known) throws PersistenceException {
        LanguageSet languageSet = null;
        try {
            final String query = "from LanguageSet as l where l.learntLanguage like :learntLanguage and l.knownLanguage like :knownLanguage";
            languageSet = (LanguageSet) entityManager
                    .createQuery(query)
                    .setParameter("learntLanguage", learnt)
                    .setParameter("knownLanguage", known)
                    .getSingleResult();
        } catch (NoResultException ignored) {
            languageSet = new LanguageSet(learnt, known);
            entityManager.persist(languageSet);
        }
        return languageSet;
    }

    @Override
    public LanguageSet selectLanguageSetByVocableUnit(VocableUnit unit) {
        final LanguageSet languageSet = (LanguageSet) entityManager
                .createQuery("select l from LanguageSet l inner join l.vocableUnits u where u = :unit")
                .setParameter("unit", unit)
                .getSingleResult();
        return languageSet;
    }

    @Override
    public List<LanguageSet> selectLanguageSets() {
        List<LanguageSet> languageSets = null;
        try {
            languageSets = (List<LanguageSet>) entityManager
                    .createQuery("select ls from LanguageSet ls fetch all properties")
                    .getResultList();
            initializeLazyLoadedLanguageSetData(languageSets);
        } catch (NoResultException ignored) {
        }
        return languageSets;
    }

    @Override
    public boolean deleteLanguageSet(LanguageSet languageSet) throws PersistenceException {
        entityManager.remove(languageSet);
        return true;
    }

    private void initializeLazyLoadedLanguageSetData(final List<LanguageSet> languageSets) {
        languageSets.forEach(ls -> {
            if (!(ls.getVocableUnits() == null) && !ls.getVocableUnits().isEmpty()) ls.getVocableUnits().forEach(vu -> {
                if (!(vu.getVocableLists() == null) && !vu.getVocableLists().isEmpty()) vu.getVocableLists().forEach(vl -> {
                    if (!(vl.getVocables() == null) && !vl.getVocables().isEmpty()) vl.getVocables().forEach(v -> {
                        Hibernate.initialize(v.getVocable().getSynonyms());
                        Hibernate.initialize(v.getVocable().getAdditionalInfo());
                        v.getTranslations().forEach(t -> {
                            Hibernate.initialize(t.getSynonyms());
                            Hibernate.initialize(t.getAdditionalInfo());
                        });
                    });
                });
            });
        });
    }

 //   public void setEntityManager(EntityManager entityManager) {
 //       this.entityManager = entityManager;
 //   }
}
