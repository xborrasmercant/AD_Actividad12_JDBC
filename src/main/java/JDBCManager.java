import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.print.Book;
import java.io.File;
import java.sql.*;
import java.util.Scanner;
import java.util.Vector;

public class JDBCManager {
    private Vector<Booking> bookingsCollection;

    public JDBCManager() {
        this.bookingsCollection = parseBookingsFile(new File("src/main/resources/bookings.xml"));
    }

    public Connection connectToDatabase(String url, String user, String pwd) throws SQLException {
        return DriverManager.getConnection(url, user, pwd);
    }

    // CRUD Methods
    public void fillDatabase(Connection conn) {

        // Try fill
        try {
            String sql = "INSERT INTO Bookings VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            System.out.println("[ACTION] Inserting rows into database...");

            for (Booking b : bookingsCollection) {
                pstmt.setString(1, b.getBookingID());
                pstmt.setString(2, b.getClientID());
                pstmt.setString(3, b.getAgencyID());
                pstmt.setDouble(4, b.getPrice());
                pstmt.setString(5, b.getRoomType(b.getRoomID()));
                pstmt.setString(6, b.getHotelID());
                pstmt.setString(7, b.getClientName());
                pstmt.setString(8, b.getAgencyName());
                pstmt.setString(9, b.getHotelName());
                pstmt.setString(10, b.getCheckIn());
                pstmt.setInt(11, b.getRoomNights());
            }

            int insertedRows = pstmt.executeUpdate();
            System.out.println("[SUCCESS] Inserted " + insertedRows + " rows into database.");


        } catch (Exception e) {
            System.out.println("[ERROR] An unexpected error occurred filling the database: " + e.getMessage());
        }
    }
    public void emptyDatabase(Connection conn) {

        // Try deletion
        try {
            String sql = "DELETE * FROM Bookings";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            int insertedRows = pstmt.executeUpdate();
            System.out.println("[SUCCESS] Inserted " + insertedRows + " rows into database.");
        } catch (Exception e) {
            System.out.println("[ERROR] An unexpected error occurred filling the database: " + e.getMessage());
        }
    }
    public void getBookingInfo(Connection conn, String bookingID) {

        // Exit function if booking not exists
        if (!bookingExists(bookingID)) {
            System.out.println("[ERROR] Booking with id '" + bookingID + "' does not exists.");
            return;
        }

        // Try deletion
        try {
            String sql = "SELECT * FROM Bookings WHERE BookingID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookingID);

            ResultSet resultSet = pstmt.executeQuery();
            Booking b = Booking.convertBooking(resultSet);

            b.printBooking();
        } catch (Exception e) {
            System.out.println("[ERROR] An unexpected error occurred while getting booking info: " + e.getMessage());
        }
    }
    public void getAgencyBookings(Connection conn, String agencyID) {

        // Exit function if booking not exists
        if (!bookingExists(agencyID)) {
            System.out.println("[ERROR] Agency with id '" + agencyID + "' does not exists.");
            return;
        }

        // Try selection
        try {
            String sql = "SELECT * FROM Bookings WHERE AgencyID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, agencyID);

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                Booking b = Booking.convertBooking(resultSet);
                b.printBooking();
            }

        } catch (Exception e) {
            System.out.println("[ERROR] An unexpected error occurred while getting agency " + agencyID + " bookings: " + e.getMessage());
        }
    }
    public void insertBooking(Connection conn) {

        // Try insertion
        try {
            String sql = "INSERT INTO Bookings VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            Booking b = Booking.createNewBooking();

            pstmt.setString(1, b.getBookingID());
            pstmt.setString(2, b.getClientID());
            pstmt.setString(3, b.getAgencyID());
            pstmt.setDouble(4, b.getPrice());
            pstmt.setString(5, b.getRoomType(b.getRoomID()));
            pstmt.setString(6, b.getHotelID());
            pstmt.setString(7, b.getClientName());
            pstmt.setString(8, b.getAgencyName());
            pstmt.setString(9, b.getHotelName());
            pstmt.setString(10, b.getCheckIn());
            pstmt.setInt(11, b.getRoomNights());

            pstmt.executeUpdate();
            System.out.println("[SUCCESS] Inserted booking '" + b.getBookingID() + "' into database.");


        } catch (Exception e) {
            System.out.println("[ERROR] An unexpected error occurred inserting the booking into the database: " + e.getMessage());
        }
    }
    public void deleteBooking(Connection conn, String bookingID) {

        // Exit function if booking not exists
        if (!bookingExists(bookingID)) {
            System.out.println("[ERROR] Booking with id '" + bookingID + "' does not exists.");
            return;
        }

        // Try deletion
        try {
            String sql = "DELETE * FROM Bookings WHERE BookingID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookingID);

            int insertedRows = pstmt.executeUpdate();
            System.out.println("[SUCCESS] Deleted booking with ID '" + bookingID + "'.");
        } catch (Exception e) {
            System.out.println("[ERROR] An unexpected error occurred deleting booking '" + bookingID + "': " + e.getMessage() );
        }
    }
    public void modifyBooking(Connection conn, String bookingID) {
        // Exit function if booking not exists
        if (!bookingExists(bookingID)) {
            System.out.println("[ERROR] Booking with id '" + bookingID + "' does not exists.");
            return;
        }

        // Try update
        try {
            String sql = "UPDATE Bookings SET clientID = ?, agencyID = ?, price = ?, roomType = ?, hotelID = ?, clientName = ?, agencyName = ?, hotelName = ?, check_in = ?, room_nights = ? WHERE BookingID = ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            Booking b = Booking.createNewBooking();
            b.setBookingID(bookingID); // Set correct booking id to newly created booking

            pstmt.setString(1, b.getClientID());
            pstmt.setString(2, b.getAgencyID());
            pstmt.setDouble(3, b.getPrice());
            pstmt.setString(4, b.getRoomType(b.getRoomID()));
            pstmt.setString(5, b.getHotelID());
            pstmt.setString(6, b.getClientName());
            pstmt.setString(7, b.getAgencyName());
            pstmt.setString(8, b.getHotelName());
            pstmt.setString(9, b.getCheckIn());
            pstmt.setInt(10, b.getRoomNights());
            pstmt.setString(11, b.getBookingID());

            pstmt.executeUpdate();
            System.out.println("[SUCCESS] Updated booking '" + bookingID + "' into database.");
        } catch (Exception e) {
            System.out.println("[ERROR] An unexpected error occurred updated booking '"+ bookingID +"' into the database: " + e.getMessage());
        }
    }

    // OTHER Methods
    public static boolean bookingExists(String bookingID) {
        for (Booking booking : parseBookingsFile(new File("src/main/resources/bookings.xml"))) {
            if (booking.getBookingID().equals(bookingID)) {
                return true;
            }
        }

        return false;
    }
    public static Vector<Booking> parseBookingsFile(File bookingsFile) {
        Vector<Booking> bookings = null;
        try{
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLSaxHandler SAXHandler = new XMLSaxHandler()   ;
            parser.parse(bookingsFile, SAXHandler);
            bookings = SAXHandler.getBookingsCollection(); // The bookings collection is retrieved after being fully parsed
        }
        catch (Exception e) {
            System.out.println("[ERROR] File '"+ bookingsFile.getName() + "' parsing failed unexpectedly: " + e.getMessage());
        }
        return bookings;
    }
}