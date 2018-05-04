package mdb;

import java.io.IOException;
import java.util.ArrayList;
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
import beans.UserFriendsMgmtLocal;
import dataAccess.ChatMessage.ChatMessageServiceLocal;
import dataAccess.User.UserServiceLocal;
import exceptions.UserAuthException;
import jms_messages.JMSMessageToWebSocket;
import jms_messages.JMSMessageToWebSocketType;
import jms_messages.JMSUserApp;
import jms_messages.JMSUserAppType;
import jms_messages.LastChatsResMsg;
import jms_messages.UserAuth.UserAuthReqMsg;
import jms_messages.UserAuth.UserAuthResMsg;
import jms_messages.UserAuth.UserAuthResMsgType;
import jms_messages.UserFriends.UserFriendsReqMsg;
import jms_messages.UserFriends.UserFriendsReqMsgType;
import jms_messages.UserFriends.UserFriendsResMsg;
import jms_messages.UserFriends.UserFriendsResMsgType;
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
	
	@EJB
	private UserFriendsMgmtLocal userFriensMgmt;
	
	@EJB
	private UserServiceLocal userService;

	@Inject
	private JMSContext context;

	@Resource(mappedName = "java:/jms/queue/wsQueue")
	private Destination destination;
	
	@Resource(mappedName = "java:/jms/queue/ouQueue")
	private Destination destination1;

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
				
				// ako je login success onda posaljem poruku preko jms chatApp-u masteru da se ulogovao i da javi ostalima
				
				JMSMessageToWebSocket message = new JMSMessageToWebSocket();
				message.setType(JMSMessageToWebSocketType.NEW_ACITE_USER);
				mapper = new ObjectMapper();
				String jsonUser;
				try {
					jsonUser = mapper.writeValueAsString(user);
					message.setContent(jsonUser);
					objectMessage = context.createObjectMessage();
					objectMessage.setObject(message);
					JMSProducer producer = context.createProducer();
					producer.send(destination1, objectMessage);
				} catch (JsonProcessingException | JMSException e) {
					e.printStackTrace();
				}
				
				
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
			if(jmsUserApp.getType() == JMSUserAppType.USER_FRIENDS_REQ) {
				UserFriendsResMsg resMsg = handleUserFriendsReq(jmsUserApp.getContent());
				JMSMessageToWebSocket message = new JMSMessageToWebSocket();
				message.setType(JMSMessageToWebSocketType.USER_FRIENDS_RES);
				try {
					String jsonObject = mapper.writeValueAsString(resMsg);
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
	
	private UserFriendsResMsg handleUserFriendsReq(String content) {
		ObjectMapper mapper = new ObjectMapper();
		UserFriendsReqMsg msg = null;
		try {
			msg = mapper.readValue(content, UserFriendsReqMsg.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<User> ret = new ArrayList<>();
		User u = null;
		UserFriendsResMsgType type = null;
		if(msg.getType() == UserFriendsReqMsgType.SEARCH) {
			ret = userFriensMgmt.searchUsers(msg.getSearch());
			return new UserFriendsResMsg(ret, msg.getHost(), msg.getSessionId(), UserFriendsResMsgType.SEARCH);
		} else if(msg.getType() == UserFriendsReqMsgType.FRIEND_REQUEST) {
			u = userFriensMgmt.friendRequest(msg.getAddRemove(), msg.getUser());
			type = UserFriendsResMsgType.SENT_REQUEST;
		} else if(msg.getType() == UserFriendsReqMsgType.FRIEND_REQUEST_DECL) {
			u = userFriensMgmt.friendRequestDecl(msg.getUser(), msg.getAddRemove());
			type = UserFriendsResMsgType.DECLINED_REQUEST;
		} else if(msg.getType() == UserFriendsReqMsgType.ADD_FRIEND) {
			u = userFriensMgmt.addFriend(msg.getUser(), msg.getAddRemove());
			type = UserFriendsResMsgType.ADDED_FRIEND;
		} else if(msg.getType() == UserFriendsReqMsgType.REMOVE_FRIEND) {
			u = userFriensMgmt.removeFriend(msg.getUser(), msg.getAddRemove());
			type = UserFriendsResMsgType.REMOVED_FRIEND;
		} else if(msg.getType() == UserFriendsReqMsgType.UPDATE) {
			u = userService.findOne(msg.getUser());
			type = UserFriendsResMsgType.UPDATE;
		}
		if(u == null)
			return new UserFriendsResMsg(null,msg.getHost(), msg.getSessionId(), UserFriendsResMsgType.ERROR);
		ret.add(u);
		return new UserFriendsResMsg(ret,msg.getHost(), msg.getSessionId(), type);
	}

}
