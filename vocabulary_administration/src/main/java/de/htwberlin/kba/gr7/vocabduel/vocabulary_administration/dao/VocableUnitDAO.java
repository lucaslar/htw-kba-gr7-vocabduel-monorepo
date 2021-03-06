package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.VocabularyOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableUnit;

import javax.persistence.PersistenceException;

public interface VocableUnitDAO {

    void insertVocableUnit(VocableUnit unit) throws VocabularyOptimisticLockException;

    VocableUnit selectVocableUnitByVocableList(VocableList vocables) throws VocabularyOptimisticLockException;

    boolean deleteVocableUnit(VocableUnit unit) throws PersistenceException, VocabularyOptimisticLockException;

}
