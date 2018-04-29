package model;

import java.util.List;

public class Chat {

	private List<String> usernames;
	
	private String adnim;
	
	private Long timeStamp;
	
	private List<Message> messages;

	public Chat() {}
	
	public Chat(List<String> usernames, String adnim, Long timeStamp, List<Message> messages) {
		super();
		this.usernames = usernames;
		this.adnim = adnim;
		this.timeStamp = timeStamp;
		this.messages = messages;
	}

	public List<String> getUsernames() {
		return usernames;
	}

	public void setUsernames(List<String> usernames) {
		this.usernames = usernames;
	}

	public String getAdnim() {
		return adnim;
	}

	public void setAdnim(String adnim) {
		this.adnim = adnim;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	
	
	
}
