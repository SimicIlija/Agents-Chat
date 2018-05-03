package ejb_beans;

import java.util.List;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.Stateless;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.fasterxml.jackson.databind.ObjectMapper;

import cluster.ClusterManagerLocal;
import cluster.UserClusterManagerLocal;
import jms_messages.JMSMessageToWebSocket;
import jms_messages.JMSMessageToWebSocketType;
import model.Host;
import model.User;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/ouQueue") })
public class UserClusterEndpoint implements MessageListener{

	@EJB
	private UserClusterManagerLocal userClusterManager;
	
	@EJB
	private ClusterManagerLocal clusterManager;
	
	
	@Override
	public void onMessage(javax.jms.Message arg0) {
		System.out.println("Stigla poruka za horvata");
		ObjectMessage objectMessage = (ObjectMessage) arg0;
		try {
			JMSMessageToWebSocket message = (JMSMessageToWebSocket) objectMessage.getObject();

			if (message.getType() == JMSMessageToWebSocketType.NEW_ACITE_USER) {
				String jsonUser = (String) message.getContent();
				ObjectMapper objectMapper = new ObjectMapper();
				User user = objectMapper.readValue(jsonUser, User.class);
				
				sendToAllHostThatNewUserIsActive( user );
			}
			else if (message.getType() == JMSMessageToWebSocketType.USER_NO_LOGER_ACTIVE) {
				User user = (User) message.getContent();
				// TODO Nebojsa uradi isto kao i ono gore
				sendToAllHostThatUserIsNoLongerActive( user );
			}
			
				
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void sendToAllHostThatUserIsNoLongerActive(User user) {
		List<Host> hosts = clusterManager.getAllHost();
		for (Host host: hosts) {
			
			ResteasyClient client = new ResteasyClientBuilder().build();
			ResteasyWebTarget target = client.target("http://"+host.getAddress()+":"+host.getPort()+"/ChatWeb/rest/User/removeActiveUser");
			Response response = target.request().post(Entity.entity(user, MediaType.APPLICATION_JSON));
		}
	}


	private void sendToAllHostThatNewUserIsActive(User user) {
		List<Host> hosts = clusterManager.getAllHost();
		for (Host host: hosts) {
			
			ResteasyClient client = new ResteasyClientBuilder().build();
			ResteasyWebTarget target = client.target("http://"+host.getAddress()+":"+host.getPort()+"/ChatWeb/rest/User/addActiveUser");
			Response response = target.request().post(Entity.entity(user, MediaType.APPLICATION_JSON));
		}	
	}
	
}
