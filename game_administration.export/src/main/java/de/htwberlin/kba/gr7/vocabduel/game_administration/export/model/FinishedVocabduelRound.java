package de.htwberlin.kba.gr7.vocabduel.game_administration.export.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue("1")
public class FinishedVocabduelRound extends VocabduelRound {
    @Enumerated(EnumType.STRING)
    private Result resultPlayerA;
    @Enumerated(EnumType.STRING)
    private Result resultPlayerB;

    public FinishedVocabduelRound(Long gameId){ super(gameId); }

    public Result getResultPlayerA() {
        return resultPlayerA;
    }

    public void setResultPlayerA(Result resultPlayerA) {
        this.resultPlayerA = resultPlayerA;
    }

    public Result getResultPlayerB() {
        return resultPlayerB;
    }

    public void setResultPlayerB(Result resultPlayerB) {
        this.resultPlayerB = resultPlayerB;
    }
}
