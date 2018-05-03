package dataAccess.ChatMessage;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import com.mongodb.DBObject;

import dataAccess.MongoConnection;
import model.Chat;
import model.Message;

@Stateless
public class ChatMessageService implements ChatMessageServiceLocal {

	@EJB
	MongoConnection conn;
	
	@Override
	public Message findOneMessage(ObjectId id) {
		Datastore ds = conn.getDatastore();
		List<Message> msg = ds.find(Message.class, "id", id).asList();
		if(msg == null || msg.isEmpty() || msg.size() > 1)
			return null;
		return msg.get(0);
	}

	@Override
	public Chat findOneChat(ObjectId id) {
		Datastore ds = conn.getDatastore();
		List<Chat> chat = ds.find(Chat.class, "id", id).asList();
		if(chat == null || chat.isEmpty() || chat.size() > 1)
			return null;
		return chat.get(0);
	}
	
	@Override
	public Chat findOneChat(String usename1, String username2) {
		Datastore ds = conn.getDatastore();
		Query<Chat> query = ds.createQuery(Chat.class);
		query.and(
				query.criteria("adnim").equal(null),
				query.criteria("usernames").contains(usename1),
				query.criteria("usernames").contains(username2)
		);
		List<Chat> chat = query.asList();
		if(chat == null || chat.isEmpty() || chat.size() > 1)
			return null;
		return chat.get(0);
	}
	
	@Override
	public List<Chat> getLastChats(String username) {
		Datastore ds = conn.getDatastore();
		List<Chat> chats = ds.createQuery(Chat.class).field("usernames").contains(username).asList();
		return chats;
	}

	@Override
	public Message saveMessage(ObjectId chatId, Message message) {
		Chat chat = findOneChat(chatId);
		if(chat == null)
			return null;
		if(!chat.getUsernames().contains(message.getSender()))
			return null;
		
		message.setId(new ObjectId());
		Datastore ds = conn.getDatastore();
		DBObject tmp = conn.getMorphia().toDBObject(message);
		MessageDao messageDao = new MessageDao(ds);
		messageDao.getCollection().insert(tmp);
		chat.getMessages().add(message);
		tmp = conn.getMorphia().toDBObject(chat);
		ChatDao chatDao = new ChatDao(ds);
		chatDao.getCollection().save(tmp);
		return message;
	}

	@Override
	public Chat creteChat(Chat chat) {
		chat.setId(new ObjectId());
		Datastore ds = conn.getDatastore();
		DBObject tmp = conn.getMorphia().toDBObject(chat);
		ChatDao dao = new ChatDao(ds);
		dao.getCollection().insert(tmp);
		return chat;
	}

	@Override
	public Chat deleteChat(ObjectId id) {
		Chat chat = findOneChat(id);
		if(chat == null)
			return null;
		Datastore ds = conn.getDatastore();
		DBObject tmp = conn.getMorphia().toDBObject(chat);
		ChatDao dao = new ChatDao(ds);
		dao.getCollection().remove(tmp);
		return chat;
	}

}
