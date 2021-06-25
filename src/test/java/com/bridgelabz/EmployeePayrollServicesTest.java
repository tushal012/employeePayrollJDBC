package com.bridgelabz;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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
       List<EmployeePayrollData> employeePayrollData =  employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
       Assert.assertEquals(3, employeePayrollData.size());
    }

    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDatabase() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData =  employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.updateEmployeeSalary("Terisa",3000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assert.assertTrue(result);

    }
}
