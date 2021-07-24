package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.dao;

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
    public void insertVocableUnit(VocableUnit unit) {
        entityManager.persist(unit);
    }

    @Override
    public VocableUnit selectVocableUnitByVocableList(VocableList vocables) throws PersistenceException {
        entityManager.clear();
        return (VocableUnit) entityManager
                .createQuery("select u from VocableUnit u inner join u.vocableLists l where l = :list")
                .setParameter("list", vocables)
                .getSingleResult();
    }

    @Override
    public boolean deleteVocableUnit(VocableUnit unit) {
        entityManager.remove(unit);
        return true;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
