package beans;

import java.util.List;

import javax.ejb.Local;

import model.Chat;

@Local
public interface GroupMgmtLocal {
	public Chat createNew(String chatName, String adminName, List<String> members);
	public Chat leaveChat(String chatId, String userName);
	public Chat addNewMember(String chatId, String userName);
	public Chat removeMember(String chatId, String userName, String myName);
}
