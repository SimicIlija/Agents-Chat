package beans;

import java.util.List;

import javax.ejb.Stateless;

import model.Chat;

@Stateless
public class GroupMgmt implements GroupMgmtLocal{

	@Override
	public Chat createNew(String chatName, String adminName, List<String> members) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Chat leaveChat(String chatName, String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addNewMember(String chatName, String userName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeMember(String chatName, String userName, String myName) {
		// TODO Auto-generated method stub
		
	}

}
