package jms_messages.GroupChat;

import java.io.Serializable;

import model.Chat;

public class GroupChatResMsg implements Serializable {
	
	private GroupChatResMsgType type;

	private String sessionId;
	
	public GroupChatResMsg() {
		super();
	}



	public GroupChatResMsg(GroupChatResMsgType type, String sessionId) {
		super();
		this.type = type;
		this.sessionId = sessionId;
	}

	public GroupChatResMsgType getType() {
		return type;
	}

	public void setType(GroupChatResMsgType type) {
		this.type = type;
	}



	public String getSessionId() {
		return sessionId;
	}



	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
}
