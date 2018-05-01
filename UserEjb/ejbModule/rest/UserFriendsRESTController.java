package rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import beans.UserFriendsMgmtLocal;
import model.User;

@Path("/user-friends")
@Stateless
public class UserFriendsRESTController {
	
	@EJB
	UserFriendsMgmtLocal userFriensMgmt;
	
	@GET
	
	public List<User> searchUsers(String search) {
		return userFriensMgmt.searchUsers(search);
	}

}
