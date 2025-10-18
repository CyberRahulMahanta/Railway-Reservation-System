<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="beans.User" %>
<%@ page import="beans.Ticket" %>
<%@ page import="beans.Passenger" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Tickets - Railway Reservation System</title>
    <link rel="stylesheet" href="style.css">
    <script src="script.js"></script>
    <style>
        /* Dropdown styles */
        .dropdown {
            position: relative;
            display: inline-block;
        }

        .dropdown-content {
            display: none;
            position: absolute;
            background-color: white;
            min-width: 160px;
            box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
            z-index: 1;
        }

        .dropdown-content a {
            color: black;
            padding: 12px 16px;
            text-decoration: none;
            display: block;
        }

        .dropdown-content a:hover {
            background-color: #f1f1f1;
        }

        .dropdown:hover .dropdown-content {
            display: block;
        }

        .user-avatar {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            cursor: pointer;
            display: inline-block;
            background-color: #ccc; /* Placeholder color */
            text-align: center;
            line-height: 40px; /* Center the text vertically */
            color: white;
            font-weight: bold;
        }
    </style>
    <script>
        function confirmCancel(ticketId) {
            if (confirm("Are you sure you want to cancel this ticket? A refund will be processed according to the cancellation policy.")) {
                window.location.href = "cancelTicket?id=" + ticketId;
            }
        }

        function confirmRemove(ticketId) {
            if (confirm("Are you sure you want to remove this ticket permanently?")) {
                window.location.href = "removeTicket?id=" + ticketId;
            }
        }

        function toggleDropdown() {
            document.getElementById("dropdownMenu").classList.toggle("show");
        }

        window.onclick = function(event) {
            if (!event.target.matches('.user-avatar')) {
                var dropdowns = document.getElementsByClassName("dropdown-content");
                for (var i = 0; i < dropdowns.length; i++) {
                    var openDropdown = dropdowns[i];
                    if (openDropdown.classList.contains('show')) {
                        openDropdown.classList.remove('show');
                    }
                }
            }
        }
    </script>
</head>
<body>
    <% 
        User user = (User ) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        List<Ticket> tickets = (List<Ticket>) request.getAttribute("tickets");
    %>
    
    <header>
        <div class="container">
            <h1>Railway Reservation System</h1>
            <nav>
                <ul>
                    <li><a href="index.jsp">Home</a></li>
                    <li><a href="trains">Train Schedule</a></li>
                    <li><a href="bookTicket">Book Ticket</a></li>
                    <li><a href="myTicket" class="active">My Tickets</a></li>
                    <li class="dropdown">
                        <div class="user-avatar" onclick="toggleDropdown()">
                            <%= user.getName().charAt(0) %> <!-- Display first letter of username -->
                        </div>
                        <div class="dropdown-content" id="dropdownMenu">
                            <a href="logout">Logout</a>
                        </div>
                    </li>
                </ul>
            </nav>
        </div>
    </header>
    
    <main>
        <section class="tickets-section">
            <div class="container">
                <h2>My Tickets</h2>
                
                <% if (request.getAttribute("success") != null) { %>
                    <div class="success-message">
                        <%= request.getAttribute("success") %>
                    </div>
                <% } %>
                
                <% if (request .getAttribute("error") != null) { %>
                    <div class="error-message">
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>
                
                <% if (tickets != null && !tickets.isEmpty()) { %>
                    <div class="ticket-list">
                        <% for (Ticket ticket : tickets) { %>
                            <div class="ticket-card">
                                <div class="ticket-header">
                                    <h3>Ticket #<%= ticket.getId() %></h3>
                                    <span class="ticket-status <%= ticket.getStatus() %>"><%= ticket.getStatus().toUpperCase() %></span>
                                </div>
                                
                                <% if (ticket.getTrain() != null) { %>
                                    <div class="train-details">
                                        <h4><%= ticket.getTrain().getTrainName() %> (<%= ticket.getTrain().getTrainNumber() %>)</h4>
                                        <div class="journey-details">
                                            <div class="journey-from">
                                                <p class="station"><%= ticket.getTrain().getSourceStation() %></p>
                                                <p class="time"><%= ticket.getTrain().getDepartureTime() %></p>
                                            </div>
                                            <div class="journey-arrow">→</div>
                                            <div class="journey-to">
                                                <p class="station"><%= ticket.getTrain().getDestinationStation() %></p>
                                                <p class="time"><%= ticket.getTrain().getArrivalTime() %></p>
                                            </div>
                                        </div>
                                        <p class="journey-date"><strong>Journey Date:</strong> <%= ticket.getJourneyDate() %></p>
                                    </div>
                                <% } %>
                                
                                <div class="passenger-details">
                                    <h4>Passenger Details</h4>
                                    <table>
                                        <thead>
                                            <tr>
                                                <th>Name</th>
                                                <th>Age</th>
                                                <th>Gender</th>
                                                <th>Seat Preference</th>
                                                <th>Seat Number</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% if (ticket.getPassengers() != null) { %>
                                                <% for (Passenger passenger : ticket.getPassengers()) { %>
                                                    <tr>
                                                        <td><%= passenger.getName() %></td>
                                                        <td><%= passenger.getAge() %></td>
                                                        <td><%= passenger.getGender().substring(0, 1).toUpperCase() + passenger.getGender().substring(1) %></td>
                                                        <td><%= passenger.getSeatPreference().substring(0, 1).toUpperCase() + passenger.getSeatPreference().substring(1) %></td>
                                                        <td><%= passenger.getSeatNumber() != null ? passenger.getSeatNumber() : "Not assigned" %></td>
                                                    </tr>
                                                <% } %>
                                            <% } %>
                                        </tbody>
                                    </table>
                                </div>
                                
                                <div class="ticket-footer">
                                    <div class="booking-info">
                                        <p><strong>Booking Date:</strong> <%= ticket.getBookingDate() %></p>
                                        <p><strong>Total Fare:</strong> ₹<%= ticket.getTotalFare() %></p>
                                    </div>
                                    
                                    <% if ("confirmed".equals(ticket.getStatus())) { %>
                                        <button class="btn btn-danger" onclick="confirmCancel(<%= ticket.getId() %>)">Cancel Ticket</button>
                                    <% } %>
                                    
                                    <% if ("cancelled".equals(ticket.getStatus())) { %>
                                        <button class="btn btn-danger" onclick="confirmRemove(<%= ticket.getId() %>)">Remove Ticket</button>
                                    <% } %>
                                </div>
                            </div>
                        <% } %>
                    </div>
                <% } else { %>
                    <div class="no-tickets">
                        <p>You haven't booked any tickets yet.</p>
                        <a href="trains" class="btn btn-primary">Book a Ticket</a>
                    </div>
                <% } %>
            </div>
        </section>
    </main>
    
    <footer>
        <div class="container">
            <p>&copy; 2023 Railway Reservation System. All rights reserved.</p>
        </div>
    </footer>
</body>
</html>