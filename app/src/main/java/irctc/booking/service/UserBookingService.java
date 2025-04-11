package irctc.booking.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import irctc.booking.entities.Train;
import irctc.booking.entities.User;
import irctc.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserBookingService {

    private User user;
    private List<User> userList;
    private static final String USERS_FILE_PATH = "app/src/main/java/irctc/booking/localDb/users.json";
    private ObjectMapper objectMapper = new ObjectMapper();
    private TrainService trainService;

    public UserBookingService() throws IOException {
        // The constructor call will load all the user data from file to the object 'userlist'.
        // This process is called deserialization.
        loadUsers();
        trainService = new TrainService();
    }

    public UserBookingService(User user) throws IOException {
        this.user = user;
        loadUsers();
        trainService = new TrainService();
    }

    public void loadUsers() throws IOException {
        File users = new File(USERS_FILE_PATH);
        userList = objectMapper.readValue(users, new TypeReference<List<User>>() {
        });
    }

    public Boolean loginUser() {
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equalsIgnoreCase(user.getName())
                    &&
                    UserServiceUtil.checkPassword(user.getPassword(), user.getHashedPassword());
        }).findFirst();
        System.out.println("Login Successful");
        return foundUser.isPresent();
    }

    public Boolean signUp(User userToSignUp) {
        try {
            userList.add(userToSignUp);
            saveUserListToFile(userList);
            System.out.println("Sign Up Successful.");
        } catch (IOException e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private void saveUserListToFile(List<User> userList) throws IOException {
        File users = new File(USERS_FILE_PATH);
        objectMapper.writeValue(users, userList);
    }

    public void fetchBooking() {
        user.printTickets();
    }

    // User can cancel the booking
    public Boolean cancelBooking(String ticketId) {
        // Get the ticket with the given ticketId

        Scanner s = new Scanner(System.in);
        System.out.println("Enter ticket ID to cancel: ");
        ticketId = s.next();

        if (ticketId == null || ticketId.isEmpty()) {
            System.out.println("Ticket ID cannot be null or empty");
            return Boolean.FALSE;
        }

        String finalTicketID1 = ticketId; // Becoz strings are immutable
        boolean removed = user.getTicketsBooked().removeIf(ticket -> ticket.getTicketId().equals(finalTicketID1));
        return Boolean.FALSE;
    }

    public List<Train> getTrains(String source, String dest) throws IOException {
        try {
            return trainService.searchTrains(source, dest);
        } catch (IOException ex) {
            System.out.println("Exception occured while searching trains. So returning an empty list");
            return new ArrayList<>();
        }
    }

    public List<List<Integer>> fetchSeats(Train train) {
        return train.getSeats();
    }

    public boolean bookTrainTicket(Train train, int row, int seat) {
        List<List<Integer>> seats = train.getSeats();
        if (row >= 0 && row < seats.size() &&
                seat >= 0 && seat < seats.get(row).size()) {
            seats.get(row).set(seat, 1);
            train.setSeats(seats);
            trainService.addTrain(train);
            return true;
        } else {
            return false;
        }
    }

}
