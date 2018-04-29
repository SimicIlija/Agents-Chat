package jms_messages;

import java.util.List;

import model.Chat;

public class LastChatsResMsg {

	private List<Chat> chats;

	public LastChatsResMsg() {}
	
	public LastChatsResMsg(List<Chat> chats) {
		super();
		this.chats = chats;
	}

	public List<Chat> getChats() {
		return chats;
	}

	public void setChats(List<Chat> chats) {
		this.chats = chats;
	}
	
	
}
