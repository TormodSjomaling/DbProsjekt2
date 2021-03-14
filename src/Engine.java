import Entities.User;
import com.mysql.cj.protocol.Resultset;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Engine extends DbConn{

    /**
     * Constructor of object Engine
     */
    public Engine() {
        connect();

        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            System.out.println("db error during setAuoCommit of RegistrerCtrl=" + e);
            return;
        }
    }

    /**
     * Responsible for handling the backend part of fetching user matching input and checking
     * if they match.
     * @param user Values of input put into a User object.
     * @return true or false, true if email and password matches row in db.
     */
    public boolean tryLogin(User user){
        Boolean successfulLogin = false;
        try{
            String query = "SELECT email, password FROM piazza4db.user WHERE email = ?";
            PreparedStatement pst = conn.prepareStatement(query);

            pst.setString(1, user.getEmail());

            ResultSet rs = pst.executeQuery();

            while(rs.next()){
                String email = rs.getString("email");
                String password =rs.getString("password");

                if(email.equals(user.getEmail()) && password.equals(user.getPassword())){
                    successfulLogin = true;
                }
            }
            return successfulLogin;

        } catch(Exception e){
            System.out.println("Noe gikk galt ved innlogging: \n" + e);
        }

        return false;
    }
}
