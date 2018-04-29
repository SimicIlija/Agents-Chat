package rest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.UserAuthMgmtLocal;
import exceptions.UserAuthException;
import jms_messages.UserAuthReqMsg;
import jms_messages.UserAuthResMsg;
import jms_messages.UserAuthResMsgType;
import model.User;

@Path("/user-auth")
@Stateless
public class UserAuthRESTController {
	
	@EJB
	UserAuthMgmtLocal userAuthMgmt;
	
	@GET
	@Path("/test")
	@Produces(MediaType.TEXT_PLAIN)
	public String test(@Context HttpServletRequest request) {
		
		return "OK";
	}
	
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserAuthResMsg register(UserAuthReqMsg msg) {
		User user = null;
		try {
			user = userAuthMgmt.register(user);
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
			return new UserAuthResMsg(user, msg.getSessionId(), UserAuthResMsgType.LOGGED_IN);
		} catch (UserAuthException e) {
			return new UserAuthResMsg(user, msg.getSessionId(), e.getResponseType());
		}
	}
	
	@POST
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserAuthResMsg logOut(UserAuthReqMsg msg) {
		User user = userAuthMgmt.logOut(msg.getUser());
		return new UserAuthResMsg(user, msg.getSessionId(), UserAuthResMsgType.LOGGED_OUT);
	}
	
}
