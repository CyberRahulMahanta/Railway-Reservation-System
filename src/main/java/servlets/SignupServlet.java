package servlets;

import beans.User;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        
        if (name == null || email == null || password == null || phone == null || address == null ||
            name.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty() || 
            phone.trim().isEmpty() || address.trim().isEmpty()) {
            
            request.setAttribute("error", "All fields are required");
            request.getRequestDispatcher("/signup.jsp").forward(request, response);
            return;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            request.setAttribute("error", "Invalid email format.");
            request.getRequestDispatcher("/signup.jsp").forward(request, response);
            return;
        }
        
        UserDAO userDAO = new UserDAO();
        User existingUser  = userDAO.getUserByEmail(email);
        
        if (existingUser  != null) {
            request.setAttribute("error", "Email already registered. Please login.");
            request.getRequestDispatcher("/signup.jsp").forward(request, response);
            return;
        }
        
        User newUser  = new User();
        newUser .setName(name);
        newUser .setEmail(email);
        newUser .setPassword(password);
        newUser .setPhone(phone);
        newUser .setAddress(address);
        
        boolean success = userDAO.createUser (newUser );
        
        if (success) {
            User createdUser  = userDAO.getUserByEmail(email);
            HttpSession session = request.getSession();
            session.setAttribute("user", createdUser );
            response.sendRedirect(request.getContextPath() + "/index.jsp?message=Account created successfully");
        } else {
            request.setAttribute("error", "Failed to create account. Please try again.");
            request.getRequestDispatcher("/signup.jsp").forward(request, response);
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        
        request.getRequestDispatcher("/signup.jsp").forward(request, response);
    }
}