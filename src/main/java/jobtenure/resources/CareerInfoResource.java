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
 * career info API:
 *
 * Returns a json formatted list of objects representing a person's career
 * data in order from most recent to least recent
 *
 * getEmployeeInfo:
 * arg: String employee - employee whose data should be retrieved
 *
 * return: JobTenure.ApiResult object with employee data
 */
@Path("/job-tenure/career-info")
@Produces(MediaType.APPLICATION_JSON)
public class CareerInfoResource {

    private JobTenure jobTenure;
    private final String defaultEmployee;

    public CareerInfoResource(JobTenure jobTenure, String defaultEmployee) {
        this.jobTenure = jobTenure;
        this.defaultEmployee = defaultEmployee;
    }

    @GET
    @Timed
    public JobTenure.ApiResult getEmployeeInfo(@QueryParam("employee") Optional<String> employee) {
        return(jobTenure.getEmployeeData(employee.orElse(defaultEmployee)));
    }
}
