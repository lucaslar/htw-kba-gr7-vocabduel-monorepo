package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableUnit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

public class VocableUnitDAOImpl implements VocableUnitDAO{

    private final EntityManager ENTITY_MANAGER;

    public VocableUnitDAOImpl(EntityManager entityManager){
        ENTITY_MANAGER = entityManager;
    }

    @Override
    public void insertVocableUnit(VocableUnit unit) {
        ENTITY_MANAGER.getTransaction().begin();
        ENTITY_MANAGER.persist(unit);
        ENTITY_MANAGER.getTransaction().commit();
    }

    @Override
    public VocableUnit selectVocableUnitByVocableList(VocableList vocables) throws PersistenceException {
        VocableUnit unit = null;
        ENTITY_MANAGER.clear();
        ENTITY_MANAGER.getTransaction().begin();
        try{
            unit = (VocableUnit) ENTITY_MANAGER
                    .createQuery("select u from VocableUnit u inner join u.vocableLists l where l = :list")
                    .setParameter("list", vocables)
                    .getSingleResult();
        } catch (PersistenceException e){
            ENTITY_MANAGER.getTransaction().rollback();
            throw e;
        }
        ENTITY_MANAGER.getTransaction().commit();
        return unit;
    }

    @Override
    public boolean deleteVocableUnit(VocableUnit unit) {
        ENTITY_MANAGER.remove(unit);
        return true;
    }
}
