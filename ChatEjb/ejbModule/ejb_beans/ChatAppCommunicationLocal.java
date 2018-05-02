package ejb_beans;

import javax.ejb.Local;

import jms_messages.MessageReqMsg;

@Local
public interface ChatAppCommunicationLocal {

	public void sendMessageToOtherUsers(String msg,String username);
}
