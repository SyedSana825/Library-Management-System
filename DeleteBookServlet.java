package com.librarymanagement;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DeleteBookServlet")
public class DeleteBookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    String url = "jdbc:mysql://localhost:3306/librarymanage";
    String user = "root";
    String pass = "0825";
    
    
    // Handle POST request to delete the book from the database
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int bookId = Integer.parseInt(request.getParameter("book_id"));

        try (Connection conn = DriverManager.getConnection(url,user,pass)) {
            String query = "DELETE FROM Books WHERE book_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, bookId);

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                response.sendRedirect("admindashboard.html");
            } else {
                response.getWriter().println("Error deleting book.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error deleting book.");
        }
    }
}