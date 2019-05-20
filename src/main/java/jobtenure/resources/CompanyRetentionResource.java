package jobtenure.resources;

import jobtenure.api.JobTenure;
import com.codahale.metrics.annotation.Timed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/*
 * company retention data API:
 *
 * Returns a json formatted list of objects ranking each company by retention, giving
 *  the average number of years people work at each company
 *
 * getCompanyRetentionData:
 *
 * return: JobTenure.ApiResult object with company tenure ranking data
 */
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
