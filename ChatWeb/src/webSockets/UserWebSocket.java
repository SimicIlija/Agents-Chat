package webSockets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Singleton;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import model.User;

@ServerEndpoint("/Socket")
public class UserWebSocket {
	
	Logger log = Logger.getLogger("Websockets endpoint");
	
	private static Map<User,Session> logedin = new HashMap<>();
	private static Map<Session,User> logedinR = new HashMap<>();

	private static ArrayList<Session> sessions = new ArrayList<>();
	
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
		User user =null;
		try {
			if (session.isOpen()) {
				if(isA(msg, User.class)) {
					log.info("Websocket endpoint: " + this.hashCode() + " primio: " + msg + " u sesiji: " + session.getId());
					ObjectMapper mapper = new ObjectMapper();
					user = mapper.readValue(msg, User.class);
					log.info(user.getEmail());
					
					// TODO provera logovanja i dodavanje tokena
					
					// dodaj sesiou u grupu ulogovanih
					logedin.put(user, session);
					logedinR.put(session, user);
					
					//posalji odgovor nazad
					String jsonObject = mapper.writeValueAsString(user);
					session.getBasicRemote().sendText(jsonObject);
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
	
}
