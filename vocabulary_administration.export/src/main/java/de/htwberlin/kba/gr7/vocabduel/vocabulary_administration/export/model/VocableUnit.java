package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
public class VocableUnit {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String title;
    @OneToMany(cascade = CascadeType.PERSIST)
    @Fetch(FetchMode.JOIN)
    private List<VocableList> vocableLists;

    @Version
    private Integer version;

    public VocableUnit() {
    }

    public VocableUnit(final Long id) {
        this.id = id;
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

    public Integer getVersion() {
        return version;
    }
}
