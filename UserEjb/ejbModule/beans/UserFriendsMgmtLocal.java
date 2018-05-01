package beans;

import java.util.List;

import javax.ejb.Local;

import org.bson.types.ObjectId;

import model.User;

@Local
public interface UserFriendsMgmtLocal {
	
	public List<User> searchUsers(String search);
	public Boolean addFriend(ObjectId user, ObjectId add);
	public Boolean removeFriend(ObjectId user, ObjectId remove);
	
}
