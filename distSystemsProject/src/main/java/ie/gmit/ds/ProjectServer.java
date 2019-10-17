package ie.gmit.ds;

import java.io.IOException;
import java.util.logging.Logger;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class ProjectServer {
	
	   //initialize the server
	   private Server grpcServer;
	   private static final Logger logger = Logger.getLogger(ProjectServer.class.getName());
	   private static final int PORT = 50551;
	   //public passwordService
	   private void start() throws IOException {
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
	        final ProjectServer projectServer = new ProjectServer();
	        projectServer.start();
	        projectServer.blockUntilShutdown();
	    }
}
