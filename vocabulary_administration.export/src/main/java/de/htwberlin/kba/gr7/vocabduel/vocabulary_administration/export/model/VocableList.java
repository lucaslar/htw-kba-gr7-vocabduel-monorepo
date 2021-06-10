package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Date;
import java.util.List;

@Entity
public class VocableList {
    @Id
    private Long id;
    @OneToOne
    private User author;
    private String title;
    private Date timestamp;
    @OneToMany
    private List<Vocable> vocables;

    public VocableList() {}

    public VocableList(Long id) {
        this.id = id;
    }

    public VocableList(Long id, User author, String title){
        this.id = id;
        this.author = author;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<Vocable> getVocables() {
        return vocables;
    }

    public void setVocables(List<Vocable> vocables) {
        this.vocables = vocables;
    }
}
