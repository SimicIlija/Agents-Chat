package ejb_beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.fasterxml.jackson.databind.ObjectMapper;

import cluster.UserClusterManagerLocal;
import config.PropertiesSupplierLocal;
import jms_messages.JMSMessageToWebSocket;
import jms_messages.JMSMessageToWebSocketType;
import jms_messages.MessageReqMsg;
import jms_messages.MessageReqMsg_JMS;
import model.User;
import model.Usernames;

@Stateless
public class ChatAppCommunication implements ChatAppCommunicationLocal {

	@EJB
	private UserClusterManagerLocal userClusterManager;

	@EJB
	private PropertiesSupplierLocal prop;

	@Inject
	private JMSContext context;

	@Resource(mappedName = "java:/jms/queue/wsQueue")
	private Destination destination;

	@Override
	public void sendMessageToOtherUsers(MessageReqMsg messageReqMsg) {
		
//		MessageReqMsg messageReqMsg= null;
//		try {
//		ObjectMapper mapper = new ObjectMapper();
//		messageReqMsg = mapper.readValue(msg, MessageReqMsg.class);
//		messageReqMsg.setSender(senderUsername);
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
		
		// Na osnovu chat id uzmi taj chat iz baze, vidi ko je sve u njemu i dobices listu usera tog chat-a
		String chatId = messageReqMsg.getChat().toString();
		ResteasyClient client = new ResteasyClientBuilder().build();
		String targetString = "http://"+prop.getProperty("MASTER_LOCATION")+":"+prop.getProperty("MASTER_PORT")+"/UserWeb/rest/chat/getUsers/"+chatId;
		ResteasyWebTarget target = client.target(targetString);
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		Usernames usernamesClass	= response.readEntity(Usernames.class);	
		List<String> usernames = usernamesClass.getUsernames();


		usernames.remove(messageReqMsg.getSender());
		messageReqMsg.setUsernames(usernames);

		List<User> activeUsers = userClusterManager.getAllActiveUsers();
		String currentHostName = prop.getProperty("NAME_OF_NODE");

		for (String username : usernames) {
			for (User user : activeUsers) {
				if (user.getUsername().equals(username)) {
					// Sad postavi da je umesto liste primaoca samo jedan primalac
					List<String> onlyReceiver = new ArrayList<>();
					onlyReceiver.add(username);
					messageReqMsg.setUsernames(onlyReceiver);

					// Ovo je alternativni dto koji nema ObjectId 
					MessageReqMsg_JMS reqJMS = new MessageReqMsg_JMS(messageReqMsg);
					if ( user.getHost().getName().equals(currentHostName)) {
						JMSMessageToWebSocket message = new JMSMessageToWebSocket();
						message.setType(JMSMessageToWebSocketType.PUSH_MESSAGE);
						message.setContent(reqJMS);
						
						try {
							System.out.println("Saljem poruku");
							ObjectMessage objectMessage = context.createObjectMessage();
							objectMessage.setObject(message);
							JMSProducer producer = context.createProducer();
							producer.send(destination, objectMessage);
						} catch (JMSException e) {
							e.printStackTrace();
						}
					} else {
						 client = new ResteasyClientBuilder().build();
						 target = client.target("http://" + user.getHost().getAddress() + ":"
								+ user.getHost().getPort() + "/ChatWeb/rest/chat/messageToPush");

						 response = target.request()
								.post(Entity.entity(reqJMS, MediaType.APPLICATION_JSON));
					}
				}
			}
		}

	}

}
