package beans;

import javax.ejb.Local;

import jms_messages.LastChatsResMsg;
import jms_messages.MessageReqMsg;

@Local
public interface DB_AccessLocal {
	
	public LastChatsResMsg getLastChats(String username);

	public void saveMessage(MessageReqMsg messageReqMsg);
}
