package ejb_beans;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import config.PropertiesSupplierLocal;
import jms_messages.JMSMessageToWebSocket;
import jms_messages.JMSMessageToWebSocketType;
import jms_messages.JMSUserApp;
import jms_messages.JMSUserAppType;
import jms_messages.LastChatsResMsg;
import jms_messages.MessageReqMsg;
import jms_messages.UserAuthReqMsg;
import jms_messages.UserAuthResMsg;
import model.Host;
import model.User;

@Stateless
public class UserAppCommunication implements UserAppCommunicationLocal {

	@EJB
	private PropertiesSupplierLocal prop;

	@Inject
	private JMSContext context;

	@Resource(mappedName = "java:/jms/queue/wsQueue")
	private Destination destination;

	@Resource(mappedName = "java:/jms/queue/appQueue")
	private Destination appDestination;

	@Override
	public void sendAuthAttempt(UserAuthReqMsg userAuthMsg) {
		boolean is_master = prop.getProperty("IS_MASTER").equals("true");

		// Podesavanje hosta
		Host host = new Host();
		String address = prop.getProperty("LOCATION");
		host.setAddress(address);
		String port = prop.getProperty("PORT");
		host.setPort(Integer.parseInt(port));
		String name = prop.getProperty("NAME_OF_NODE");
		host.setName(name);
		userAuthMsg.getUser().setHost(host);
		if (is_master) {
			sendAuthAttempt_JMS(userAuthMsg);
		} else {
			sendAuthAttempt_REST(userAuthMsg);
		}
	}

	@Override
	public void sendAuthAttempt_JMS(UserAuthReqMsg userAuthMsg) {
		JMSUserApp message = new JMSUserApp();
		ObjectMapper mapper = new ObjectMapper();
		message.setType(JMSUserAppType.LOGIN);
		try {
			String json = mapper.writeValueAsString(userAuthMsg);
			message.setContent(json);
			ObjectMessage objectMessage = context.createObjectMessage();
			objectMessage.setObject(message);
			JMSProducer producer = context.createProducer();
			producer.send(appDestination, objectMessage);
		} catch (JsonProcessingException | JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void sendAuthAttempt_REST(UserAuthReqMsg userAuthMsg) {
		ResteasyClient client = new ResteasyClientBuilder().build();
		// TODO izmeniti da nije hardCoded adresa
		ResteasyWebTarget target = client.target("http://localhost:8080/UserWeb/rest/user-auth/login");
		Response response = target.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(userAuthMsg, MediaType.APPLICATION_JSON));
		UserAuthResMsg resMsg = response.readEntity(UserAuthResMsg.class);
		User user = resMsg.getUser();
		if (user != null) {
			sendLoginSuccess(resMsg);
		} else {
			sendLoginFailure(resMsg);
		}
	}

	private void sendLoginFailure(UserAuthResMsg resMsg) {
		JMSMessageToWebSocket message = new JMSMessageToWebSocket();
		message.setType(JMSMessageToWebSocketType.LOGIN_FAILURE);
		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsonObject = mapper.writeValueAsString(resMsg);
			message.setContent(jsonObject);
			ObjectMessage objectMessage = context.createObjectMessage();
			objectMessage.setObject(message);
			JMSProducer producer = context.createProducer();
			producer.send(destination, objectMessage);
		} catch (JMSException | JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	private void sendLoginSuccess(Object object) {
		JMSMessageToWebSocket message = new JMSMessageToWebSocket();
		message.setType(JMSMessageToWebSocketType.LOGIN_SUCCESS);
		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsonObject = mapper.writeValueAsString(object);
			message.setContent(jsonObject);
			ObjectMessage objectMessage = context.createObjectMessage();
			objectMessage.setObject(message);
			JMSProducer producer = context.createProducer();
			producer.send(destination, objectMessage);
		} catch (JMSException | JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getLastChats(String username) {
		boolean is_master = prop.getProperty("IS_MASTER").equals("true");
		if (is_master) {
			getLastChats_JMS(username);
		} else {
			getLastChats_REST(username);
		}
	}

	@Override
	public void getLastChats_JMS(String username) {
		JMSUserApp message = new JMSUserApp();
		message.setType(JMSUserAppType.LAST_CHAT);
		try {
			message.setContent(username);
			ObjectMessage objectMessage = context.createObjectMessage();
			objectMessage.setObject(message);
			JMSProducer producer = context.createProducer();
			producer.send(appDestination, objectMessage);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getLastChats_REST(String username) {
		ResteasyClient client = new ResteasyClientBuilder().build();
		// TODO Skloniti hard coded putanju
		ResteasyWebTarget target = client.target("http://localhost:8080/UserWeb/rest/chat/lastChats/" + username);
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		LastChatsResMsg resMsg = response.readEntity(LastChatsResMsg.class);
		resMsg.setUsername(username);
		JMSMessageToWebSocket message = new JMSMessageToWebSocket();
		message.setType(JMSMessageToWebSocketType.LAST_CHATS);
		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsonObject = mapper.writeValueAsString(resMsg);
			message.setContent(jsonObject);
			ObjectMessage objectMessage = context.createObjectMessage();
			objectMessage.setObject(message);
			JMSProducer producer = context.createProducer();
			producer.send(destination, objectMessage);
		} catch (JMSException | JsonProcessingException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void logoutAttempt(String username) {
		boolean is_master = prop.getProperty("IS_MASTER").equals("true");

		// TODO kad Sima uradi JMS SKOLoniti komentare
		// if(is_master) {
		// logoutAttempt_JMS(username);
		// }else {
		logoutAttempt_REST(username);
		// }

	}

	@Override
	public void logoutAttempt_JMS(String username) {
		// TODO Simo uradi

	}

	@Override
	public void logoutAttempt_REST(String username) {
		if (username == null)
			return;
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target("http://localhost:8080/UserWeb/rest/user-auth/logout/" + username);
		Response response = target.request().delete();
		JMSMessageToWebSocket message = new JMSMessageToWebSocket();
		message.setType(JMSMessageToWebSocketType.LOGOUT);
		message.setContent(username);
		try {
			ObjectMessage objectMessage = context.createObjectMessage();
			objectMessage.setObject(message);
			JMSProducer producer = context.createProducer();
			producer.send(destination, objectMessage);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendMessage(MessageReqMsg messageReqMsg) {
		boolean is_master = prop.getProperty("IS_MASTER").equals("true");

		// TODO kad Sima uradi JMS SKOLoniti komentare
		// if(is_master) {
		// sendMessageToUserApp_JMS(messageReqMsg);
		// }else {
		sendMessageToUserApp_REST(messageReqMsg);
		// }

	}

	@Override
	public void sendMessageToUserApp_JMS(MessageReqMsg messageReqMsg) {
		// TODO Nema sime da nam nije umro?

	}

	@Override
	public void sendMessageToUserApp_REST(MessageReqMsg messageReqMsg) {
		ResteasyClient client = new ResteasyClientBuilder().build();
		// TODO Skloniti hard coded putanju
		ResteasyWebTarget target = client.target("http://localhost:8080/UserWeb/rest/chat/receiveMessage");
		Response response = target.request().post(Entity.entity(messageReqMsg, MediaType.APPLICATION_JSON));

	}

}
