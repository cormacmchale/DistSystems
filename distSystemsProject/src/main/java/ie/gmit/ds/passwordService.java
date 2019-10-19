package ie.gmit.ds;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;

public class passwordService extends PasswordServiceGrpc.PasswordServiceImplBase {
	
	  //need a constructor for use in server
	  public passwordService(){}

	  //override the hash method - I assume that this will be sent from the client
	  @Override
	  public void hash(HashRequest request, StreamObserver<HashResponse> responseObserver) 
	  {
		      try 
		      {
		    	      //just testing
			    	  //int clientId = request.getUserId();
			    	  //not needed
			    	  //takeId(clientId);
		    	  String getPassword = request.getPassword();
		    	 
		    	  char[] hashThis = getPassword.toCharArray();
		    	  byte[] saltForHash = Passwords.getNextSalt();
		    	  byte[] hashedPassword = Passwords.hash(hashThis, saltForHash);
		    	  
		    	  //convert to byteStrings
		    	  ByteString hashedPasswordByteString = ByteString.copyFrom(hashedPassword);
		    	  ByteString saltByteString = ByteString.copyFrom(saltForHash);
		    	  //build appropriate response	  
		    	  responseObserver.onNext(HashResponse.newBuilder().setUserId(request.getUserId())
		    			  										   .setHashedPassword(hashedPasswordByteString)
		    			  										   .setSalt(saltByteString).build());
		      }
		      //send default if there is a problem
		      catch(RuntimeException ex)
		      {
		    	  responseObserver.onNext(HashResponse.newBuilder().getDefaultInstanceForType());
		      }
		      responseObserver.onCompleted();
		}//end hash	
	    
	    //write the validation logic here
		@Override
		public void validate(ValidatorRequest request, StreamObserver<BoolValue> responseObserver) 
		{
			//TODO Auto-generated method stub
			//super.validate(request, responseObserver)
			//get the info from the request
			try
			{
				ByteString hashedPasswordByteArray = request.getHashedPassword();
				ByteString hashedSalt = request.getSalt();

				//everything here to return boolean
				//need to get the string here before turning it into a char array
				String getPassword = request.getPassword();
				
				//three values for checking if the password is correct
				char [] actualPassword = getPassword.toCharArray();
				byte[] hashedPassword = hashedPasswordByteArray.toByteArray();
				byte[] salt = hashedSalt.toByteArray();
				
			    //call method, check, return true if applicable
				if(Passwords.isExpectedPassword(actualPassword, salt, hashedPassword))
				{
					responseObserver.onNext(BoolValue.newBuilder().setValue(true).build());
				}
				//method return false if salt does not confirm hashed password
				else
				{	
					responseObserver.onNext(BoolValue.newBuilder().setValue(false).build());
				}
			}
			//if there is a problem return false
			catch(RuntimeException ex)
			{
				responseObserver.onNext(BoolValue.newBuilder().setValue(false).build());
			}
			
		}//end validate  
}
