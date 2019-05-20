# restapi

How to start the restapi application
---
1. Git clone package from https://github.com/bobkupp/job-tenure-dropwizard-proj
2. change directory to job-tenure-dropwizard-proj
3. Run `mvn package` to build the application
4. Install code challenge csv data file to known path.
    - a copy can be found in the job-tenure-dropwizard-proj/data directory
    - path should be specified for the 'generate-dataset' API call
    - default file/path used by API is: '/tmp/code-challenge-data.csv'
5. Start application with command: `java -jar target/evertrue-1.0-SNAPSHOT.jar server config.yml`

RestAPIs:

http://localhost:8080/job-tenure/generate-dataset?file={file_path}			
: generate dataset for supplied csv file (default: /tmp/code-challenge-data.csv)

http://localhost:8080/job-tenure/career-info?employee={firstname+lastname}	
: get career data for specified employee

http://localhost:8080/job-tenure/company-retention		
: get data ranking companies by average retention

http://localhost:8080/job-tenure/most-tenured?company={company_name}		
: get list of current/former employees whose tenure was greater than the company average


Example url's:

- http://localhost:8080/job-tenure/generate-dataset?file=/Users/bkupperstein/job-tenure-proj/data/code-challenge-data.csv
- http://localhost:8080/job-tenure/career-info?employee=Tina+Romero
- http://localhost:8080/job-tenure/most-tenured?company=Northeastern+University
- http://localhost:8080/job-tenure/company-retention


Assumptions:

- individuals need to be at least 18 years old to be assigned to a job

- individuals can only be assigned to a job in their state of residence (a potential enhancement
might be to allow them to also work in adjacent/nearby states)

- start dates are randomly generated (within some fixed parameters), with some input from the
employee's age

- end dates are randomly generated (again, with some fixed parameters), for a date after the
start date

- the number of jobs an employee might have in their work history is dependent on the number of
employers in their state (up to the maximum of 3)
