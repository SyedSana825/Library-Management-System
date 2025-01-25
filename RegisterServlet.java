package com.librarymanagement;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // Retrieve form parameters
   
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String role = request.getParameter("role");
        
        Connection con = null;
        PreparedStatement pst = null;
        
        try {
            // Load JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Connect to Database
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarymanage", "root", "0825");
            
            // Insert Query
            String sql = "INSERT INTO Users (username, password, email, role) VALUES (?, ?, ?, ?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, email);
            pst.setString(4, role);

            int rows = pst.executeUpdate();

            if (rows > 0) {
            	out.println("<html><body>");
            	out.println("<h2>Registration Successful!</h2>");
            	out.println("</body></html>");
            	response.sendRedirect("login.html");
            } else {
            	out.println("<html><body>");
            	out.println("<h2>Registration Filed</h2>");
            	out.println("</body></html>");
                
            }

        } catch (Exception e) {
            response.getWriter().println("Something went wrong: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
    