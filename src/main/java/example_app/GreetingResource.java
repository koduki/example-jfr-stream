package example_app;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class GreetingResource {

    @WebTrace
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("hello")
    public String hello() throws InterruptedException {
        Thread.sleep((int) (Math.random() * 5000));
        return "Hello JFR";
    }
}
