package jms_messages;

import java.io.Serializable;
import java.util.List;

import model.Host;

public class GroupChatReqMsg implements Serializable {
	
	private String chat;
	
	private String admin;
	
	private String memebers;
	
	private String sessionId;

	private GroupChatReqMsgType type;
	
	public GroupChatReqMsg() {}

	public GroupChatReqMsg(String chat, String admin, String memebers, GroupChatReqMsgType type) {
		super();
		this.chat = chat;
		this.admin = admin;
		this.memebers = memebers;
		this.type = type;
	}

	public String getChat() {
		return chat;
	}

	public void setChat(String chat) {
		this.chat = chat;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getMemebers() {
		return memebers;
	}

	public void setMemebers(String memebers) {
		this.memebers = memebers;
	}

	public GroupChatReqMsgType getType() {
		return type;
	}

	public void setType(GroupChatReqMsgType type) {
		this.type = type;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
