package edu.milkrest.sample.message;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;

@Path("messages")
public class MessageService {

    @Path("{id}")
    @GET
    @Produces("application/json")
    public Message get(@PathParam("id") Long id) {
        return new Message(id);
    }

}
