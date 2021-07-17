package de.htwberlin.kba.gr7.vocabduel.auth_interceptor.rest;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.AuthService;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ServerResponse;
import org.springframework.stereotype.Controller;

import javax.annotation.security.PermitAll;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

@Controller
@Provider
@ServerInterceptor
public class AuthInterceptor implements ContainerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "x-access-token";

    private final ServerResponse ACCESS_DENIED = new ServerResponse("This action requires a preceding login/valid " + AUTHORIZATION_HEADER + " header!", 401, new Headers<>());
    private final AuthService AUTH_SERVICE;

    @Context
    ResourceInfo resourceInfo;

    public AuthInterceptor(AuthService authService) {
        AUTH_SERVICE = authService;
    }

    @Override
    public void filter(final ContainerRequestContext requestContext) {
        if (!resourceInfo.getResourceMethod().isAnnotationPresent(PermitAll.class) && !AUTH_SERVICE.hasAccessRights(requestContext.getHeaderString(AUTHORIZATION_HEADER))) {
            requestContext.abortWith(ACCESS_DENIED);
        }
    }
}