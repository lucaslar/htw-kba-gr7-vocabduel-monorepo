package de.htwberlin.kba.gr7.vocabduel.shared_logic.rest;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.AuthService;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.springframework.stereotype.Controller;

import javax.annotation.security.PermitAll;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Controller
@Provider
@ServerInterceptor
public class AuthInterceptor implements ContainerRequestFilter {
    public static final String USER_HEADER = "user";
    private final Response ACCESS_DENIED = StandardizedUnauthorized.respond("This action requires a preceding login/valid " + HttpHeaders.AUTHORIZATION + " header!");
    private final Response ACCESS_DENIED_NO_TOKEN = StandardizedUnauthorized.respond("This action requires a preceding login/valid " + HttpHeaders.AUTHORIZATION + " header, but no such or an empty token was given!", false);
    private final AuthService AUTH_SERVICE;

    @Context
    ResourceInfo resourceInfo;

    public AuthInterceptor(AuthService authService) {
        AUTH_SERVICE = authService;
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