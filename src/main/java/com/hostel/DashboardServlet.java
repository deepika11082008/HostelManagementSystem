package com.hostel;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        try (Connection con = DBConnection.getConnection()) {

            /* =====================================================
               STUDENT COUNT
               ===================================================== */

            int totalStudents = 0;

            String studentSQL =
                    "SELECT COUNT(*) FROM STUDENTS";

            try (PreparedStatement ps =
                         con.prepareStatement(studentSQL);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    totalStudents = rs.getInt(1);
                }
            }


            /* =====================================================
               ROOM STATISTICS
               ===================================================== */

            int totalRooms = 0;
            int occupiedRooms = 0;
            int availableRooms = 0;
            int maintenanceRooms = 0;

            int totalCapacity = 0;
            int totalOccupiedBeds = 0;


            String roomStatsSQL =
                    "SELECT " +
                    "COUNT(*) AS TOTAL_ROOMS, " +
                    "SUM(CASE " +
                    "    WHEN UPPER(STATUS) IN ('OCCUPIED','FULL') " +
                    "    THEN 1 ELSE 0 END) AS OCCUPIED_ROOMS, " +
                    "SUM(CASE " +
                    "    WHEN UPPER(STATUS) = 'AVAILABLE' " +
                    "    THEN 1 ELSE 0 END) AS AVAILABLE_ROOMS, " +
                    "SUM(CASE " +
                    "    WHEN UPPER(STATUS) = 'MAINTENANCE' " +
                    "    THEN 1 ELSE 0 END) AS MAINTENANCE_ROOMS, " +
                    "NVL(SUM(CAPACITY),0) AS TOTAL_CAPACITY, " +
                    "NVL(SUM(OCCUPIED),0) AS TOTAL_OCCUPIED " +
                    "FROM ROOMS";


            try (PreparedStatement ps =
                         con.prepareStatement(roomStatsSQL);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    totalRooms =
                            rs.getInt("TOTAL_ROOMS");

                    occupiedRooms =
                            rs.getInt("OCCUPIED_ROOMS");

                    availableRooms =
                            rs.getInt("AVAILABLE_ROOMS");

                    maintenanceRooms =
                            rs.getInt("MAINTENANCE_ROOMS");

                    totalCapacity =
                            rs.getInt("TOTAL_CAPACITY");

                    totalOccupiedBeds =
                            rs.getInt("TOTAL_OCCUPIED");
                }
            }


            /* =====================================================
               PENDING REQUESTS
               ===================================================== */

            int pendingRequests = 0;

            String requestSQL =
                    "SELECT COUNT(*) " +
                    "FROM HOSTEL_REQUESTS " +
                    "WHERE UPPER(STATUS) = 'PENDING'";


            try (PreparedStatement ps =
                         con.prepareStatement(requestSQL);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    pendingRequests = rs.getInt(1);
                }
            }


            /* =====================================================
               REAL OCCUPANCY PERCENTAGE
               Based on occupied beds / total beds
               ===================================================== */

            int occupancyPercent = 0;

            if (totalCapacity > 0) {

                occupancyPercent =
                        (totalOccupiedBeds * 100)
                        / totalCapacity;
            }


            /* =====================================================
               JSON START
               ===================================================== */

            out.println("{");


            /* STUDENTS */

            out.println(
                    "\"totalStudents\":" +
                    totalStudents + ","
            );


            /* ROOMS */

            out.println(
                    "\"totalRooms\":" +
                    totalRooms + ","
            );


            out.println(
                    "\"occupiedRooms\":" +
                    occupiedRooms + ","
            );


            out.println(
                    "\"availableRooms\":" +
                    availableRooms + ","
            );


            out.println(
                    "\"maintenanceRooms\":" +
                    maintenanceRooms + ","
            );


            /* REQUESTS */

            out.println(
                    "\"pendingRequests\":" +
                    pendingRequests + ","
            );


            /* OCCUPANCY */

            out.println(
                    "\"occupancyPercent\":" +
                    occupancyPercent + ","
            );


            /* TOTAL CAPACITY */

            out.println(
                    "\"totalCapacity\":" +
                    totalCapacity + ","
            );


            /* OCCUPIED BEDS */

            out.println(
                    "\"totalOccupiedBeds\":" +
                    totalOccupiedBeds + ","
            );


            /* =====================================================
               REAL ROOM RECORDS
               ===================================================== */

            out.println("\"rooms\":[");


            String roomsSQL =
                    "SELECT ROOM_NO, " +
                    "FLOOR_NO, " +
                    "CAPACITY, " +
                    "OCCUPIED, " +
                    "STATUS " +
                    "FROM ROOMS " +
                    "ORDER BY ROOM_NO";


            try (PreparedStatement ps =
                         con.prepareStatement(roomsSQL);
                 ResultSet rs = ps.executeQuery()) {

                boolean firstRoom = true;


                while (rs.next()) {

                    if (!firstRoom) {
                        out.println(",");
                    }

                    firstRoom = false;


                    out.print("{");


                    out.print(
                            "\"roomNo\":\"" +
                            escapeJson(
                                rs.getString("ROOM_NO")
                            ) +
                            "\","
                    );


                    out.print(
                            "\"floor\":" +
                            rs.getInt("FLOOR_NO") +
                            ","
                    );


                    out.print(
                            "\"capacity\":" +
                            rs.getInt("CAPACITY") +
                            ","
                    );


                    out.print(
                            "\"occupied\":" +
                            rs.getInt("OCCUPIED") +
                            ","
                    );


                    out.print(
                            "\"status\":\"" +
                            escapeJson(
                                rs.getString("STATUS")
                            ) +
                            "\""
                    );


                    out.print("}");
                }
            }


            out.println("],");


            /* =====================================================
               REAL RECENT REQUESTS
               ===================================================== */

            out.println("\"recentRequests\":[");


            String recentSQL =
                    "SELECT REQUEST_ID, " +
                    "REGISTER_NO, " +
                    "REQUEST_TYPE, " +
                    "REQUEST_DATE, " +
                    "STATUS " +
                    "FROM HOSTEL_REQUESTS " +
                    "ORDER BY REQUEST_DATE DESC, REQUEST_ID DESC " +
                    "FETCH FIRST 5 ROWS ONLY";


            try (PreparedStatement ps =
                         con.prepareStatement(recentSQL);
                 ResultSet rs = ps.executeQuery()) {

                boolean firstRequest = true;


                while (rs.next()) {

                    if (!firstRequest) {
                        out.println(",");
                    }

                    firstRequest = false;


                    out.print("{");


                    out.print(
                            "\"requestId\":" +
                            rs.getInt("REQUEST_ID") +
                            ","
                    );


                    out.print(
                            "\"registerNo\":" +
                            rs.getInt("REGISTER_NO") +
                            ","
                    );


                    out.print(
                            "\"type\":\"" +
                            escapeJson(
                                rs.getString("REQUEST_TYPE")
                            ) +
                            "\","
                    );


                    out.print(
                            "\"date\":\"" +
                            rs.getDate("REQUEST_DATE") +
                            "\","
                    );


                    out.print(
                            "\"status\":\"" +
                            escapeJson(
                                rs.getString("STATUS")
                            ) +
                            "\""
                    );


                    out.print("}");
                }
            }


            out.println("]");


            /* =====================================================
               JSON END
               ===================================================== */

            out.println("}");


        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );


            out.println("{");


            out.println(
                    "\"error\":\"" +
                    escapeJson(e.getMessage()) +
                    "\""
            );


            out.println("}");
        }
    }


    /* =========================================================
       JSON ESCAPE
       ========================================================= */

    private String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}