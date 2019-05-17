package jobtenure;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;

public class restapiConfiguration extends Configuration {
    @NotEmpty
    private String defaultDatafile = "/tmp/code-challenge-data.csv";

    @NotEmpty
    private String defaultEmployee = "Jose Richards";

    @NotEmpty
    private String defaultCompany = "EverTrue";

    @JsonProperty
    public String getDefaultDatafile() {
        return defaultDatafile;
    }

    @JsonProperty
    public void setDefaultDatafile(String datafile) {
        this.defaultDatafile = datafile;
    }

    @JsonProperty
    public String getDefaultEmployee() {
        return defaultEmployee;
    }

    @JsonProperty
    public void setDefaultEmployee(String employee) {
        this.defaultEmployee = employee;
    }

    @JsonProperty
    public String getDefaultCompany() {
        return defaultCompany;
    }

    @JsonProperty
    public void setDefaultCompany(String company) {
        this.defaultCompany = company;
    }
}
