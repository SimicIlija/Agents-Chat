package restControllers;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jms_messages.JMSMessageToWebSocket;
import jms_messages.JMSMessageToWebSocketType;
import jms_messages.MessageReqMsg;

@LocalBean
@Path("/chat")
@Stateless
public class ChatController {

	@POST
	@Path("/messageToPush")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String messageToPush(MessageReqMsg messageReqMsg) {
		
		JMSMessageToWebSocket message = new JMSMessageToWebSocket();
		message.setType(JMSMessageToWebSocketType.PUSH_MESSAGE);
		message.setContent(messageReqMsg);
		
		// TODO JSM do webSocketa-a (posalji message) i onda pukni klijentu
		
		return "ok";
	}
}
