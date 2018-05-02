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
		
	}
	
	@Lock(LockType.WRITE)
	public void addUserToActiveList(User user)
	{
		// TODO obavesti userApp da je dodat novi aktivni user
		activeUsers.add(user);
	}
	
	@Lock(LockType.WRITE)
	public void removeUserFroActiveList(User user) {
		
		// TODO obavesti userApp da je sklonjen ativni korisnik
		activeUsers.remove(user);
	}
	
	@Lock(LockType.READ)
	public List<User> getAllActiveUsers()
	{
		
		return activeUsers;
	}
	
}
