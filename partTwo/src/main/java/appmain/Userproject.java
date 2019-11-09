package appmain;
import com.fasterxml.jackson.annotation.JsonProperty;
public class Userproject {

	public Userproject(){//Json deserialisation
	}
    
	public Userproject(int id, String uN ,String e, String p)
	{
		this.userId = id;
		this.userName = uN;
		this.email = e;
		this.password = p;
	}
	
	//variables required for a post
	private int userId;
	private String userName;
	private String email;
	private String password;
	
	 @JsonProperty
	public int getUserId() {
		return userId;
	}
	 @JsonProperty
	public String getUserName() {
		return userName;
	}
	 @JsonProperty
	public String getEmail() {
		return email;
	}
	 @JsonProperty
	public String getPassword() {
		return password;
	}
	
}
