package com.hostel;

import java.sql.Connection;

public class DBTest {

    public static void main(String[] args) {

        Connection con = DBConnection.getConnection();

        if (con != null) {
            System.out.println("TEST SUCCESSFUL!");
        } else {
            System.out.println("TEST FAILED!");
        }
    }
}
