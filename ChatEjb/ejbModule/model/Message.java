package model;

public class Message extends BaseDO {

	private String sender;
	
	private Long timeStamp;
	
	private String content;

	public Message() {}
	
	public Message(String sender, Long timeStamp, String content) {
		super();
		this.sender = sender;
		this.timeStamp = timeStamp;
		this.content = content;
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
	
	
}
