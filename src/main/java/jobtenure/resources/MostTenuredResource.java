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

@Path("/job-tenure/most-tenured")
@Produces(MediaType.APPLICATION_JSON)
public class MostTenuredResource {
    private final String defaultCompany;
    private final AtomicLong counter;

    public MostTenuredResource(String defaultCompany) {
        this.defaultCompany = defaultCompany;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public Saying sayHello(@QueryParam("company") Optional<String> company) {
        final String value = String.format("Hello, %s!", company.orElse(defaultCompany));
        return new Saying(counter.incrementAndGet(), value);
    }
}
