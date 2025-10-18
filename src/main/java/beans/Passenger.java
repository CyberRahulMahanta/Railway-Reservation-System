package beans;

public class Passenger {
    private int id;
    private int ticketId;
    private String name;
    private int age;
    private String gender;
    private String seatPreference;
    private String seatNumber;

    // Default constructor
    public Passenger() {
    }

    // Constructor with fields
    public Passenger(int id, int ticketId, String name, int age, String gender, String seatPreference, String seatNumber) {
        this.id = id;
        this.ticketId = ticketId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.seatPreference = seatPreference;
        this.seatNumber = seatNumber;
    }

    // Constructor without id and ticketId (for creating new passengers)
    public Passenger(String name, int age, String gender, String seatPreference) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.seatPreference = seatPreference;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSeatPreference() {
        return seatPreference;
    }

    public void setSeatPreference(String seatPreference) {
        this.seatPreference = seatPreference;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "id=" + id +
                ", ticketId=" + ticketId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", seatPreference='" + seatPreference + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                '}';
    }
}