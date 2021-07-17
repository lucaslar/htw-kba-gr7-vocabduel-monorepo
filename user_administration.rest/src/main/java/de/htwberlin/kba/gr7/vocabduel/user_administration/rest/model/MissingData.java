package de.htwberlin.kba.gr7.vocabduel.user_administration.rest.model;

import java.util.List;

public class MissingData {
    private String message;
    private List<String> missingParams;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getMissingParams() {
        return missingParams;
    }

    public void setMissingParams(List<String> missingParams) {
        this.missingParams = missingParams;
    }
}
