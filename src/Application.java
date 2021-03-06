import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Makes up the user interface (text based) of the application.
 * Responsible for all user interaction, like displaying the menu
 * and receiving input from the user.
 */
public class Application {

    /**
     * Constructor for objects of class ApplicationUI
     */
    public Application() {
    }

    private String[] menuItems = { // Edit later
            "1. Valg 1.",
            "2. Valg 2.",
            "3. Valg 3.",
            "4. Valg 4.",
            "5. Valg 5.",
            "6. Valg 6.",
    };

    /**
     * Initializes the application.
     */
    private void init() {
        System.out.println("init() was called");
    }

    /**
     * Starts the application by showing the menu and retrieving input from the
     * user.
     */
    public void start()
    {
        this.init();

        boolean quit = false;

        while (!quit)
        {
            try
            {
                int menuSelection = this.showMenu();
                switch (menuSelection)
                {
                    case 1:
                        break;

                    case 2:
                        break;

                    case 3:
                        break;

                    case 4:
                        break;

                    case 5:
                        break;

                    case 6:
                        break;

                    case 7:
                        System.out.println("\nTakk for at du brukte vår applikasjon. Ses!\n");
                        quit = true;
                        break;

                    default:
                        System.out.println("Noe gikk galt, prøv igjen!");
                }
            }
            catch (InputMismatchException ime)
            {
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
        System.out.println("\n**** Applikasjon ****\n");
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
}