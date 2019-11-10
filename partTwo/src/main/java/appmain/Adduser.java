package appmain;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import ie.gmit.ds.Client;

@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Adduser {
	
	Client passwordclient = new Client("127.0.0.1", 40000);
	private HashMap<Integer, Userproject> users = new HashMap<>();
	private final Validator validator;

	public Adduser(Validator validator)
	{
	    this.validator = validator;
		Userproject userOne = new Userproject(1, "The GZA", "HipHop", "test");
	    users.put(userOne.getUserId(), userOne);
	}
	
	@POST
	public Response adduser(Userproject newUser)
	{
		
		Set<ConstraintViolation<Userproject>> violations = validator.validate(newUser);
		if(violations.size()>0)
		{
			return Response.status(Status.CONFLICT).type(MediaType.TEXT_PLAIN).entity("Json failed Validation!").build();
		}
		else
		{
			users.put(newUser.getUserId(), newUser);
			System.out.println(grpcCall(newUser.getUserId(), newUser.getPassword()));
			return Response.status(200).build();

		}
		//call password service grpc
	}	
	@GET
	public Collection<Userproject> getArtists() 
	{	      
	      return users.values();
	}
	
	private String grpcCall(int id, String password)
	{
		String hashedPassword = passwordclient.requestAHash(id, password);
		return hashedPassword;
	}

}
