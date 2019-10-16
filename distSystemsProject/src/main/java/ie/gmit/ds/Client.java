package ie.gmit.ds;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import com.google.j2objc.annotations.ReflectionSupport.Level;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import java.util.Scanner;

public class Client {
	//ask the user for an input
	Scanner UI = new Scanner(System.in);
	
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
    //try synchronous first??
    //add the functionality to ask for a request in console window
    public void requestAHash()
    {
    	System.out.println("Please Enter ID:");
    	int clientId = UI.nextInt();
    	System.out.println("Please Enter Password:");
    	String password = UI.next();
    	HashRequest h = HashRequest.newBuilder()
    			                   .setUserId(clientId)
    			                   .setPassword(password)
    			                   .build();
    	
    	HashResponse result = HashResponse.newBuilder().getDefaultInstanceForType();
    	try 
    	{
    		result = syncPasswordService.hash(h);
    	}
        catch (StatusRuntimeException ex) 
    	{
	        System.out.println(ex.getLocalizedMessage());
	        return;
        }
    	
    	if(result.getUserId()!= 0)
    	{
    		logger.info("Hashed password = "+ result.getHashedPassword());
    	} 	
    }
    
    public static void main(String[] args) throws Exception {
    	//start the client
        Client passwordclient = new Client("localhost", 50551);
        try {
        	//everything should happen here
            passwordclient.requestAHash();
        } finally {
            // Don't stop process, keep alive to receive async response
            Thread.currentThread().join();
        }
    }
    
}
