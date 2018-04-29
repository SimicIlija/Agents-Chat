package rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import beans.DB_AccessLocal;
import jms_messages.LastChatsResMsg;
import model.Chat;

@Path("/chat")
@Stateless
public class ChatRESTController {

	@EJB
	private DB_AccessLocal DB_Access;
	
	@GET
	@Path("/lastChats/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public LastChatsResMsg getLastChats(@PathParam("username") String username) {
		return DB_Access.getLastChats(username);
	}
	
}
