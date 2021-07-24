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
        entityManager.getTransaction().begin();
        entityManager.persist(unit);
        entityManager.getTransaction().commit();
    }

    @Override
    public VocableUnit selectVocableUnitByVocableList(VocableList vocables) throws PersistenceException {
        VocableUnit unit = null;
        entityManager.clear();
        entityManager.getTransaction().begin();
        try {
            unit = (VocableUnit) entityManager
                    .createQuery("select u from VocableUnit u inner join u.vocableLists l where l = :list")
                    .setParameter("list", vocables)
                    .getSingleResult();
        } catch (PersistenceException e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
        entityManager.getTransaction().commit();
        return unit;
    }

    @Override
    public boolean deleteVocableUnit(VocableUnit unit) {
        entityManager.remove(unit);
        return true;
    }
}
