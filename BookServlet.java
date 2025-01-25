package com.librarymanagement;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@SuppressWarnings("serial")
@WebServlet("/BookServlet")
public class BookServlet extends HttpServlet {
	

	String url = "jdbc:mysql://localhost:3306/librarymanage";
    String user = "root";
    String pass = "0825";
    String query = "SELECT * FROM Books WHERE title LIKE ?";  
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String searchTitle = request.getParameter("title"); // Get search parameter from the HTML form
        
        
        try {
        	Connection con = DriverManager.getConnection(url,user,pass);
            PreparedStatement ps;
            
            if (searchTitle != null && !searchTitle.isEmpty()) {
                // Search query based on book title
                ps = con.prepareStatement(query);
                ps.setString(1, "%" + searchTitle + "%");
            } else {
                // Fetch all books if no search parameter is provided
                ps = con.prepareStatement("SELECT * FROM Books");
            }
            
            ResultSet rs = ps.executeQuery();
            
            out.println("<html><head><title>Book List</title></head><body>");
            out.println("<h2>Book Records</h2>");
            out.println("<table border='1'>");
            out.println("<tr><th>Book ID</th><th>Title</th><th>Author</th><th>Category</th><th>Availability</th><th>number of books</tr>");
            
            while (rs.next()) {
                out.println("<tr >");
                out.println("<td>" + rs.getInt("book_id") + "</td>");
                out.println("<td>" + rs.getString("title") + "</td>");
                out.println("<td>" + rs.getString("author") + "</td>");
                out.println("<td>" + rs.getString("category") + "</td>");
                out.println("<td>" + rs.getString("availability") + "</td>");
                out.println("<td>"+ rs.getInt("num_books")+"</td>");
                out.println("</tr>");
            }
            
            out.println("</table>");
            out.println("<br><a href='search.html'>Search Again</a>");
            out.println("<br><a href='admindashboard.html'>Back to Admin Dashboard</a>");
            out.println("<br><a href='userdashboard.html'>Back to User Dashboard</a>");
            out.println("</body></html>");
            out.println("</table>");
           
            
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error occurred while fetching book records. Please try again later.</h3>");
        }
    }
}