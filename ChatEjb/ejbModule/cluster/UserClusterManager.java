package cluster;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.fasterxml.jackson.databind.ObjectMapper;

import config.PropertiesSupplierLocal;
import model.User;
import model.Users;

@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Startup
@Singleton
public class UserClusterManager implements UserClusterManagerLocal{

	private List<User> activeUsers;
	
	@EJB
	private PropertiesSupplierLocal prop;
	
	@PostConstruct
	private void init () {
		activeUsers = new ArrayList<>();
		
		String isMaster = prop.getProperty("IS_MASTER");
		if( isMaster.equals("true"))
			return ;
		
		try {
			ResteasyClient client = new ResteasyClientBuilder().build();
			String targetString1 = "http://"+prop.getProperty("MASTER_LOCATION")+":"+prop.getProperty("MASTER_PORT")+"/ChatWeb/rest/User/getAllActiveUsers";
			ResteasyWebTarget target1 = client.target(targetString1);
			Response response1 = target1.request().get();
			String jsonUsers = response1.readEntity(String.class);
			ObjectMapper objectMapper = new ObjectMapper();
			Users users = objectMapper.readValue(jsonUsers, Users.class);
			activeUsers = users.getListOfUsers();
			
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
