package restControllers;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cluster.ClusterManagerLocal;
import cluster.UserClusterManagerLocal;
import config.PropertiesSupplierLocal;
import jms_messages.JMSMessageToWebSocket;
import jms_messages.JMSMessageToWebSocketType;
import jms_messages.MessageReqMsg;
import model.Host;

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
	public String messageToPush(Host host) {
		
		String isMaster = prop.getProperty("IS_MASTER");
		if(!isMaster.equals("true"))
			return "no no";
		
		clusterManager.addHostToActiveList(host);
		
		return "ok";
	}
	
	
	
}
