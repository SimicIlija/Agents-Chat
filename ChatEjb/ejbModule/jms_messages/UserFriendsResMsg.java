package jms_messages;

import java.io.Serializable;
import java.util.List;

import model.Host;
import model.User;

public class UserFriendsResMsg implements Serializable {
	
	private List<User> user;
	
	private Host host;
	
	private String sessionId;
	
	private UserFriendsResMsgType type;

	public UserFriendsResMsg() {}

	public UserFriendsResMsg(List<User> user, Host host, String sessionId, UserFriendsResMsgType type) {
		this.user = user;
		this.type = type;
		this.host = host;
		this.sessionId = sessionId;
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

	public Host getHost() {
		return host;
	}

	public void setHost(Host host) {
		this.host = host;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
}
