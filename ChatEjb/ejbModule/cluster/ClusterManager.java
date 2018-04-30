package cluster;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;

@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)

@Startup
@Singleton
public class ClusterManager {

	@PostConstruct
	private void init () {
	   
	}
	
	@Lock(LockType.WRITE)
	public void addUserToActiveList()
	{
		
	}
}
