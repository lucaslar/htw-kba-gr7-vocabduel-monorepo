package de.htwberlin.kba.gr7.vocabduel.shared_logic.rest;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class StandardizedUnauthorized {

    public static Response respond(final String message) {
        return respond(message, true);
    }

    public static Response respond(final String message, final boolean isTokenGiven) {
        final String additionalHeaderInformation = isTokenGiven ? "error=\"invalid_token\", \"error_description\"=\"The access token is invalid or expired\"" : "";
        return Response
                .status(Response.Status.UNAUTHORIZED)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .entity(message)
                .header(HttpHeaders.WWW_AUTHENTICATE, "Bearer realm=\"example\"" + additionalHeaderInformation)
                .build();
    }
}
