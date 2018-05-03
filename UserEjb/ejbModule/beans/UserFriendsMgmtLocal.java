package beans;

import java.util.List;

import javax.ejb.Local;

import org.bson.types.ObjectId;

import model.User;

@Local
public interface UserFriendsMgmtLocal {
	
	public List<User> searchUsers(String search);
	public User friendRequest(ObjectId user, ObjectId add);
	public User friendRequestDecl(ObjectId user, ObjectId remove);
	public User addFriend(ObjectId user, ObjectId add);
	public User removeFriend(ObjectId user, ObjectId remove);
	
}
