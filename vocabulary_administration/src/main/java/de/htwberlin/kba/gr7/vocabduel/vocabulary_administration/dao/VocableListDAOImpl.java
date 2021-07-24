package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;

@Repository
public class VocableListDAOImpl implements VocableListDAO{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public VocableList selectVocableList(VocableList vocables) throws PersistenceException {
        try {
            return entityManager.find(VocableList.class, vocables.getId());
        } catch (PersistenceException e){
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public VocableList selectVocableListById(Long id) {
        entityManager.getTransaction().begin();
        final VocableList vocableList = entityManager.find(VocableList.class, id);
        entityManager.getTransaction().commit();
        return vocableList;
    }

    @Override
    public List<VocableList> selectVocableListsByUserId(Long userId) {
        List<VocableList> vocableLists = null;
        try{
            entityManager.getTransaction().begin();
            vocableLists = (List<VocableList>) entityManager
                .createQuery("select vl from VocableList vl inner join vl.author a where a.id = :id")
                .setParameter("id", userId)
                .getResultList();
        } catch (
        NoResultException ignored) {
        }
        entityManager.getTransaction().commit();
        return vocableLists;
    }

    @Override
    public boolean deleteVocableList(VocableList vocables) {
        entityManager.getTransaction().begin();
        try {
            entityManager.remove(vocables);
        } catch (PersistenceException e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
        entityManager.getTransaction().commit();
        return true;
    }
}
