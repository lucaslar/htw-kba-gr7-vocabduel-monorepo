package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.InternalVocabularyModuleException;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableUnit;

import javax.persistence.PersistenceException;

public interface VocableUnitDAO {

    void insertVocableUnit(VocableUnit unit) throws InternalVocabularyModuleException;

    VocableUnit selectVocableUnitByVocableList(VocableList vocables) throws InternalVocabularyModuleException;

    boolean deleteVocableUnit(VocableUnit unit) throws PersistenceException, InternalVocabularyModuleException;

}
