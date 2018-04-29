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

import ejb_beans.UserAppCommunicationLocal;
import jms_messages.UserAuthReqMsg;
import jms_messages.UserAuthReqMsgType;
import jms_messages.UserAuthResMsg;
import jms_messages.UserAuthResMsgType;
import model.User;

@ServerEndpoint("/Socket")
@Singleton
public class UserWebSocket {
	
	Logger log = Logger.getLogger("Websockets endpoint");
	
	private static Map<User,Session> logedin = new HashMap<>();
	private static Map<Session,User> logedinR = new HashMap<>();

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
				else if(msg.equals("getLatestChat")) {
					
					//TODO nabavi par poslednjih chat-ova za ovog korisnika
					session.getBasicRemote().sendText("LC");
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
		User user = logedinR.get(session);
		logedin.remove(user);
		logedinR.remove(session);
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
		     e.printStackTrace();
		     return false;
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
//				ResteasyClient client = new ResteasyClientBuilder().build();
//				// TODO izmeniti da nije hardCoded adresa
//				ResteasyWebTarget target = client.target("http://localhost:8080/UserWeb/rest/user-auth/login");
//				Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(userAuthMsg, MediaType.APPLICATION_JSON));
			UserAuthResMsg resMsg = userAppCommunication.sendAuthAttempt(userAuthMsg);
			user = resMsg.getUser();
				
			// dodaj sesiou u grupu ulogovanih
			logedin.put(user, session);
			logedinR.put(session, user);
			
			//posalji odgovor nazad
			String jsonObject = mapper.writeValueAsString(user);
			session.getBasicRemote().sendText(jsonObject);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
