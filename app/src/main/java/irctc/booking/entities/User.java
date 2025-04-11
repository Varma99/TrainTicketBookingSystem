package irctc.booking.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class User {
    private String name;
    private String password;
    @JsonProperty("hashed_password")
    private String hashedPassword; // hashed_password
    @JsonProperty("tickets_booked")
    private List<Ticket> ticketsBooked;
    @JsonProperty("user_id")
    private String userId;

    public User() {

    }

    public User(String name, String password, String hashedPassword, List<Ticket> ticketsBooked,String userId) {
        this.name = name;
        this.password = password;
        this.hashedPassword = hashedPassword;
        this.ticketsBooked = ticketsBooked;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public List<Ticket> getTicketsBooked() {
        return ticketsBooked;
    }

    public void setTicketsBooked(List<Ticket> ticketsBooked) {
        this.ticketsBooked = ticketsBooked;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void printTickets() {
        for (int i = 0;i < ticketsBooked.size();i++) {
            System.out.println(ticketsBooked.get(i).getTicketInfo());
        }
    }
}
