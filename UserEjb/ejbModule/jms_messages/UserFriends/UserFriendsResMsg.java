package jms_messages.UserFriends;

import java.util.List;

import model.User;

public class UserFriendsResMsg {
	
	private List<User> user;
	
	private UserFriendsResMsgType type;

	public UserFriendsResMsg() {}

	public UserFriendsResMsg(List<User> user, UserFriendsResMsgType type) {
		this.user = user;
		this.type = type;
	}

	public List<User> getUser() {
		return user;
	}

	public void setUser(List<User> user) {
		this.user = user;
	}

	public UserFriendsResMsgType getType() {
		return type;
	}

	public void setType(UserFriendsResMsgType type) {
		this.type = type;
	}
	
}
