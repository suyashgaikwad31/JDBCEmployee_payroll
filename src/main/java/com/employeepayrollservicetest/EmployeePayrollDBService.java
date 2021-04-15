package com.employeepayrollservicetest;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeePayrollDBService {
    private PreparedStatement employeePayrollDataStatement;
    private static EmployeePayrollDBService employeePayrollDBService;
    private PreparedStatement updateEmployeeSalary;
    private PreparedStatement employeeSalary;
    private PreparedStatement prepareStatement;
    private String sql;


    public static EmployeePayrollDBService getInstance() {
        if (employeePayrollDBService == null)
            employeePayrollDBService = new EmployeePayrollDBService();
        return employeePayrollDBService;
    }

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String username = "root";
        String password = "Suy31@ash";
        Connection connection;
        System.out.println("Connecting to database:" + jdbcURL);
        connection = DriverManager.getConnection(jdbcURL, username, password);
        System.out.println("Connection is successful!" + connection);
        return connection;
    }

    public List<EmployeePayrollData> readData() {
        String sql = "SELECT * FROM employee_payroll; ";
        List<EmployeePayrollData> employeePayrollDataList = new ArrayList<>();
        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                double salary = result.getDouble("salary");
                LocalDate startDate = result.getDate("start").toLocalDate();
                employeePayrollDataList.add(new EmployeePayrollData(id, name, salary, startDate));
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeePayrollDataList;
    }

    int updateEmployeeData(String name, Double salary) {
        return this.updateEmployeeDataUsingStatement(name, salary);
    }

    private int updateEmployeeDataUsingStatement(String name, Double salary) {
        String sql = String.format("update employee_payroll set salary = %.2f where name = '%s';", salary, name);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<EmployeePayrollData> getEmployeePayrollData(String name) {
        List<EmployeePayrollData> employeePayrollList = null;
        if (this.employeePayrollDataStatement == null)
            employeePayrollDataStatement = this.prepareStatementForEmployeeData(sql);
        try {
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private PreparedStatement prepareStatementForEmployeeData(String sql) {
        try {
            Connection connection = this.getConnection();
            prepareStatement = connection.prepareStatement(sql);
            return prepareStatement;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int updateEmployeeDataUsingPreparedStatement(String name, double salary) {
        List<EmployeePayrollData> employeePayrollList = null;
        if (this.updateEmployeeSalary == null)
            this.prepareStatementForToUpdateSalary();
        try {
            updateEmployeeSalary.setString(2, name);
            updateEmployeeSalary.setDouble(1, salary);
            return updateEmployeeSalary.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void prepareStatementForToUpdateSalary() {
        try {
            Connection connection = this.getConnection();
            String sql = "update employee_payroll set salary = ? where name = ?";
            updateEmployeeSalary = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<EmployeePayrollData> getSalary(String name, double salary) {
        List<EmployeePayrollData> employeePayrollList = null;
        String sql = "SELECT * FROM employee_payroll WHERE name = ? AND salary = ?";
        if (this.employeeSalary == null)
            employeeSalary = this.prepareStatementForEmployeeData(sql);
        try {
            employeeSalary.setString(1, name);
            employeeSalary.setDouble(2, salary);
            ResultSet resultSet = employeeSalary.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
            return employeePayrollList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<EmployeePayrollData> getDataForDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = String.format("SELECT * FROM employee_payroll WHERE START BETWEEN '%s' AND '%s';", Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getDataUsingDB(sql);
    }

    private List<EmployeePayrollData> getDataUsingDB(String sql) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    public Map<String, Double> getSalaryByGender() {
        String sql = "SELECT gender, AVG(salary) as avg_salary FROM employee_payroll GROUP BY gender;";
        Map<String, Double> genderSalaryMap = new HashMap<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("salary");
                genderSalaryMap.put(gender, salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genderSalaryMap;
    }

}
