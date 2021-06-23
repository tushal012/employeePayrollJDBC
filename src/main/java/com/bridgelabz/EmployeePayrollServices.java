package com.bridgelabz;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

public class EmployeePayrollServices {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://Localhost:3306/payroll_service?useSSL=false";
        String userName = "root";
        String password = "Tush@1234";
        Connection connection;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("cannot find the driver in the classpath", e);
        }
        ListDrivers();
        try {
            System.out.println("Connecting to the database: " + jdbcURL);
            connection = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection is successful: " + connection);
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }


    private static void ListDrivers() {
        Enumeration<Driver> driverList = DriverManager.getDrivers();
        while(driverList.hasMoreElements()) {
            Driver driverClass =(Driver) driverList.nextElement();
            System.out.println(" "+driverClass.getClass().getName());
        }
    }
}
