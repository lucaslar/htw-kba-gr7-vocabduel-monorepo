package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class TranslationGroup {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "synonyms", nullable = false)
    @ElementCollection
//    @CollectionTable(name="Synonyms", joinColumns = @JoinColumn(name=""))
    private List<String> synonyms;

    @Column(name = "example_sentence_info", nullable = false)
    @ElementCollection
    private List<String> exemplarySentencesOrAdditionalInfo;

    public TranslationGroup(){ }

    public TranslationGroup(List<String> synonyms){ this.synonyms = synonyms; }

    public TranslationGroup(List<String> synonyms, List<String> exemplarySentencesOrAdditionalInfo){
        this.synonyms = synonyms;
        this.exemplarySentencesOrAdditionalInfo = exemplarySentencesOrAdditionalInfo;
    }

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
