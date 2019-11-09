package appmain;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class Appmain extends Application<Webconfigproject> {

    public static void main(String[] args) throws Exception {
        new Appmain().run(args);
    }

    public void run(Webconfigproject webconfig, Environment environment) throws Exception {
        final Adduser resource = new Adduser();
        environment.jersey().register(resource);
    }
}
