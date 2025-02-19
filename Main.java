 import java.util.*;

class User {
    String username, password;
    boolean isAdmin;
    List<Booking> bookings = new ArrayList<>();

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public void cancelBooking(Booking booking) {
        bookings.remove(booking);
    }

    public List<Booking> getBookings() {
        return bookings;
    }
}

class Movie {
    String title, genre, language, timing;
    int duration;
    double price;

    public Movie(String title, String genre, String language, int duration, double price, String timing) {
        this.title = title;
        this.genre = genre;
        this.language = language;
        this.duration = duration;
        this.price = price;
        this.timing = timing;
    }

    public String toString() {
        return title + " (" + genre + ", " + language + ", " + duration + " mins, Price: ₹" + price + ", Time: " + timing + ")";
    }
}

class Booking {
    User user;
    Movie movie;
    Theater theater;
    int row, col;

    public Booking(User user, Movie movie, Theater theater, int row, int col) {
        this.user = user;
        this.movie = movie;
        this.theater = theater;
        this.row = row;
        this.col = col;
    }

    public String toString() {
        return "Movie: " + movie.title + ", Theater: " + theater.name + ", Seat: " + (char) ('A' + row) + (col + 1);
    }
}

class Theater {
    String name;
    boolean[][] seats;

    public Theater(String name, int rows, int cols) {
        this.name = name;
        this.seats = new boolean[rows][cols];
    }

    public boolean bookSeat(int row, int col) {
        if (!seats[row][col]) {
            seats[row][col] = true;
            return true;
        }
        return false;
    }

    public void cancelSeat(int row, int col) {
        seats[row][col] = false;
    }

    public boolean isSeatAvailable(int row, int col) {
        return !seats[row][col];
    }

    public void displaySeats() {
        System.out.println("Seat Layout for " + name + ":");
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                System.out.print(seats[i][j] ? "[X] " : "[ ] ");
            }
            System.out.println();
        }
    }
}

