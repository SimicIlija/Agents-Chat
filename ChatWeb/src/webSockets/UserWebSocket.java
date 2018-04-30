package webSockets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import dto.MessageDTO;
import ejb_beans.UserAppCommunicationLocal;
import jms_messages.LastChatsResMsg;
import jms_messages.UserAuthReqMsg;
import jms_messages.UserAuthReqMsgType;
import jms_messages.UserAuthResMsg;
import jms_messages.UserAuthResMsgType;
import jms_messages.MessageReqMsg;

import model.User;

@ServerEndpoint("/Socket")
@Singleton
public class UserWebSocket {
	
	Logger log = Logger.getLogger("Websockets endpoint");
	
	private static Map<String,String> userSession = new HashMap<>();
	private static Map<String,String> sessionUser = new HashMap<>();

	private static ArrayList<Session> sessions = new ArrayList<>();
	
	@EJB
	UserAppCommunicationLocal userAppCommunication;
	
	@OnOpen
	public void onOpen(Session session) {
		if (!sessions.contains(session)) {
			sessions.add(session);
			log.info("Dodao sesiju: " + session.getId() + " u endpoint-u: " + this.hashCode() + ", ukupno sesija: " + sessions.size());
			log.info("BROJ SESIJA: "+ sessions.size());
		}
	} 

	@OnMessage
	public void echoTextMessage(Session session, String msg, boolean last) {
		
		try {
			if (session.isOpen()) {
				log.info("Websocket endpoint: " + this.hashCode() + " primio: " + msg + " u sesiji: " + session.getId());
				
				if(isA(msg, User.class)) {
					handleLoginMessage(session, msg);
				}
				else if(isA(msg,MessageReqMsg.class)) {
					handleSendSessage(session,msg);
				}
				else if(msg.equals("getLatestChat")) {
					
					handleGetLastChats(session);
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
	

	

	@OnClose
	public void close(Session session) {
		sessions.remove(session);
		String username = sessionUser.get(session.getId());
		
		//radim logout ako je neko ulogovan
		userAppCommunication.logoutAttempt(username);
		
		userSession.remove(username);
		sessionUser.remove(session);
		log.info("Zatvorio: " + session.getId() + " u endpoint-u: " + this.hashCode());
		
		
	}
	
	@OnError
	public void error(Session session, Throwable t) {
		sessions.remove(session);
		log.log(Level.SEVERE, "Gre�ka u sessiji: " + session.getId() + " u endpoint-u: " + this.hashCode() + ", tekst: " + t.getMessage());
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
	
	private void handleSendSessage(Session session, String msg) {
		String username = sessionUser.get(session.getId());
		if(username == null)
			return ;
		MessageReqMsg messageReqMsg= null;
		try {
			
			ObjectMapper mapper = new ObjectMapper();
			messageReqMsg = mapper.readValue(msg, MessageReqMsg.class);
			messageReqMsg.setSender(username);
			userAppCommunication.sendMessage(messageReqMsg);
				
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void handleGetLastChats(Session session) {
		String username = sessionUser.get(session.getId());
		if(username == null)
			return ;
		try {
			
			ObjectMapper mapper = new ObjectMapper();
			LastChatsResMsg ret = userAppCommunication.getLastChats(username);
			
			String jsonObject = mapper.writeValueAsString(ret);
			session.getBasicRemote().sendText(jsonObject);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void handleLoginMessage(Session session, String msg)
	{
		User user =null;
		try {
			
			ObjectMapper mapper = new ObjectMapper();
			user = mapper.readValue(msg, User.class);
			log.info(user.getUsername());
			
			// TODO provera logovanja i dodavanje tokena
			UserAuthReqMsg userAuthMsg = new UserAuthReqMsg(user, session.getId(), null, UserAuthReqMsgType.LOGIN);
			UserAuthResMsg resMsg = userAppCommunication.sendAuthAttempt(userAuthMsg);
			user = resMsg.getUser();
			if(user == null)
				return;
			
			// dodaj sesiou u grupu ulogovanih
			userSession.put(user.getUsername(), session.getId());
			sessionUser.put(session.getId(), user.getUsername());
			
			//posalji odgovor nazad
			String jsonObject = mapper.writeValueAsString(user);
			session.getBasicRemote().sendText(jsonObject);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
