package de.htwberlin.kba.gr7.vocabduel.shared_logic.rest;

import de.htwberlin.kba.gr7.vocabduel.shared_logic.rest.model.StandardizedUnauthorized;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.AuthService;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.exceptions.InternalUserModuleException;
import de.htwberlin.kba.gr7.vocabduel.user_administration.export.model.User;
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
    private final Response ACCESS_DENIED = StandardizedUnauthorized.respond("This action requires a preceding login/valid " + HttpHeaders.AUTHORIZATION + " header!");
    private final Response ACCESS_DENIED_NO_USER = StandardizedUnauthorized.respond("This action requires a preceding login/valid " + HttpHeaders.AUTHORIZATION + " header. Your token was valid but does not seem to belong to an existing user account!", false);
    private final Response ACCESS_DENIED_NO_TOKEN = StandardizedUnauthorized.respond("This action requires a preceding login/valid " + HttpHeaders.AUTHORIZATION + " header, but either no token, an empty token or a value not matching \"Bearer <token>\" was given!", false);
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
            if (tokenStr == null || tokenStr.isEmpty() || !tokenStr.startsWith("Bearer")) {
                requestContext.abortWith(ACCESS_DENIED_NO_TOKEN);
            } else {
                final String token = tokenStr.replaceFirst("Bearer ", "");
                try {
                    if (!AUTH_SERVICE.hasAccessRights(token)) requestContext.abortWith(ACCESS_DENIED);
                    else {
                        final User user = AUTH_SERVICE.fetchUser(token);
                        if (user == null) requestContext.abortWith(ACCESS_DENIED_NO_USER);
                        else requestContext.getHeaders().add(USER_HEADER, String.valueOf(user.getId()));
                    }
                } catch (InternalUserModuleException e) {
                    e.printStackTrace();
                    requestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build());
                }
            }
        }
    }
}