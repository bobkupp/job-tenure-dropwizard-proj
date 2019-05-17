package jobtenure.resources;

import jobtenure.api.Saying;
import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;

@Path("/job-tenure/career-info")
@Produces(MediaType.APPLICATION_JSON)
public class CareerInfoResource {
    private final String defaultEmployee;
    private final AtomicLong counter;

    public CareerInfoResource(String defaultEmployee) {
        this.defaultEmployee = defaultEmployee;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public Saying sayHello(@QueryParam("employee") Optional<String> employee) {
        final String value = String.format("Hello, %s!", employee.orElse(defaultEmployee));
        return new Saying(counter.incrementAndGet(), value);
    }
}
