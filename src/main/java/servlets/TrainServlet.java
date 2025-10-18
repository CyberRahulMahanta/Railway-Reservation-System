package servlets;


import java.io.IOException;
import java.util.List;

import beans.Train;
import dao.TrainDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/trains")
public class TrainServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String source = request.getParameter("source");
        String destination = request.getParameter("destination");
        
        TrainDAO trainDAO = new TrainDAO();
        List<Train> trains;
        
        if (source != null && destination != null && !source.trim().isEmpty() && !destination.trim().isEmpty()) {
            // Search trains by source and destination
            trains = trainDAO.searchTrains(source, destination);
            request.setAttribute("source", source);
            request.setAttribute("destination", destination);
        } else {
            // Get all trains
            trains = trainDAO.getAllTrains();
        }
        
        request.setAttribute("trains", trains);
        request.getRequestDispatcher("/trains.jsp").forward(request, response);
    }
}