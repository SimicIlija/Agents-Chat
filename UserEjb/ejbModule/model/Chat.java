package model;

import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.annotations.Reference;

public class Chat extends BaseDO {
	
	private List<String> usernames;
	
	private String adnim;
	
	private Long timeStamp;
	
	@Reference
	private List<Message> messages;

	public Chat() {}
	
	public Chat(List<String> usernames, String adnim, Long timeStamp) {
		super();
		this.usernames = usernames;
		this.adnim = adnim;
		this.timeStamp = timeStamp;
		this.messages = new ArrayList<>();
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
