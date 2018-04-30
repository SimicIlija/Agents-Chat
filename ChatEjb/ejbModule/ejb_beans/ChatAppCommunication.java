package ejb_beans;

import javax.ejb.Stateless;

import jms_messages.MessageReqMsg;

@Stateless
public class ChatAppCommunication implements ChatAppCommunicationLocal{

	@Override
	public void sendMessageToOtherUsers(MessageReqMsg messageReqMsg) {
		// TODO Auto-generated method stub
		
	}

}
