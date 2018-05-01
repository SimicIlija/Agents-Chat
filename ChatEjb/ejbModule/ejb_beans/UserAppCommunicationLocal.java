package ejb_beans;

import javax.ejb.Local;

import jms_messages.LastChatsResMsg;
import jms_messages.MessageReqMsg;
import jms_messages.UserAuthReqMsg;
import jms_messages.UserAuthResMsg;

@Local
public interface UserAppCommunicationLocal {

	public UserAuthResMsg sendAuthAttempt(UserAuthReqMsg userAuthMsg);
	
	public UserAuthResMsg sendAuthAttempt_JMS(UserAuthReqMsg userAuthMsg);
	
	public UserAuthResMsg sendAuthAttempt_REST(UserAuthReqMsg userAuthMsg);

	public LastChatsResMsg getLastChats(String username);
	
	public LastChatsResMsg getLastChats_JMS(String username);
	
	public LastChatsResMsg getLastChats_REST(String username);

	public void logoutAttempt(String username);
	
	public void logoutAttempt_JMS(String username);
	
	public void logoutAttempt_REST(String username);

	public void sendMessage(MessageReqMsg messageReqMsg);
	
	public void sendMessageToUserApp_JMS(MessageReqMsg messageReqMsg);

	public void sendMessageToUserApp_REST(MessageReqMsg messageReqMsg);
	
	

}
