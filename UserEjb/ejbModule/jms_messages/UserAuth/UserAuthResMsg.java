package jms_messages.UserAuth;

import model.User;

public class UserAuthResMsg {
	
	private User user;
	
	private String sessionId;
	
	private UserAuthResMsgType type;

	public UserAuthResMsg() {}

	public UserAuthResMsg(User user, String sessionId, UserAuthResMsgType type) {
		this.user = user;
		this.sessionId = sessionId;
		this.type = type;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public UserAuthResMsgType getType() {
		return type;
	}

	public void setType(UserAuthResMsgType type) {
		this.type = type;
	}
	
}
