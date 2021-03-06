import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class DbConn {
    protected Connection conn;
    private ResultSet rs;
    //private Statement st;

    /**
     * Constructor for objekter av DbConn
     */
    public DbConn() {
    }

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("","","");
            //st = conn.createStatement();
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to connect to db", e);
        }
    }
}