package appmain;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class Appmain extends Application<Webconfig> {

    public static void main(String[] args) throws Exception {
        new Appmain().run(args);
    }

    public void run(Webconfig webconfig, Environment environment) throws Exception {
        final Adduser resource = new Adduser();
        environment.jersey().register(resource);
    }
}
