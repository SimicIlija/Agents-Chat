package ejb_beans;

import java.io.Serializable;
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

import cluster.UserClusterManagerLocal;
import config.PropertiesSupplierLocal;
import jms_messages.JMSMessageToWebSocket;
import jms_messages.JMSMessageToWebSocketType;
import jms_messages.MessageReqMsg;
import model.User;

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
		// TODO na osnovu chat id uzmi taj chat iz baze, vidi ko je sve u njemu i
		// dobices listu usera tog chat-a
		List<String> usernames; // = ono sta chat iz base kaze da su useri ALI BEZ ONOGA KO SALJE

		// ovo je sve za test
		usernames = new ArrayList<>();
		usernames.add("proba");
		// kraj testa

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

					if (user.getHost().getName().equals(currentHostName)) {
						JMSMessageToWebSocket message = new JMSMessageToWebSocket();
						message.setType(JMSMessageToWebSocketType.PUSH_MESSAGE);
						message.setContent(messageReqMsg);
						
						try {
							System.out.println("Saljem poruku");
							ObjectMessage objectMessage = context.createObjectMessage();
							objectMessage.setObject(message);
							JMSProducer producer = context.createProducer();
							producer.send(destination, objectMessage);
						} catch (JMSException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						ResteasyClient client = new ResteasyClientBuilder().build();
						ResteasyWebTarget target = client.target("http://" + user.getHost().getAddress() + ":"
								+ user.getHost().getPort() + "/ChatWeb/rest/chat/messageToPush");

						Response response = target.request()
								.post(Entity.entity(messageReqMsg, MediaType.APPLICATION_JSON));
					}
				}
			}
		}

	}

}
