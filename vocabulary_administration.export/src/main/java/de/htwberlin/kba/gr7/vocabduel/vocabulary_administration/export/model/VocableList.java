package de.htwberlin.kba.gr7.vocabduel.vocabulary_administration.export.model;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class VocableList {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User author;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Date timestamp;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Vocable> vocables;

    @Version
    private Integer version;

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

    public Integer getVersion() {
        return version;
    }
}
