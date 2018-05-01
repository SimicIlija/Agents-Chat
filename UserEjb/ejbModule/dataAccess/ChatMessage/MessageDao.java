package dataAccess.ChatMessage;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import model.Message;

public class MessageDao extends BasicDAO<Message, ObjectId> {

	public MessageDao(Datastore ds) {
		super(ds);
	}
	
}
