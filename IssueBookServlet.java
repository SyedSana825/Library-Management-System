package com.librarymanagement;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings("serial")
@WebServlet("/IssueBookServlet")
public class IssueBookServlet extends HttpServlet {

    String url = "jdbc:mysql://localhost:3306/librarymanage";
    String user = "root";
    String pass = "0825";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Get parameters from the form
        String userId = request.getParameter("user_id");
        String bookId = request.getParameter("book_id");
        String issueDate = request.getParameter("issue_date");
        String returnDate = request.getParameter("return_date");

        try {
            // Database connection
            Connection con = DriverManager.getConnection(url, user, pass);

            // Check book availability
            String query = "SELECT availability, num_books FROM Books WHERE book_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, bookId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String availabilityStatus = rs.getString("availability"); // "Available" or "Not Available"
                int numBooks = rs.getInt("num_books"); // Numeric value of books available

                if (availabilityStatus.equalsIgnoreCase("Available") && numBooks > 0) {
                    numBooks--; // Reduce the number of books available by 1

                    // Update book availability
                    String updateQuery = "UPDATE Books SET num_books = ?, availability = ? WHERE book_id = ?";
                    PreparedStatement updatePs = con.prepareStatement(updateQuery);

                    String newAvailabilityStatus = (numBooks > 0) ? "Available" : "Not Available";
                    updatePs.setInt(1, numBooks);
                    updatePs.setString(2, newAvailabilityStatus);
                    updatePs.setString(3, bookId);

                    updatePs.executeUpdate();

                    // Insert issued book details
                    String insertQuery = "INSERT INTO IssuedBooks (user_id, book_id, issue_date, return_date) VALUES (?, ?, ?, ?)";
                    PreparedStatement insertPs = con.prepareStatement(insertQuery);

                    insertPs.setString(1, userId);
                    insertPs.setString(2, bookId);
                    insertPs.setString(3, issueDate);
                    insertPs.setString(4, returnDate);

                    insertPs.executeUpdate();

                    out.println("<h3>Book issued successfully!</h3>");
                } else {
                    out.println("<h3>Sorry, the book is not available for issuing.</h3>");
                }
            } else {
                out.println("<h3>Error: Book not found in the database.</h3>");
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
        out.println("<a href=\"userdashboard.html\">Back to Dashboard</a>");
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try  {
        	Class.forName("com.mysql.cj.jdbc.Driver");
           Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarymanage", "root", "0825");
            String query = "SELECT ib.book_id, b.title, ib.issue_date, ib.return_date "
                         + "FROM issuedbooks ib JOIN books b ON ib.book_id = b.book_id";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            out.println("<table border='1'><tr><th>Book ID</th><th>Title</th><th>Issue Date</th><th>Return Date</th></tr>");
            while (rs.next()) {
                out.println("<tr><td>" + rs.getInt("book_id") + "</td>"
                          + "<td>" + rs.getString("title") + "</td>"
                          + "<td>" + rs.getDate("issue_date") + "</td>"
                          + "<td>" + rs.getDate("return_date") + "</td></tr>");
            }
            out.println("</table>");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}




 
        
        
        
        
        
       