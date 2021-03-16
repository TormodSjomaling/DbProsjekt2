import Entities.Post;
import Entities.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Engine extends DbConn{

    private int userCurrentlyLoggedIn;
    private int rolePermissions;

    private ResultSet rs = null;
    private PreparedStatement pst = null;

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
        try {
            String query = "SELECT userID, email, password, role FROM piazza4db.user WHERE email = ? AND password = ?";
            pst = conn.prepareStatement(query);
            pst.setString(1, user.getEmail());
            pst.setString(2, user.getPassword());

            rs = pst.executeQuery();

            if(rs.next()){
                int userID = rs.getInt("userID");
                int role = rs.getInt("role");

                rolePermissions = role;
                userCurrentlyLoggedIn = userID;
                successfulLogin = true;
            }
            return successfulLogin;

        } catch(Exception e){
            System.out.println("Noe gikk galt ved innlogging: \n" + e);
        } finally {
            try { if (pst != null) pst.close(); } catch (Exception e) {};
        }

        return false;
    }

    /**
     * Responsible for handling the backend part of inserting the post into the database.
     * @param post Values of user input put into a Post object
     * @param folderInput String, name of folder given by the user
     * @param tagInput String, name of user tag given by the user
     */
    public void registerThread(Post post, String folderInput, String tagInput){
        try {
            String query = "SELECT courseID FROM piazza4db.usertocourse WHERE userID = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, userCurrentlyLoggedIn);
            ResultSet rs = pst.executeQuery();

            if(rs.next()){
                int courseID = rs.getInt("courseID");

                String query2 = "SELECT folderID FROM piazza4db.folder WHERE name = ?";
                pst = conn.prepareStatement(query2);
                pst.setString(1, folderInput);
                rs = pst.executeQuery();

                if(rs.next()){
                    int folderID = rs.getInt("folderID");
                    String query3 = "INSERT INTO piazza4db.post (createdAt, content, threadTitle, folderID, courseID, userID) VALUES (?, ?, ?, ?, ?, ?)";
                    pst = conn.prepareStatement(query3, Statement.RETURN_GENERATED_KEYS);

                    pst.setObject(1, post.getDate());
                    pst.setString(2, post.getContent());
                    pst.setString(3, post.getThreadTitle());
                    pst.setInt(4, folderID);
                    pst.setInt(5, courseID);
                    pst.setInt(6, userCurrentlyLoggedIn);

                    if (pst.executeUpdate() == 1){
                        conn.commit();
                        System.out.println("Post ble laget.\n");

                        rs = pst.getGeneratedKeys();
                        if(rs.next()){
                            int postID = rs.getInt(1);
                            registerTagOnPost(tagInput, postID);
                        }
                    }
                }
            }

        } catch (Exception e){
            System.out.println("Noe gikk galt ved lagring av thread!" + e);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {};
            try { if (pst != null) pst.close(); } catch (Exception e) {};
        }
    }

    /**
     * Responsible for handling the backend part of inserting the post into the database.
     * @param postID int, postID to make a reply to
     */
    public void registerComment(int postID, String commentInput){
        try {
            Date date = new Date(1);

            String query = "INSERT INTO piazza4db.post (createdAt, content, isCommentOnPostID, userID) VALUES (?, ?, ?, ?)";
            pst = conn.prepareStatement(query);
            pst.setDate(1, date);
            pst.setString(2, commentInput);
            pst.setInt(3, postID);
            pst.setInt(4, userCurrentlyLoggedIn);

            if (pst.executeUpdate() == 1){
                conn.commit();
                System.out.println("\nPost ble laget.");
            }
        } catch (Exception e){
            System.out.println("Noe gikk galt ved lagring av kommentar. " + e);
        } finally {
            try { if (pst != null) pst.close(); } catch (Exception e) {};
        }
    }

    /**
     * Responsible for connection the user Tag input with the thread created
     * @param tagInput String value given by the user. Tag to connect with post creation.
     * @param postID int, ID of the post that user created, used in connection table between tag and post.
     */
    private void registerTagOnPost(String tagInput, int postID) {
        try {
            String query = "SELECT tagID FROM piazza4db.tag WHERE name = ?";
            pst = conn.prepareStatement(query);
            pst.setString(1, tagInput);
            rs = pst.executeQuery();

            if(rs.next()){
                int tagID = rs.getInt("tagID");
                String query2 = "INSERT INTO piazza4db.tagsonthread (tagID, postID) VALUES (?, ?)";
                pst = conn.prepareStatement(query2);
                pst.setInt(1, tagID);
                pst.setInt(2, postID);

                if (pst.executeUpdate() == 1) {
                    conn.commit();
                    System.out.println("Tag og Post ble knyttet til koblingstabellen.");
                }
            }
        } catch (Exception e){
            System.out.println("Noe gikk galt ved knyttingen mellom Tag og Post!" + e);
        } finally {
            try { if (pst != null) pst.close(); } catch (Exception e) {};
        }
    }

    /**
     * Responsible for fetching all posts in the database
     * @return List<Post> List of posts sent back to the application layer.
     */
    public List<Post> getPosts(){
        List<Post> posts = new ArrayList<Post>();
        try {
            String query = "SELECT postID, createdAt, content, threadTitle, piazza4db.folder.name FROM piazza4db.post INNER JOIN piazza4db.folder ON (post.folderID = folder.folderID)";
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

            while (rs.next()){
                Post post = new Post(rs.getInt("postID"), rs.getDate("createdAt"), rs.getString("content"), rs.getString("threadTitle"), null, rs.getString("name"));
                posts.add(post);
            }
        } catch (Exception e){
            System.out.println("Noe gikk galt ved henting a posts." + e);
        } finally {
            try { if (pst != null) pst.close(); } catch (Exception e) {};
        }

        return posts;
    }

    /**
     * Responsible for handling the backend part of fetching all posts matching user input and returning
     * all matching posts
     * @param userInput String, keyword that user searched for.
     * @return List of postIDs matching the user input.
     */
    public List<Integer> getPostsMatching(String userInput){
        List<Integer> matchingPosts = new ArrayList<>();
        try {
            String query = "SELECT postID FROM piazza4db.post WHERE threadTitle LIKE ? OR content LIKE ?";
            pst = conn.prepareStatement(query);
            pst.setString(1, '%' + userInput + '%');
            pst.setString(2, '%' + userInput + '%');

            rs = pst.executeQuery();
            while(rs.next()) {
                matchingPosts.add(rs.getInt("postID"));
            }

            return matchingPosts;
        } catch (Exception e){
            System.out.println("Noe gikk galt ved søket. " + e);
        } finally {
            try { if (pst != null) pst.close(); } catch (Exception e) {};
        }

        return matchingPosts;
    }

    /**
     * Responsible for fetching statistics about each user in the database.
     * Fetches the name, the number of posts viewed/read and number of posts created by each user
     * Also responsible for checking the permission of the user.
     * @return Returns the ResultSet from the query.
     */
    public ResultSet getStatisticsOfUsers(){
        if (rolePermissions == 1){
            try {
                String query = "SELECT name, (SELECT COUNT(*) FROM piazza4db.userpostview WHERE userID=user.userID) AS numberOfPostsRead, (SELECT COUNT(*) FROM piazza4db.post WHERE userID=user.userID) AS numberOfPostCreated\n" +
                        "FROM piazza4db.user ORDER BY numberOfPostsRead DESC;";
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();

                return rs;
            } catch (Exception e) {
                System.out.println("Noe gikk galt ved henting av statestikk. " + e);
            }
        }
        return null;
    }
}
