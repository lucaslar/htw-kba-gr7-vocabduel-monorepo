package de.htwberlin.kba.gr7.vocabduel.shared_logic.rest;

import de.htwberlin.kba.gr7.vocabduel.shared_logic.rest.model.NoNullableProperty;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.springframework.stereotype.Controller;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;

@Controller
@Provider
@ServerInterceptor
public class RequiredArgsInterceptor implements ContainerRequestFilter {

    @Context
    ResourceInfo resourceInfo;

    @Override
    public void filter(final ContainerRequestContext requestContext) {
//        Arrays.stream(resourceInfo.getResourceMethod().getParameters()).forEach((parameter) -> {
//            System.out.println(NoNullableProperty.class.isAssignableFrom(parameter.getType()));
//        });

        Arrays.stream(resourceInfo.getResourceMethod().getParameters())
                .filter(p -> NoNullableProperty.class.isAssignableFrom(p.getType()))
                .forEach(p -> System.out.println(resourceInfo.getResourceMethod()));

        requestContext.getProperty("");
    }
}
