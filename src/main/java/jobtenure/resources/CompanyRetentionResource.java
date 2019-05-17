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

@Path("/job-tenure/company-retention")
@Produces(MediaType.APPLICATION_JSON)
public class CompanyRetentionResource {
    private final AtomicLong counter;

    public CompanyRetentionResource() {
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public Saying sayHello() {
        final String value = String.format("no argument for this command");
        return new Saying(counter.incrementAndGet(), value);
    }
}
