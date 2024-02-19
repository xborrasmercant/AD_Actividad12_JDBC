import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class MainMenu {
    ArrayList<Booking> bookingsCollection = new ArrayList<>();
    static Connection conn;
    static JDBCManager jdbc;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String i;

        // CONNECT TO DATABASE
        try {
            conn = jdbc.connectToDatabase("jdbc:mysql://localhost:3306/actividad11_jdbc", "root", "");
        } catch (Exception e) {
            System.out.println("[ERROR] Can't establish connection to the database: " + e.getMessage());
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

            System.out.print("Enter an option please (1-3): ");
            i = input.nextLine();
            System.out.println("-------------------------------");

            switch (i) {
                case "1":
                    jdbc.fillDatabase(conn);
                    break;
                case "2":

                    break;
                case "6":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please, try again.");
                    break;
            }
        }
    }
}
