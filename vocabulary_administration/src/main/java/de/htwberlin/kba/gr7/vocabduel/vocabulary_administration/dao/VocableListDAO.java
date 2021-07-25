package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.VocabularyOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;

import javax.persistence.PersistenceException;
import java.util.List;

public interface VocableListDAO {

    VocableList selectVocableList(VocableList vocables) throws VocabularyOptimisticLockException;

    VocableList selectVocableListById(Long id) throws VocabularyOptimisticLockException;

    List<VocableList> selectVocableListsByUserId(Long userId) throws VocabularyOptimisticLockException;

    boolean deleteVocableList(VocableList vocables) throws PersistenceException, VocabularyOptimisticLockException;

}
