package beans;

import javax.ejb.Local;

import jms_messages.LastChatsResMsg;

@Local
public interface DB_AccessLocal {
	
	public LastChatsResMsg getLastChats(String username);
}
