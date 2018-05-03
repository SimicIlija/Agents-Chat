package dataAccess.User;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import com.mongodb.DBObject;
import dataAccess.MongoConnection;
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
	public User add(User input) {
		User user = findOne(input.getUsername());
		if(user == null) {
			input.setId(new ObjectId());
			Datastore ds = conn.getDatastore();
			DBObject tmp = conn.getMorphia().toDBObject(input);
			UserDao dao = new UserDao(ds);
			dao.getCollection().insert(tmp);
			return input;
		} else {
			return null;
		}
	}

	@Override
	public void edit(User input) {
		Datastore ds = conn.getDatastore();
		DBObject tmp = conn.getMorphia().toDBObject(input);
		UserDao dao = new UserDao(ds);
		dao.getCollection().save(tmp);
	}

	@Override
	public List<User> search(String search) {
		if(search == null || search.trim().isEmpty())
			return null;
		search = search.trim();
		String[] splitTmp = search.split(" ");
		List<String> split = new ArrayList<>();
		for (String s : splitTmp) {
			if(!s.isEmpty())
				split.add(s);
		}
		if(split.isEmpty())
			return null;
		Datastore ds = conn.getDatastore();
		Query<User> query = ds.createQuery(User.class);
		for (String s : split) {
			query.or(
				query.criteria("username").containsIgnoreCase(s),
				query.criteria("firstName").containsIgnoreCase(s),
				query.criteria("lastName").containsIgnoreCase(s)
			);
		}
		
		return query.asList();
	}
	
}
