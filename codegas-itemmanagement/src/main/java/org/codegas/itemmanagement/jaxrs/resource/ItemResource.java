package org.codegas.itemmanagement.jaxrs.resource;

import java.util.Arrays;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.codegas.webservice.hal.api.HalConfig;
import org.codegas.webservice.hal.api.HalLink;
import org.codegas.webservice.hal.api.HalRepresentation;
import org.codegas.webservice.hal.api.HalRepresentationFactory;
import org.codegas.webservice.hal.jaxrs.HalResourceLinkBuilder;
import org.codegas.itemmanagement.service.api.ItemService;
import org.codegas.itemmanagement.service.dto.ItemDto;

@Path("/items/{primaryCode}/")
@RolesAllowed("ADMIN")
public class ItemResource extends HalJsonResource {

    private final ItemService itemService;

    @Inject
    public ItemResource(HalConfig halConfig, ItemService itemService) {
        super(halConfig);
        this.itemService = itemService;
    }

    @GET
    public Response get(@PathParam("primaryCode") String primaryCode) {
        return buildHalResponse(asRepresentationOf(itemService.get(primaryCode)));
    }

    @PUT
    public Response update(@PathParam("primaryCode") String primaryCode,
        @QueryParam("name") String name) {
        return buildHalResponse(asRepresentationOf(itemService.update(primaryCode, name)));
    }

    @PUT
    @Path("makeGeneral")
    public Response makeGeneral(@PathParam("primaryCode") String primaryCode) {
        return buildHalResponse(asRepresentationOf(itemService.makeGeneral(primaryCode)));
    }

    @PUT
    @Path("move")
    public Response move(@PathParam("primaryCode") String primaryCode,
        @QueryParam("superItemCode") String superItemCode) {
        return buildHalResponse(asRepresentationOf(itemService.move(primaryCode, superItemCode)));
    }

    @DELETE
    public Response delete(@PathParam("primaryCode") String primaryCode) {
        itemService.delete(primaryCode);
        return Response.ok().build();
    }

    protected HalRepresentation asRepresentationOf(ItemDto itemDto) {
        return asRepresentationOf(halRepresentationFactory, itemDto);
    }

    protected static HalRepresentation asRepresentationOf(HalRepresentationFactory halRepresentationFactory, ItemDto itemDto) {
        return halRepresentationFactory.createFor(itemDto).withLinks(createLinks(itemDto));
    }

    protected static HalLink createRootLink(String rel) {
        return createSelfLinkBuilder().withRel(rel);
    }

    private static List<HalLink> createLinks(ItemDto itemDto) {
        return Arrays.asList(
            createSelfLinkBuilder().pathArgs(itemDto.getPrimaryCode()).withSelfRel(),
            HalResourceLinkBuilder.linkTo(ItemResource.class).method("makeGeneral").pathArgs(itemDto.getPrimaryCode()).withRel("makeGeneral"),
            HalResourceLinkBuilder.linkTo(ItemResource.class).method("move").pathArgs(itemDto.getPrimaryCode()).queryParams("superItemCode").withRel("move")
        );
    }

    private static HalResourceLinkBuilder createSelfLinkBuilder() {
        return HalResourceLinkBuilder.linkTo(ItemResource.class).queryParams("name");
    }
}
