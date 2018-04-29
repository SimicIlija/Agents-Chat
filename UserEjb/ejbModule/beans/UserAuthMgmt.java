package beans;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import com.mongodb.DBObject;
import dataAccess.MongoConnection;
import dataAccess.User.UserDao;
import exceptions.UserAuthException;
import jms_messages.UserAuthResMsgType;
import model.Host;
import model.User;

@Stateless
public class UserAuthMgmt implements UserAuthMgmtLocal {
	
	@EJB
	ActiveUsers activeUSers;
	
	@EJB
	MongoConnection conn;

	@Override
	public User register(User user) throws UserAuthException {
		Datastore ds = conn.getDatastore();
		Query<User> query = ds.createQuery(User.class);
		query.field("username").equal(user.getUsername());
		List<User> users = query.asList();
		if(!(users == null || users.isEmpty()))
			throw new UserAuthException(UserAuthResMsgType.USERNAME_EXISTS);
		DBObject tmp = conn.getMorphia().toDBObject(user);
		UserDao dao = new UserDao(ds);
		dao.getCollection().insert(tmp);
		return user;
	}

	@Override
	public User logIn(User user, Host host) throws UserAuthException {
		// TODO login logika
		user.setHost(host);
		
		if(activeUSers.addUser(user))
			return user;
		return null;
	}

	@Override
	public boolean logOut(String username) {
		return activeUSers.removeUserByUsername(username);

	}

}
