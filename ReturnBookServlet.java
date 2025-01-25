package com.librarymanagement;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ReturnBookServlet")
public class ReturnBookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    String url = "jdbc:mysql://localhost:3306/librarymanage";
    String user = "root";
    String pass = "0825";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Get form data
        String userId = request.getParameter("user_id");
        String bookId = request.getParameter("book_id");
        @SuppressWarnings("unused")
		String returnDate = request.getParameter("return_date");

        try {
            // Database connection
            Connection con = DriverManager.getConnection(url, user, pass);

            // Debugging: Print received parameters
            System.out.println("User ID: " + userId);
            System.out.println("Book ID: " + bookId);

            // Check if the book is currently issued
            String query = "SELECT * FROM IssuedBooks WHERE user_id = ? AND book_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userId);
            ps.setString(2, bookId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Debugging: Confirm the record exists
                System.out.println("Record found in IssuedBooks table.");

                // Book is issued, process the return
                String deleteQuery = "DELETE FROM IssuedBooks WHERE user_id = ? AND book_id = ?";
                PreparedStatement deletePs = con.prepareStatement(deleteQuery);
                deletePs.setString(1, userId);
                deletePs.setString(2, bookId);

                int rowsDeleted = deletePs.executeUpdate();

                // Update book availability
                if (rowsDeleted > 0) {
                    String updateQuery = "UPDATE Books SET num_books = num_books + 1, availability = 'Available' WHERE book_id = ?";
                    PreparedStatement updatePs = con.prepareStatement(updateQuery);
                    updatePs.setString(1, bookId);

                    int rowsUpdated = updatePs.executeUpdate();

                    if (rowsUpdated > 0) {
                        out.println("<h3>Book returned successfully!</h3>");
                        out.println("<br><a href='userdashboard.html'>Back to User Dashboard</a>");
                    } else {
                        out.println("<h3>Error: Could not update book availability.</h3>");
                    }
                } else {
                    out.println("<h3>Error: Could not process the return.</h3>");
                }
            } else {
                // Debugging: Record not found
                System.out.println("No matching record found in IssuedBooks table.");
                out.println("<h3>Error: Could not find the issued book record. Please check your details.</h3>");
            }

            // Close the connection
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}