package beans;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.bson.types.ObjectId;

import dataAccess.ChatMessage.ChatMessageServiceLocal;
import dataAccess.User.UserServiceLocal;
import model.Chat;
import model.User;

@Stateless
public class UserFriendsMgmt implements UserFriendsMgmtLocal {
	
	@EJB
	private UserServiceLocal userService;
	
	@EJB
	private ChatMessageServiceLocal chatMessageService;
	
	@Override
	public List<User> searchUsers(String search) {
		return userService.search(search);
	}

	@Override
	public User addFriend(ObjectId user, ObjectId add) {
		User u = userService.findOne(user);
		if(u == null)
			return null;
		User a = userService.findOne(add);
		if(a == null)
			return null;
		if(u.getFriendReq().removeIf(p -> p.getId().equals(add))) {
			u.getFriends().add(a);
			a.getFriends().add(u);
			userService.edit(u);
			userService.edit(a);
			List<String> list = new ArrayList<>();
			list.add(u.getUsername());
			list.add(a.getUsername());
			Chat c = new Chat(list, null, null);
			chatMessageService.creteChat(c);
		}
		return u;
	}

	@Override
	public User removeFriend(ObjectId user, ObjectId remove) {
		User u = userService.findOne(user);
		if(u == null)
			return null;
		User r = userService.findOne(remove);
		if(r == null)
			return null;
		u.getFriends().removeIf(p -> p.getId().equals(remove));
		r.getFriends().removeIf(p -> p.getId().equals(user));
		userService.edit(u);
		userService.edit(r);
		Chat c = chatMessageService.findOneChat(u.getUsername(), r.getUsername());
		if(c != null)
			chatMessageService.deleteChat(c.getId());
		return u;
	}

	@Override
	public User friendRequest(ObjectId user, ObjectId add) {
		User u = userService.findOne(user);
		if(u == null)
			return null;
		User a = userService.findOne(add);
		if(a == null)
			return null;
		u.getFriendReq().add(a);
		userService.edit(u);
		return u;
	}

	@Override
	public User friendRequestDecl(ObjectId user, ObjectId remove) {
		User u = userService.findOne(user);
		if(u == null)
			return null;
		User r = userService.findOne(remove);
		if(r == null)
			return null;
		u.getFriendReq().removeIf(p -> p.getId().equals(remove));
		userService.edit(u);
		return u;
	}

}
