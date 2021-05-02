package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.model;

import de.htwberlin.kba.gr7.vocabduel.user_administration.model.User;

import java.util.Date;
import java.util.List;

public class VocableList {
    private Long id;
    private User author;
    private String title;
    private Date timestamp;
    private List<Vocable> vocables;

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
