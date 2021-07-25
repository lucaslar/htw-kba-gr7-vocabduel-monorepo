package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.InternalVocabularyModuleException;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;

import javax.persistence.PersistenceException;
import java.util.List;

public interface VocableListDAO {

    VocableList selectVocableList(VocableList vocables); // throws InternalVocabularyModuleException;

    VocableList selectVocableListById(Long id); // throws InternalVocabularyModuleException;

    List<VocableList> selectVocableListsByUserId(Long userId); // throws InternalVocabularyModuleException;

    boolean deleteVocableList(VocableList vocables) throws PersistenceException;

}
