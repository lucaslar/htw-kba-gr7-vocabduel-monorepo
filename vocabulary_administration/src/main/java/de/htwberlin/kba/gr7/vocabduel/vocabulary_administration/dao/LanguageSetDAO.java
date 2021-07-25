package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.exceptions.InternalVocabularyModuleException;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.LanguageSet;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.SupportedLanguage;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableUnit;

import java.util.List;

public interface LanguageSetDAO {

    void insertLanguageSet(LanguageSet languageSet) throws InternalVocabularyModuleException;

    LanguageSet selectOrInsertLanguageSetBySupportedLanguages(SupportedLanguage learnt, SupportedLanguage known) throws InternalVocabularyModuleException;

    LanguageSet selectLanguageSetByVocableUnit(VocableUnit unit) throws InternalVocabularyModuleException;

    List<LanguageSet> selectLanguageSets() throws InternalVocabularyModuleException;

    boolean deleteLanguageSet(LanguageSet languageSet) throws InternalVocabularyModuleException;

}
