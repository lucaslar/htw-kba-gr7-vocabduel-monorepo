package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableList;

import javax.persistence.PersistenceException;
import java.util.List;

public interface VocableListDAO {

    VocableList selectVocableList(VocableList vocables) throws PersistenceException;

    VocableList selectVocableListById(Long id);

    List<VocableList> selectVocableListsByUserId(Long userId);

    boolean deleteVocableList(VocableList vocables) throws PersistenceException;

}
