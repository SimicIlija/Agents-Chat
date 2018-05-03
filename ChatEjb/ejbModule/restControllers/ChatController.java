package restControllers;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jms_messages.JMSMessageToWebSocket;
import jms_messages.JMSMessageToWebSocketType;
import jms_messages.MessageReqMsg;
import jms_messages.MessageReqMsg_JMS;

@LocalBean
@Path("/chat")
@Stateless
public class ChatController {
	
	@Inject
	private JMSContext context;

	@Resource(mappedName = "java:/jms/queue/wsQueue")
	private Destination destination;
	
	

	@POST
	@Path("/messageToPush")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String messageToPush(MessageReqMsg_JMS messageReqMsg_JMS) {
		
		JMSMessageToWebSocket message = new JMSMessageToWebSocket();
		message.setType(JMSMessageToWebSocketType.PUSH_MESSAGE);
		message.setContent(messageReqMsg_JMS);
		
		try {
			System.out.println("Saljem poruku");
			ObjectMessage objectMessage = context.createObjectMessage();
			objectMessage.setObject(message);
			JMSProducer producer = context.createProducer();
			producer.send(destination, objectMessage);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
		return "ok";
	}
}
