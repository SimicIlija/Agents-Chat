package webSockets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.databind.ObjectMapper;

import cluster.UserClusterManagerLocal;
import ejb_beans.ChatAppCommunicationLocal;
import ejb_beans.UserAppCommunicationLocal;
import jms_messages.GroupChatReqMsg;
import jms_messages.GroupChatResMsg;
import jms_messages.JMSMessageToWebSocket;
import jms_messages.JMSMessageToWebSocketType;
import jms_messages.LastChatsResMsg;
import jms_messages.MessageReqMsg;
import jms_messages.MessageReqMsg_JMS;
import jms_messages.UserAuthReqMsg;
import jms_messages.UserAuthReqMsgType;
import jms_messages.UserAuthResMsg;
import jms_messages.UserAuthResMsgType;
import jms_messages.UserFriendsReqMsg;
import jms_messages.UserFriendsResMsg;
import jms_messages.WebSocketMessage;
import jms_messages.WebSocketMessageType;
import jms_messages.UserFriendsReqMsgType;
import jms_messages.UserFriendsResMsgType;
import model.User;

@ServerEndpoint("/Socket")
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/wsQueue") })
public class UserWebSocket implements MessageListener {

	Logger log = Logger.getLogger("Websockets endpoint");

	private static Map<String, String> userSession = new HashMap<>();
	private static Map<String, String> sessionUser = new HashMap<>();

	private static ArrayList<Session> sessions = new ArrayList<>();

	@EJB
	UserAppCommunicationLocal userAppCommunication;

	@EJB
	ChatAppCommunicationLocal chatAppCommunication;

	@EJB
	private UserClusterManagerLocal userClusterManager;

	@OnOpen
	public void onOpen(Session session) {
		if (!sessions.contains(session)) {
			sessions.add(session);
			log.info("Dodao sesiju: " + session.getId() + " u endpoint-u: " + this.hashCode() + ", ukupno sesija: "
					+ sessions.size());
			log.info("BROJ SESIJA: " + sessions.size());
		}
	}

