package jobtenure;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
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

    @Override
    public void run(final restapiConfiguration configuration,
                    final Environment environment) {
        final JobTenureResource jtResource = new JobTenureResource(
                configuration.getDefaultDatafile()
        );
        environment.jersey().register(jtResource);

        final CareerInfoResource ciResource = new CareerInfoResource(
                configuration.getDefaultEmployee()
        );
        environment.jersey().register(ciResource);

        final CompanyRetentionResource crResource = new CompanyRetentionResource(
        );
        environment.jersey().register(crResource);

        final MostTenuredResource mtResource = new MostTenuredResource(
                configuration.getDefaultCompany()
        );
        environment.jersey().register(mtResource);
    }

}
