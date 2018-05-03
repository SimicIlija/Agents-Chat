package ejb_beans;

import javax.ejb.Local;

import jms_messages.MessageReqMsg;
import jms_messages.UserAuthReqMsg;
import jms_messages.UserFriendsReqMsg;
import jms_messages.UserFriendsResMsg;

@Local
public interface UserAppCommunicationLocal {

	public void sendAuthAttempt(UserAuthReqMsg userAuthMsg);

	public void sendAuthAttempt_JMS(UserAuthReqMsg userAuthMsg);

	public void sendAuthAttempt_REST(UserAuthReqMsg userAuthMsg);

	public void getLastChats(String username);

	public void getLastChats_JMS(String username);

	public void getLastChats_REST(String username);

	public void logoutAttempt(String username);

	public void logoutAttempt_JMS(String username);

	public void logoutAttempt_REST(String username);

	public void sendMessage(MessageReqMsg messageReqMsg);

	public void sendMessageToUserApp_JMS(MessageReqMsg messageReqMsg);

	public void sendMessageToUserApp_REST(MessageReqMsg messageReqMsg);

	public void sendUserFriendsReqMsg(UserFriendsReqMsg msg);

	public void sendUserFriendsReqMsg_JMS(UserFriendsReqMsg msg);

	public void sendUserFriendsReqMsg_REST(UserFriendsReqMsg msg);

	public void getMyGroups(String username);

	public void getMyGroups_JMS(String username);

	public void getMyGroups_REST(String username);

}
