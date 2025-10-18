<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="beans.User" %>
<%@ page import="beans.Train" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Train Schedule - Railway Reservation System</title>
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
</head>
<body>
    <c:set var="user" value="${sessionScope.user}" />
    <c:set var="isLoggedIn" value="${not empty user}" />

    <header>
        <div class="container">
            <h1>Railway Reservation System</h1>
            <nav>
                <ul>
                    <li><a href="index.jsp">Home</a></li>
                    <li><a href="trains" class="active">Train Schedule</a></li>
                    <c:choose>
                        <c:when test="${isLoggedIn}">
                            <li><a href="bookTicket">Book Ticket</a></li>
                            <li><a href="myTicket">My Tickets</a></li>
                            <li class="dropdown">
                                <div class="user-avatar" onclick="toggleDropdown()">
                                    <c:out value="${user.name.charAt(0)}" /> <!-- Display first letter of username -->
                                </div>
                                <div class="dropdown-content" id="dropdownMenu">
                                    <a href="logout">Logout</a>
                                </div>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li><a href="login.jsp">Login</a></li>
                            <li><a href="signup.jsp">Sign Up</a></li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </nav>
        </div>
    </header>
    
    <main>
        <section class="search-section">
            <div class="container">
                <h2>Search Trains</h2>
                <form action="trains" method="get">
                    <div class="search-form">
                        <div class="form-group">
                            <label for="source">From</label>
                            <input type="text" id="source" name="source" placeholder="Enter source station" required>
                        </div>
                        <div class="form-group">
                            <label for="destination">To</label>
                            <input type="text" id="destination" name="destination" placeholder="Enter destination station" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Search</button>
                    </div>
                </form>
 </div>
        </section>
        
        <section class="trains-section">
            <div class="container">
                <h2>Available Trains</h2>
                <c:if test="${not empty trains}">
                    <div class="train-list">
                        <table>
                            <thead>
                                <tr>
                                    <th>Train Number</th>
                                    <th>Train Name</th>
                                    <th>From</th>
                                    <th>To</th>
                                    <th>Departure</th>
                                    <th>Arrival</th>
                                    <th>Duration</th>
                                    <th>Available Seats</th>
                                    <th>Fare (₹)</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="train" items="${trains}">
                                    <tr>
                                        <td>${train.trainNumber}</td>
                                        <td>${train.trainName}</td>
                                        <td>${train.sourceStation}</td>
                                        <td>${train.destinationStation}</td>
                                        <td>${train.departureTime}</td>
                                        <td>${train.arrivalTime}</td>
                                        <td>${train.duration}</td>
                                        <td>${train.availableSeats}</td>
                                        <td>${train.fare}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${isLoggedIn}">
                                                    <a href="bookTicket?trainId=${train.id}" class="btn btn-small">Book</a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="login" class="btn btn-small">Login to Book</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>
                <c:if test="${empty trains}">
                    <div class="no-trains">
                        <p>No trains found. Please try a different search.</p>
                    </div>
                </c:if>
            </div>
        </section>
    </main>
    
    <footer>
        <div class="container">
            <p>&copy; 2023 Railway Reservation System. All rights reserved.</p>
        </div>
    </footer>
    <script>
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
</body>
</html>