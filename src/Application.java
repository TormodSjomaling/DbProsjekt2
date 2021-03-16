import Entities.Post;
import Entities.User;

import java.sql.ResultSet;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Makes up the user interface (text based) of the application.
 * Responsible for all user interaction, like displaying the menu
 * and receiving input from the user.
 */
public class Application {
    Engine engine = new Engine();

    /**
     * Constructor for objects of class ApplicationUI
     */
    public Application() {
    }

    private String[] menuItems = { // Edit later
            "1. Ny Post.",
            "2. Svar på en post.",
            "3. Søk.",
            "4. Se statistikk.",
    };

    /**
     * Starts the application by showing the menu and retrieving input from the
     * user.
     */
    public void start()
    {
        System.out.println("\n**** Piazza Ripoff ****");
        boolean loginSuccessful = false;

        boolean quit = false;
        while (!quit)
            if(!loginSuccessful) {
                loginSuccessful = loginHandler();
            }
            else {
                try {
                    int menuSelection = this.showMenu();
                    switch (menuSelection) {
                        case 1:
                            addNewPost();
                            break;

                        case 2:
                            replyToPost();
                            break;

                        case 3:
                            getAllPostsMatchingKeyword();
                            break;

                        case 4:
                            getStatisticsAboutUsers();
                            break;

                        case 5:
                            System.out.println("\nTakk for at du brukte vår applikasjon.\n");
                            quit = true;
                            break;

                        default:
                            System.out.println("Noe gikk galt, prøv igjen!");
                    }
                } catch (InputMismatchException ime) {
                    System.out.println("\nERROR: Vennligst skriv inn et nummer mellom 1 og " + this.menuItems.length + "..\n");
                }
            }
    }

    /**
     * Displays the menu to the user, and waits for the users input. The user is
     * expected to input an integer between 1 and the max number of menu items.
     * If the user inputs anything else, an InputMismatchException is thrown.
     * The method returns the valid input from the user.
     *
     * @return the menu number (between 1 and max menu item number) provided by the user.
     * @throws InputMismatchException if user enters an invalid number/menu choice
     */
    private int showMenu() throws InputMismatchException {
        System.out.println("\n**** Applikasjonsvalg  ****\n");

        // Display the menu
        for ( String menuItem : menuItems )
        {
            System.out.println(menuItem);
        }
        int maxMenuItemNumber = menuItems.length + 1;
        // Add the "Exit"-choice to the menu
        System.out.println(maxMenuItemNumber + ". Avslutt\n");
        System.out.println("Vennligst velg et valg (1-" + maxMenuItemNumber + "): ");
        // Read input from user
        Scanner reader = new Scanner(System.in);
        int menuSelection = reader.nextInt();
        if ((menuSelection < 1) || (menuSelection > maxMenuItemNumber))
        {
            throw new InputMismatchException();
        }

        return menuSelection;
    }

    /**
     * Handles the login interaction with the user.
     * @return true or false, true if email and password matches row in db, false otherwise
     */
    public boolean loginHandler(){
        Scanner reader = new Scanner(System.in);

        System.out.println("\nLogg inn for å fortsette:");
        System.out.println("Email: ");
        String emailInput = reader.nextLine();
        System.out.println("Passord: ");
        String passwordInput = reader.nextLine();

        User user = new User(emailInput, "placeholder", passwordInput, 0);

        if(!engine.tryLogin(user)){
            System.out.println("Ikke riktig email eller passord. Prøv igjen.");
            return false;
        }

        return engine.tryLogin(user);
    }

    /**
     * Handles interaction with the user to create a new post.
     */
    public void addNewPost(){
        Scanner reader = new Scanner(System.in);

        System.out.println("\nOpprettelse av en ny post: ");
        System.out.println("Tittel: ");
        String threadTitleInput = reader.nextLine();
        System.out.println("Innhold: ");
        String contentInput = reader.nextLine();
        System.out.println("Mappe: ");
        String folderInput = reader.nextLine();
        System.out.println("Tag: ");
        String tagInput = reader.nextLine();

        Date date = new Date();
        Post post = new Post(date, contentInput, threadTitleInput, null);

        engine.registerThread(post, folderInput, tagInput);
    }

    /**
     * Handles interaction with the user to replay to a specific post.
     */
    public void replyToPost(){
        List<Post> posts = engine.getPosts();
        Scanner stringReader = new Scanner(System.in);
        Scanner intReader = new Scanner(System.in);

        System.out.println("\nHvilken thread vil du lage en kommentar til?");
        for (Post post : posts){
            System.out.println("Folder: " + post.getFolderName() + " postID: " + post.getPostID() + " Tittel: " + post.getThreadTitle());
        }

        System.out.println("\nPostID: ");
        int postToReply = intReader.nextInt();
        System.out.println("Kommentar: ");
        String commentInput = stringReader.nextLine();
        engine.registerComment(postToReply, commentInput);

    }

    /**
     * Handles interaction with the user to search for posts matching keyword.
     */
    public void getAllPostsMatchingKeyword(){
        Scanner reader = new Scanner(System.in);

        System.out.println("Søk: ");
        String searchInput = reader.nextLine();

        List<Integer> matchingPosts = engine.getPostsMatching(searchInput);

        if(matchingPosts.size() == 0){
            System.out.println("\nFant ingen posts som matchet ditt søk.");
        }
        else {
            System.out.println("\nHer er posts vi fant som matchet ditt søk:");
            for (Integer postID : matchingPosts) {
                System.out.println("postID: " + postID);
            }
        }
    }

    /**
     * Handles interaction with the user to search for posts matching keyword.
     */
    public void getStatisticsAboutUsers(){
        ResultSet rs = engine.getStatisticsOfUsers();

        if (rs != null) {
            try {
                System.out.println("\nFormat: Navn...numberOfPostsRead...numberOfPostCreated");
                while(rs.next()){
                    System.out.println(rs.getString("name") + " " + rs.getInt("numberOfPostsRead") + " " + rs.getInt("numberOfPostCreated"));
                }
            } catch (Exception e) {
                System.out.println("Noe gikk galt ved printing av statistikk." + e);
            } finally {
                try { if (rs != null) rs.close(); } catch (Exception e) {};
            }
        }
        else {
            System.out.println("Du har ikke tilgang til å se statistikk.");
        }
    }
}