class MovieTicketSystem {
    List<User> users = new ArrayList<>();
    List<Movie> movies = new ArrayList<>();
    List<Theater> theaters = new ArrayList<>();
    List<Booking> bookings = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);
    User loggedInUser = null;

    public void registerUser() {
        System.out.print("Enter username:");
        String username = scanner.nextLine();
        System.out.print("Enter password:");
        String password = scanner.nextLine();
        System.out.print("Are you an admin? (yes/no):");
        boolean isAdmin = scanner.nextLine().equalsIgnoreCase("yes");
        User newUser = new User(username, password, isAdmin);
        users.add(newUser);
        loggedInUser = newUser;
        System.out.println("User registered and logged in successfully!");
    }

    public void loginUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        for (User user : users) {
            if (user.username.equals(username) && user.password.equals(password)) {
                loggedInUser = user;
                System.out.println("Login successful!");
                return;
            }
        }
        System.out.println("Invalid credentials! Try again.");
    }

    public void logoutUser() {
        if (loggedInUser != null) {
            System.out.println("Logging out " + loggedInUser.username + "...");
            loggedInUser = null;
        } else {
            System.out.println("No user is logged in.");
        }
    }

    public void addMovie() {
        if (loggedInUser == null || !loggedInUser.isAdmin) {
            System.out.println("Only admins can add movies!");
            return;
        }
        System.out.print("Enter movie title: ");
        String title = scanner.nextLine();
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();
        System.out.print("Enter language: ");
        String language = scanner.nextLine();
        int duration = getIntInput("Enter duration (in minutes): ");
        double price = getDoubleInput("Enter ticket price : ");
        System.out.print("Enter movie timing (e.g., 10:00 AM, 3:30 PM): ");
        String timing = scanner.nextLine();

        movies.add(new Movie(title, genre, language, duration, price, timing));
        System.out.println("Movie added successfully!");
    }

    public void addTheater() {
        if (loggedInUser == null || !loggedInUser.isAdmin) {
            System.out.println("Only admins can add theaters!");
            return;
        }
        System.out.print("Enter theater name: ");
        String name = scanner.nextLine();
        int rows = getIntInput("Enter number of rows: ");
        int cols = getIntInput("Enter number of columns: ");
        theaters.add(new Theater(name, rows, cols));
        System.out.println("Theater added successfully!");
    }

    public void bookTicket() {
        if (loggedInUser == null) {
            System.out.println("Please login to book a ticket!");
            return;
        }
        if (movies.isEmpty() || theaters.isEmpty()) {
            System.out.println("No movies or theaters available!");
            return;
        }

        System.out.println("Available movies:");
        for (int i = 0; i < movies.size(); i++) {
            System.out.println((i + 1) + ". " + movies.get(i));
        }
        System.out.println("Select a movie:");
        int movieChoice = getValidIndex("Enter movie number: ", movies.size());
        Movie selectedMovie = movies.get(movieChoice - 1);

        System.out.println("Available theaters:");
        for (int i = 0; i < theaters.size(); i++) {
            System.out.println((i + 1) + ". " + theaters.get(i).name);
        }
        System.out.println("Select a theater:");
        int theaterChoice = getValidIndex("Enter theater number: ", theaters.size());
        Theater selectedTheater = theaters.get(theaterChoice - 1);

        selectedTheater.displaySeats();

        System.out.println("Enter row (A-H): ");
        char row = scanner.nextLine().toUpperCase().charAt(0);
        System.out.println("Enter column (1-6): ");
        int col = getIntInput("Enter column number: ");

        if (row < 'A' || row > 'H' || col < 1 || col > 6) {
            System.out.println("Invalid seat! Please select a valid seat.");
            return;
        }

        int rowIndex = row - 'A';
        int colIndex = col - 1;

        if (selectedTheater.bookSeat(rowIndex, colIndex)) {
            System.out.println("Seat " + row + "-" + col + " booked successfully!");
        } else {
            System.out.println("Seat " + row + "-" + col + " is already booked or invalid.");
        }

        System.out.println("Ticket price for " + selectedMovie.title + " is ₹" + selectedMovie.price);
        System.out.println("Show timing: " + selectedMovie.timing);
        System.out.println("Proceed to payment? (yes/no)");
        String confirm = scanner.nextLine();
        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("Booking cancelled.");
            return;
        }

        System.out.println("Please select the Payment method: ");
        System.out.println("1) Paytm");
        System.out.println("2) GPay");
        System.out.println("3) NetBanking");

        int methi = scanner.nextInt();
        scanner.nextLine();

        switch (methi) {
            case 1:
                System.out.println("You selected Paytm.");
                break;
            case 2:
                System.out.println("You selected GPay.");
                break;
            case 3:
                System.out.println("You selected NetBanking.");
                break;
            default:
                System.out.println("Invalid selection.");
        }

        System.out.println("Please enter your UPI pin for payment: (eg. 1111)");
        String upiPin = scanner.nextLine();

        if (upiPin.equals("1111")) {
            try {
                System.out.println("Processing payment...");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("Error processing payment.");
            }
            System.out.println("Payment successful!");
            System.out.println("Booking confirmed!");
            bookings.add(new Booking(loggedInUser, selectedMovie, selectedTheater, rowIndex, colIndex));
        } else {
            System.out.println("Invalid UPI pin! Payment failed.");
        }
    }

    private int getIntInput(String message) {
        int input;
        while (true) {
            try {
                System.out.print(message);
                input = scanner.nextInt();
                scanner.nextLine();
                return input;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    private int getValidIndex(String message, int size) {
        int choice;
        while (true) {
            choice = getIntInput(message);
            if (choice >= 1 && choice <= size) {
                return choice;
            }
            System.out.println("Invalid choice! Please enter a number between 1 and " + size);
        }
    }

    public void showAllShows() {
        if (movies.isEmpty() || theaters.isEmpty()) {
            System.out.println("No shows available at the moment.");
            return;
        }

        System.out.println("\n===== Available Shows =====");
        for (Movie movie : movies) {
            System.out.println("\nMovie: " + movie);
            System.out.println("Available Theaters:");
            for (Theater theater : theaters) {
                System.out.println("- " + theater.name);
            }
        }
    }

    public void run() {
        while (true) {
            if (loggedInUser == null) {
                System.out.println("\n===== Welcome to Movie Ticket Booking System =====");
                System.out.println("1. Register\n2. Login\n3. Exit");
                int choice = getValidIndex("Enter your choice: ", 3);
                switch (choice) {
                    case 1: registerUser(); break;
                    case 2: loginUser(); break;
                    case 3:
                        System.out.println("Exiting the system. Goodbye!");
                        return;
                }
            } else {
                if (loggedInUser.isAdmin) {
                    System.out.println("\n===== Admin Panel =====");
                    System.out.println("1. Add Movie\n2. Add Theater\n3. Logout");
                    int choice = getValidIndex("Enter your choice: ", 3);
                    switch (choice) {
                        case 1: addMovie(); break;
                        case 2: addTheater(); break;
                        case 3:
                            loggedInUser = null;
                            System.out.println("Logged out successfully!");
                            break;
                    }
                } else {
                    System.out.println("\n===== User Panel =====");
                    System.out.println("1. Book Ticket\n2. Show All Shows\n3. Show my bookings\n4. cancelling shows\n5. Logout");
                    int choice = getValidIndex("Enter your choice: ", 5);
                    switch (choice) {
                        case 1: bookTicket(); break;
                        case 2: showAllShows(); break;
                        case 3: showMyBookings() ; break ;
                        case 4: cancelBooking() ; break ;
                        case 5:
                            loggedInUser = null;
                            System.out.println("Logged out successfully!");
                            break;
                    }
                }
            }
        }
    }

    private double getDoubleInput(String message) {
        double input;
        while (true) {
            try {
                System.out.print(message);
                input = scanner.nextDouble();
                scanner.nextLine();
                return input;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid price.");
                scanner.nextLine();
            }
        }
    }

    public void bookShow(User user, Movie movie, Theater theater, int row, int col) {
        if (theater.isSeatAvailable(row, col)) {
            if (theater.bookSeat(row, col)) {
                Booking booking = new Booking(user, movie, theater, row, col);
                user.addBooking(booking);
                System.out.println("Booking confirmed: " + booking);
            } else {
                System.out.println("Seat is already booked.");
            }
        } else {
            System.out.println("Seat is not available.");
        }
    }

    public void cancelBooking(User user, Booking booking) {
        if (user.getBookings().contains(booking)) {
            booking.theater.cancelSeat(booking.row, booking.col);
            user.cancelBooking(booking);
            System.out.println("Booking canceled: " + booking);
        } else {
            System.out.println("Booking not found.");
        }
    }

    public void viewBookings(User user) {
        List<Booking> bookings = user.getBookings();
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
        } else {
            System.out.println("Your bookings:");
            for (Booking booking : bookings) {
                System.out.println(booking);
            }
        }
    }

    public boolean canCancelBooking(Booking booking) {
        // Assume currentTime is a datetime object representing the current time
        // and showTime is a datetime object representing the show time
        // For now, return true to allow cancellation
        return true;
    }
    
    public void showMyBookings() {
    if (loggedInUser == null) {
        System.out.println("Please login to view your bookings!");
        return;
    }

    List<Booking> userBookings = loggedInUser.getBookings();
    if (userBookings.isEmpty()) {
        System.out.println("You have no bookings.");
        return;
    }

    System.out.println("\n===== Your Booked Shows =====");
    for (int i = 0; i < userBookings.size(); i++) {
        System.out.println((i + 1) + ". " + userBookings.get(i));
    }
}

    public void cancelBooking() {
    if (loggedInUser == null) {
        System.out.println("Please login to cancel a booking!");
        return;
    }

    List<Booking> userBookings = loggedInUser.getBookings();
    if (userBookings.isEmpty()) {
        System.out.println("You have no bookings to cancel.");
        return;
    }

    showMyBookings();
    int choice = getValidIndex("Enter the booking number to cancel: ", userBookings.size());

    Booking bookingToCancel = userBookings.get(choice - 1);
    Theater theater = bookingToCancel.theater;
    theater.cancelSeat(bookingToCancel.row, bookingToCancel.col);
    bookings.remove(bookingToCancel);
    loggedInUser.cancelBooking(bookingToCancel);

    System.out.println("Your booking for " + bookingToCancel.movie.title + " at " + bookingToCancel.theater.name + " has been canceled successfully!");
}

}




public class Main {
    public static void main(String[] args) {
        MovieTicketSystem system = new MovieTicketSystem();
        system.run();
    }
}

 