package javado.lec06;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("calc")
public class CalcResource {

    @Path("add")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public int add(@QueryParam("a") int a, @QueryParam("b") int b) {
        return a + b;
    }

    @Path("div")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public int div(@QueryParam("a") int a, @QueryParam("b") int b) {
        return a * b;
    }
}
