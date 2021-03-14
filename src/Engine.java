import Entities.Post;
import Entities.User;
import com.mysql.cj.protocol.Resultset;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Engine extends DbConn{

    int userCurrentlyLoggedIn;

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
            String query = "SELECT userID, email, password FROM piazza4db.user WHERE email = ?";
            PreparedStatement pst = conn.prepareStatement(query);

            pst.setString(1, user.getEmail());

            ResultSet rs = pst.executeQuery();

            while(rs.next()){
                Integer userID = rs.getInt("userID");
                String email = rs.getString("email");
                String password =rs.getString("password");

                if(email.equals(user.getEmail()) && password.equals(user.getPassword())){
                    successfulLogin = true;
                    userCurrentlyLoggedIn = userID;
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
    public void registerPost(Post post, String folder, String tag){
        if(post.getIsCommentOnPostID() == null){
            registerThread(post, folder, tag);
        }
        else {
            registerComment(post);
        }
    }

    /**
     * Responsible for handling the backend part of inserting the post into the database.
     * @param post Values of user input put into a Post object
     */
    private void registerThread(Post post, String folder, String tag){
        try {
            String query = "SELECT courseID FROM piazza4db.usertocourse WHERE userID = ?";
            PreparedStatement pst = conn.prepareStatement(query);

            pst.setInt(1, userCurrentlyLoggedIn);
            ResultSet rs = pst.executeQuery();

            if(rs.next()) {
                Integer courseID = rs.getInt("courseID");

                String query2 = "SELECT folderID FROM piazza4db.folder WHERE name = ?";
                PreparedStatement pst2 = conn.prepareStatement(query2);

                pst2.setString(1, folder);
                ResultSet rs2 = pst2.executeQuery();

                if(rs2.next()){
                    Integer folderID = rs2.getInt("folderID");

                    String query3 = "INSERT INTO piazza4db.post (createdAt, content, threadTitle, folderID, courseID, userID) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement pst3 = conn.prepareStatement(query3, Statement.RETURN_GENERATED_KEYS);

                    pst3.setObject(1, post.getDate());
                    pst3.setString(2, post.getContent());
                    pst3.setString(3, post.getThreadTitle());
                    pst3.setInt(4, folderID);
                    pst3.setInt(5, courseID);
                    pst3.setInt(6, userCurrentlyLoggedIn);

                    if (pst3.executeUpdate() == 1) {
                        conn.commit();
                        System.out.println("\n Post ble laget.");

                        ResultSet rs3 = pst3.getGeneratedKeys();
                        if(rs3.next()){
                            int postID = rs3.getInt(1);
                            registerTagOnPost(tag, postID);
                        }
                    }
                }
            }

        } catch (Exception e){
            System.out.println("Noe gikk galt!");
        }
    }

    /**
     * Responsible for handling the backend part of inserting the post into the database.
     * @param post Values of user input put into a Post object
     */
    private boolean registerComment(Post post){
        return false;
    }

    /**
     *
     */
    private void registerTagOnPost(String tag, int postID) {
        try{
            String query = "SELECT tagID FROM piazza4db.tag WHERE name = ?";
            PreparedStatement pst = conn.prepareStatement(query);

            pst.setString(1, tag);
            ResultSet rs = pst.executeQuery();

            if(rs.next()){
                int tagID = rs.getInt("tagID");

                String query2 = "INSERT INTO piazza4db.tagsonthread (tagID, postID) VALUES (?, ?)";
                PreparedStatement pst2 = conn.prepareStatement(query2);

                pst2.setInt(1, tagID);
                pst2.setInt(2, postID);

                if (pst2.executeUpdate() == 1) {
                    conn.commit();
                    System.out.println("Tag og Post ble knyttet til koblingstabellen.");
                }
            }


        } catch (Exception e){
            System.out.println("Noe gikk galt!");
        }
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
