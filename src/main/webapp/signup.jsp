<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign Up - Railway Reservation System</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <header>
        <div class="container">
            <h1>Railway Reservation System</h1>
            <nav>
                <ul>
                    <li><a href="index.jsp">Home</a></li>
                    <li><a href="trains.jsp">Train Schedule</a></li>
                    <li><a href="login.jsp">Login</a></li>
                    <li><a href="signup.jsp" class="active">Sign Up</a></li>
                </ul>
            </nav>
        </div>
    </header>
    
    <main>
        <section class="form-section">
            <div class="container">
                <div class="form-container">
                    <h2>Create an Account</h2>
                    
                    <% if (request.getAttribute("error") != null) { %>
                        <div class="error-message">
                            <%= request.getAttribute("error") %>
                        </div>
                    <% } %>
                    
                    <form action="signup" method="post">
                        <div class="form-group">
                            <label for="name">Full Name</label>
                            <input type="text" id="name" name="name" required>
                        </div>
                        <div class="form-group">
                            <label for="email">Email</label>
                            <input type="email" id="email" name="email" required>
                        </div>
                        <div class="form-group">
                            <label for="password">Password</label>
                            <input type="password" id="password" name="password" required>
                        </div>
                        <div class="form-group">
                            <label for="phone">Phone Number</label>
                            <input type="tel" id="phone" name="phone" required>
                        </div>
                        <div class="form-group">
                            <label for="address">Address</label>
                            <textarea id="address" name="address" required></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary btn-block">Create Account</button>
                    </form>
                    
                    <div class="form-footer">
                        <p>Already have an account? <a href="login.jsp">Login</a></p>
                    </div>
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


