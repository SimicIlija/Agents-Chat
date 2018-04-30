package jms_messages;

public class WebSocketMessage {

	private String content;
	
	private WebSocketMessageType type;
	
	public WebSocketMessage() {}

	public WebSocketMessage(String content, WebSocketMessageType type) {
		super();
		this.content = content;
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public WebSocketMessageType getType() {
		return type;
	}

	public void setType(WebSocketMessageType type) {
		this.type = type;
	}
	
	
}
