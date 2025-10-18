package dao;

import beans.Train;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrainDAO {
    
    // Get all trains
    public List<Train> getAllTrains() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Train> trains = new ArrayList<>();
        
        try {
            connection = DBConnection.getConnection();
            String query = "SELECT * FROM trains";
            statement = connection.createStatement();
            
            resultSet = statement.executeQuery(query);
            
            while (resultSet.next()) {
                Train train = new Train();
                train.setId(resultSet.getInt("id"));
                train.setTrainNumber(resultSet.getString("train_number"));
                train.setTrainName(resultSet.getString("train_name"));
                train.setSourceStation(resultSet.getString("source_station"));
                train.setDestinationStation(resultSet.getString("destination_station"));
                train.setDepartureTime(resultSet.getTime("departure_time"));
                train.setArrivalTime(resultSet.getTime("arrival_time"));
                train.setDuration(resultSet.getString("duration"));
                train.setDistance(resultSet.getString("distance"));
                train.setAvailableSeats(resultSet.getInt("available_seats"));
                train.setFare(resultSet.getDouble("fare"));
                
                trains.add(train);
            }
            
            return trains;
            
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                DBConnection.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Search trains by source and destination
    public List<Train> searchTrains(String source, String destination) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Train> trains = new ArrayList<>();
        
        try {
            connection = DBConnection.getConnection();
            String query = "SELECT * FROM trains WHERE source_station LIKE ? AND destination_station LIKE ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, "%" + source + "%");
            statement.setString(2, "%" + destination + "%");
            
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Train train = new Train();
                train.setId(resultSet.getInt("id"));
                train.setTrainNumber(resultSet.getString("train_number"));
                train.setTrainName(resultSet.getString("train_name"));
                train.setSourceStation(resultSet.getString("source_station"));
                train.setDestinationStation(resultSet.getString("destination_station"));
                train.setDepartureTime(resultSet.getTime("departure_time"));
                train.setArrivalTime(resultSet.getTime("arrival_time"));
                train.setDuration(resultSet.getString("duration"));
                train.setDistance(resultSet.getString("distance"));
                train.setAvailableSeats(resultSet.getInt("available_seats"));
                train.setFare(resultSet.getDouble("fare"));
                
                trains.add(train);
            }
            
            return trains;
            
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                DBConnection.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Get train by id
    public Train getTrainById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Train train = null;
        
        try {
            connection = DBConnection.getConnection();
            String query = "SELECT * FROM trains WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                train = new Train();
                train.setId(resultSet.getInt("id"));
                train.setTrainNumber(resultSet.getString("train_number"));
                train.setTrainName(resultSet.getString("train_name"));
                train.setSourceStation(resultSet.getString("source_station"));
                train.setDestinationStation(resultSet.getString("destination_station"));
                train.setDepartureTime(resultSet.getTime("departure_time"));
                train.setArrivalTime(resultSet.getTime("arrival_time"));
                train.setDuration(resultSet.getString("duration"));
                train.setDistance(resultSet.getString("distance"));
                train.setAvailableSeats(resultSet.getInt("available_seats"));
                train.setFare(resultSet.getDouble("fare"));
            }
            
            return train;
            
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                DBConnection.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Update available seats
    public boolean updateAvailableSeats(int trainId, int seatsToReduce) {
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DBConnection.getConnection();
            String query = "UPDATE trains SET available_seats = available_seats - ? WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, seatsToReduce);
            statement.setInt(2, trainId);
            
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
            
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (statement != null) statement.close();
                DBConnection.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Increase available seats (for ticket cancellation)
    public boolean increaseAvailableSeats(int trainId, int seatsToAdd) {
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DBConnection.getConnection();
            String query = "UPDATE trains SET available_seats = available_seats + ? WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, seatsToAdd);
            statement.setInt(2, trainId);
            
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
            
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (statement != null) statement.close();
                DBConnection.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}