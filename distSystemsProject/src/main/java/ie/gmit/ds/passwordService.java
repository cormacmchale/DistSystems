package ie.gmit.ds;
import com.google.protobuf.ByteString;
public class passwordService extends PasswordServiceGrpc.PasswordServiceImplBase {
	
	  //need a constructor for use in server
	  public passwordService(){}

	  //override the hash method - I assume that this will be sent from the client
	  @Override
	  public void hash(ie.gmit.ds.HashRequest request, io.grpc.stub.StreamObserver<ie.gmit.ds.HashResponse> responseObserver) 
	  {
		      try 
		      {
		    	      //just testing
			    	  //int clientId = request.getUserId();
			    	  //not needed
			    	  //takeId(clientId);
		    	  char[] hashThis = request.getPassword().toCharArray();
		    	  byte[] saltForHash = Passwords.getNextSalt();
		    	  byte[] hashedPassword = Passwords.hash(hashThis, saltForHash);
		    	  
		    	  //convert to byteStrings
		    	  ByteString hashedPasswordByteString = ByteString.copyFrom(hashedPassword);
		    	  ByteString saltByteString = ByteString.copyFrom(hashedPassword);
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
		    }	  
		}