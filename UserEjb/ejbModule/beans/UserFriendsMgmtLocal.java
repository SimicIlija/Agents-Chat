package beans;

import java.util.List;

import javax.ejb.Local;

import org.bson.types.ObjectId;

import model.User;

@Local
public interface UserFriendsMgmtLocal {
	
	public List<User> searchUsers(String search);
	public User friendRequest(String user, ObjectId add);
	public User friendRequestDecl(ObjectId user, String remove);
	public User addFriend(ObjectId user, String add);
	public User removeFriend(ObjectId user, String remove);
	
}
