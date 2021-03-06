package org.codegas.stores.jaxrs.resource;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.codegas.webservice.hal.api.HalConfig;
import org.codegas.webservice.hal.api.HalLink;
import org.codegas.webservice.hal.jaxrs.HalResourceLinkBuilder;
import org.codegas.stores.service.api.StoreService;

@Path("/stores/")
public class StoresResource extends HalJsonResource {

    private final StoreService storeService;

    @Inject
    public StoresResource(HalConfig halConfig, StoreService storeService) {
        super(halConfig);
        this.storeService = storeService;
    }

    @GET
    public Response get() {
        return buildHalResponse(halRepresentationFactory.createFor(storeService.get()).withLinks(createLinks()));
    }

    protected static HalLink createRootLink(String rel) {
        return createSelfLinkBuilder().withRel(rel);
    }

    private static List<HalLink> createLinks() {
        return Collections.singletonList(createSelfLinkBuilder().withSelfRel());
    }

    private static HalResourceLinkBuilder createSelfLinkBuilder() {
        return HalResourceLinkBuilder.linkTo(StoresResource.class);
    }
}
