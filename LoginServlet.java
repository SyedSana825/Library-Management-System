package com.librarymanagement;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");

        // Get login details from the form
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        PrintWriter out = response.getWriter();

        try {
            // Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarymanage", "root", "0825");

            // SQL query to validate login
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            // Check if user exists
            if (rs.next()) {
            	HttpSession session = request.getSession();
                session.setAttribute("user_id", rs.getInt("user_id"));
                session.setAttribute("username", rs.getString("username"));
                session.setAttribute("email", rs.getString("email"));
                
                
                // Redirect to user dashboard (or admin dashboard based on role)
                String role = rs.getString("role"); // Assuming there's a 'role' column in your database

                if (role.equals("admin")) {
                    response.sendRedirect("admindashboard.html");
                } else {
                    response.sendRedirect("userdashboard.html");
                }
            } else {
            	out.println("<p style='color:red;'>Invalid username or password</p>");
                RequestDispatcher rd = request.getRequestDispatcher("login.html");
                rd.include(request, response);
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("An error occurred. Please try again later.");
        }
    }
}