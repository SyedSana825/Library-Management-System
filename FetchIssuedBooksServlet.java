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

@WebServlet("/FetchIssuedBooksServlet")
public class FetchIssuedBooksServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    String url = "jdbc:mysql://localhost:3306/librarymanage";
    String user = "root";
    String pass = "0825";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String userId = request.getParameter("user_id"); // Get user_id from the form

        if (userId == null || userId.isEmpty()) {
            out.println("<h3>Error: User ID is required.</h3>");
            return;
        }

        try {
            // Connect to the database
            Connection con = DriverManager.getConnection(url, user, pass);

            // SQL query to fetch issued books for the user
            String query = "SELECT ib.book_id, b.title, ib.issue_date, ib.return_date "
                         + "FROM IssuedBooks ib "
                         + "JOIN Books b ON ib.book_id = b.book_id "
                         + "WHERE ib.user_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userId);

            ResultSet rs = ps.executeQuery();

            // HTML table for displaying issued books
            out.println("<table border='1'>");
            out.println("<tr><th>Book ID</th><th>Title</th><th>Issue Date</th><th>Return Date</th></tr>");

            boolean hasResults = false;

            while (rs.next()) {
                hasResults = true;
                out.println("<tr>");
                out.println("<td>" + rs.getInt("book_id") + "</td>");
                out.println("<td>" + rs.getString("title") + "</td>");
                out.println("<td>" + rs.getDate("issue_date") + "</td>");
                out.println("<td>" + rs.getDate("return_date") + "</td>");
                out.println("</tr>");
            }

            if (!hasResults) {
                out.println("<tr><td colspan='4'>No books issued yet.</td></tr>");
               
            }

            out.println("</table>");
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
        out.println("<a href=\"userdashboard.html\">Back to Dashboard</a>");
    }
}
