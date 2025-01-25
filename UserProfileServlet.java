package com.librarymanagement;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@SuppressWarnings("serial")
@WebServlet("/UserProfileServlet")
public class UserProfileServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Fetch user details from session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            out.println("<p style='color:red;'>Session expired. Please login again.</p>");
            response.sendRedirect("login.html");
            return;
        }

        int userId = (int) session.getAttribute("user_id");
        String username = (String) session.getAttribute("username");
        String email = (String) session.getAttribute("email");

        // Display user details
        out.println("<h2>User Profile</h2>");
        out.println("<table border='1'>");
        out.println("<tr><th>User ID</th><td>" + userId + "</td></tr>");
        out.println("<tr><th>Username</th><td>" + username + "</td></tr>");
        out.println("<tr><th>Email</th><td>" + email + "</td></tr>");
        out.println("</table>");
    
        out.println("<a href='userdashboard.html'>Back to User Dashboard</a>");
    
    }
    
    
}