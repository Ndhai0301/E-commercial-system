import IOInterface.IOInterface;
import model.Customer;
import model.User;
import operation.AdminOperation;
import operation.CustomerOperation;
import operation.OrderOperation;
import operation.ProductOperation;
import operation.UserOperation;

public class Main {
    public static void main(String[] args) {
        IOInterface io = IOInterface.getInstance();
        UserOperation userOp = UserOperation.getInstance();
        CustomerOperation customerOp = CustomerOperation.getInstance();
        AdminOperation adminOp = AdminOperation.getInstance();

        adminOp.registerAdmin();

        boolean exit = false;

        while (!exit) {
            try {
                io.mainMenu();
                String[] choice = io.getUserInput("Enter your choice (1-3):", 1);
                switch (choice[0]) {
                    case "1": {
                        String[] loginInput = io.getUserInput("Login (username password):", 2);
                        User user = userOp.login(loginInput[0], loginInput[1]);

                        if (user == null) {
                            io.printErrorMessage("Login", "Invalid username or password.");
                        } else if (user.getUserRole().equals("admin")) {
                            handleAdmin(user);
                        } else if (user.getUserRole().equals("customer")) {
                            handleCustomer((Customer) user);
                        }
                        break;
                    }
                    case "2": {
                        String[] registerInput = io.getUserInput("Register (username password email phone):", 4);
                        boolean success = customerOp.registerCustomer(registerInput[0], registerInput[1], registerInput[2], registerInput[3]);
                        if (success) {
                            io.printMessage("Registration successful!");
                        } else {
                            io.printErrorMessage("Register", "Registration failed. Username might exist or invalid data.");
                        }
                        break;
                    }
                    case "3": {
                        io.printMessage("Thank you for using the system. Goodbye!");
                        exit = true;
                        break;
                    }
                    default: {
                        io.printErrorMessage("Main Menu", "Invalid option. Please enter 1, 2, or 3.");
                    }
                }
            } catch (Exception e) {
                io.printErrorMessage("Main", "Unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private static void handleAdmin(User admin) {
        IOInterface io = IOInterface.getInstance();
        boolean logout = false;

        while (!logout) {
            try {
                io.adminMenu();
                String[] adminChoice = io.getUserInput("Enter admin option (1-8):", 1);
                switch (adminChoice[0]) {
                    case "1":
                        // Call ProductOperation.getInstance().getProductList(...)
                        io.printMessage("Feature: Show products");
                        break;
                    case "2":
                        // Register customer manually
                        io.printMessage("Feature: Add customers");
                        break;
                    case "3":
                        io.printMessage("Feature: Show customers");
                        break;
                    case "4":
                        io.printMessage("Feature: Show orders");
                        break;
                    case "5":
                        io.printMessage("Generating test data...");
                        OrderOperation.getInstance().generateTestOrderData();
                        break;
                    case "6":
                        io.printMessage("Generating all statistical figures...");
                        // Call statistical generation methods
                        break;
                    case "7":
                        io.printMessage("Deleting all data...");
                        ProductOperation.getInstance().deleteAllProducts();
                        CustomerOperation.getInstance().deleteAllCustomers();
                        OrderOperation.getInstance().deleteAllOrders();
                        break;
                    case "8":
                        io.printMessage("Logging out...");
                        logout = true;
                        break;
                    default:
                        io.printErrorMessage("Admin Menu", "Invalid choice.");
                }
            } catch (Exception e) {
                io.printErrorMessage("Admin", "Error: " + e.getMessage());
            }
        }
    }

    private static void handleCustomer(Customer customer) {
        IOInterface io = IOInterface.getInstance();
        boolean logout = false;

        while (!logout) {
            try {
                io.customerMenu();
                String[] cusChoice = io.getUserInput("Enter customer option (1-6):", 1);
                switch (cusChoice[0]) {
                    case "1":
                        io.printObject(customer);
                        break;
                    case "2":
                        String[] input = io.getUserInput("Update (field new_value):", 2);
                        boolean updated = CustomerOperation.getInstance().updateProfile(input[0], input[1], customer);
                        if (updated) {
                            io.printMessage("Update successful.");
                        } else {
                            io.printErrorMessage("Update", "Update failed.");
                        }
                        break;
                    case "3":
                        io.printMessage("Feature: Show products");
                        break;
                    case "4":
                        io.printMessage("Feature: Show history orders");
                        break;
                    case "5":
                        io.printMessage("Generating your consumption figures...");
                        OrderOperation.getInstance().generateSingleCustomerConsumptionFigure(customer.getUserId());
                        break;
                    case "6":
                        io.printMessage("Logging out...");
                        logout = true;
                        break;
                    default:
                        io.printErrorMessage("Customer Menu", "Invalid option.");
                }
            } catch (Exception e) {
                io.printErrorMessage("Customer", "Error: " + e.getMessage());
            }
        }
    }
}
