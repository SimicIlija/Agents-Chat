package beans;

import java.util.List;

import javax.ejb.Local;

import model.Chat;

@Local
public interface GroupMgmtLocal {
	public Chat createNew(String chatName, String adminName, List<String> members);
	public Chat leaveChat(String chatName, String userName);
	public void addNewMember(String chatName, String userName);
	public void removeMember(String chatName, String userName, String myName);
}
