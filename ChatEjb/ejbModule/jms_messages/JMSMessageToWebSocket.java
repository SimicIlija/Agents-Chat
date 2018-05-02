package jms_messages;

import java.io.Serializable;

public class JMSMessageToWebSocket implements Serializable{

	private JMSMessageToWebSocketType type;
	
	private Object content;

	public JMSMessageToWebSocket() {}
	
	public JMSMessageToWebSocket(JMSMessageToWebSocketType type, Object content) {
		super();
		this.type = type;
		this.content = content;
	}

	public JMSMessageToWebSocketType getType() {
		return type;
	}

	public void setType(JMSMessageToWebSocketType type) {
		this.type = type;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
	
	
}
