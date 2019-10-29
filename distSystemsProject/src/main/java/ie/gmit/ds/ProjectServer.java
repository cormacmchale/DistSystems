package ie.gmit.ds;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.*;

public class ProjectServer {
	
	   //initialize the server
	   private Server grpcServer;
	   private static final Logger logger = Logger.getLogger(ProjectServer.class.getName());
	   private static int PORT;
	   //public passwordService
	   private void start(int userPort) throws IOException {
		    PORT = userPort;
	        grpcServer = ServerBuilder.forPort(PORT)
	                .addService(new passwordService())
	                .build()
	                .start();
	        		logger.info("Server started, listening on " + PORT);
	    }
	    @SuppressWarnings("unused")
		private void stop() {
	        if (grpcServer != null) {
	            grpcServer.shutdown();
	        }
	    }
	    private void blockUntilShutdown() throws InterruptedException {
	        if (grpcServer != null) {
	            grpcServer.awaitTermination();
	        }
	    }	    
	    //Main Method to run the server
	    public static void main(String[] args) throws IOException, InterruptedException {
	    	//ask the user for an input
	    	Scanner UI = new Scanner(System.in);
	    	System.out.print("Enter Port Number you wish to listen for connections on:");
	    	int userPort = UI.nextInt();
	    	//intialize and start the server
	    	final ProjectServer projectServer = new ProjectServer();
	        projectServer.start(userPort);
	        projectServer.blockUntilShutdown();
	    }
}
