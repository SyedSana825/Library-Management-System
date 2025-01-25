package com.librarymanagement;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // Retrieve form parameters
        
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String category = request.getParameter("category");
        String availability = request.getParameter("availability");
        String numBooksStr = request.getParameter("num_books");
        int numBooks = Integer.parseInt(numBooksStr); 
        
        Connection con = null;
        PreparedStatement pst = null;
        
        try {
            // Load JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Connect to Database
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarymanage", "root", "0825");
            
            // Insert Query
            String sql = "INSERT INTO Books ( title, author, category, availability,num_books) VALUES ( ?, ?, ?, ?,?)";
            pst = con.prepareStatement(sql);
            
            // Set query parameters
            
            pst.setString(1, title);
            pst.setString(2, author);
            pst.setString(3, category);
            pst.setString(4, availability);
            pst.setInt(5, numBooks);

            
            // Execute the update
            int rows = pst.executeUpdate();
            
            if (rows > 0) {
                out.println("<h3>Book added successfully!</h3>");
                out.println("<a href='admindashboard.html'>Back to Admin Dashboard</a>");
            } else {
                out.println("<h3>Failed to add book. Please try again.</h3>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Something went wrong: " + e.getMessage() + "</h3>");
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}