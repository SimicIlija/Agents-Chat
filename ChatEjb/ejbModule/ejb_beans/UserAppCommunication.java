package ejb_beans;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import config.PropertiesSupplier;
import config.PropertiesSupplierLocal;
import jms_messages.LastChatsResMsg;
import jms_messages.UserAuthReqMsg;
import jms_messages.UserAuthResMsg;

@Stateless
public class UserAppCommunication implements UserAppCommunicationLocal{

	@EJB
	private PropertiesSupplierLocal prop;
	
	@Override
	public UserAuthResMsg sendAuthAttempt(UserAuthReqMsg userAuthMsg) {
		boolean is_master = prop.getProperty("IS_MASTER").equals("true");
		UserAuthResMsg ret =null;
		// TODO kad Sima uradi JMS SKOLoniti komentare
//		if(is_master) {
//			ret = sendAuthAttempt_JMS(userAuthMsg);
//		}else {
			ret = sendAuthAttempt_REST(userAuthMsg);
//		}
		return ret;
	}

	@Override
	public UserAuthResMsg sendAuthAttempt_JMS(UserAuthReqMsg userAuthMsg) {
		// TODO Simo uradti preko JMS
		return null;
	}

	@Override
	public UserAuthResMsg sendAuthAttempt_REST(UserAuthReqMsg userAuthMsg) {
		ResteasyClient client = new ResteasyClientBuilder().build();
		// TODO izmeniti da nije hardCoded adresa
		ResteasyWebTarget target = client.target("http://localhost:8080/UserWeb/rest/user-auth/login");
		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(userAuthMsg, MediaType.APPLICATION_JSON));
		UserAuthResMsg resMsg = response.readEntity(UserAuthResMsg.class);
		
		return resMsg;
	}

	@Override
	public LastChatsResMsg getLastChats(String username) {
		boolean is_master = prop.getProperty("IS_MASTER").equals("true");
		LastChatsResMsg ret =null;
		// TODO kad Sima uradi JMS SKOLoniti komentare
//		if(is_master) {
//			ret = getLastChats_JMS(username);
//		}else {
			ret = getLastChats_REST(username);
//		}
		return ret;
	}

	@Override
	public LastChatsResMsg getLastChats_JMS(String username) {
		// TODO Simo uradi
		return null;
	}

	@Override
	public LastChatsResMsg getLastChats_REST(String username) {
		ResteasyClient client = new ResteasyClientBuilder().build();
		// TODO Skloniti hard coded putanju 
		ResteasyWebTarget target = client.target("http://localhost:8080/UserWeb/rest/chat/lastChats/"+username);
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		LastChatsResMsg resMsg = response.readEntity(LastChatsResMsg.class);
		
		return resMsg;
	}

}
