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
	public User addFriend(ObjectId user, String add) {
		User u = userService.findOne(user);
		if(u == null)
			return null;
		User a = userService.findOne(add);
		if(a == null)
			return null;
		if(u.getFriendReq().removeIf(p -> p.equals(a.getUsername()))) {
			u.getFriends().add(a.getUsername());
			a.getFriends().add(u.getUsername());
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
	public User removeFriend(ObjectId user, String remove) {
		User u = userService.findOne(user);
		if(u == null)
			return null;
		User r = userService.findOne(remove);
		if(r == null)
			return null;
		u.getFriends().removeIf(p -> p.equals(r.getUsername()));
		r.getFriends().removeIf(p -> p.equals(u.getUsername()));
		userService.edit(u);
		userService.edit(r);
		Chat c = chatMessageService.findOneChat(u.getUsername(), r.getUsername());
		if(c != null)
			chatMessageService.deleteChat(c.getId());
		return u;
	}

	@Override
	public User friendRequest(String user, ObjectId add) {
		User u = userService.findOne(user);
		if(u == null)
			return null;
		User a = userService.findOne(add);
		if(a == null)
			return null;
		if(user.equals(a.getUsername()))
			return null;
		
		for (String uIt : u.getFriends()) {
			if(uIt.equals(a.getUsername()))
			return u;
		}
		for (String uIt : u.getFriendReq()) {
			if(uIt.equals(a.getUsername()))
				return u;
		}
		u.getFriendReq().add(a.getUsername());
		userService.edit(u);
		return u;
	}

	@Override
	public User friendRequestDecl(ObjectId user, String remove) {
		User u = userService.findOne(user);
		if(u == null)
			return null;
		User r = userService.findOne(remove);
		if(r == null)
			return null;
		u.getFriendReq().removeIf(p -> p.equals(remove));
		userService.edit(u);
		return u;
	}

}
