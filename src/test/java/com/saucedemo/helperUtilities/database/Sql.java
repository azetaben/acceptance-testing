package com.saucedemo.helperutilities.database;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;


public class Sql {
    private static final Logger log = LogManager.getLogger(Sql.class);


    private final Connection conn;

    @SuppressWarnings("unused")
    private int results;

    public Sql(Connection conn) {
        this.conn = conn;
    }

    public void update(String query) {
        try {
            this.conn.prepareStatement(query).executeUpdate();
        } catch (SQLException e) {
            log.info(String.valueOf(e.getMessage()));
        }
    }

    public void select(String query) {
        try {
            this.results = this.conn.prepareStatement(query).executeUpdate();
        } catch (SQLException e) {
            log.info(String.valueOf(e.getMessage()));
        }
    }
}
