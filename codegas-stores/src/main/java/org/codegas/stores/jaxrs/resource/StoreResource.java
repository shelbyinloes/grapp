package org.codegas.stores.jaxrs.resource;

import java.util.Collections;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.codegas.commons.lang.spacial.GeoPoint;
import org.codegas.webservice.hal.api.HalConfig;
import org.codegas.webservice.hal.api.HalLink;
import org.codegas.webservice.hal.api.HalRepresentation;
import org.codegas.webservice.hal.api.HalRepresentationFactory;
import org.codegas.webservice.hal.jaxrs.HalResourceLinkBuilder;
import org.codegas.stores.service.api.StoreService;
import org.codegas.stores.service.dto.StoreDto;

@Path("/stores/{id}/")
@RolesAllowed("STORE_MANAGER")
public class StoreResource extends HalJsonResource {

    private final StoreService storeService;

    @Inject
    public StoreResource(HalConfig halConfig, StoreService storeService) {
        super(halConfig);
        this.storeService = storeService;
    }

    @GET
    @PermitAll
    public Response get(@PathParam(value = "id") String id) {
        return buildHalResponse(asRepresentationOf(storeService.get(id)));
    }

    @PUT
    public Response update(@PathParam(value = "id") String id,
        @QueryParam(value = "name") String name,
        @QueryParam(value = "location") GeoPoint location) {
        return buildHalResponse(asRepresentationOf(storeService.update(id, name, location)));
    }

    @DELETE
    public Response delete(@PathParam(value = "id") String id) {
        storeService.delete(id);
        return Response.ok().build();
    }

    protected HalRepresentation asRepresentationOf(StoreDto storeDto) {
        return asRepresentationOf(halRepresentationFactory, storeDto);
    }

    protected static HalRepresentation asRepresentationOf(HalRepresentationFactory halRepresentationFactory, StoreDto storeDto) {
        return halRepresentationFactory.createFor(storeDto).withLinks(createLinks(storeDto));
    }

    protected static HalLink createRootLink(String rel) {
        return createSelfLinkBuilder().withRel(rel);
    }

    private static List<HalLink> createLinks(StoreDto storeDto) {
        return Collections.singletonList(createSelfLinkBuilder().pathArgs(storeDto.getId()).withSelfRel());
    }

    private static HalResourceLinkBuilder createSelfLinkBuilder() {
        return HalResourceLinkBuilder.linkTo(StoreResource.class).queryParams("name", "location");
    }
}
