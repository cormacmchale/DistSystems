package appmain;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import java.util.Collection;
import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class Adduser {
	
	private HashMap<Integer, Userproject> users = new HashMap<>();
	

	public Adduser()
	{
	    Userproject userOne = new Userproject(1, "The GZA", "HipHop", "test");
	    users.put(userOne.getUserId(), userOne);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void adduser(Userproject u) throws JsonParseException, JsonMappingException
	{
		System.out.println("test this method"+ u.getUserId());
		//call password service grpc
	}
	
	@GET
	public Collection<Userproject> getArtists() 
	{	      
	      return users.values();
	}

}
