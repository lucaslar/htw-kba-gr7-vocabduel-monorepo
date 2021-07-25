package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.VocabularyOptimisticLockException;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.LanguageSet;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.SupportedLanguage;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableUnit;

import javax.persistence.PersistenceException;
import java.util.List;

public interface LanguageSetDAO {

    void insertLanguageSet(LanguageSet languageSet) throws VocabularyOptimisticLockException;

    LanguageSet selectOrInsertLanguageSetBySupportedLanguages(SupportedLanguage learnt, SupportedLanguage known) throws VocabularyOptimisticLockException;

    LanguageSet selectLanguageSetByVocableUnit(VocableUnit unit) throws PersistenceException, VocabularyOptimisticLockException;

    List<LanguageSet> selectLanguageSets() throws VocabularyOptimisticLockException;

    boolean deleteLanguageSet(LanguageSet languageSet) throws PersistenceException, VocabularyOptimisticLockException;

}
