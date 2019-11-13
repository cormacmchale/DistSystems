package appmain;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
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
			return Response.status(400).type(MediaType.TEXT_PLAIN).entity("Json failed Validation!").build();
		}
		else
		{
			users.put(newUser.getUserId(), new Userproject(newUser.getUserId(), newUser.getUserName(), newUser.getEmail(), newUser.getPassword(),
										   grpcCall(newUser.getUserId(), newUser.getPassword()).get(0),
										   grpcCall(newUser.getUserId(), newUser.getPassword()).get(1)));
			return Response.status(200).build();

		}
		//call password service grpc
	}	
	@GET
	public Collection<Userproject> getUsers() 
	{	      
	      return users.values();
	}
	//get next piece on path
	//this work return the user
	@Path("/{id}")	
	@GET
	public Userproject getUser(@PathParam("id") int id) 
	{
	      return users.get(id);
	}
	
	//alter the user
	@Path("/{id}")	
	@PUT
	public Response alterUser(Userproject changeUser, @PathParam("id") int id)
	{
		users.replace(id, changeUser);
		return Response.status(200).build();
	}
	
	private ArrayList<String> grpcCall(int id, String password)
	{
		ArrayList<String> hashedPassword = new ArrayList<String>();
		hashedPassword = passwordclient.requestAHash(id, password);
		return hashedPassword;
	}

}
