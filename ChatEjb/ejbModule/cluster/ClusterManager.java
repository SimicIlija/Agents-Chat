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
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import config.PropertiesSupplierLocal;
import jms_messages.UserAuthResMsg;
import model.Host;

@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)

@Startup
@Singleton
public class ClusterManager implements ClusterManagerLocal{

	@EJB
	private PropertiesSupplierLocal prop;
	
	private List<Host> activeHosts;
	
	@PostConstruct
	private void init () {
		activeHosts = new ArrayList<>();
		
		String isMaster = prop.getProperty("IS_MASTER");
		// TODO PROMENITI OVO NA FALSE POSLE TESTIRANJA
		if( isMaster.equals("true"))// pre isMaster... NE treba da ide !
			return ;
		// Napravim objekat Host koji predtavlja ovaj node koji salje masteru da se registruje
		Host host = new Host(
				prop.getProperty("LOCATION"),
				prop.getProperty("NAME_OF_NODE"),
				Integer.parseInt(prop.getProperty("PORT")) );
		// Sad trazim on mastera da me registruje mendju postojece nodove i ubaci u evidenciju
		ResteasyClient client = new ResteasyClientBuilder().build();
		String targetString = "http://"+prop.getProperty("MASTER_LOCATION")+":"+prop.getProperty("MASTER_PORT")+"/ChatWeb/rest/cluster/addHost";
		ResteasyWebTarget target = client.target(targetString);
		Response response = target.request().post(Entity.entity(host, MediaType.APPLICATION_JSON));
		
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
