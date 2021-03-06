package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.exceptions.InternalVocabularyModuleException;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.VocabularyOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class VocableListDAOImpl implements VocableListDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public VocableList selectVocableList(VocableList vocables) throws VocabularyOptimisticLockException {
        try{
            return entityManager.find(VocableList.class, vocables.getId());
        } catch (OptimisticLockException e){
            throw new VocabularyOptimisticLockException(e);
        } catch (Exception e){
            throw new InternalVocabularyModuleException(e);
        }
    }

    @Override
    public VocableList selectVocableListById(Long id) throws VocabularyOptimisticLockException {
        try {
            final VocableList vocableList = entityManager.find(VocableList.class, id);
            if (vocableList != null) initializeLazyLoadedVocableData(vocableList);
            return vocableList;
        } catch (OptimisticLockException e){
            throw new VocabularyOptimisticLockException(e);
        } catch (Exception e){
            throw new InternalVocabularyModuleException(e);
        }
    }

    @Override
    public List<VocableList> selectVocableListsByUserId(Long userId) throws VocabularyOptimisticLockException {
        List<VocableList> vocableLists = null;
        try {
            vocableLists = (List<VocableList>) entityManager
                    .createQuery("select vl from VocableList vl inner join vl.author a where a.id = :id")
                    .setParameter("id", userId)
                    .getResultList();
            vocableLists.forEach(this::initializeLazyLoadedVocableData);
        } catch (NoResultException ignored) {
            // ignored => return null (vocableLists) if no lists could be found
        } catch (OptimisticLockException e){
            throw new VocabularyOptimisticLockException(e);
        } catch (Exception e){
            throw new InternalVocabularyModuleException(e);
        }
        return vocableLists;
    }

    @Override
    public boolean deleteVocableList(VocableList vocables) throws PersistenceException, VocabularyOptimisticLockException {
        try {
            entityManager.remove(vocables);
            return true;
        } catch (OptimisticLockException e){
            throw new VocabularyOptimisticLockException(e);
        } catch (PersistenceException e){
            throw new PersistenceException(e);
        } catch (Exception e){
            throw new InternalVocabularyModuleException(e);
        }
    }

    private void initializeLazyLoadedVocableData(VocableList vocableList) {
        vocableList.getVocables().forEach(v -> {
            Hibernate.initialize(v.getVocable().getSynonyms());
            Hibernate.initialize(v.getVocable().getAdditionalInfo());
            v.getTranslations().forEach(t -> {
                Hibernate.initialize(t.getSynonyms());
                Hibernate.initialize(t.getAdditionalInfo());
            });
        });
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
