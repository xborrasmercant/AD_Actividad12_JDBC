import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class MainMenu {
    ArrayList<Booking> bookingsCollection = new ArrayList<>();
    static Connection conn;
    static JDBCManager jdbc = new JDBCManager();

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String option;

        // CONNECT TO DATABASE
        try {
            conn = jdbc.connectToDatabase("jdbc:mysql://localhost:3306/actividad11_jdbc", "root", "");
        } catch (Exception e) {
            System.out.println("[ERROR] Can't establish connection to the database: " + e.getMessage());
            System.exit(0);
        }

        // Main menu
        while (true) {

            System.out.println();
            System.out.println("===============================");
            System.out.println("1. Fill database");
            System.out.println("2. Delete database data");
            System.out.println("3. Show booking info");
            System.out.println("4. Show agency bookings");
            System.out.println("5. Insert booking");
            System.out.println("6. Delete booking");
            System.out.println("7. Modify booking");
            System.out.println("8. Exit");
            System.out.println("===============================");

            System.out.print("Enter an option please (1-8): ");
            option = input.nextLine();
            System.out.println("-------------------------------");

            switch (option) {
                case "1" -> jdbc.fillDatabase(conn);
                case "2" -> jdbc.emptyDatabase(conn);
                case "3" -> {
                    System.out.print("Enter Booking ID to show info: ");
                    String bookingId = input.nextLine();
                    jdbc.getBookingInfo(conn, bookingId);
                }
                case "4" -> {
                    System.out.print("Enter Agency ID to show bookings: ");
                    String agencyId = input.nextLine();
                    jdbc.getAgencyBookings(conn, agencyId);
                }
                case "5" -> jdbc.insertBooking(conn);
                case "6" -> {
                    System.out.print("Enter Booking ID to delete: ");
                    String bookingId = input.nextLine();
                    jdbc.deleteBooking(conn, bookingId);
                }
                case "7" -> {
                    System.out.print("Enter Booking ID to modify: ");
                    String bookingId = input.nextLine();
                    jdbc.modifyBooking(conn, bookingId);
                }
                case "8" -> System.exit(0);
                default -> System.out.println("Invalid option. Please, try again.");
            }
        }
    }
}
