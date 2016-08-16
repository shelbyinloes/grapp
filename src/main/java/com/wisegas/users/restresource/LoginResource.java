package com.wisegas.users.restresource;

import com.wisegas.common.lang.value.Email;
import com.wisegas.common.webservices.hal.api.HalLink;
import com.wisegas.common.webservices.jaxrs.hal.JaxrsHalJsonResource;
import com.wisegas.common.webservices.jaxrs.hal.JaxrsHalResourceLinkBuilder;
import com.wisegas.users.service.api.LoginService;

import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/login/")
public class LoginResource extends JaxrsHalJsonResource {

   private final LoginService loginService;

   @Inject
   public LoginResource(LoginService loginService) {
      this.loginService = loginService;
   }

   @PUT
   public Response logIn(@QueryParam(value = "email") Email email,
                         @QueryParam(value = "avatar") String avatar) {
      return buildHalResponse(UserResource.asRepresentationOf(loginService.logIn(email, avatar)));
   }

   public static HalLink createRootLink(String rel) {
      return JaxrsHalResourceLinkBuilder.linkTo(LoginResource.class).queryParams("email", "avatar").withRel(rel);
   }
}
