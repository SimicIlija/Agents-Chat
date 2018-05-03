package mdb;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.UserAuthMgmtLocal;
import dataAccess.ChatMessage.ChatMessageServiceLocal;
import exceptions.UserAuthException;
import jms_messages.JMSMessageToWebSocket;
import jms_messages.JMSMessageToWebSocketType;
import jms_messages.JMSUserApp;
import jms_messages.JMSUserAppType;
import jms_messages.LastChatsResMsg;
import jms_messages.UserAuth.UserAuthReqMsg;
import jms_messages.UserAuth.UserAuthResMsg;
import jms_messages.UserAuth.UserAuthResMsgType;
import model.Chat;
import model.User;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/appQueue") })
public class UserMDB implements MessageListener {

	@EJB
	UserAuthMgmtLocal userAuthMgmt;

	@EJB
	private ChatMessageServiceLocal chatMessageService;

	@Inject
	private JMSContext context;

	@Resource(mappedName = "java:/jms/queue/wsQueue")
	private Destination destination;

	@Override
	public void onMessage(Message arg0) {
		ObjectMessage objectMessage = (ObjectMessage) arg0;
		ObjectMapper mapper = new ObjectMapper();
		try {
			JMSUserApp jmsUserApp = (JMSUserApp) objectMessage.getObject();
			if (jmsUserApp.getType() == JMSUserAppType.LOGIN) {
				String json = jmsUserApp.getContent();
				UserAuthReqMsg request = mapper.readValue(json, UserAuthReqMsg.class);
				User user = null;
				try {
					user = userAuthMgmt.logIn(request.getUser(), request.getHost());
				} catch (UserAuthException e) {
					sendLoginFailure(
							new UserAuthResMsg(user, request.getSessionId(), UserAuthResMsgType.INVALID_CREDENTIALS));
				}
				if (user == null) {
					sendLoginFailure(
							new UserAuthResMsg(user, request.getSessionId(), UserAuthResMsgType.INVALID_CREDENTIALS));
				}
				sendLoginSuccess(new UserAuthResMsg(user, request.getSessionId(), UserAuthResMsgType.LOGGED_IN));
			}
			if (jmsUserApp.getType() == JMSUserAppType.LAST_CHAT) {
				List<Chat> chats = chatMessageService.getLastChats(jmsUserApp.getContent());
				LastChatsResMsg ret = new LastChatsResMsg();
				ret.setChats(chats);
				ret.setUsername(jmsUserApp.getContent());
				JMSMessageToWebSocket message = new JMSMessageToWebSocket();
				message.setType(JMSMessageToWebSocketType.LAST_CHATS);
				try {
					String jsonObject = mapper.writeValueAsString(ret);
					message.setContent(jsonObject);
					ObjectMessage oMessage = context.createObjectMessage();
					oMessage.setObject(message);
					JMSProducer producer = context.createProducer();
					producer.send(destination, oMessage);
				} catch (JMSException | JsonProcessingException e) {
					e.printStackTrace();
				}
			}
		} catch (JMSException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendLoginSuccess(UserAuthResMsg response) {
		JMSMessageToWebSocket message = new JMSMessageToWebSocket();
		message.setType(JMSMessageToWebSocketType.LOGIN_SUCCESS);
		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsonObject = mapper.writeValueAsString(response);
			message.setContent(jsonObject);
			ObjectMessage objectMessage = context.createObjectMessage();
			objectMessage.setObject(message);
			JMSProducer producer = context.createProducer();
			producer.send(destination, objectMessage);
		} catch (JMSException | JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	private void sendLoginFailure(UserAuthResMsg response) {
		JMSMessageToWebSocket message = new JMSMessageToWebSocket();
		message.setType(JMSMessageToWebSocketType.LOGIN_FAILURE);
		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsonObject = mapper.writeValueAsString(response);
			message.setContent(jsonObject);
			ObjectMessage objectMessage = context.createObjectMessage();
			objectMessage.setObject(message);
			JMSProducer producer = context.createProducer();
			producer.send(destination, objectMessage);
		} catch (JMSException | JsonProcessingException e) {
			e.printStackTrace();
		}

	}

}
