package beans;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.bson.types.ObjectId;

import dataAccess.User.UserServiceLocal;
import model.User;

@Stateless
public class UserFriendsMgmt implements UserFriendsMgmtLocal {
	
	@EJB
	private UserServiceLocal userService;
	
	@Override
	public List<User> searchUsers(String search) {
		return userService.search(search);
	}

	@Override
	public Boolean addFriend(ObjectId user, ObjectId add) {
		User u = userService.findOne(user);
		if(u == null)
			return false;
		User a = userService.findOne(add);
		if(a == null)
			return false;
		u.getFriends().add(a);
		userService.edit(u);
		return true;
	}

	@Override
	public Boolean removeFriend(ObjectId user, ObjectId remove) {
		User u = userService.findOne(user);
		if(u == null)
			return false;
		User r = userService.findOne(remove);
		if(r == null)
			return false;
		u.getFriends().removeIf(p -> p.getId().equals(remove));
		userService.edit(u);
		return true;
	}

}
