import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

public class JDBCManager {
    private Vector<Booking> bookingsCollection;

    public JDBCManager() {
        parseBookingsFile(new File("src/bookings.xml"));
    }

    public Connection connectToDatabase(String url, String user, String pwd) throws SQLException {
        return DriverManager.getConnection(url, user, pwd);
    }

    public void fillDatabase(Connection conn) {
        String sql = "INSERT INTO Bookings VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
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
            System.out.println("[ERROR] An unexpected error occurred filling the database.");
        }
    }

    public void emptyDatabase(Connection conn) {
        String sql = "DELETE * FROM Bookings";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            int insertedRows = pstmt.executeUpdate();
            System.out.println("[SUCCESS] Inserted " + insertedRows + " rows into database.");
        } catch (Exception e) {
            System.out.println("[ERROR] An unexpected error occurred filling the database.");
        }
    }

    public void parseBookingsFile(File bookingsFile) {
        try{
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLSaxHandler SAXHandler = new XMLSaxHandler()   ;
            parser.parse(bookingsFile, SAXHandler);

            bookingsCollection = SAXHandler.getBookingsCollection(); // The bookings collection is retrieved after being fully parsed
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}