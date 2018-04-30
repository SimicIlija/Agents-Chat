package jms_messages.UserAuth;

import model.Host;
import model.User;

public class UserAuthReqMsg {

	private User user;
	
	private String sessionId;
	
	private Host host;
	
	private UserAuthReqMsgType type;

	public UserAuthReqMsg() {}

	public UserAuthReqMsg(User user, String sessionId, Host host, UserAuthReqMsgType type) {
		this.user = user;
		this.sessionId = sessionId;
		this.host = host;
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

	public Host getHost() {
		return host;
	}

	public void setHost(Host host) {
		this.host = host;
	}

	public UserAuthReqMsgType getType() {
		return type;
	}

	public void setType(UserAuthReqMsgType type) {
		this.type = type;
	}
	
}
