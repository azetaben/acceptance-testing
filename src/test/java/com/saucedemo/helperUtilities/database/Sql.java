package com.saucedemo.helperUtilities.database;

import java.sql.Connection;
import java.sql.SQLException;

public class Sql {

    private Connection conn;

    @SuppressWarnings("unused")
    private int results;

    public Sql(Connection conn) {
        this.conn = conn;
    }

    public void update(String query) {
        try {
            this.conn.prepareStatement(query).executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void select(String query) {
        try {
            this.results = this.conn.prepareStatement(query).executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
