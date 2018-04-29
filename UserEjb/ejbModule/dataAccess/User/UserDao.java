package dataAccess.User;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import model.User;

public class UserDao extends BasicDAO<User, ObjectId> {

	public UserDao(Datastore ds) {
		super(ds);
	}
	
}
