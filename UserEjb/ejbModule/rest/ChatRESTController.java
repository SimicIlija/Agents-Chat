package rest;

import java.util.List;

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
import dataAccess.ChatMessage.ChatMessageServiceLocal;
import jms_messages.LastChatsResMsg;
import jms_messages.MessageReqMsg;
import model.Chat;

@Path("/chat")
@Stateless
public class ChatRESTController {

	@EJB
	private DB_AccessLocal DB_Access;
	
	@EJB
	private ChatMessageServiceLocal chatMessageService;
	
	@GET
	@Path("/lastChats/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public LastChatsResMsg getLastChats(@PathParam("username") String username) {
		//return  DB_Access.getLastChats(username);
		List<Chat> chats =  chatMessageService.getLastChats(username);
		
		LastChatsResMsg ret = new LastChatsResMsg();
		ret.setChats(chats);
		return ret;
	}
	
	@POST
	@Path("/receiveMessage")
	@Consumes(MediaType.APPLICATION_JSON)
	public void receiveMessage(MessageReqMsg messageReqMsg) {
		 DB_Access.saveMessage(messageReqMsg);
	}
	
}
