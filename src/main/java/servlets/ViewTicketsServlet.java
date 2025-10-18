package servlets;

import java.io.IOException;
import java.util.List;
import beans.Ticket;
import beans.User;
import dao.TicketDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/myTicket")
public class ViewTicketsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        User user = (User ) session.getAttribute("user");
        TicketDAO ticketDAO = new TicketDAO();
        List<Ticket> tickets = null;
        
        try {
            tickets = ticketDAO.getTicketsByUserId(user.getId());
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading tickets. Please try again.");
        }
        
        // Handle success messages
        if (request.getParameter("success") != null) {
            request.setAttribute("success", "Ticket booked successfully!");
        }
        
        request.setAttribute("tickets", tickets);
        request.getRequestDispatcher("/myTicket.jsp").forward(request, response);
    }
}