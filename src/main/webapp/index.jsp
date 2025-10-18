<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="beans.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Railway Reservation System</title>
    <link rel="stylesheet" href="style.css">
    <script src="script.js"></script>
    <style>
        /* Add styles for the dropdown */
        .dropdown {
            position: relative;
            display: inline-block;
        }

        .dropdown-content {
            display: none;
            position: absolute;
            background-color: var(--white);
            min-width: 160px;
            box-shadow: var(--shadow);
            z-index: var(--z-20);
        }

        .dropdown-content a {
            color: var(--gray-900);
            padding: var(--space-2) var(--space-4);
            text-decoration: none;
            display: block;
        }

        .dropdown-content a:hover {
            background-color: var(--gray-100);
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
            background-color: var(--gray-300); /* Placeholder color */
            text-align: center;
            line-height: 40px; /* Center the text vertically */
            color: var(--white);
            font-weight: bold;
        }

        /* Styles for cookie consent dialog */
        #cookieConsent {
            position: fixed;
            bottom: 20px;
            left: 20px;
            background-color: #fff;
            border: 1px solid #ccc;
            padding: 15px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            display: none; /* Hidden by default */
            z-index: 1000;
        }

        #cookieConsent button {
            margin: 5px;
        }
    </style>
</head>
<body>
    <% 
        User user = (User ) session.getAttribute("user");
        boolean isLoggedIn = (user != null);
    %>
    
    <header>
        <div class="container">
            <h1>Railway Reservation System</h1>
            <nav>
                <ul>
                    <li><a href="index.jsp" class="active">Home</a></li>
                    <li><a href="trains.jsp">Train Schedule</a></li>
                    <% if (isLoggedIn) { %>
                        <li><a href="bookTicket.jsp">Book Ticket</a></li>
                        <li><a href="myTicket">My Tickets</a></li>
                        <li class="dropdown">
                            <div class="user-avatar"><%= user.getName().charAt(0) %></div>
                            <div class="dropdown-content">
                                <a href="logout">Logout</a>
                            </div>
                        </li>
                    <% } else { %>
                        <li><a href="login.jsp">Login</a></li>
                        <li><a href="signup.jsp">Sign Up</a></li>
                    <% } %>
                </ul>
            </nav>
        </div>
    </header>
    
    <main>
        <section class="hero">
            <div class="container">
                <h2>Welcome to Railway Reservation System</h2>
                <p>Book your train tickets easily and efficiently</p>
                <div class="cta-buttons">
                    <% if (isLoggedIn) { %>
                        <a href="bookTicket" class="btn btn-primary">Book Ticket</a>
                        <a href="myTicket" class="btn btn-secondary">View My Tickets</a>
                    <% } else { %>
                        <a href="login.jsp" class="btn btn-primary">Login</a>
                        <a href="signup.jsp" class="btn btn-secondary">Sign Up</a>
                    <% } %>
                </div>
            </div>
        </section>
        
        <section class="features">
            <div class="container">
                <h2>Our Features</h2>
                <div class="feature-grid">
                     <div class="feature-card">
                        <div class="feature-icon">🚆</div>
                        <h3>Easy Booking</h3>
                        <p>Book tickets in just a few clicks with our streamlined process.</p>
                    </div>
                    <div class="feature-card">
                        <div class="feature-icon">📅</div>
                        <h3>Live Schedules</h3>
                        <p>Access real-time train schedules and plan your journey accordingly.</p>
                    </div>
                    <div class="feature-card">
                        <div class="feature-icon">🎫</div>
                        <h3>Manage Tickets</h3>
                        <p>View and manage your booked tickets from your personalized dashboard.</p>
                    </div>
                    <div class="feature-card">
                        <div class="feature-icon">👤</div>
                        <h3>User Profiles</h3>
                        <p>Create your profile to save preferences and speed up future bookings.</p>
                    </div>
                </div>
            </div>
        </section>
        
        <section class="quick-search">
            <div class="container">
                <h2>Quick Train Search</h2>
                <form action="trains" method="get">
                    <div class="form-group">
                        <label for="source">From</label>
                        <input type="text" id="source" name="source" placeholder="Enter source station">
                    </div>
                    <div class="form-group">
                        <label for="destination">To</label>
                        <input type="text" id="destination" name="destination" placeholder="Enter destination station">
                    </div>
                    <button type="submit" class="btn btn-primary">Search Trains</button>
                </form>
            </div>
        </section>
    </main>
    
    <footer>
        <div class="container">
            <p>&copy; 2023 Railway Reservation System. All rights reserved.</p>
        </div>
    </footer>

    <!-- Cookie Consent Dialog -->
    <div id="cookieConsent">
        <p>This website uses cookies to ensure you get the best experience on our website. 
        <a href="#">Learn more</a></p>
        <button id="acceptCookies">Accept All</button>
        <button id="declineCookies">Decline</button>
        <button id="remindLater">Remind Me Later</button>
    </div>

    <script>
        // Check if cookies have been accepted
        if (!document.cookie.split('; ').find(row => row.startsWith('cookieConsent='))) {
            document.getElementById('cookieConsent').style.display = 'block';
        }

        document.getElementById('acceptCookies').onclick = function() {
            document.cookie = "cookieConsent=accepted; max-age=" + 60;
            document.getElementById('cookieConsent').style.display = 'none';
        };

        document.getElementById('declineCookies').onclick = function() {
            document.cookie = "cookieConsent=declined; max-age=" + 60;
            document.getElementById('cookieConsent').style.display = 'none';
        };

        document.getElementById('remindLater').onclick = function() {
            document.getElementById('cookieConsent').style.display = 'none';
        };
    </script>
</body>
</html>