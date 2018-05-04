package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.bson.types.ObjectId;

import dataAccess.ChatMessage.ChatMessageServiceLocal;
import dataAccess.User.UserServiceLocal;
import model.Chat;
import model.User;

@Stateless
public class GroupMgmt implements GroupMgmtLocal{

	@EJB
	private ChatMessageServiceLocal chatMessageService;
	
	@EJB
	private UserServiceLocal userService;
	
	@Override
	public Chat createNew(String chatName, String adminName, String members) {
		if(members == null || chatName == null || chatName.trim().isEmpty() || adminName == null || adminName.trim().isEmpty())
			return null;
		
		User admin = userService.findOne(adminName);
		if(admin == null)
			return null;
		String[] split = members.split(",");
		List<String> toAdd = new ArrayList<>();
		for (String string : split) {
			admin = userService.findOne(string.trim());
			if(admin != null)
				toAdd.add(string.trim());
		}
		toAdd.add(adminName);
		Chat chat = new Chat(toAdd, adminName, new Date().getTime());
		return chatMessageService.creteChat(chat);
	}

	@Override
	public Chat leaveChat(String chatId, String userName) {
		Chat chat = chatMessageService.findOneChat(new ObjectId(chatId));
		if(chat == null)
			return null;
		if(chat.getAdnim().equals(userName)) {
			chatMessageService.deleteChat(chat.getId());
			return chat;
		}
		chat.getUsernames().removeIf(p -> p.equals(userName));
		chatMessageService.editChat(chat);
		return chat;
	}

	@Override
	public Chat addNewMember(String chatId, String admin, String userName) {
		Chat chat = chatMessageService.findOneChat(new ObjectId(chatId));
		if(chat == null)
			return null;
		chat.getUsernames().add(userName);
		chatMessageService.editChat(chat);
		return chat;
	}

	@Override
	public Chat removeMember(String chatId, String userName, String myName) {
		Chat chat = chatMessageService.findOneChat(new ObjectId(chatId));
		if(chat == null)
			return null;
		if(!chat.getAdnim().equals(myName))
			return null;
		chat.getUsernames().removeIf(p -> p.equals(userName));
		chatMessageService.editChat(chat);
		return chat;
	}

}
