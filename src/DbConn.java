import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class DbConn {
    protected Connection conn;
    private ResultSet rs;
    private Statement st;

    /**
     * Constructor for objects of DbConn
     */
    public DbConn() {
    }

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/?user=root","root","root");
            //st = conn.createStatement();
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to connect to db", e);
        }
    }

    public void getData(){
        try{
            String query = "select * from piazza4db.user";
            rs = st.executeQuery(query);
            System.out.println("Dette er i user tabellen: ");
            while(rs.next()){
                String email = rs.getString("email");
                System.out.println("Email: " + email);

            }
        }
        catch(Exception e){
            throw new RuntimeException("Unable to retrieve data", e);
        }
    }
}