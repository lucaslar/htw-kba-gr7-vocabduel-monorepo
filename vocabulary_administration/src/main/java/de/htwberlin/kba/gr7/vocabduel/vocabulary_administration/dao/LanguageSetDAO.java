package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.dao;

import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.LanguageSet;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.SupportedLanguage;
import de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model.VocableUnit;

import javax.persistence.PersistenceException;
import java.text.ParseException;
import java.util.List;

public interface LanguageSetDAO {

    void insertLanguageSet(LanguageSet languageSet);

    LanguageSet selectOrInsertLanguageSetBySupportedLanguages(SupportedLanguage learnt, SupportedLanguage known);

    LanguageSet selectLanguageSetByVocableUnit(VocableUnit unit) throws PersistenceException;

    List<LanguageSet> selectLanguageSets();

    boolean deleteLanguageSet(LanguageSet languageSet) throws PersistenceException;

}
