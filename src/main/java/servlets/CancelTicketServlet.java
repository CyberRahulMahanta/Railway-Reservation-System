package servlets;

import dao.TicketDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/cancelTicket")
public class CancelTicketServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int ticketId = Integer.parseInt(request.getParameter("id"));
        TicketDAO ticketDAO = new TicketDAO();

        if (ticketDAO.cancelTicket(ticketId)) {
            request.setAttribute("success", "Ticket cancelled successfully.");
        } else {
            request.setAttribute("error", "Failed to cancel the ticket.");
        }

        request.getRequestDispatcher("myTicket").forward(request, response);
    }
}