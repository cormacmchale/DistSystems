package appmain;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class Adduser {
	
	@POST
	public void adduser(@PathParam("userId") int id)
	{
		System.out.println("this was added!: "+id);
	}

}
