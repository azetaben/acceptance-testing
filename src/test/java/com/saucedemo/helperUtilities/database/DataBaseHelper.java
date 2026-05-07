package com.saucedemo.helperutilities.database;

import com.saucedemo.steps.AccessibilitySteps;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.*;

public class DataBaseHelper {
    private static final Logger log = LogManager.getLogger(DataBaseHelper.class);

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
        log.info(String.valueOf("Executing query: " + dbQuery));
        try {
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(dbQuery);
            return result;
        } catch (SQLException e) {
            log.warn("Exception: " + e.getMessage(), e);
        }
        return null;
    }

    private Connection getSingleInstanceConnection() {
        try {
            Class.forName(driverName);
            try {
                connection = DriverManager.getConnection(url, userName, password);
                if (connection != null) {
                    log.info(String.valueOf("Connected to testData.data base.."));
                }
            } catch (SQLException e) {

            }
        } catch (ClassNotFoundException e) {
            log.info(String.valueOf("Driver not found.." + e));
        }
        return connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
