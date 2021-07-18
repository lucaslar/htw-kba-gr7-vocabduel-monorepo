package de.htwberlin.kba.gr7.vocabduel.auth_interceptor.rest;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.AuthService;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.springframework.stereotype.Controller;

import javax.annotation.security.PermitAll;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Controller
@Provider
@ServerInterceptor
public class AuthInterceptor implements ContainerRequestFilter {
    public static final String USER_HEADER = "user";
    private final Response ACCESS_DENIED = unauthorizedResponse("This action requires a preceding login/valid " + HttpHeaders.AUTHORIZATION + " header!");
    private final Response ACCESS_DENIED_NO_TOKEN = unauthorizedResponse("This action requires a preceding login/valid " + HttpHeaders.AUTHORIZATION + " header, but no such or an empty token was given!", false);
    private final AuthService AUTH_SERVICE;

    @Context
    ResourceInfo resourceInfo;

    public AuthInterceptor(AuthService authService) {
        AUTH_SERVICE = authService;
    }

    public static Response unauthorizedResponse(final String message) {
        return unauthorizedResponse(message, true);
    }

    public static Response unauthorizedResponse(final String message, final boolean isTokenGiven) {
        final String additionalHeaderInformation = isTokenGiven ? "error=\"invalid_token\", \"error_description\"=\"The access token is invalid or expired\"" : "";
        return Response
                .status(Response.Status.UNAUTHORIZED)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .entity(message)
                .header(HttpHeaders.WWW_AUTHENTICATE, "Bearer realm=\"example\"" + additionalHeaderInformation)
                .build();
    }

    @Override
    public void filter(final ContainerRequestContext requestContext) {
        if (!resourceInfo.getResourceMethod().isAnnotationPresent(PermitAll.class)) {
            final String tokenStr = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (tokenStr == null || tokenStr.isEmpty()) requestContext.abortWith(ACCESS_DENIED_NO_TOKEN);
            else {
                final String token = tokenStr.replaceFirst("Bearer ", "");
                if (!AUTH_SERVICE.hasAccessRights(token)) requestContext.abortWith(ACCESS_DENIED);
                else requestContext
                        .getHeaders()
                        .add(USER_HEADER, String.valueOf(AUTH_SERVICE.fetchUser(token).getId()));
            }
        }
    }
}