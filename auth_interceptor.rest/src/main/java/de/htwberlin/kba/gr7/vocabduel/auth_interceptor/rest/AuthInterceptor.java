package de.htwberlin.kba.gr7.vocabduel.auth_interceptor.rest;

import de.htwberlin.kba.gr7.vocabduel.user_administration.export.AuthService;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;
import org.springframework.stereotype.Controller;

import javax.annotation.security.PermitAll;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import java.util.*;

@Controller
@Provider
@ServerInterceptor
public class AuthInterceptor implements PreProcessInterceptor {
    public static final String AUTHORIZATION_HEADER = "x-access-token";

    private final ServerResponse ACCESS_DENIED = new ServerResponse("This action requires a preceding login/valid " + AUTHORIZATION_HEADER + " header!", 401, new Headers<>());
    private final AuthService AUTH_SERVICE;

    public AuthInterceptor(AuthService authService) {
        AUTH_SERVICE = authService;
    }

    @Override
    public ServerResponse preProcess(HttpRequest request, ResourceMethodInvoker methodInvoked) throws Failure, WebApplicationException {
        if (methodInvoked.getMethod().isAnnotationPresent(PermitAll.class)) return null;

        final List<String> authorization = request.getHttpHeaders().getRequestHeader(AUTHORIZATION_HEADER);
        if (authorization == null || authorization.isEmpty() || !AUTH_SERVICE.hasAccessRights(authorization.get(0))) {
            return ACCESS_DENIED;
        } else return null;
    }
}