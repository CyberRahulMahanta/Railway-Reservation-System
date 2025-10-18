package servlets;


import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import beans.Passenger;
import beans.Ticket;
import beans.Train;
import beans.User;
import dao.TicketDAO;
import dao.TrainDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/bookTicket")
public class BookTicketServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get train id from request
        String trainId = request.getParameter("trainId");
        
        if (trainId != null && !trainId.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(trainId);
                TrainDAO trainDAO = new TrainDAO();
                Train train = trainDAO.getTrainById(id);
                
                if (train != null) {
                    request.setAttribute("train", train);
                }
            } catch (NumberFormatException e) {
                // Invalid train id, ignore
            }
        }
        
        request.getRequestDispatcher("/bookTicket.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        // Get form data
        String trainIdStr = request.getParameter("trainId");
        String journeyDateStr = request.getParameter("journeyDate");
        String passengerCountStr = request.getParameter("passengerCount");
        
        // Validate input
        if (trainIdStr == null || journeyDateStr == null || passengerCountStr == null ||
            trainIdStr.trim().isEmpty() || journeyDateStr.trim().isEmpty() || passengerCountStr.trim().isEmpty()) {
            
            request.setAttribute("error", "All fields are required");
            request.getRequestDispatcher("/bookTicket.jsp").forward(request, response);
            return;
        }
        
        try {
            int trainId = Integer.parseInt(trainIdStr);
            Date journeyDate = Date.valueOf(journeyDateStr);
            int passengerCount = Integer.parseInt(passengerCountStr);
            
            if (passengerCount <= 0 || passengerCount > 6) {
                request.setAttribute("error", "Passenger count must be between 1 and 6");
                request.getRequestDispatcher("/bookTicket.jsp").forward(request, response);
                return;
            }
            
            // Get train details
            TrainDAO trainDAO = new TrainDAO();
            Train train = trainDAO.getTrainById(trainId);
            
            if (train == null) {
                request.setAttribute("error", "Invalid train selected");
                request.getRequestDispatcher("/bookTicket.jsp").forward(request, response);
                return;
            }
            
            if (train.getAvailableSeats() < passengerCount) {
                request.setAttribute("error", "Not enough seats available");
                request.getRequestDispatcher("/bookTicket.jsp").forward(request, response);
                return;
            }
            
            // Create passenger list
            List<Passenger> passengers = new ArrayList<>();
            
            for (int i = 1; i <= passengerCount; i++) {
                String name = request.getParameter("passenger" + i + "Name");
                String ageStr = request.getParameter("passenger" + i + "Age");
                String gender = request.getParameter("passenger" + i + "Gender");
                String seatPreference = request.getParameter("passenger" + i + "SeatPreference");
                
                if (name == null || ageStr == null || gender == null || seatPreference == null ||
                    name.trim().isEmpty() || ageStr.trim().isEmpty()) {
                    
                    request.setAttribute("error", "All passenger details are required");
                    request.getRequestDispatcher("/bookTicket.jsp").forward(request, response);
                    return;
                }
                
                try {
                    int age = Integer.parseInt(ageStr);
                    
                    if (age <= 0 || age > 120) {
                        request.setAttribute("error", "Age must be between 1 and 120");
                        request.getRequestDispatcher("/bookTicket.jsp").forward(request, response);
                        return;
                    }
                    
                    Passenger passenger = new Passenger(name, age, gender, seatPreference);
                    passengers.add(passenger);
                    
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid age");
                    request.getRequestDispatcher("/bookTicket.jsp").forward(request, response);
                    return;
                }
            }
            
            // Calculate total fare
            double totalFare = train.getFare() * passengerCount;
            
            // Create ticket
            Ticket ticket = new Ticket(user.getId(), trainId, journeyDate, "confirmed", totalFare);
            
            // Book ticket
            TicketDAO ticketDAO = new TicketDAO();
            int ticketId = ticketDAO.bookTicket(ticket, passengers);
            
            if (ticketId > 0) {
                // Redirect to view tickets page
                response.sendRedirect(request.getContextPath() + "/myTicket?success=true");
            } else {
                request.setAttribute("error", "Failed to book ticket. Please try again.");
                request.getRequestDispatcher("/bookTicket.jsp").forward(request, response);
            }
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid input data");
            request.getRequestDispatcher("/bookTicket.jsp").forward(request, response);
        }
    }
}