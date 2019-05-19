# restapi

How to start the restapi application
---

1. Run `mvn clean install` to build your application
2. Start application with `java -jar target/evertrue-1.0-SNAPSHOT.jar server config.yml`
3. To check that your application is running enter url `http://localhost:8080`

RestAPIs:

http://localhost:8080/job-tenure/generate-dataset?file={file_path}			: generate dataset for supplied csv file (default: /tmp/code-challenge-data.csv)

http://localhost:8080/job-tenure/career-info?employee={firstname+lastname}	: get career data for employee specified

http://localhost:8080/job-tenure/company-retention				: get data ranking companies by average retention

http://localhost:8080/job-tenure/most-tenured?company={company_name}		: get list of current/former employees whose tenure was greater than the company average



