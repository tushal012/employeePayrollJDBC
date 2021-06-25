package com.bridgelabz;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.bridgelabz.EmployeePayrollService.IOService.DB_IO;

public class EmployeePayrollServicesTest {
   @Test
   public  void given3EmployeeWhenWrittenToFileShouldMatchEmployeeEntries() {
       EmployeePayrollData[] arrayOfEmps = {
               new EmployeePayrollData(1, "Jeff Bezos", 100000.0),
               new EmployeePayrollData(2, "Bill Gates", 20000.0),
               new EmployeePayrollData(3, "Mark Zuckerberg", 30000.0)
       };
       EmployeePayrollService employeePayrollService;
       employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
       employeePayrollService.writeEmployeePayrollData(EmployeePayrollService.IOService.FILE_IO);
       employeePayrollService.printData(EmployeePayrollService.IOService.FILE_IO);
       long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.FILE_IO);
       Assert.assertEquals(3, entries);
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
    public void givenDateRange_WhenRetrievedEmployee_ShouldReturnEmpCount()  {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        LocalDate startDate = LocalDate.of(2018, 01, 01);
        LocalDate endDate = LocalDate.now();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollDataForDateRange(DB_IO, startDate, endDate);
        Assert.assertEquals(3, employeePayrollData.size());
    }

    @Test
    public void givenPayrollData_WhenAverageSalaryRetrievedByGender_shouldReturnProperValue() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        Map<String, Double> averageSalaryByGender = employeePayrollService.readAverageSalaryByGender(DB_IO);
        Assert.assertTrue(averageSalaryByGender.get("M").equals(2000000.00) &&
                averageSalaryByGender.get("F").equals(3000000.00));
    }
}
