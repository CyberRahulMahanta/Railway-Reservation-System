package dao;

import beans.Passenger;
import beans.Ticket;
import beans.Train;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

    public int bookTicket(Ticket ticket, List<Passenger> passengers) {
        Connection connection = null;
        PreparedStatement ticketStatement = null;
        PreparedStatement passengerStatement = null;
        PreparedStatement updateSeatsStatement = null;
        ResultSet generatedKeys = null;
        int ticketId = -1;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            // 1. Insert ticket
            String ticketQuery = "INSERT INTO tickets (user_id, train_id, journey_date, status, total_fare) VALUES (?, ?, ?, ?, ?)";
            ticketStatement = connection.prepareStatement(ticketQuery, Statement.RETURN_GENERATED_KEYS);
            ticketStatement.setInt(1, ticket.getUserId());
            ticketStatement.setInt(2, ticket.getTrainId());
            ticketStatement.setDate(3, new java.sql.Date(ticket.getJourneyDate().getTime()));
            ticketStatement.setString(4, ticket.getStatus());
            ticketStatement.setDouble(5, ticket.getTotalFare());

            if (ticketStatement.executeUpdate() == 0) {
                throw new SQLException("Creating ticket failed");
            }

            // 2. Get generated ticket ID
            try (ResultSet keys = ticketStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    ticketId = keys.getInt(1);
                } else {
                    throw new SQLException("No ticket ID generated");
                }
            }

            // 3. Insert passengers
            passengerStatement = connection.prepareStatement(
                "INSERT INTO passengers (ticket_id, name, age, gender, seat_preference) VALUES (?, ?, ?, ?, ?)"
            );
            for (Passenger passenger : passengers) {
                passengerStatement.setInt(1, ticketId);
                passengerStatement.setString(2, passenger.getName());
                passengerStatement.setInt(3, passenger.getAge());
                passengerStatement.setString(4, passenger.getGender());
                passengerStatement.setString(5, passenger.getSeatPreference());
                passengerStatement.addBatch();
            }
            passengerStatement.executeBatch();

            // 4. Update available seats
            updateSeatsStatement = connection.prepareStatement(
                "UPDATE trains SET available_seats = available_seats - ? WHERE id = ?"
            );
            updateSeatsStatement.setInt(1, passengers.size());
            updateSeatsStatement.setInt(2, ticket.getTrainId());
            if (updateSeatsStatement.executeUpdate() != 1) {
                throw new SQLException("Seat update failed");
            }

            connection.commit();
            return ticketId;

        } catch (SQLException | ClassNotFoundException e) {
            rollback(connection);
            e.printStackTrace();
            return -1;
        } finally {
            closeResources(connection, ticketStatement, passengerStatement, updateSeatsStatement, generatedKeys);
        }
    }

    private void closeResources(Connection connection, PreparedStatement ticketStatement,
			PreparedStatement passengerStatement, PreparedStatement updateSeatsStatement, ResultSet generatedKeys) {
		// TODO Auto-generated method stub
		
	}

	public List<Ticket> getTicketsByUserId(int userId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Ticket> tickets = new ArrayList<>();

        try {
            connection = DBConnection.getConnection();
            String query = 
                "SELECT t.id AS ticket_id, t.user_id, t.train_id, t.journey_date, " +
                "t.booking_date, t.status, t.total_fare, " +
                "tr.id AS train_id, tr.train_number, tr.train_name, " +
                "tr.source_station, tr.destination_station, " +
                "tr.departure_time, tr.arrival_time, " +
                "tr.available_seats, tr.fare " +
                "FROM tickets t " +
                "JOIN trains tr ON t.train_id = tr.id " +
                "WHERE t.user_id = ? ORDER BY t.booking_date DESC";

            statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            resultSet = statement.executeQuery();

            System.out.println("[DEBUG] Fetching tickets for user: " + userId);

            while (resultSet.next()) {
                Ticket ticket = mapTicketFromResultSet(resultSet);
                ticket.setPassengers(getPassengersByTicketId(ticket.getId())); // Ensure passengers are set
                tickets.add(ticket);
                System.out.println("[DEBUG] Found ticket ID: " + ticket.getId());
            }
            return tickets;

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("[ERROR] Failed to fetch tickets for user: " + userId);
            e.printStackTrace();
            return null;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    private Ticket mapTicketFromResultSet(ResultSet rs) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(rs.getInt("ticket_id"));
        ticket.setUserId(rs.getInt("user_id"));
        ticket.setTrainId(rs.getInt("train_id"));
        ticket.setJourneyDate(rs.getDate("journey_date"));
        ticket.setBookingDate(rs.getTimestamp("booking_date"));
        ticket.setStatus(rs.getString("status"));
        ticket.setTotalFare(rs.getDouble("total_fare"));

        Train train = new Train();
        train.setId(rs.getInt("train_id"));  // Use aliased column
        train.setTrainNumber(rs.getString("train_number"));
        train.setTrainName(rs.getString("train_name"));
        train.setSourceStation(rs.getString("source_station"));
        train.setDestinationStation(rs.getString("destination_station"));
        train.setDepartureTime(rs.getTime("departure_time"));
        train.setArrivalTime(rs.getTime("arrival_time"));
        train.setAvailableSeats(rs.getInt("available_seats"));
        train.setFare(rs.getDouble("fare"));
        
        ticket.setTrain(train);
        return ticket;
    }

    public List<Passenger> getPassengersByTicketId(int ticketId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Passenger> passengers = new ArrayList<>(); // Initialize to an empty list

        try {
            connection = DBConnection.getConnection();
            statement = connection.prepareStatement(
                "SELECT * FROM passengers WHERE ticket_id = ?"
            );
            statement.setInt(1, ticketId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Passenger passenger = new Passenger();
                passenger.setId(resultSet.getInt("id"));
                passenger.setTicketId(ticketId);
                passenger.setName(resultSet.getString("name"));
                passenger.setAge(resultSet.getInt("age"));
                passenger.setGender(resultSet.getString("gender"));
                passenger.setSeatPreference(resultSet.getString("seat_preference"));
                passenger.setSeatNumber(resultSet.getString("seat_number"));
                passengers.add(passenger);
            }
            return passengers; // Return the list (could be empty)

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("[ERROR] Failed to fetch passengers for ticket: " + ticketId);
            e.printStackTrace();
            return new ArrayList<>(); // Return empty list instead of null
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    public boolean cancelTicket(int ticketId) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBConnection.getConnection();
            String query = "UPDATE tickets SET status = 'cancelled' WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, ticketId);
            return statement.executeUpdate() > 0; // Return true if the ticket was updated

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false; // An error occurred
        } finally {
            closeResources(connection, statement, null);
        }
    }

    public boolean removeTicket(int ticketId) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBConnection.getConnection();
            String query = "DELETE FROM tickets WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, ticketId);
            return statement.executeUpdate() > 0; // Return true if a ticket was deleted

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false; // An error occurred
        } finally {
            closeResources(connection, statement, null);
        }
    }

    private void rollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeResources(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}