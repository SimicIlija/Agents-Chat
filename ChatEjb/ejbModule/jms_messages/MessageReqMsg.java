package jms_messages;

import java.util.List;

public class MessageReqMsg {

	private String sender;
	
	private Long timeStamp;
	
	private String content;
	
	private Long chat;
	
	private List<String> usernames;

	public MessageReqMsg() {}
	
	public MessageReqMsg(String sender, Long timeStamp, String content, Long chat, List<String> usernames) {
		super();
		this.sender = sender;
		this.timeStamp = timeStamp;
		this.content = content;
		this.chat = chat;
		this.usernames = usernames;
	}

	public List<String> getUsernames() {
		return usernames;
	}

	public void setUsernames(List<String> usernames) {
		this.usernames = usernames;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getChat() {
		return chat;
	}

	public void setChat(Long chat) {
		this.chat = chat;
	}
	
	
}
