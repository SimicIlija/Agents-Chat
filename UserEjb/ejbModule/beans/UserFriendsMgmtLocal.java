package beans;

import java.util.List;

import javax.ejb.Local;

import org.bson.types.ObjectId;

import model.User;

@Local
public interface UserFriendsMgmtLocal {
	
	public List<User> searchUsers(String search);
	public void addFriend(ObjectId user, ObjectId add);
	public void removeFriend(ObjectId user, ObjectId remove);
	
}
