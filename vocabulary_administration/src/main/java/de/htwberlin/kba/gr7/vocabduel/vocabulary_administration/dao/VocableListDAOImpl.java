package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableUnit;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;

public class VocableListDAOImpl implements VocableListDAO{

    private final EntityManager ENTITY_MANAGER;

    public VocableListDAOImpl(EntityManager entityManager){
        ENTITY_MANAGER = entityManager;
    }

    @Override
    public VocableList selectVocableList(VocableList vocables) throws PersistenceException {
        try {
            return ENTITY_MANAGER.find(VocableList.class, vocables.getId());
        } catch (PersistenceException e){
            ENTITY_MANAGER.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public VocableList selectVocableListById(Long id) {
        ENTITY_MANAGER.getTransaction().begin();
        final VocableList vocableList = ENTITY_MANAGER.find(VocableList.class, id);
        ENTITY_MANAGER.getTransaction().commit();
        return vocableList;
    }

    @Override
    public List<VocableList> selectVocableListsByUserId(Long userId) {
        List<VocableList> vocableLists = null;
        try{
            ENTITY_MANAGER.getTransaction().begin();
            vocableLists = (List<VocableList>) ENTITY_MANAGER
                .createQuery("select vl from VocableList vl inner join vl.author a where a.id = :id")
                .setParameter("id", userId)
                .getResultList();
        } catch (
        NoResultException ignored) {
        }
        ENTITY_MANAGER.getTransaction().commit();
        return vocableLists;
    }

    @Override
    public boolean deleteVocableList(VocableList vocables) {
        try{
            ENTITY_MANAGER.remove(vocables);
        } catch (PersistenceException e){
            ENTITY_MANAGER.getTransaction().rollback();
            throw e;
        }
        return true;
    }
}
