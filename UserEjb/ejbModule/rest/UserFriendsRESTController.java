package rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.ws.rs.Path;

import model.User;

@Path("/user-friends")
@Stateless
public class UserFriendsRESTController {
	
	
	
	public List<User> searchUsers(String search) {
		return null;
	}

}
