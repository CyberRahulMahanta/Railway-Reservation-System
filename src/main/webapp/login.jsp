<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Railway Reservation System</title>
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
                    <li><a href="login.jsp" class="active">Login</a></li>
                    <li><a href="signup.jsp">Sign Up</a></li>
                </ul>
            </nav>
        </div>
    </header>
    
    <main>
        <section class="form-section">
            <div class="container">
                <div class="form-container">
                    <h2>Login to Your Account</h2>
                    
                    <% if (request.getAttribute("error") != null) { %>
                        <div class="error-message">
                            <%= request.getAttribute("error") %>
                        </div>
                    <% } %>
                    
                    <form action="login" method="post">
                        <div class="form-group">
                            <label for="email">Email</label>
                            <input type="email" id="email" name="email" required>
                        </div>
                        <div class="form-group">
                            <label for="password">Password</label>
                            <input type="password" id="password" name="password" required>
                        </div>
                        <button type="submit" class="btn btn-primary btn-block">Login</button>
                    </form>
                    
                    <div class="form-footer">
                        <p>Don't have an account? <a href="signup">Sign Up</a></p>
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