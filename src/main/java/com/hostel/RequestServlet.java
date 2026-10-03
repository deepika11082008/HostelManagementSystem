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

@WebServlet("/RequestServlet")
public class RequestServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // DISPLAY REQUESTS
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        String sql =
            "SELECT * FROM HOSTEL_REQUESTS " +
            "ORDER BY REQUEST_ID";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            out.println("<table>");

            out.println("<tr>");
            out.println("<th>Request ID</th>");
            out.println("<th>Register No</th>");
            out.println("<th>Request Type</th>");
            out.println("<th>Request Date</th>");
            out.println("<th>Status</th>");
            out.println("</tr>");

            while (rs.next()) {

                out.println("<tr>");

                out.println("<td>" +
                        rs.getInt("REQUEST_ID") +
                        "</td>");

                out.println("<td>" +
                        rs.getInt("REGISTER_NO") +
                        "</td>");

                out.println("<td>" +
                        rs.getString("REQUEST_TYPE") +
                        "</td>");

                out.println("<td>" +
                        rs.getDate("REQUEST_DATE") +
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


    // ADD REQUEST
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String requestId = request.getParameter("requestId");
        String registerNo = request.getParameter("registerNo");
        String requestType = request.getParameter("requestType");

        String sql =
            "INSERT INTO HOSTEL_REQUESTS " +
            "(REQUEST_ID, REGISTER_NO, REQUEST_TYPE, STATUS) " +
            "VALUES (?, ?, ?, 'Pending')";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(requestId));
            ps.setInt(2, Integer.parseInt(registerNo));
            ps.setString(3, requestType);

            ps.executeUpdate();

            response.sendRedirect("requests.html");

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html");

            PrintWriter out = response.getWriter();

            out.println("<h2>Request could not be added</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
        }
    }
}