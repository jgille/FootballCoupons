package org.jon.ivmark.footballcoupons.application.greeting.resources;

import org.jon.ivmark.footballcoupons.api.GreetingDto;
import org.jon.ivmark.footballcoupons.application.greeting.services.GreetingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/greeting")
@Produces(APPLICATION_JSON)
public class GreetingResource {

    private final GreetingService service;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public GreetingResource(GreetingService service) {
        this.service = service;
    }

    @GET
    @Path("/{user}")
    public GreetingDto greetMe(@PathParam("user") String user) {
        logger.info("Greeting user {}", user);
        return service.greetMe(user);
    }
}
