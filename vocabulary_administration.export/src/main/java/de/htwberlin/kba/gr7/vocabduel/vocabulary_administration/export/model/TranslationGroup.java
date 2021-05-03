package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model;

import java.util.List;

public class TranslationGroup {
    private List<String> synonyms;
    private List<String> exemplarySentencesOrAdditionalInfo;

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public List<String> getExemplarySentencesOrAdditionalInfo() {
        return exemplarySentencesOrAdditionalInfo;
    }

    public void setExemplarySentencesOrAdditionalInfo(List<String> exemplarySentencesOrAdditionalInfo) {
        this.exemplarySentencesOrAdditionalInfo = exemplarySentencesOrAdditionalInfo;
    }
}
