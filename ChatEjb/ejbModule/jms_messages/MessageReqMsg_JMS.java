package jms_messages;

import java.io.Serializable;
import java.util.List;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class MessageReqMsg_JMS implements Serializable{
	
private String sender;
	
	private Long timeStamp;
	
	private String content;
	
	private String chatId;
	
	private List<String> usernames;

	public MessageReqMsg_JMS() {}
	

	public MessageReqMsg_JMS(MessageReqMsg msg) {
		super();
		this.sender = msg.getSender();
		this.timeStamp = msg.getTimeStamp();
		this.content = msg.getContent();
		this.usernames = msg.getUsernames();
		this.chatId = msg.getChat().toString();
		
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


	public String getChatId() {
		return chatId;
	}


	public void setChatId(String chatId) {
		this.chatId = chatId;
	}

}
