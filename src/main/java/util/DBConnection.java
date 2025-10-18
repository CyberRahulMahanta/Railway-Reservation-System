package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/railway_reservation";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Get database connection
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        // Load MySQL JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        // Create connection
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    // Close connection
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}