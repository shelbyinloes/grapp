package com.wisegas.grapp.restresource;

import com.wisegas.common.lang.value.GeoPoint;
import com.wisegas.common.webserver.hal.api.HALLink;
import com.wisegas.common.webserver.hal.api.HALRepresentation;
import com.wisegas.common.webserver.jersey.hal.JerseyHalResource;
import com.wisegas.common.webserver.jersey.hal.JerseyHalResourceLinkBuilder;
import com.wisegas.grapp.service.api.GrappStoreService;
import com.wisegas.grapp.service.dto.GrappStoreDTO;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Path("/stores/{id}/")
public class GrappStoreResource extends JerseyHalResource {

   private final GrappStoreService grappStoreService;

   @Inject
   public GrappStoreResource(GrappStoreService grappStoreService) {
      this.grappStoreService = grappStoreService;
   }

   @GET
   public Response get(@PathParam(value = "id") final String id) {
      return buildHalResponse(asRepresentationOf(grappStoreService.get(id)));
   }

   @PUT
   public Response update(@PathParam(value = "id") final String id,
                          @QueryParam(value = "name") final String name,
                          @QueryParam(value = "location") final GeoPoint location) {
      GrappStoreDTO grappStoreDTO = grappStoreService.update(id, name, location);
      return buildHalResponse(asRepresentationOf(grappStoreDTO));
   }

   @DELETE
   public Response delete(@PathParam(value = "id") final String id) {
      grappStoreService.delete(id);
      return Response.ok().build();
   }

   protected static HALRepresentation asRepresentationOf(GrappStoreDTO grappStoreDTO) {
      return halRepresentationFactory.createFor(grappStoreDTO).withLinks(createLinks(grappStoreDTO));
   }

   protected static HALLink createRootLink(String rel) {
      return createSelfLinkBuilder().withRel(rel);
   }

   private static List<HALLink> createLinks(GrappStoreDTO grappStoreDTO) {
      return Collections.singletonList(createSelfLinkBuilder().pathArgs(grappStoreDTO.getId()).withSelfRel());
   }

   private static JerseyHalResourceLinkBuilder createSelfLinkBuilder() {
      return JerseyHalResourceLinkBuilder.linkTo(GrappStoreResource.class).queryParams("name", "location");
   }
}
