package jms_messages.GroupChatMsg;

import java.util.List;

public class GroupChatMsgReq {
	
	private String chat;
	
	private String admin;
	
	private String memebers;

	private GroupChatMsgReqType type;
	
	public GroupChatMsgReq() {}

	public GroupChatMsgReq(String chat, String admin, String memebers, GroupChatMsgReqType type) {
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

	public GroupChatMsgReqType getType() {
		return type;
	}

	public void setType(GroupChatMsgReqType type) {
		this.type = type;
	}
	
}
