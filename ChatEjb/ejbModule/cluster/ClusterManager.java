package cluster;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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
import jms_messages.LastChatsResMsg;
import jms_messages.UserAuthResMsg;
import model.Host;
import model.Hosts;

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
		try {
			
		
		// Sad trazim on mastera da me registruje mendju postojece nodove i ubaci u evidenciju
		ResteasyClient client = new ResteasyClientBuilder().build();
		String targetString = "http://"+prop.getProperty("MASTER_LOCATION")+":"+prop.getProperty("MASTER_PORT")+"/ChatWeb/rest/cluster/addHost";
		ResteasyWebTarget target = client.target(targetString);
		Response response = target.request().post(Entity.entity(host, MediaType.APPLICATION_JSON));
		
		// I posto sam prosao IF i ja nisam master onda cu traziti i od mastera da mi posalje listu svih hostova
		
		String targetString1 = "http://"+prop.getProperty("MASTER_LOCATION")+":"+prop.getProperty("MASTER_PORT")+"/ChatWeb/rest/cluster/getAllHosts";
		ResteasyWebTarget target1 = client.target(targetString1);
		Response response1 = target1.request(MediaType.APPLICATION_JSON).get();
		Hosts hosts = response.readEntity(Hosts.class);
		activeHosts = hosts.getListOfHosts();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@PreDestroy
	private void onDestroy() {
		String isMaster = prop.getProperty("IS_MASTER");
		// TODO PROMENITI OVO NA FALSE POSLE TESTIRANJA
		if( isMaster.equals("true"))// pre isMaster... NE treba da ide !
			return ;
		
		// Napravim objekat Host koji predtavlja ovaj node koji salje masteru kako bi ga obrisao
		Host host = new Host(
				prop.getProperty("LOCATION"),
				prop.getProperty("NAME_OF_NODE"),
				Integer.parseInt(prop.getProperty("PORT")) );
		// Sad trazim on mastera da me izbrise iz liste registrovanih nodova
		ResteasyClient client = new ResteasyClientBuilder().build();
		String targetString = "http://"+prop.getProperty("MASTER_LOCATION")+":"+prop.getProperty("MASTER_PORT")+"/ChatWeb/rest/cluster/removeHost";
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
		Host hostForRemoval = null;
		for(Host h : activeHosts) {
			if( h.getName().equals(host.getName())) {
				hostForRemoval = h;
			}
			break;
		}
		
		if(host != null)
			activeHosts.remove(hostForRemoval);
	}

	@Override
	public List<Host> getAllHost() {
		return activeHosts;
	}
}
