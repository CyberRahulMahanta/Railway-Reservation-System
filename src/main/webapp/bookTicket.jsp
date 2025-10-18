<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="beans.User" %>
<%@ page import="beans.Train" %>
<%@ page import="java.time.LocalDate" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book Ticket - Railway Reservation System</title>
    <link rel="stylesheet" href="style.css">
    <script src="script.js"></script>
    <script>
        function updatePassengerCount() {
            const count = document.getElementById('passengerCount').value;
            const container = document.getElementById('passengersContainer');
            
            // Clear existing passengers
            container.innerHTML = '';
            
            // Add passenger forms
            for (let i = 1; i <= count; i++) {
                const passengerHtml = `
                    <div class="passenger-form">
                        <h4>Passenger \${i}</h4>
                        <div class="form-group">
                            <label for="passenger\${i}Name">Full Name</label>
                            <input type="text" id="passenger\${i}Name" name="passenger\${i}Name" required>
                        </div>
                        <div class="form-group">
                            <label for="passenger\${i}Age">Age</label>
                            <input type="number" id="passenger\${i}Age" name="passenger\${i}Age" min="1" max="120" required>
                        </div>
                        <div class="form-group">
                            <label for="passenger\${i}Gender">Gender</label>
                            <select id="passenger\${i}Gender" name="passenger\${i}Gender" required>
                                <option value="">Select Gender</option>
                                <option value="male">Male</option>
                                <option value="female">Female</option>
                                <option value="other">Other</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="passenger\${i}SeatPreference">Seat Preference</label>
                            <select id="passenger\${i}SeatPreference" name="passenger\${i}SeatPreference" required>
                                <option value="">Select Preference</option>
                                <option value="window">Window</option>
                                <option value="aisle">Aisle</option>
                                <option value="middle">Middle</option>
                                <option value="any">Any</option>
                            </select>
                        </div>
                    </div>
                `;
                
                container.innerHTML += passengerHtml;
            }
            
            // Update total fare
            updateTotalFare();
        }
        
        function updateTotalFare() {
            const count = document.getElementById('passengerCount').value;
            const farePerPassenger = document.getElementById('farePerPassenger').value;
            const totalFare = count * farePerPassenger;
            
            document.getElementById('totalFareDisplay').textContent = '₹' + totalFare.toFixed(2);
        }
    </script>
</head>
<body>
    <% 
        User user = (User ) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        
        Train train = (Train) request.getAttribute("train");
        
        // Get today's date for min date in journey date picker
        LocalDate today = LocalDate.now();
        String minDate = today.toString();
        
        // Get date 3 months from now for max date
        LocalDate maxDateObj = today.plusMonths(3);
        String maxDate = maxDateObj.toString();
    %>
    
    <header>
        <div class="container">
            <h1>Railway Reservation System</h1>
            <nav>
                <ul>
                    <li><a href="index.jsp">Home</a></li>
                    <li><a href="trains">Train Schedule</a></li>
                    <li><a href="bookTicket" class="active">Book Ticket</a></li>
                    <li><a href="myTicket">My Tickets</a></li>
                    <li><a href="logout">Logout</a></li>
                </ul>
            </nav>
        </div>
    </header>
    
    <main>
        <section class="form-section">
            <div class="container">
                <div class="form-container booking-form">
                    <h2>Book Train Ticket</h2>
                    
                    <% if (request.getAttribute("error") != null) { %>
                        <div class="error-message">
                            <%= request.getAttribute("error") %>
                        </div>
                    <% } %>
                    
                    <% if (train != null) { %>
                        <div class="selected-train">
                            <h3>Selected Train</h3>
                            <div class="train-details">
                                <p><strong><%= train.getTrainName() %> (<%= train.getTrainNumber() %>)</strong></p>
                                <p><strong>From:</strong> <%= train.getSourceStation() %> | <strong>Departure:</strong> <%= train.getDepartureTime() %></p>
                                <p><strong>To:</strong> <%= train.getDestinationStation() %> | <strong>Arrival:</strong> <%= train.getArrivalTime() %></p>
                                <p><strong>Duration:</strong> <%= train.getDuration() %> | <strong>Distance:</strong> <%= train.getDistance() %></p>
                                <p><strong>Available Seats:</strong> <%= train.getAvailableSeats() %> | <strong>Fare:</strong> ₹<%= train.getFare() %> per passenger</p>
                            </div>
                        </div>
                        
                        <form action="bookTicket" method="post">
                            <input type="hidden" name="trainId" value="<%= train.getId() %>">
                            <input type="hidden" id="farePerPassenger" value="<%= train.getFare() %>">
                            
                            <div class="form-group">
                                <label for="journeyDate">Journey Date</label>
                                <input type="date" id="journeyDate" name="journeyDate" min="<%= minDate %>" max="<%= maxDate %>" required>
                            </div>
                            
                            <div class="form-group">
                                <label for="passengerCount">Number of Passengers</label>
                                <select id="passengerCount" name="passengerCount" onchange="updatePassengerCount()" required>
                                    <option value="">Select</option>
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                    <option value="5">5</option>
                                    <option value="6">6</option>
                                </select>
                            </div>
                            
                            <div id="passengersContainer">
                                <!-- Passenger forms will be added here dynamically -->
                            </div>
                            
                            <div class="fare-summary">
                                <p><strong>Total Fare:</strong> <span id="totalFareDisplay">₹0.00</span></p>
                            </div>
                            
                            <button type="submit" class="btn btn-primary btn-block">Book Ticket</button>
                        </form>
                    <% } else { %>
                        <div class="no-train-selected">
                            <p>No train selected. Please select a train from the <a href="trains">train schedule</a>.</p>
                        </div>
                    <% } %>
                </div>
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