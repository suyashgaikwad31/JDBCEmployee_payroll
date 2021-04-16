package com.employeepayrollservicetest;

import java.time.LocalDate;

import java.util.List;
import java.util.Map;

public class EmployeePayrollService {
    public void updateEmployeeSalaryWithPreparedStatement(String name, double salary) {
        int result = employeePayrollDBService.updateEmployeeDataUsingPreparedStatement(name, salary);
        if (result == 0) return;
        EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
        if (employeePayrollData != null)
            employeePayrollData.salary = salary;
    }

    public List<EmployeePayrollData> getEmployeeSalary(String name, double salary) {
        List<EmployeePayrollData> employeePayrollData = employeePayrollDBService.getSalary(name, salary);
        return employeePayrollData;
    }

    public List<EmployeePayrollData> readDataForDateRange(LocalDate startDate, LocalDate endDate) {
        return employeePayrollDBService.getDataForDateRange(startDate, endDate);
    }

    public Map<String, Double> SalaryByGender() {
        return employeePayrollDBService.getSalaryByGender();
    }

    public void addEmployeeToPayroll(String name, double salary, LocalDate startDate, String gender) {
        employeePayrollList.add(employeePayrollDBService.addEmployeePayroll(name, salary, startDate, gender));
    }


    public enum IOService {CONSOLE_IO, FILE_IO, DB_IO, REST_IO}
    private List<EmployeePayrollData> employeePayrollList;
    private EmployeePayrollDBService employeePayrollDBService;

    public EmployeePayrollService() {
        employeePayrollDBService = EmployeePayrollDBService.getInstance();
    }

    public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
        this();
        this.employeePayrollList = employeePayrollList;
    }

    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollList = employeePayrollDBService.readData();
        return this.employeePayrollList;
    }

    public boolean checkEmployeePayrollInSyncWithDB(String name) {
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
        return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
    }

    public void updateEmployeeSalary(String name, Double salary) {
        int result = employeePayrollDBService.updateEmployeeData(name, salary);
        if (result == 0) return;
        EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
        if (employeePayrollData != null)
            EmployeePayrollData.salary = salary;
    }

    private EmployeePayrollData getEmployeePayrollData(String name) {
        return this.employeePayrollList.stream().filter(employeePayrollDataItem -> employeePayrollDataItem.name.equals(name)).findFirst().orElse(null);
    }


}
