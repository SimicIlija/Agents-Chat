package restControllers;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.ObjectMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import cluster.ClusterManagerLocal;
import cluster.UserClusterManagerLocal;
import jms_messages.JMSMessageToWebSocket;
import jms_messages.JMSMessageToWebSocketType;
import jms_messages.LastChatsResMsg;
import model.Host;
import model.User;

@LocalBean
@Path("/User")
@Stateless
public class UserController {

	@EJB
	private UserClusterManagerLocal userClusterManager;
	
	@EJB
	private ClusterManagerLocal clusterManager;
	
	@POST
	@Path("/addActiveUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String addActiveUser( User user) {
		
		if( userClusterManager.addUserToActiveList(user))
			return "ok";
		
		return "failed";
	}
	
	
	@POST
	@Path("/removeActiveUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String removeActiveUser( User user) {
		
		if(userClusterManager.removeUserFroActiveList(user))
			return "ok";
		return "failed";
	}
	
	
	// TODO SIMO Namesti da primi poruku
	//@Override
	public void onMessage(javax.jms.Message arg0) {
		System.out.println("Stigla poruka");
		ObjectMessage objectMessage = (ObjectMessage) arg0;
		try {
			JMSMessageToWebSocket message = (JMSMessageToWebSocket) objectMessage.getObject();

			if (message.getType() == JMSMessageToWebSocketType.NEW_ACITE_USER) {
				User user = (User) message.getContent();
				
				sendToAllHostThatNewUserIsActive( user );
			}
			else if (message.getType() == JMSMessageToWebSocketType.USER_NO_LOGER_ACTIVE) {
				User user = (User) message.getContent();
				
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
