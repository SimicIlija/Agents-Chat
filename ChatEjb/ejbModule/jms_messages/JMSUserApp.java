package jms_messages;

import java.io.Serializable;

public class JMSUserApp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7817461259961207195L;
	private JMSUserAppType type;
	private String content;

	public JMSUserAppType getType() {
		return type;
	}

	public void setType(JMSUserAppType type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public JMSUserApp() {
		super();
	}

	public JMSUserApp(JMSUserAppType type, String content) {
		super();
		this.type = type;
		this.content = content;
	}

}
