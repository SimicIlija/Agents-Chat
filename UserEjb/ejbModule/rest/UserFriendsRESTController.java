package rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import beans.UserFriendsMgmtLocal;
import jms_messages.UserFriends.UserFriendsReqMsg;
import jms_messages.UserFriends.UserFriendsResMsg;
import jms_messages.UserFriends.UserFriendsResMsgType;
import model.User;

@Path("/user-friends")
@Stateless
public class UserFriendsRESTController {
	
	@EJB
	UserFriendsMgmtLocal userFriensMgmt;
	
	@GET()
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserFriendsResMsg searchUsers(UserFriendsReqMsg msg) {
		List<User> ret = userFriensMgmt.searchUsers(msg.getSearch());
		return new UserFriendsResMsg(ret, UserFriendsResMsgType.SEARCH);
	}
	
	@POST
	@Path("/send-request")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserFriendsResMsg friendRequest(UserFriendsReqMsg msg) {
		User u = userFriensMgmt.friendRequest(msg.getUser(), msg.getAddRemove());
		List<User> ret = new ArrayList<>();
		ret.add(u);
		if(u == null)
			return new UserFriendsResMsg(null, UserFriendsResMsgType.ERROR);
		return new UserFriendsResMsg(ret, UserFriendsResMsgType.SENT_REQUEST);
	}
	
	@POST
	@Path("/decline-request")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserFriendsResMsg friendRequestDecl(UserFriendsReqMsg msg) {
		User u = userFriensMgmt.friendRequestDecl(msg.getUser(), msg.getAddRemove());
		List<User> ret = new ArrayList<>();
		ret.add(u);
		if(u == null)
			return new UserFriendsResMsg(null, UserFriendsResMsgType.ERROR);
		return new UserFriendsResMsg(ret, UserFriendsResMsgType.SENT_REQUEST);
	}
	
	@POST
	@Path("/add-friend")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserFriendsResMsg addFriend(UserFriendsReqMsg msg) {
		User u = userFriensMgmt.addFriend(msg.getUser(), msg.getAddRemove());
		List<User> ret = new ArrayList<>();
		ret.add(u);
		if(u == null)
			return new UserFriendsResMsg(null, UserFriendsResMsgType.ERROR);
		return new UserFriendsResMsg(ret, UserFriendsResMsgType.ADDED_FRIEND);
	}
	
	@POST
	@Path("/remove-friend")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserFriendsResMsg removeFriend(UserFriendsReqMsg msg) {
		User u = userFriensMgmt.removeFriend(msg.getUser(), msg.getAddRemove());
		List<User> ret = new ArrayList<>();
		ret.add(u);
		if(u == null)
			return new UserFriendsResMsg(null, UserFriendsResMsgType.ERROR);
		return new UserFriendsResMsg(ret, UserFriendsResMsgType.REMOVED_FRIEND);
	}
	
}
