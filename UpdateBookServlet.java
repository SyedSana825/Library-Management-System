package com.librarymanagement;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("unused")
@WebServlet("/UpdateBookServlet")
public class UpdateBookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    String url = "jdbc:mysql://localhost:3306/librarymanage";
    String user = "root";
    String pass = "0825";

    

    // Handle POST request to update the book details in the database
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String category = request.getParameter("category");
        int availability = Integer.parseInt(request.getParameter("availability"));

        try (Connection conn = DriverManager.getConnection(url,user,pass)) {
            String query = "UPDATE Books SET title = ?, author = ?, category=?, availability = ? WHERE book_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setString(3, category);
            ps.setInt(4,availability);
            

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                response.sendRedirect("admindashboard.html");
            } else {
                response.getWriter().println("Error updating book.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error updating book.");
        }
    }
}