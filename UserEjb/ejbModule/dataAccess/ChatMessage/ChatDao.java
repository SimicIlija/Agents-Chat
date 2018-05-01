package dataAccess.ChatMessage;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import model.Chat;

public class ChatDao extends BasicDAO<Chat, ObjectId> {

	protected ChatDao(Datastore ds) {
		super(ds);
	}

}
