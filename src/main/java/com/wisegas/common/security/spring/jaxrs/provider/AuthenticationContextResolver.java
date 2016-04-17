package com.wisegas.common.security.spring.jaxrs.provider;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.WILDCARD)
public class AuthenticationContextResolver implements ContextResolver<Authentication> {

   @Override
   public Authentication getContext(Class<?> aClass) {
      return SecurityContextHolder.getContext().getAuthentication();
   }
}
