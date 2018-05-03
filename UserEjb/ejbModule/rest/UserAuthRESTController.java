package rest;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.UserAuthMgmtLocal;
import dataAccess.User.UserServiceLocal;
import exceptions.UserAuthException;
import jms_messages.JMSMessageToWebSocket;
import jms_messages.JMSMessageToWebSocketType;
import jms_messages.UserAuth.UserAuthReqMsg;
import jms_messages.UserAuth.UserAuthResMsg;
import jms_messages.UserAuth.UserAuthResMsgType;
import model.User;

@Path("/user-auth")
@Stateless
public class UserAuthRESTController {
	
	@EJB
	UserAuthMgmtLocal userAuthMgmt;
	
	@EJB
	UserServiceLocal userService;
	
	@Inject
	private JMSContext context;

	@Resource(mappedName = "java:/jms/queue/ouQueue")
	private Destination destination;
	
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserAuthResMsg register(UserAuthReqMsg msg) {
		User user = null;
		try {
			user = userAuthMgmt.register(msg.getUser());
			return new UserAuthResMsg(user, msg.getSessionId(), UserAuthResMsgType.REGISTERED);
		} catch (UserAuthException e) {
			return new UserAuthResMsg(user, msg.getSessionId(), e.getResponseType());
		}
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserAuthResMsg logIn(UserAuthReqMsg msg) {
		User user = null;
		try {
			user = userAuthMgmt.logIn(msg.getUser(), msg.getHost()); 
			if(user != null) {
				
				JMSMessageToWebSocket message = new JMSMessageToWebSocket();
				message.setType(JMSMessageToWebSocketType.NEW_ACITE_USER);
				ObjectMapper mapper = new ObjectMapper();
				String jsonUser;
				try {
					jsonUser = mapper.writeValueAsString(user);
					message.setContent(jsonUser);
					ObjectMessage objectMessage = context.createObjectMessage();
					objectMessage.setObject(message);
					JMSProducer producer = context.createProducer();
					producer.send(destination, objectMessage);
				} catch (JsonProcessingException | JMSException e) {
					e.printStackTrace();
				}
			}
			
			return new UserAuthResMsg(user, msg.getSessionId(), UserAuthResMsgType.LOGGED_IN);
			
		} catch (UserAuthException e) {
			return new UserAuthResMsg(user, msg.getSessionId(), e.getResponseType());
		}
	}
	
	@DELETE
	@Path("/logout/{username}")
	@Produces(MediaType.TEXT_PLAIN)
	public String logOut(@PathParam("username") String username) {
		boolean ret = userAuthMgmt.logOut(new User(username, null));
		if(ret)
			return "ok";
		else 
			return "error";
	}
	
}
