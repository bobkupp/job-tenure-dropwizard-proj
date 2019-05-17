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

@Path("/job-tenure/generate-dataset")
@Produces(MediaType.APPLICATION_JSON)
public class JobTenureResource {
    private final String defaultDatafile;
    private final AtomicLong counter;

    public JobTenureResource(String defaultDatafile) {
        this.defaultDatafile = defaultDatafile;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public Saying sayHello(@QueryParam("file") Optional<String> file) {
        final String value = String.format("Hello, %s!", file.orElse(defaultDatafile));
        return new Saying(counter.incrementAndGet(), value);
    }
}
