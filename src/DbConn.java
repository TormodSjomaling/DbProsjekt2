import java.sql.Connection;
import java.sql.DriverManager;

public class DbConn {
    protected Connection conn;

    /**
     * Constructor for objects of DbConn
     */
    public DbConn() {
    }

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/?user=root","root","root");
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to connect to db", e);
        }
    }
}