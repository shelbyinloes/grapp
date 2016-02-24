package com.wisegas.grapp.restresource;

import com.wisegas.grapp.service.api.GrappStoreService;
import com.wisegas.grapp.service.dto.GrappStoreDTO;
import com.wisegas.value.GeoPoint;
import com.wisegas.webserver.hal.HALResource;
import com.wisegas.webserver.hal.HALResourceLinkBuilder;
import com.wisegas.webserver.hal.api.HALLink;
import com.wisegas.webserver.hal.api.HALRepresentation;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Path("/stores/{id}/")
public class GrappStoreResource extends HALResource {

   private final GrappStoreService grappStoreService;

   @Inject
   public GrappStoreResource(GrappStoreService grappStoreService) {
      this.grappStoreService = grappStoreService;
   }

   @GET
   public Response findByID(@PathParam(value = "id") final String id) {
      return buildHALResponse(asRepresentation(grappStoreService.findByID(id)));
   }

   @PUT
   @Path("name")
   public Response updateName(@PathParam(value = "id") final String id,
                              @QueryParam(value = "name") final String name) {
      GrappStoreDTO grappStoreDTO = grappStoreService.updateName(id, name);
      return buildHALResponse(asRepresentation(grappStoreDTO));
   }

   @PUT
   @Path("location")
   public Response updateLocation(@PathParam(value = "id") final String id,
                                  @QueryParam(value = "location") final GeoPoint location) {
      GrappStoreDTO grappStoreDTO = grappStoreService.updateLocation(id, location);
      return buildHALResponse(asRepresentation(grappStoreDTO));
   }

   @DELETE
   public Response deleteByID(@PathParam(value = "id") final String id) {
      grappStoreService.deleteByID(id);
      return Response.ok().build();
   }

   protected static List<HALRepresentation> asRepresentations(Iterable<GrappStoreDTO> grappStoreDTOs) {
      List<HALRepresentation> representations = new ArrayList<>();
      for (GrappStoreDTO grappStoreDTO : grappStoreDTOs) {
         representations.add(asRepresentation(grappStoreDTO));
      }
      return representations;
   }

   protected static HALRepresentation asRepresentation(GrappStoreDTO grappStoreDTO) {
      return halRepresentationFactory.createFor(grappStoreDTO).withLinks(createLinks(grappStoreDTO));
   }

   protected static List<HALLink> createLinks(GrappStoreDTO grappStoreDTO) {
      return Arrays.asList(
         HALResourceLinkBuilder.linkTo(GrappStoreResource.class).pathArgs(grappStoreDTO.getId()).withSelfRel(),
         HALResourceLinkBuilder.linkTo(GrappStoreResource.class).method("updateName").pathArgs(grappStoreDTO.getId()).queryParams("name").withRel("updateName"),
         HALResourceLinkBuilder.linkTo(GrappStoreResource.class).method("updateLocation").pathArgs(grappStoreDTO.getId()).queryParams("location").withRel("updateLocation")
      );
   }
}