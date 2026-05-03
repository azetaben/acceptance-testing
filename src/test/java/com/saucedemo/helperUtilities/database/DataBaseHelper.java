package com.saucedemo.helperUtilities.database;

import java.sql.*;

public class DataBaseHelper {

    private static final String url = "jdbc:mysql://localhost/person";
    private static final String driverName = "com.mysql.jdbc.Driver";
    private static final String userName = "root";
    private static final String password = "password";
    private static Connection connection;
    private static DataBaseHelper instance = null;

    public DataBaseHelper() {
        connection = getSingleInstanceConnection();
    }

    public static DataBaseHelper getInstance() {
        if (instance == null) {
            instance = new DataBaseHelper();
        }
        return instance;
    }

    public static ResultSet getResultSet(String dbQuery) {
        instance = DataBaseHelper.getInstance();
        connection = instance.getConnection();
        System.out.println("Executing query: " + dbQuery);
        try {
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(dbQuery);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Connection getSingleInstanceConnection() {
        try {
            Class.forName(driverName);
            try {
                connection = DriverManager.getConnection(url, userName, password);
                if (connection != null) {
                    System.out.println("Connected to testData.data base..");
                }
            } catch (SQLException e) {
                // log.error("Failed to create Data base connection.." + e);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found.." + e);
        }
        return connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
