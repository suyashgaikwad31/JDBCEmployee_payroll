package com.employeepayrollservicetest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public class EmployeePayrollServiceTest {
    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Assertions.assertEquals(4, employeePayrollData.size());
    }

    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
       employeePayrollService.updateEmployeeSalary("Terisa", 3000000.0);
       boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
       Assertions.assertTrue(result);
    }

    @Test
    public void givenNewSalaryForEmp_WhenUpdated_ShouldSyncWithDB() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.updateEmployeeSalaryWithPreparedStatement("Terisa", 3000000.0);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assertions.assertTrue(result);
    }
    @Test
    public void givenEmployeeName_WhenRetrieveSalary_ShouldMatch() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        List<EmployeePayrollData> employeePayrollList = employeePayrollService.getEmployeeSalary("Bill", 3000000.0);
        Assertions.assertEquals(3000000.0, employeePayrollData.get(0).salary);
    }
    @Test
    public void givenEmployeeRange_WhenRetrievedData_ShouldReturnEmployeeCount()  {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        LocalDate startDate = LocalDate.of(2018, 01, 01);
        LocalDate endDate = LocalDate.now();
        List<EmployeePayrollData> employeePayrollList = employeePayrollService.readDataForDateRange(startDate, endDate);
        Assertions.assertEquals(4, employeePayrollList.size());
    }
    @Test
    public void givenEmployeePayrollData_WhenSalaryChangeByGender_ShouldReturnValue() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Map<String, Double> SalaryByGender = employeePayrollService.SalaryByGender();
        Assertions.assertTrue(SalaryByGender.get("M").equals(2000000.0) && SalaryByGender.get("F").equals(3000000.0));
    }
}
