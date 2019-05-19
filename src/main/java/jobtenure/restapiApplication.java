package jobtenure;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import jobtenure.api.JobTenure;
import jobtenure.resources.CareerInfoResource;
import jobtenure.resources.CompanyRetentionResource;
import jobtenure.resources.JobTenureResource;
import jobtenure.resources.MostTenuredResource;

public class restapiApplication extends Application<restapiConfiguration> {

    public static void main(final String[] args) throws Exception {
        new restapiApplication().run(args);
    }

    @Override
    public String getName() {
        return "restapi";
    }

    @Override
    public void initialize(final Bootstrap<restapiConfiguration> bootstrap) {
        // TODO: application initialization
    }

    private JobTenure jobTenure;

    @Override
    public void run(final restapiConfiguration configuration,
                    final Environment environment) {
        final JobTenureResource jtResource = new JobTenureResource(
                configuration.getDefaultDatafile()
        );
        environment.jersey().register(jtResource);
        jobTenure = jtResource.getJobTenure();

        final CareerInfoResource ciResource = new CareerInfoResource(
                jobTenure,
                configuration.getDefaultEmployee()
        );
        environment.jersey().register(ciResource);

        final CompanyRetentionResource crResource = new CompanyRetentionResource(
                jobTenure
        );
        environment.jersey().register(crResource);

        final MostTenuredResource mtResource = new MostTenuredResource(
                jobTenure,
                configuration.getDefaultCompany()
        );
        environment.jersey().register(mtResource);
    }

}
