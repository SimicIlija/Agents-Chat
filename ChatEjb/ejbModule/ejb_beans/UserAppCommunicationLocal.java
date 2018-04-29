package ejb_beans;

import javax.ejb.Local;
import jms_messages.UserAuthReqMsg;
import jms_messages.UserAuthResMsg;

@Local
public interface UserAppCommunicationLocal {

	public UserAuthResMsg sendAuthAttempt(UserAuthReqMsg userAuthMsg);
	
	public UserAuthResMsg sendAuthAttempt_JMS(UserAuthReqMsg userAuthMsg);
	
	public UserAuthResMsg sendAuthAttempt_REST(UserAuthReqMsg userAuthMsg);
}
