package de.htwberlin.kba.gr7.model;

public class FinishedVocabduelRound extends VocabduelRound {
    private Result resultPlayerA;
    private Result resultPlayerB;

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
