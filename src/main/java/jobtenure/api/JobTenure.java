package jobtenure.api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import jobtenure.*;

public class JobTenure {
    private static final int MAX_JOBS = 3;

    private Connection conn = null;
    private Company companies = new Company();
    private static ArrayList<Employee> employees = new ArrayList<>();

    private HashMap<String, ArrayList<Company>> ds = null;
    private String csvFile;

    public JobTenure() {
        // Jackson deserialization
    }

public JobTenure(String content) {
        this.csvFile = content;
    }

    /*
     *  API: 4: generate random dataset that gives each of the people in the csv 0-3 jobs, drawing from the
     *  company and position lists.
     */
    public ApiResult BuildDataSet() {
        int employeeRecordCount = 0;
        if (importCsvData(csvFile)) {
            ds = generateRandomDataset();
            companies.generateAllTenureData();
            for (Company company : Company.getCompanies()) {
                employeeRecordCount += company.getCurrentAndFormerEmployees().size();
            }
            employeeRecordCount = ds.size();
        }
        closeDatabase();
        return employeeRecordCount == 0 ?
                new ApiResult("no company/employee records generated") :
                new ApiResult(ds);
    }

    private Boolean importCsvData(String csvFilePath) {
        String line;
        String cvsSplitBy = ",";
        boolean importedCsvData  = false;

        createDatabase();
        if (conn != null) {
            try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
//                int TEST_ONLY=0;
                while ((line = br.readLine()) != null) {
//                    if (TEST_ONLY++ > 10)
//                        break;

                    // use comma as separator
                    String[] employeeData = line.split(cvsSplitBy);

                    // add any row except for header line
                    if (!employeeData[0].equals("first_name") && !employeeData[1].equals("last_name")) {
                        if (!addRow(employeeData)) {
                            break;
                        }
                    }
                }
                importedCsvData = true;

            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.out.println("Error reading csv data file: " + csvFilePath + ", error: " + ioe.getMessage());
            }
        }
        return importedCsvData;
    }

    private HashMap<String, ArrayList<Company>> generateRandomDataset() {
        ArrayList<HashMap<String, String>> results = queryTable("select * from people;", getPeopleTableColumns());
        Iterator<HashMap<String, String>> rows = results.iterator();
        HashMap<String, ArrayList<Company>> dataset = new HashMap<>();

        // generate array of ints, that, when shuffled, gives a list of unique random numbers
        ArrayList<Integer> randomNumbersMax = new ArrayList<>();
        for (int i =0 ; i < companies.getCompanyCount(); i++) {
            randomNumbersMax.add(i);
        }

        while (rows.hasNext()) {
            HashMap<String, String> employeeInfo = rows.next();
            String state = employeeInfo.get("state");
            Employee employee = new Employee();
            employee.setName(employeeInfo.get("firstname") + " " + employeeInfo.get("lastname"));
            employee.setAge(Integer.parseInt(employeeInfo.get("age")));
            employee.setState(state);

            if (employee.getAge() >= Employee.MINIMUM_AGE_OF_EMPLOYMENT) {

                ArrayList<Company> inStateCompanies = companies.getCompaniesForState(state);
                // check if any eligible companies were found
                if (inStateCompanies != null && !inStateCompanies.isEmpty()) {
                    Company inStateCompany;
                    int companyCount = inStateCompanies.size();
                    ArrayList<Integer> randomNumbers = new ArrayList<>(randomNumbersMax.subList(0, companyCount));
                    Collections.shuffle(randomNumbers);
                    int companyIndex;

                    // find up to MAX_JOBS jobs
                    for (int jobCount = 0; jobCount < MAX_JOBS && jobCount < companyCount; jobCount++) {
                        companyIndex = randomNumbers.get(jobCount);
                        inStateCompany = inStateCompanies.get(companyIndex);
                        Job job = new Job(inStateCompany.getName(), employee.getState(), Position.getRandomOccupation());
                        employee.addJob(job);
                        inStateCompany.addEmployee(employee);
                        if (!dataset.containsKey(state)) {
                            ArrayList<Company> company = new ArrayList<>();
                            company.add(inStateCompany);
                            dataset.put(state, company);
                        } else {
                            ArrayList<Company> company = dataset.get(state);
                            if (!company.contains(inStateCompany)) {
                                company.add(inStateCompany);
                            }
                        }
                    }
                }
            }
            employees.add(employee);
        }
        return dataset;
    }

    /*
     *  API: 5a: return json-formatted list of objects representing person's career data
     *  from most recent to least recent
     */
    public ApiResult getEmployeeData(String employeeName) {
        Employee employeeData = findEmployeeInDataset(employeeName);

        return employeeData == null ?
                new ApiResult("no data found for employee: " + employeeName) :
                new ApiResult(employeeData);
    }

    private Employee findEmployeeInDataset(String name) {
        for (Employee employee : employees) {
            if (employee.getName().equals(name)) {
                return employee;
            }
        }
        return null;
    }

    /*
     *  API: 5b: return json-formatted list of companies with average tenure
     */
    public ApiResult getTenureRanking() {
//        TreeMap<Double, String> tenureMap = new TreeMap<>(Comparator.reverseOrder());
//
//        HashSet<Company> companies = Company.getCompanies();
//        System.out.println("getTenureRanking: company count: " + companies.size());
//        for (Company company : companies) {
//            tenureMap.put(company.getAverageTenure(), company.getName());
//        }
        HashMap<String, Double> tenureMap = new HashMap<>();

        HashSet<Company> companies = Company.getCompanies();
        System.out.println("getTenureRanking: company count: " + companies.size());
        for (Company company : companies) {
            tenureMap.put(company.getName(), company.getAverageTenure());
        }
        System.out.println("getTenureRanking: tenure map count: " + tenureMap.size());
        Map sortedMap = sortByValues(tenureMap);
        return new ApiResult(sortedMap);
    }

    /*
     *  API: 5c: return json-formatted list of employees whose tenure at the company
     *  is/was greater than the company average
     */
    public ApiResult getMostTenured(String companyName) {
        TenureData tenureData = new TenureData();
        tenureData.most_tenured = new ArrayList<>();

        Company company = Company.getByName(companyName);

        if (company != null) {
            tenureData.company = companyName;
            tenureData.avg_retention = company.getAverageTenure();
            for (Employee employee : company.getCurrentAndFormerEmployees()) {
                double tenure = employee.calculateTenureAtCompany(companyName);
                if (tenure > company.getAverageTenure()) {
                    // fill in company info, if not there, then add employee info to list
                    EmployeeTenure employeeTenure = new EmployeeTenure();
                    employeeTenure.name = employee.getName();
                    employeeTenure.age = employee.getAge();
                    employeeTenure.tenure = tenure;
                    tenureData.most_tenured.add(employeeTenure);
                }
            }
            return new ApiResult(tenureData);
        }
        return new ApiResult("no data found for company: " + companyName);
    }

    private void createDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            Statement stat = conn.createStatement();
            stat.executeUpdate("drop table if exists people;");
            stat.executeUpdate("create table people (firstname, lastname, age, state);");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Unable to create database, JDBC class not found: " + cnfe.getMessage());
        } catch (SQLException sqle) {
            System.out.println("Unable to create database, failed to create table: " + sqle.getMessage());
            closeDatabase();
            conn = null;
        }
    }

    private void closeDatabase() {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException sqle) {
                System.out.println("Failure attempting to close database: " + sqle.getMessage());
            }
        }
    }

    private Boolean addRow(String[] employeeData) {
        try {
            PreparedStatement prep = conn.prepareStatement("insert into people values (?, ?, ?, ?);");

            for (int i = 0; i < employeeData.length; i++) {
                prep.setString(i + 1, employeeData[i]);
            }
            prep.addBatch();

            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);
        } catch (SQLException sqle) {
            System.out.println("Unable to add row to 'people' table: " + sqle.getMessage());
            return false;
        }
        return true;
    }

    private ArrayList<HashMap<String, String>> queryTable(String query, ArrayList<String> columns) {
        ResultSet rs = null;
        ArrayList<HashMap<String, String>> rows = new ArrayList<>();
        try {
            Statement stat = conn.createStatement();
            rs = stat.executeQuery(query);
            while (rs.next()) {
                HashMap<String, String> resultMap = new HashMap<>();
                for (String columnName : columns) {
                    resultMap.put(columnName, rs.getString(columnName));
                }
                rows.add(resultMap);
            }
        } catch (SQLException sqle) {
            System.out.println("Unable to add row to 'people' table: " + sqle.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException sqle) {
                System.out.println("Error trying to close resultSet: " + sqle.getMessage());
            }
        }
        return rows;
    }

    private ArrayList<String> getPeopleTableColumns() {
        return new ArrayList<>(Arrays.asList("firstname", "lastname", "age", "state"));
    }

    class TenureData {
        public String getCompany() {
            return company;
        }

        public double getAvg_retention() {
            return avg_retention;
        }

        public ArrayList<EmployeeTenure> getMost_tenured() {
            return most_tenured;
        }

        String company;
        double avg_retention;
        ArrayList<EmployeeTenure> most_tenured;
    }

    class EmployeeTenure {
        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public double getTenure() {
            return tenure;
        }

        String name;
        int age;
        double tenure;
    }

    private static <K, V extends Comparable<V>> Map<K, V>
    sortByValues(final Map<K, V> map) {
        Comparator<K> valueComparator =
            new Comparator<K>() {
                public int compare(K k1, K k2) {
                    int compare =
//                        map.get(k1).compareTo(map.get(k2));   // sort ascending
                        map.get(k2).compareTo(map.get(k1));     // sort descending
                    if (compare == 0)
                        return 1;
                    else
                        return compare;
                }
            };

        Map<K, V> sortedByValues =
                new TreeMap<K, V>(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;
    }

    public class ApiResult {
        ApiResult(Object content) {
            this.content = content;
        }

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }

        private Object content;
    }
}
