package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Vocable extends UntranslatedVocable {
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<TranslationGroup> translations;

    public Vocable() {
    }

    public Vocable(TranslationGroup vocable, List<TranslationGroup> translations) {
        super(vocable);
        this.translations = translations;
    }

    public Vocable(Long id, TranslationGroup vocable, List<TranslationGroup> translations){
        super(id, vocable);
        this.translations = translations;
    }

    public List<TranslationGroup> getTranslations() {
        return translations;
    }

    public void setTranslations(List<TranslationGroup> translations) {
        this.translations = translations;
    }
}
