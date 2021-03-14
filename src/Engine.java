import Entities.Post;
import Entities.User;
import com.mysql.cj.protocol.Resultset;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Engine extends DbConn{

    /**
     * Constructor of object Engine
     */
    public Engine() {
        connect();
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            System.out.println("db error during setAuoCommit of Engine=" + e);
            return;
        }
    }

    /**
     * Responsible for handling the backend part of fetching user matching input and checking
     * if they match.
     * @param user Values of user input put into a User object.
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

    /**
     * Responsible for checking if the post created by the user is a Thread or a Replay/Comment
     * @param post Values of user input put into a Post object
     */
    public boolean registerPost(Post post){
        if(post.getIsCommentOnPostID() == null){
            registerThread(post);
        }
        else {
            registerComment(post);
        }
        return false;
    }

    /**
     * Responsible for handling the backend part of inserting the post into the database.
     * @param post Values of user input put into a Post object
     */
    private boolean registerThread(Post post){
        try {
            String query = "INSERT INTO piazza4db.post (createdAt, content, threadTitle, isCommentOnPostID) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);


        } catch (Exception e){

        }
        return false;
    }

    /**
     * Responsible for handling the backend part of inserting the post into the database.
     * @param post Values of user input put into a Post object
     */
    private boolean registerComment(Post post){
        return false;
    }

    /**
     * Responsible for handling the backend part of fetching all posts matching user input and returning
     * all matching posts
     * @param userInput String, keyword that user searched for.
     * @return List of postIDs matching the user input
     */
    public List<Integer> getPostsMatching(String userInput){
        return null;
    }

    public boolean getStatisticsOfUsers(){
        return false;
    }
}
