package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.InternalVocabularyModuleException;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableUnit;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

@Repository
public class VocableUnitDAOImpl implements VocableUnitDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void insertVocableUnit(VocableUnit unit) throws InternalVocabularyModuleException {
        try {
            entityManager.persist(unit);
        } catch (Exception e){
            throw new InternalVocabularyModuleException(e);
        }
    }

    @Override
    public VocableUnit selectVocableUnitByVocableList(VocableList vocables) throws InternalVocabularyModuleException {
        try {
            entityManager.clear();
            return (VocableUnit) entityManager
                    .createQuery("select u from VocableUnit u inner join u.vocableLists l where l = :list")
                    .setParameter("list", vocables)
                    .getSingleResult();
        } catch (Exception e){
            throw new InternalVocabularyModuleException(e);
        }
    }

    @Override
    public boolean deleteVocableUnit(VocableUnit unit) throws PersistenceException, InternalVocabularyModuleException{
        try {
            entityManager.remove(unit);
            return true;
        } catch (PersistenceException e){
            throw new PersistenceException(e);
        } catch (Exception e){
            throw new InternalVocabularyModuleException(e);
        }
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
