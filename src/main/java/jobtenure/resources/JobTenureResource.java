package jobtenure.resources;

import jobtenure.api.JobTenure;
import com.codahale.metrics.annotation.Timed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

@Path("/job-tenure/generate-dataset")
@Produces(MediaType.APPLICATION_JSON)
public class JobTenureResource {

    private final String defaultDatafile;

    public JobTenure getJobTenure() {
        return jobTenure;
    }

    private JobTenure jobTenure;

    public JobTenureResource(String defaultDatafile) {
        this.defaultDatafile = defaultDatafile;
        this.jobTenure = new JobTenure();
    }

    @GET
    @Timed
    public JobTenure.ApiResult createDataset(@QueryParam("file") Optional<String> file) {
        this.jobTenure = new JobTenure(file.orElse(defaultDatafile));
        return(jobTenure.BuildDataSet());
    }
}
