package com.hostel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:oracle:thin:@localhost:1521/XEPDB1";

    private static final String USER =
            "SYSTEM";

    // Enter your Oracle password between the quotes.
    private static final String PASSWORD =
            "NewPassword123";

    public static Connection getConnection() {

        Connection con = null;

        try {
            Class.forName("oracle.jdbc.OracleDriver");

            con = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

            System.out.println("Oracle Database Connected Successfully!");

        } catch (ClassNotFoundException e) {
            System.out.println("Oracle JDBC Driver not found!");
            e.printStackTrace();

        } catch (SQLException e) {
            System.out.println("Database Connection Failed!");
            e.printStackTrace();
        }

        return con;
    }
}