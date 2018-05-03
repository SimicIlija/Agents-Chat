package cluster;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import model.User;

@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Startup
@Singleton
public class UserClusterManager implements UserClusterManagerLocal{

	private List<User> activeUsers;
	
	@PostConstruct
	private void init () {
		activeUsers = new ArrayList<>();
		
		//TODO pitaj userApp da ti da sve aktivne usere
		try {
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Lock(LockType.WRITE)
	public boolean addUserToActiveList(User user)
	{
		// Zastita da ne unese 2 puta istog
		for(User u : activeUsers) {
			if(u.getUsername().equals(user.getUsername()))
				return false;
		}
		return activeUsers.add(user);
	}
	
	@Lock(LockType.WRITE)
	public boolean removeUserFroActiveList(User user) {
		
		return activeUsers.remove(user);
	}
	
	@Lock(LockType.READ)
	public List<User> getAllActiveUsers()
	{
		
		return activeUsers;
	}
	
}
