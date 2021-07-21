package de.htwberlin.kba.gr7.vocabduel.game_administration.rest.model;

import de.htwberlin.kba.gr7.vocabduel.shared_logic.rest.model.NoNullableProperty;

import java.util.ArrayList;
import java.util.List;

public class StartGameData implements NoNullableProperty {
    private Long opponentId;
    private List<Long> vocableListIds;

    public Long getOpponentId() {
        return opponentId;
    }

    public List<Long> getVocableListIds() {
        return vocableListIds;
    }

    @Override
    public List<String> missingProperties() {
        final ArrayList<String> missing = new ArrayList<>();
        if (opponentId == null) missing.add("opponentId");
        if (vocableListIds == null) missing.add("vocableListIds");
        return missing.isEmpty() ? null : missing;
    }
}
