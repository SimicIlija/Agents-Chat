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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addFriend(ObjectId user, ObjectId add) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeFriend(ObjectId user, ObjectId remove) {
		// TODO Auto-generated method stub

	}

}
