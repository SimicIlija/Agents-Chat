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
	public List<Chat> getLastChats(String username);
	public Message saveMessage(ObjectId chatId, Message message);
	public Chat creteChat(Chat chat);

}
