package jobtenure.resources;

import jobtenure.api.JobTenure;
import com.codahale.metrics.annotation.Timed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

/*
 * most tenured employees API:
 *
 * Returns an object containing a list of people who are/were tenured (including
 * past employment, even if they work a different job now) at the given
 * company for longer than the company average
 *
 * getEmployeeInfo:
 * arg: String company - name of company from specified company list to calculate tenured employee list from
 *
 * return: JobTenure.ApiResult object with tenured employee data
 */
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
