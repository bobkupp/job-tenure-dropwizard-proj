package jobtenure.resources;

import jobtenure.api.JobTenure;
import com.codahale.metrics.annotation.Timed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

@Path("/job-tenure/most-tenured")
@Produces(MediaType.APPLICATION_JSON)
public class MostTenuredResource {

    private JobTenure jobTenure;
    private final String defaultCompany;

    public MostTenuredResource(JobTenure jobTenure, String defaultCompany) {
        this.jobTenure = jobTenure;
        this.defaultCompany = defaultCompany;
    }

    @GET
    @Timed
    public JobTenure.ApiResult getCompanyRetentionData(@QueryParam("company") Optional<String> company) {
        return(jobTenure.getMostTenured(company.orElse(defaultCompany)));
    }
}
