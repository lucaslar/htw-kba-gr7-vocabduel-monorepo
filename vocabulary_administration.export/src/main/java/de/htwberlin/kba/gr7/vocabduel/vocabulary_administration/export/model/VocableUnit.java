package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class VocableUnit {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<VocableList> vocableLists;
//    @ManyToOne(targetEntity = LanguageSet.class) // TODO map vu to vl in order access languages from vl
//    @JoinColumns({
//            @JoinColumn(name = "knownLanguage", insertable = false, updatable = false),
//            @JoinColumn(name = "learntLanguage", insertable = false, updatable = false)
//    })
//    private LanguageSet parentLanguageSet;

    public VocableUnit() {
    }

    public VocableUnit(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<VocableList> getVocableLists() {
        return vocableLists;
    }

    public void setVocableLists(List<VocableList> vocableLists) {
        this.vocableLists = vocableLists;
    }
}
