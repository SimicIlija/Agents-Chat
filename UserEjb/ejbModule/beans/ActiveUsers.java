package beans;

import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import model.User;

@Singleton
@Startup
public class ActiveUsers {
	
	private ConcurrentHashMap<String, User> activeUsers;
	
	@PostConstruct
	public void initialize() {
		activeUsers = new ConcurrentHashMap<>();
	}
	
	public Boolean addUser(User user) {
		if(!activeUsers.containsKey(user.getUsername())) {
			activeUsers.put(user.getUsername(), user);
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean removeUser(User user) {
		if(activeUsers.containsKey(user.getUsername())) {
			activeUsers.remove(user.getUsername());
			return true;
		} else {
			return false;
		}
	}
	
}
