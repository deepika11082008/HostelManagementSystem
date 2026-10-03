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

@WebServlet("/StudentServlet")
public class StudentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // DISPLAY STUDENTS
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        String sql = "SELECT * FROM STUDENTS ORDER BY REGISTER_NO";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            out.println("<html><body>");
            out.println("<h1>Student Records</h1>");

            out.println("<table border='1'>");

            out.println("<tr>");
            out.println("<th>Register No</th>");
            out.println("<th>Name</th>");
            out.println("<th>Department</th>");
            out.println("<th>Room No</th>");
            out.println("<th>Phone</th>");
            out.println("<th>Status</th>");
            out.println("</tr>");

            while (rs.next()) {

                out.println("<tr>");

                out.println("<td>" +
                        rs.getInt("REGISTER_NO") +
                        "</td>");

                out.println("<td>" +
                        rs.getString("NAME") +
                        "</td>");

                out.println("<td>" +
                        rs.getString("DEPARTMENT") +
                        "</td>");

                out.println("<td>" +
                        rs.getString("ROOM_NO") +
                        "</td>");

                out.println("<td>" +
                        rs.getString("PHONE") +
                        "</td>");

                out.println("<td>" +
                        rs.getString("STATUS") +
                        "</td>");

                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body></html>");

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<h2>Database Error</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
        }
    }

    // ADD STUDENT
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
    	
    	System.out.println("StudentServlet POST called");

        request.setCharacterEncoding("UTF-8");

        String registerNo = request.getParameter("registerNo");
        String name = request.getParameter("name");
        String department = request.getParameter("department");
        String roomNo = request.getParameter("roomNo");
        String phone = request.getParameter("phone");

        String sql =
            "INSERT INTO STUDENTS " +
            "(REGISTER_NO, NAME, DEPARTMENT, ROOM_NO, PHONE, STATUS) " +
            "VALUES (?, ?, ?, ?, ?, 'Active')";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(registerNo));
            ps.setString(2, name);
            ps.setString(3, department);
            ps.setString(4, roomNo);
            ps.setString(5, phone);

            ps.executeUpdate();

            response.sendRedirect("StudentServlet");

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html");

            PrintWriter out = response.getWriter();

            out.println("<h2>Student could not be added</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
        }
    }
}