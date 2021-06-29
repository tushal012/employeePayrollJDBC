package com.bridgelabz;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.bridgelabz.EmployeePayrollService.IOService.DB_IO;

public class EmployeePayrollServicesTest {
   @Test
   public  void given3EmployeeWhenWrittenToFileShouldMatchEmployeeEntries() {
       EmployeePayrollData[] arrayOfEmps ={
               new EmployeePayrollData(1,"Jeff Bezos",100000.0),
               new EmployeePayrollData(2,"Bill Gates",200000.0),
               new EmployeePayrollData(3,"Mark Zuckerberg",300000.0),
       };
       EmployeePayrollService employeePayrollService;
       employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
       employeePayrollService.writeEmployeeData(EmployeePayrollService.IOService.FILE_IO);
       employeePayrollService.printData(EmployeePayrollService.IOService.FILE_IO);
       long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.FILE_IO);
       Assert.assertEquals(3,entries);
   }

    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {

       EmployeePayrollService employeePayrollService = new EmployeePayrollService();
       List<EmployeePayrollData> employeePayrollData =  employeePayrollService.readEmployeePayrollData(DB_IO);
       Assert.assertEquals(3, employeePayrollData.size());
    }

    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDatabase() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData =  employeePayrollService.readEmployeePayrollData(DB_IO);
        employeePayrollService.updateEmployeeSalary("Terisa",3000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assert.assertTrue(result);

    }

    @Test
    public void givenDataRange_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        LocalDate startDate = LocalDate.of(2018,01,01);
        LocalDate endDate = LocalDate.now();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollDataRange(EmployeePayrollService.IOService.DB_IO,startDate,endDate);
        Assert.assertEquals(3,employeePayrollData.size());
    }

    @Test
    public void givenPayrollData_WhenAverageSalaryRetrievedByGender_shouldReturnProperValue() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        Map<String, Double> averageSalaryByGender = employeePayrollService.readAverageSalaryByGender(DB_IO);
        Assert.assertTrue(averageSalaryByGender.get("M").equals(2000000.00) &&
                averageSalaryByGender.get("F").equals(3000000.00));
    }

    @Test
    public void givenNewEmployee_WhenAdded_ShouldSyncWithDB()  {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        employeePayrollService.addEmployeeToPayroll("Mark", 5000000.00, LocalDate.now(), 'M');
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Mark");
        Assert.assertTrue(result);
    }


    @Test
    public void given6Employee_WhenAddedToDB_ShouldMatchEmployeeEntries() {
        EmployeePayrollData[] arrayOfEmps = {
                new EmployeePayrollData(0,"Jeff Bezos",'M',100000.00,LocalDate.now()),
                new EmployeePayrollData(0,"Bill Gates",'M',200000.00,LocalDate.now()),
                new EmployeePayrollData(0,"Mark Zuckerberg",'M',300000.00,LocalDate.now()),
                new EmployeePayrollData(0,"Sunder",'M',400000.00,LocalDate.now()),
                new EmployeePayrollData(0,"Mukesh",'M',500000.00,LocalDate.now()),
                new EmployeePayrollData(0,"Anil",'M',600000.00,LocalDate.now())
        };
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Instant start = Instant.now();
        employeePayrollService.addEmployeeToPayroll((Arrays.asList(arrayOfEmps)));
        Instant end = Instant.now();
        System.out.println("Duration without Thread: " + Duration.between(start, end));
        Instant threadStart = Instant.now();
        employeePayrollService.addEmployeeToPayrollWithThread(Arrays.asList(arrayOfEmps));
        Instant threadEnd = Instant.now();
        System.out.println("Duration with Thread: " + Duration.between(threadStart, threadEnd));
        Assert.assertEquals(15, employeePayrollService.countEntries(EmployeePayrollService.IOService.DB_IO));
    }

    @Before
    public void setup(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3000;
    }

    public EmployeePayrollData[] getEmployeeList(){
        Response response = RestAssured.get("/employees");
        System.out.println("EMPLOYEE PAYROLL IN JSONServer: \n" +response.asString());
        EmployeePayrollData[] arrayOfEmps = new Gson().fromJson(response.asString(),EmployeePayrollData[].class);
        return arrayOfEmps;
    }

    @Test
    public void givenEmployeeDataInJSONServer_WhenRetrieved_ShouldMatchTheCount(){
        EmployeePayrollData[] arrayOfEmps = getEmployeeList();
        EmployeePayrollService employeePayrollService;
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
        long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.REST_IO);
        Assert.assertEquals(2,entries);
    }

}
