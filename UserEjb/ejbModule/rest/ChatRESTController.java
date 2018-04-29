package rest;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jms_messages.LastChatsResMsg;

@Path("/chat")
@Stateless
public class ChatRESTController {

	@GET
	@Path("/lastChats/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public LastChatsResMsg getLastChats(@PathParam("username") String alias) {
		return null;
	}
	
}
