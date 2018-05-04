package dataAccess.ChatMessage;

import java.util.List;

import javax.ejb.Local;

import org.bson.types.ObjectId;

import model.Chat;
import model.Message;

@Local
public interface ChatMessageServiceLocal {
	
	public Message findOneMessage(ObjectId id);
	public Chat findOneChat(ObjectId id);
	public Chat findOneChat(String usename1, String username2);
	public List<Chat> getLastChats(String username);
	public Message saveMessage(ObjectId chatId, Message message);
	public Chat creteChat(Chat chat);
	public Chat deleteChat(ObjectId id);
	public void editChat(Chat input);
	public List<Chat> getGroupsAdmin(String admin);
}
