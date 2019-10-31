package ie.gmit.ds;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import java.util.Scanner;

public class Client {
	
	//ask the user for an input
	Scanner UI = new Scanner(System.in);
	
	//save hashRequest results for request for validation
	private ByteString hashedPassword;
	private ByteString salt;
	private int clientId;
	private String password;
	
	//initialize applicable fields
    private static final Logger logger = Logger.getLogger(Client.class.getName());  
    private final ManagedChannel channel;
    private final PasswordServiceGrpc.PasswordServiceStub asyncPasswordService;
    private final PasswordServiceGrpc.PasswordServiceBlockingStub syncPasswordService;

    public Client(String host, int port) {
        channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        syncPasswordService = PasswordServiceGrpc.newBlockingStub(channel);
        asyncPasswordService = PasswordServiceGrpc.newStub(channel);
    }
    
    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
    
    //method for making the request for a hashed password with salt
    //we should send a message (hashrequest in this) and wait on a message back (hash response)
    //add the functionality to ask for a request in console window
    public void requestAHash()
    {
    	System.out.println("Please Enter ID:");
    	clientId = UI.nextInt();
    	System.out.println("Please Enter Password:");
    	password = UI.next();
    	HashRequest h = HashRequest.newBuilder()
    			                   .setUserId(clientId)
    			                   .setPassword(password)
    			                   .build();
    	
    	HashResponse result = HashResponse.newBuilder().getDefaultInstanceForType(); 	
    	try 
    	{
    		result = syncPasswordService.hash(h);
	        	//just checking for functionality... this is not necessary
	        	//if(result.getUserId()!= 0)
	        	//{
	        	//	logger.info("Hashed password = "+ result.getHashedPassword());
	        	//}
        	//save these here for validation later
        	hashedPassword = result.getHashedPassword();
        	salt = result.getSalt();
    	}
        catch (StatusRuntimeException ex) 
    	{
	        System.out.println(ex.getLocalizedMessage());
	        return;
        }
    }
    
    //async method to check password here
    public void asyncPasswordValidation()
    {
    	StreamObserver<BoolValue> responseObserver = new StreamObserver<BoolValue>()
    	{
			@Override
			public void onNext(BoolValue value) {
				// TODO Auto-generated method stub
				if(value.getValue())
				{
					System.out.println("Password is correct");
				}
				else
				{
					System.out.println("Password is incorrect");
				}
				
			}
			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub
				//dont care
			}

			@Override
			public void onCompleted() {
				// TODO Auto-generated method stub
				System.exit(0);
			}   		
    	};   	
        try {
            asyncPasswordService.validate(ValidatorRequest.newBuilder().setPassword(password)
            														   .setHashedPassword(hashedPassword)
            														   .setSalt(salt).build(), responseObserver);
        } catch (
                StatusRuntimeException ex) {
            //logger.log(Level.WARNING, "RPC failed: {0}", ex.getStatus());
            return;
        }
    }

    
    
    public static void main(String[] args) throws Exception {
    	//start the client
        Client passwordclient = new Client("192.168.0.101", 40000);
        try {
        	//everything should happen here
            passwordclient.requestAHash();
            //afterwards send the message back for validation
            passwordclient.asyncPasswordValidation();
        } finally {
            // Don't stop process, keep alive to receive async response
            Thread.currentThread().join();
        }
    }
    
}
