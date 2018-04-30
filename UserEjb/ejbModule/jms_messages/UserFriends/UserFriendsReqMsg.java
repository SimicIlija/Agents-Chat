package jms_messages.UserFriends;

import model.Host;

public class UserFriendsReqMsg {
	
private String sessionId;
	
	String request;

	private Host host;
	
	private UserFriendsReqMsgType type;

	public UserFriendsReqMsg() {}

	public UserFriendsReqMsg(String request, String sessionId, Host host, UserFriendsReqMsgType type) {
		this.request = request;
		this.sessionId = sessionId;
		this.host = host;
		this.type = type;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Host getHost() {
		return host;
	}

	public void setHost(Host host) {
		this.host = host;
	}

	public UserFriendsReqMsgType getType() {
		return type;
	}

	public void setType(UserFriendsReqMsgType type) {
		this.type = type;
	}

}
