package jms_messages;

import java.io.Serializable;
import java.util.List;
import org.bson.types.ObjectId;


public class MessageReqMsg implements Serializable{

	private String sender;
	
	private Long timeStamp;
	
	private String content;
	
	//private ObjectId chat;
	
	private List<String> usernames;

	public MessageReqMsg() {}
	
	

	public MessageReqMsg(String sender, Long timeStamp, String content, List<String> usernames) {
		super();
		this.sender = sender;
		this.timeStamp = timeStamp;
		this.content = content;
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

//	public ObjectId getChat() {
//		return chat;
//	}
//
//	public void setChat(ObjectId chat) {
//		this.chat = chat;
//	}


	
	
}
