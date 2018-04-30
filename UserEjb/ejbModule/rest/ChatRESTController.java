package rest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import beans.DB_AccessLocal;
import jms_messages.LastChatsResMsg;
import jms_messages.MessageReqMsg;

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
	
	@POST
	@Path("/receiveMessage")
	@Consumes(MediaType.APPLICATION_JSON)
	public void receiveMessage(MessageReqMsg messageReqMsg) {
		 DB_Access.saveMessage(messageReqMsg);
	}
	
}
