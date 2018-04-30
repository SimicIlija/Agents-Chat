package exceptions;

import jms_messages.UserAuth.UserAuthResMsgType;

public class UserAuthException extends Exception{
	private static final long serialVersionUID = 1L;
	
	private UserAuthResMsgType responseType;

	public UserAuthException(UserAuthResMsgType responseType) {
		super(responseType.toString());
		this.responseType = responseType;
	}

	public UserAuthResMsgType getResponseType() {
		return responseType;
	}

	public void setResponseType(UserAuthResMsgType responseType) {
		this.responseType = responseType;
	}
	
}
