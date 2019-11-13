package appmain;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
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
	//hopefully jackson knows?
	//it knows - well done jackson
	public Userproject(int id, String uN ,String e, String p, String h, String s)
	{
		this.userId = id;
		this.userName = uN;
		this.email = e;
		this.password = p;
		this.hashedPassword = h;
		this.salt = s;
	}


	//variables required for a post
	@NotNull
	private int userId;
	@NotBlank
	private String userName;
	@NotBlank
	private String email;
	@NotBlank
	private String password;
	
	//testing
	private String hashedPassword;
	private String salt;
	 @JsonProperty
	public String getHashedPassword() {
		return hashedPassword;
	}
	 @JsonProperty
	public String getSalt() {
		return salt;
	}
	
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
