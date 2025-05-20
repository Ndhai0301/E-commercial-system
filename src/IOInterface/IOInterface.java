package IOInterface;

import java.util.List;
import java.util.Scanner;

public class IOInterface {

    private static IOInterface instance;
    private Scanner scanner;

    // Singleton Pattern
    private IOInterface() {
        scanner = new Scanner(System.in);
    }

    public static IOInterface getInstance() {
        if (instance == null) {
            instance = new IOInterface();
        }
        return instance;
    }

    /**
     * Accept user input.
     * @param message The message to display for input prompt
     * @param numOfArgs The number of arguments expected
     * @return An array of strings containing the arguments
     */
    public String[] getUserInput(String message, int numOfArgs) {
        System.out.print(message + " ");
        String inputLine = scanner.nextLine().trim();
        String[] inputParts = inputLine.split("\\s+");

        String[] result = new String[numOfArgs];
        for (int i = 0; i < numOfArgs; i++) {
            if (i < inputParts.length) {
                result[i] = inputParts[i];
            } else {
                result[i] = ""; // Fill remaining with empty strings
            }
        }
        return result;
    }

    public void mainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Quit");
    }

    public void adminMenu() {
        System.out.println("\n=== Admin Menu ===");
        System.out.println("1. Show products");
        System.out.println("2. Show customers");
        System.out.println("3. Show orders");
        System.out.println("4. Generate Data");
        System.out.println("5. Delete all data");
        System.out.println("6. Logout");
    }

    public void customerMenu() {
        System.out.println("\n=== Customer Menu ===");
        System.out.println("1. Show profile");
        System.out.println("2. Update profile");
        System.out.println("3. Show products (or: 3 keyword)");
        System.out.println("4. Show history orders");
        System.out.println("5. Logout");
    }

    public void showList(String userRole, String listType, List<?> objectList, int pageNumber, int totalPages) {
        System.out.println("\n=== " + listType.toUpperCase() + " LIST (Page " + pageNumber + " of " + totalPages + ") ===");
        int row = 1;
        for (Object obj : objectList) {
            System.out.println((row++) + ". " + obj.toString());
        }
    }

    public void printErrorMessage(String errorSource, String errorMessage) {
        System.out.println("[ERROR] In " + errorSource + ": " + errorMessage);
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printObject(Object targetObject) {
        if (targetObject != null) {
            System.out.println(targetObject.toString());
        } else {
            System.out.println("Null object provided.");
        }
    }
}

