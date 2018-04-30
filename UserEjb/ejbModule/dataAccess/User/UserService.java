package dataAccess.User;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import com.mongodb.DBObject;
import dataAccess.MongoConnection;
import exceptions.UserAuthException;
import jms_messages.UserAuth.UserAuthResMsgType;
import model.User;

@Stateless
public class UserService implements UserServiceLocal {

	@EJB
	MongoConnection conn;
	
	@Override
	public User findOne(ObjectId id) {
		Datastore ds = conn.getDatastore();
		List<User> users = ds.find(User.class, "id", id).asList();
		if(users == null || users.isEmpty() || users.size() > 1)
			return null;
		return users.get(0);
	}

	@Override
	public User findOne(String username) {
		Datastore ds = conn.getDatastore();
		List<User> users = ds.find(User.class, "username", username).asList();
		if(users == null || users.isEmpty())
			return null;
		return users.get(0);
	}

	@Override
	public void add(User input) throws UserAuthException{
		User user = findOne(input.getUsername());
		if(user == null) {
			Datastore ds = conn.getDatastore();
			DBObject tmp = conn.getMorphia().toDBObject(input);
			UserDao dao = new UserDao(ds);
			dao.getCollection().insert(tmp);
		} else {
			throw new UserAuthException(UserAuthResMsgType.USERNAME_EXISTS);
		}
	}

	@Override
	public void edit(User input) {
		// TODO remove
		Datastore ds = conn.getDatastore();
		DBObject tmp = conn.getMorphia().toDBObject(input);
		UserDao dao = new UserDao(ds);
		dao.getCollection().save(tmp);
	}

	@Override
	public List<User> search(String search) {
		
		return null;
	}
	
}
