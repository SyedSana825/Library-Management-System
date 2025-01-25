package com.librarymanagement;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings("serial")
@WebServlet("/SearchBooksServlet")
public class SearchBooksServlet extends HttpServlet {
	
	 String url ="jdbc:mysql://localhost:3306/librarymanage";
     String user = "root";
     String pass = "0825";
     String query = "SELECT * FROM Books WHERE title LIKE ? OR author LIKE ?";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String search = request.getParameter("search");

        try {
            // Connect to the database
        	Connection con = DriverManager.getConnection(url,user,pass);

            // Search query
            PreparedStatement ps = con.prepareStatement(query);
                    
            ps.setString(1, "%" + search + "%");
            ps.setString(2, "%" + search + "%");

            ResultSet rs = ps.executeQuery();

            out.println("<h2>Search Results</h2>");
            out.println("<table border='1'>");
            out.println("<tr><th>Book ID</th><th>Title</th><th>Author</th><th>Category</th><th>Availability</th><th>Number of Books</td></tr>");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("book_id") + "</td>");
                out.println("<td>" + rs.getString("title") + "</td>");
                out.println("<td>" + rs.getString("author") + "</td>");
                out.println("<td>" + rs.getString("category") + "</td>");
                out.println("<td>" + rs.getString("availability")  + "</td>");
                out.println("<td>" + rs.getInt("num_books") + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
        out.println("<a href='userdashboard.html'>issue books</a>");
    }
}