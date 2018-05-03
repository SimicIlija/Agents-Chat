package jms_messages;

import java.util.List;

import model.Chat;

public class LastChatsResMsg {
	private String username;

	private List<Chat> chats;

	public LastChatsResMsg() {
	}

	public LastChatsResMsg(List<Chat> chats, String username) {
		super();
		this.chats = chats;
		this.username = username;
	}

	public List<Chat> getChats() {
		return chats;
	}

	public void setChats(List<Chat> chats) {
		this.chats = chats;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
