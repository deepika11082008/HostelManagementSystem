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

@WebServlet("/RoomServlet")
public class RoomServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // DISPLAY ROOMS
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        String sql = "SELECT * FROM ROOMS ORDER BY ROOM_NO";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            out.println("<table>");

            out.println("<tr>");
            out.println("<th>Room No</th>");
            out.println("<th>Floor</th>");
            out.println("<th>Capacity</th>");
            out.println("<th>Occupied</th>");
            out.println("<th>Status</th>");
            out.println("</tr>");

            while (rs.next()) {

                out.println("<tr>");

                out.println("<td>" +
                        rs.getString("ROOM_NO") +
                        "</td>");

                out.println("<td>" +
                        rs.getInt("FLOOR_NO") +
                        "</td>");

                out.println("<td>" +
                        rs.getInt("CAPACITY") +
                        "</td>");

                out.println("<td>" +
                        rs.getInt("OCCUPIED") +
                        "</td>");

                out.println("<td>" +
                        rs.getString("STATUS") +
                        "</td>");

                out.println("</tr>");
            }

            out.println("</table>");

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<h2>Database Error</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
        }
    }


    // ADD ROOM
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String roomNo = request.getParameter("roomNo");
        String floorNo = request.getParameter("floorNo");
        String capacity = request.getParameter("capacity");
        String occupied = request.getParameter("occupied");

        int cap = Integer.parseInt(capacity);
        int occ = Integer.parseInt(occupied);

        String status;

        if (occ >= cap) {
            status = "Full";
        } else if (occ == 0) {
            status = "Available";
        } else {
            status = "Occupied";
        }

        String sql =
            "INSERT INTO ROOMS " +
            "(ROOM_NO, FLOOR_NO, CAPACITY, OCCUPIED, STATUS) " +
            "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, roomNo);
            ps.setInt(2, Integer.parseInt(floorNo));
            ps.setInt(3, cap);
            ps.setInt(4, occ);
            ps.setString(5, status);

            ps.executeUpdate();

            response.sendRedirect("rooms.html");

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html");

            PrintWriter out = response.getWriter();

            out.println("<h2>Room could not be added</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
        }
    }
}