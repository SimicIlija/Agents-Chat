package Rest;
import java.io.File;
import java.io.IOException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.Form;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;



@LocalBean
@Path("/Test")
@Stateless
public class Test  {


	@GET
	@Path("/test")
	@Produces(MediaType.TEXT_PLAIN)
	public String test(@Context HttpServletRequest request) {
		
		return "OK";
	}
	
	@GET
	@Path("/hello/{p}")
	@Produces(MediaType.TEXT_PLAIN)
	public String hello(@PathParam("p") String s) {
		System.out.println("################" + s);
		return "Hello " + s;
	}
	
//	@GET
//	@Path("/napravi/{student}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public Student napravi(@PathParam("student") PathSegment params) {
//		System.out.println("path parametar {student} je: " + params.getPath());
//		return new Student(params.getMatrixParameters().get("ime").get(0),
//				params.getMatrixParameters().get("prezime").get(0));
//	}
	
	@GET
	@Path("/helloQ")
	@Produces(MediaType.TEXT_PLAIN)
	public String helloQ(@QueryParam("s") String s) {
		System.out.println("################" + s);
		return "Hello " + s;
	}

	@GET
	@Path("/testHeader")
	@Produces(MediaType.TEXT_PLAIN)
	public String testHeader(@HeaderParam("User-Agent") String userAgent) {
		System.out.println(userAgent);
		return userAgent;
	}

	@GET
	@Path("/testSession")
	@Produces(MediaType.TEXT_PLAIN)
	public String testSession(@Context HttpServletRequest request) {
		String user = (String)request.getSession().getAttribute("user");
		System.out.println(user);
		return user;
	}

	
	@GET
	@Path("/other")
	@Produces(MediaType.TEXT_PLAIN)
	public String other() {
		ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:8080/TestREST/rest/students/test");
        Response response = target.request().get();
        String ret = response.readEntity(String.class);
        return ret;
	}

	

}
