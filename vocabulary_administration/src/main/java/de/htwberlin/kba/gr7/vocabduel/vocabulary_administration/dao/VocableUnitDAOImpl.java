package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.VocabularyOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableUnit;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

@Repository
public class VocableUnitDAOImpl implements VocableUnitDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void insertVocableUnit(VocableUnit unit) throws VocabularyOptimisticLockException {
        try {
            entityManager.persist(unit);
        } catch (OptimisticLockException e){
            throw new VocabularyOptimisticLockException(e);
        }
    }

    @Override
    public VocableUnit selectVocableUnitByVocableList(VocableList vocables) throws VocabularyOptimisticLockException {
        try {
            entityManager.clear();
            return (VocableUnit) entityManager
                    .createQuery("select u from VocableUnit u inner join u.vocableLists l where l = :list")
                    .setParameter("list", vocables)
                    .getSingleResult();
        } catch (OptimisticLockException e){
            throw new VocabularyOptimisticLockException(e);
        }
    }

    @Override
    public boolean deleteVocableUnit(VocableUnit unit) throws PersistenceException, VocabularyOptimisticLockException {
        try {
            entityManager.remove(unit);
            return true;
        } catch (OptimisticLockException e){
            throw new VocabularyOptimisticLockException(e);
        }catch (PersistenceException e){
            throw new PersistenceException(e);
        }
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
