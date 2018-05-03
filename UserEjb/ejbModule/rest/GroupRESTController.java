package rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jms_messages.LastChatsResMsg;
import model.Chat;

@Path("/group")
@Stateless
public class GroupRESTController {

	@GET
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public LastChatsResMsg getGroups(@PathParam("username") String username) {
		return null;
	}
}
