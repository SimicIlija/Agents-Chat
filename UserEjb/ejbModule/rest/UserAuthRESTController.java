package rest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import beans.UserAuthMgmtLocal;
import dataAccess.User.UserServiceLocal;
import exceptions.UserAuthException;
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