	@OnMessage
	public void onWSMessage(Session session, String msg, boolean last) {

		try {
			if (session.isOpen()) {
				log.info(
						"Websocket endpoint: " + this.hashCode() + " primio: " + msg + " u sesiji: " + session.getId());

				if (isA(msg, WebSocketMessage.class)) {
					handleWebSocketMessage(session, msg);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				session.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void handleWebSocketMessage(Session session, String msg) {
		WebSocketMessage webSocketMessage;
		try {

			ObjectMapper mapper = new ObjectMapper();
			webSocketMessage = mapper.readValue(msg, WebSocketMessage.class);

			if (webSocketMessage.getType() == WebSocketMessageType.LOGIN) {
				handleLoginMessage(session, webSocketMessage.getContent());
			} else if (webSocketMessage.getType() == WebSocketMessageType.MESSAGE) {
				handleSendMessage(session, webSocketMessage.getContent());
			} else if (webSocketMessage.getType() == WebSocketMessageType.LAST_CHATS) {
				handleGetLastChats(session, webSocketMessage.getContent());
			} else if (webSocketMessage.getType() == WebSocketMessageType.LOGOUT) {
				handleLogOut(session);
			} else if (webSocketMessage.getType() == WebSocketMessageType.GROUPS) {
				handleMyGroups(session);
			} else if (webSocketMessage.getType() == WebSocketMessageType.USER_FRIENDS_REQ) {
				handleUserFriendsReq(session, webSocketMessage.getContent());
			} else if (webSocketMessage.getType() == WebSocketMessageType.REGISTER) {
				handleRegister(session, webSocketMessage.getContent());
			} else if(webSocketMessage.getType() == WebSocketMessageType.GROUP_CHAT_REQ) {
				handleGroupChatMsgReq(session, webSocketMessage.getContent());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@OnClose
	public void close(Session session) {
		sessions.remove(session);
		String username = sessionUser.get(session.getId());

		// radim logout ako je neko ulogovan
		userAppCommunication.logoutAttempt(username);

		userSession.remove(username);
		sessionUser.remove(session.getId());
		log.info("Zatvorio: " + session.getId() + " u endpoint-u: " + this.hashCode());

	}

	@OnError
	public void error(Session session, Throwable t) {
		sessions.remove(session);
		log.log(Level.SEVERE, "Gre�ka u sessiji: " + session.getId() + " u endpoint-u: " + this.hashCode() + ", tekst: "
				+ t.getMessage());
		t.printStackTrace();
	}

	boolean isA(String json, Class expected) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.readValue(json, expected);
			return true;
		} catch (Exception e) {
			System.out.println("CANT CONVERT!");
			return false;
		}
	}

	private void handleSendMessage(Session session, String msg) {
		String username = sessionUser.get(session.getId());
		if (username == null)
			return;
		MessageReqMsg messageReqMsg = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			messageReqMsg = mapper.readValue(msg, MessageReqMsg.class);
			messageReqMsg.setSender(username);

			// send message to userApp for saving
			userAppCommunication.sendMessage(messageReqMsg);

			// send message to other users
			chatAppCommunication.sendMessageToOtherUsers(messageReqMsg);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleGetLastChats(Session session, String numberOfChats) {
		String username = sessionUser.get(session.getId());
		if (username == null)
			return;
		try {
			userAppCommunication.getLastChats(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleRegister(Session session, String content) {
		User user = null;
		try {

			ObjectMapper mapper = new ObjectMapper();
			user = mapper.readValue(content, User.class);
			log.info(user.getUsername());

			UserAuthReqMsg userAuthMsg = new UserAuthReqMsg(user, session.getId(), null, UserAuthReqMsgType.LOGIN);
			userAppCommunication.register(userAuthMsg);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void handleLoginMessage(Session session, String msg) {
		User user = null;
		try {

			ObjectMapper mapper = new ObjectMapper();
			user = mapper.readValue(msg, User.class);
			log.info(user.getUsername());

			UserAuthReqMsg userAuthMsg = new UserAuthReqMsg(user, session.getId(), null, UserAuthReqMsgType.LOGIN);
			userAppCommunication.sendAuthAttempt(userAuthMsg);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleMyGroups(Session session) {
		String username = sessionUser.get(session.getId());
		userAppCommunication.getMyGroups(username);
	}

	private void handleLogOut(Session session) {
		String username = sessionUser.get(session.getId());
		System.out.println(username);
		userAppCommunication.logoutAttempt(username);
	}

	@Override
	public void onMessage(javax.jms.Message arg0) {
		System.out.println("Stigla poruka");
		ObjectMessage objectMessage = (ObjectMessage) arg0;
		try {
			JMSMessageToWebSocket message = (JMSMessageToWebSocket) objectMessage.getObject();

			if (message.getType() == JMSMessageToWebSocketType.PUSH_MESSAGE) {
				MessageReqMsg_JMS messageReqMsg_JMS = (MessageReqMsg_JMS) message.getContent();
				pushMessageToClient(messageReqMsg_JMS);
			}
			if (message.getType() == JMSMessageToWebSocketType.LOGIN_FAILURE) {
				System.out.println("Neuspesno logovanje");
				String json = (String) message.getContent();
				System.out.println(json);
				ObjectMapper mapper = new ObjectMapper();
				UserAuthResMsg userAuthResMsg = mapper.readValue(json, UserAuthResMsg.class);
				String id = userAuthResMsg.getSessionId();
				WebSocketMessage wsm = new WebSocketMessage();
				wsm.setType(WebSocketMessageType.LOGIN_FAILURE);
				String wsmJSON = mapper.writeValueAsString(wsm);
				System.out.println(wsmJSON);
				Session session = null;
				for (Session s : sessions) {
					if (s.getId().equals(id)) {
						session = s;
					}
				}
				session.getBasicRemote().sendText(wsmJSON);
			}
			if (message.getType() == JMSMessageToWebSocketType.LOGIN_SUCCESS) {
				System.out.println("Uspesno logovanje");
				String json = (String) message.getContent();
				System.out.println(json);
				ObjectMapper mapper = new ObjectMapper();
				UserAuthResMsg userAuthResMsg = mapper.readValue(json, UserAuthResMsg.class);
				User user = userAuthResMsg.getUser();

				// TODO SIMO OVDE NISI PAZIO DA LI USER MOZE BITI NULL KONTAM OD PRILIKE IZ ZBOG
				// CEPA ALI JA SAM SAMO VRATIO NULL PA TI VIDI JEL TO OK
				if (user == null)
					return;

				String id = userAuthResMsg.getSessionId();
				userSession.put(user.getUsername(), id);
				sessionUser.put(id, user.getUsername());
				userClusterManager.addUserToActiveList(user);
				WebSocketMessage wsm = new WebSocketMessage();
				wsm.setType(WebSocketMessageType.LOGIN_SUCCESS);
				String jsonUser = mapper.writeValueAsString(user);
				wsm.setContent(jsonUser);
				String wsmJSON = mapper.writeValueAsString(wsm);
				Session session = null;
				for (Session s : sessions) {
					if (s.getId().equals(id)) {
						session = s;
					}
				}
				session.getBasicRemote().sendText(wsmJSON);
			}
			if (message.getType() == JMSMessageToWebSocketType.LAST_CHATS) {
				String json = (String) message.getContent();
				System.out.println(json);
				ObjectMapper mapper = new ObjectMapper();
				LastChatsResMsg response = mapper.readValue(json, LastChatsResMsg.class);
				String username = response.getUsername();
				String id = userSession.get(username);
				WebSocketMessage wsm = new WebSocketMessage();
				wsm.setType(WebSocketMessageType.LAST_CHATS);
				String content = mapper.writeValueAsString(response);
				wsm.setContent(content);
				String wsmJSON = mapper.writeValueAsString(wsm);
				Session session = null;
				for (Session s : sessions) {
					if (s.getId().equals(id)) {
						session = s;
					}
				}
				session.getBasicRemote().sendText(wsmJSON);
			}
			if (message.getType() == JMSMessageToWebSocketType.LOGOUT) {
				String username = (String) message.getContent();
				String id = userSession.get(username);
				userSession.remove(username);
				Session session = null;
				for (Session s : sessions) {
					if (s.getId().equals(id)) {
						session = s;
					}
				}
				ObjectMapper mapper = new ObjectMapper();
				WebSocketMessage wsm = new WebSocketMessage();
				wsm.setType(WebSocketMessageType.LOGOUT);
				String wsmJSON = mapper.writeValueAsString(wsm);
				session.getBasicRemote().sendText(wsmJSON);
				sessionUser.remove(session.getId());
			}
			if (message.getType() == JMSMessageToWebSocketType.GROUP_CHAT_RES) {
				handleGroupChatRes((String)message.getContent());
			}
			if (message.getType() == JMSMessageToWebSocketType.USER_FRIENDS_RES) {
				handleUserFriendsRes((String) message.getContent());
			}
			if (message.getType() == JMSMessageToWebSocketType.REGISTER) {
				String json = (String) message.getContent();
				ObjectMapper mapper = new ObjectMapper();
				UserAuthResMsg userAuthResMsg = mapper.readValue(json, UserAuthResMsg.class);
				String id = userAuthResMsg.getSessionId();
				Session session = null;
				for (Session s : sessions) {
					if (s.getId().equals(id)) {
						session = s;
					}
				}
				WebSocketMessage wsm = new WebSocketMessage();
				wsm.setType(WebSocketMessageType.REGISTER);
				wsm.setContent(mapper.writeValueAsString(userAuthResMsg.getType()));
				String wsmJSON = mapper.writeValueAsString(wsm);
				session.getBasicRemote().sendText(wsmJSON);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void pushMessageToClient(MessageReqMsg_JMS messageReqMsg_JMS) {

		try {
			ObjectMapper mapper = new ObjectMapper();
			WebSocketMessage wsm = new WebSocketMessage();
			wsm.setType(WebSocketMessageType.MESSAGE);
			String content = mapper.writeValueAsString(messageReqMsg_JMS);
			wsm.setContent(content);
			String wsmJSON = mapper.writeValueAsString(wsm);

			// Nalazim na kojoj je sesiji taj user
			String username = messageReqMsg_JMS.getUsernames().get(0);
			String sessionId = userSession.get(username);
			for (Session s : sessions) {
				if (s.getId().equals(sessionId)) {
					s.getBasicRemote().sendText(wsmJSON);
					break;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleUserFriendsReq(Session session, String content) {
		String username = sessionUser.get(session.getId());
		if (username == null)
			return;
		try {
			ObjectMapper mapper = new ObjectMapper();
			UserFriendsReqMsg msg = mapper.readValue(content, UserFriendsReqMsg.class);
			msg.setSessionId(session.getId());
			userAppCommunication.sendUserFriendsReqMsg(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleUserFriendsRes(String json) {
		ObjectMapper mapper = new ObjectMapper();
		UserFriendsResMsg msg = null;
		try {
			msg = mapper.readValue(json, UserFriendsResMsg.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(json);
		Session session = null;
		for (Session s : sessions) {
			if (s.getId().equals(msg.getSessionId())) {
				session = s;
			}
		}
		if (session == null)
			return;
		WebSocketMessage wsm = new WebSocketMessage();
		wsm.setType(WebSocketMessageType.USER_FRIENDS_RES);
		wsm.setContent(json);
		try {
			session.getBasicRemote().sendText(mapper.writeValueAsString(wsm));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleGroupChatMsgReq(Session session, String content) {
		String username = sessionUser.get(session.getId());
		if (username == null)
			return;
		try {
			ObjectMapper mapper = new ObjectMapper();
			GroupChatReqMsg msg = mapper.readValue(content, GroupChatReqMsg.class);
			msg.setAdmin(username);
			msg.setSessionId(session.getId());
			userAppCommunication.groupChatReqMsg(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void handleGroupChatRes(String json) {
		ObjectMapper mapper = new ObjectMapper();
		GroupChatResMsg msg = null;
		try {
			msg = mapper.readValue(json, GroupChatResMsg.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(json);
		Session session = null;
		for (Session s : sessions) {
			if (s.getId().equals(msg.getSessionId())) {
				session = s;
			}
		}
		if (session == null)
			return;
		WebSocketMessage wsm = new WebSocketMessage();
		wsm.setType(WebSocketMessageType.GROUP_CHAT_RES);
		wsm.setContent(json);
		try {
			session.getBasicRemote().sendText(mapper.writeValueAsString(wsm));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
