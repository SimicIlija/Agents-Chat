package restControllers;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cluster.ClusterManagerLocal;
import config.PropertiesSupplierLocal;
import model.Host;
import model.Hosts;

@Path("/cluster")
@Stateless
public class ClasterController {

	@EJB
	private ClusterManagerLocal clusterManager;
	
	@EJB
	private PropertiesSupplierLocal prop;
	
	@POST
	@Path("/addHost")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String addHost(Host host) {
		
		String isMaster = prop.getProperty("IS_MASTER");
		if(!isMaster.equals("true"))
			return "no no";
		
		clusterManager.addHostToActiveList(host);
		
		return "ok";
	}
	
	
	@POST
	@Path("/removeHost")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String removeHost(Host host) {
		
		String isMaster = prop.getProperty("IS_MASTER");
		if(!isMaster.equals("true"))
			return "no no";
		
		clusterManager.removeHostFromActiveList(host);
		
		return "ok";
	}
	
	@GET
	@Path("/getAllHosts")
	@Produces(MediaType.APPLICATION_JSON)
	public Hosts getAllHosts() {
		Hosts hosts = new Hosts(clusterManager.getAllHost());
		
		return hosts;
	}
	
}
