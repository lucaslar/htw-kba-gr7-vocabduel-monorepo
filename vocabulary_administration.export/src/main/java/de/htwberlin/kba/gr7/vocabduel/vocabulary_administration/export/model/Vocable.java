package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model;


import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
public class Vocable extends UntranslatedVocable {
    @OneToMany(cascade = CascadeType.PERSIST)
    @Fetch(FetchMode.JOIN)
    private List<TranslationGroup> translations;

    private String exampleOrAdditionalInfoLearntLang;
    private String exampleOrAdditionalInfoKnownLang;

    @Version
    private Integer version;

    public Vocable() {
    }

    public Vocable(TranslationGroup vocable, List<TranslationGroup> translations, String exampleOrAdditionalInfoKnownLang, String exampleOrAdditionalInfoLearntLang) {
        super(vocable);
        this.translations = translations;
        this.exampleOrAdditionalInfoKnownLang = exampleOrAdditionalInfoKnownLang;
        this.exampleOrAdditionalInfoLearntLang = exampleOrAdditionalInfoLearntLang;
    }

    public Vocable(Long id, TranslationGroup vocable, List<TranslationGroup> translations, String exampleOrAdditionalInfoKnownLang, String exampleOrAdditionalInfoLearntLang) {
        super(id, vocable);
        this.translations = translations;
        this.exampleOrAdditionalInfoKnownLang = exampleOrAdditionalInfoKnownLang;
        this.exampleOrAdditionalInfoLearntLang = exampleOrAdditionalInfoLearntLang;
    }

    public List<TranslationGroup> getTranslations() {
        return translations;
    }

    public void setTranslations(List<TranslationGroup> translations) {
        this.translations = translations;
    }

    public String getExampleOrAdditionalInfoLearntLang() {
        return exampleOrAdditionalInfoLearntLang;
    }

    public String getExampleOrAdditionalInfoKnownLang() {
        return exampleOrAdditionalInfoKnownLang;
    }

    public Integer getVersion() {
        return version;
    }
}
