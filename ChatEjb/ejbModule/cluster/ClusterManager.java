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

import model.Host;

@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)

@Startup
@Singleton
public class ClusterManager implements ClusterManagerLocal{

	private List<Host> activeHosts;
	@PostConstruct
	private void init () {
		activeHosts = new ArrayList<>();
	   
	}
	
	@Lock(LockType.WRITE)
	public void addHostToActiveList(Host host)
	{
		activeHosts.add(host);
	}
	
	@Lock(LockType.WRITE)
	public void removeHostFromActiveList(Host host)
	{
		activeHosts.remove(host);
	}
}
