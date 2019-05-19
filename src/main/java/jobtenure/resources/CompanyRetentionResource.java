package jobtenure.resources;

import jobtenure.api.JobTenure;
import com.codahale.metrics.annotation.Timed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/job-tenure/company-retention")
@Produces(MediaType.APPLICATION_JSON)
public class CompanyRetentionResource {

    private JobTenure jobTenure;

    public CompanyRetentionResource(JobTenure jobTenure) {
        this.jobTenure = jobTenure;
    }

    @GET
    @Timed
    public JobTenure.ApiResult getCompanyRetentionData() {
        return(jobTenure.getTenureRanking());
    }
}
