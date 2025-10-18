package beans;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class Ticket {
    private int id;
    private int userId;
    private int trainId;
    private Date journeyDate;
    private Timestamp bookingDate;
    private String status;
    private double totalFare;
    private List<Passenger> passengers; // List of passengers
    
    // For joining with train details
    private Train train;

    // Default constructor
    public Ticket() {
    }

    // Constructor with fields
    public Ticket(int id, int userId, int trainId, Date journeyDate, Timestamp bookingDate, String status, double totalFare) {
        this.id = id;
        this.userId = userId;
        this.trainId = trainId;
        this.journeyDate = journeyDate;
        this.bookingDate = bookingDate;
        this.status = status;
        this.totalFare = totalFare;
    }

    // Constructor without id and bookingDate (for creating new tickets)
    public Ticket(int userId, int trainId, Date journeyDate, String status, double totalFare) {
        this.userId = userId;
        this.trainId = trainId;
        this.journeyDate = journeyDate;
        this.status = status;
        this.totalFare = totalFare;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }

    public Date getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(Date journeyDate) {
        this.journeyDate = journeyDate;
    }

    public Timestamp getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Timestamp bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(double totalFare) {
        this.totalFare = totalFare;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", userId=" + userId +
                ", trainId=" + trainId +
                ", journeyDate=" + journeyDate +
                ", bookingDate=" + bookingDate +
                ", status='" + status + '\'' +
                ", totalFare=" + totalFare +
                '}';
    }
}