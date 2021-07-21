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
    private List<String> synonyms;

    @Column(name = "additional_info", nullable = false)
    @ElementCollection
    private List<String> additionalInfo;

    @Version
    private Integer version;

    public TranslationGroup(){ }

    public TranslationGroup(Long id, List<String> synonyms){
        this.id = id;
        this.synonyms = synonyms;
    }

    public TranslationGroup(List<String> synonyms){ this.synonyms = synonyms; }

    public TranslationGroup(List<String> synonyms, List<String> additionalInfo){
        this.synonyms = synonyms;
        this.additionalInfo = additionalInfo;
    }

    public Long getId() {
        return id;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public List<String> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(List<String> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Integer getVersion() {
        return version;
    }
}
