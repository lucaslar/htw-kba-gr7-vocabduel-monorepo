package de.htwberlin.kba.gr7.vocabduel.shared_logic.rest.model;

import javax.ws.rs.core.Response;
import java.util.List;

public class MissingData {
    private String message;
    private List<String> missingParams;

    private MissingData(final String action, final List<String> missingParams) {
        this.message = "Missing data for action: \"" + action + "\"";
        this.missingParams = missingParams;
    }

    public static Response createMissingDataResponse(final NoNullableProperty data, final String action) {
        final List<String> missingParams = data.missingProperties();
        if (missingParams == null) return null;
        final MissingData missingInfo = new MissingData(action, missingParams);
        System.out.println("Request (Action: \"" + action + "\") has been blocked due to missing data: " + missingParams);
        return Response.status(Response.Status.BAD_REQUEST).entity(missingInfo).build();
    }

    public String getMessage() {
        return message;
    }

    public List<String> getMissingParams() {
        return missingParams;
    }
}
