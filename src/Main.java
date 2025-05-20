import java.util.List;

import IOInterface.IOInterface;
import model.Customer;
import model.Order;
import model.Product;
import model.User;
import operation.AdminOperation;
import operation.CustomerOperation;
import operation.OrderOperation;
import operation.ProductOperation;
import operation.UserOperation;
import util.CustomerListResult;
import util.OrderListResult;
import util.ProductListResult;

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
                String[] adminChoice = io.getUserInput("Enter admin option (1-6):", 1);
                switch (adminChoice[0]) {
                    case "1":
                        io.printMessage("Feature: Show products");
                        int product_page = 1;
                        boolean back = false;
                        ProductOperation productOp = ProductOperation.getInstance();
                        while (!back) {
                            ProductListResult result = productOp.getProductList(product_page);
                            io.showList("admin", "order", result.getProductList(), result.getCurrentPage(), result.getTotalPages());

                            String[] nav = io.getUserInput("Type 'n' for next, 'p' for previous, or 'b' to go back:", 1);
                            switch (nav[0].toLowerCase()) {
                                case "n":
                                    if (product_page < result.getTotalPages()) product_page++;
                                    else io.printMessage("Already at last page.");
                                    break;
                                case "p":
                                    if (product_page > 1) product_page--;
                                    else io.printMessage("Already at first page.");
                                    break;
                                case "b":
                                    back = true;
                                    break;
                                default:
                                    io.printErrorMessage("Navigation", "Invalid input.");
                            }
                        }
                        break;
                    case "2":
                        io.printMessage("Feature: Show customers");
                        int customer_page = 1;
                        boolean cBack = false;
                        CustomerOperation customerOp = CustomerOperation.getInstance();
                        while (!cBack) {
                            CustomerListResult result = customerOp.getCustomerList(customer_page);
                            io.showList("customer", "customer", result.getCustomers(), result.getCurrentPage(), result.getTotalPages());

                            String[] nav = io.getUserInput("Type 'n' for next, 'p' for previous, or 'b' to go back:", 1);
                            switch (nav[0].toLowerCase()) {
                                case "n":
                                    if (customer_page < result.getTotalPages()) customer_page++;
                                    else io.printMessage("Already at last page.");
                                    break;
                                case "p":
                                    if (customer_page > 1) customer_page--;
                                    else io.printMessage("Already at first page.");
                                    break;
                                case "b":
                                    cBack = true;
                                    break;
                                default:
                                    io.printErrorMessage("Navigation", "Invalid input.");
                            }
                        }
                        break;
                    case "3":
                        io.printMessage("Feature: Show orders");
                        int order_page = 1;
                        boolean oBack = false;
                        OrderOperation orderOp = OrderOperation.getInstance();
                        while (!oBack) {
                            OrderListResult result = orderOp.getOrderList("",order_page);
                            io.showList("admin", "orders", result.getOrders(), result.getCurrentPage(), result.getTotalPages());

                            String[] nav = io.getUserInput("Type 'n' for next, 'p' for previous, or 'b' to go back:", 1);
                            switch (nav[0].toLowerCase()) {
                                case "n":
                                    if (order_page < result.getTotalPages()) order_page++;
                                    else io.printMessage("Already at last page.");
                                    break;
                                case "p":
                                    if (order_page > 1) order_page--;
                                    else io.printMessage("Already at first page.");
                                    break;
                                case "b":
                                    oBack = true;
                                    break;
                                default:
                                    io.printErrorMessage("Navigation", "Invalid input.");
                            }
                        }
                        break;
                    case "4":
                        io.printMessage("Generating test data...");
                        OrderOperation.getInstance().generateTestOrderData();
                        break;
                    case "5":
                        io.printMessage("Deleting all data...");
                        ProductOperation.getInstance().deleteAllProducts();
                        CustomerOperation.getInstance().deleteAllCustomers();
                        OrderOperation.getInstance().deleteAllOrders();
                        break;
                    case "6":
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
                        io.printMessage("Feature: Show product");
                        List<Product> productList = ProductOperation.getInstance().getProductListByKeyword("");
                        if (productList.isEmpty()) {
                            io.printMessage("No products available.");
                        } else {
                            for (Product product : productList) {
                                io.printObject(product);
                            }
                        }
                        break;
                    case "4":
                        io.printMessage("Feature: Show history orders");
                        OrderOperation orderOp = OrderOperation.getInstance();
                        OrderListResult result = orderOp.getOrderList(customer.getUserId(), 1); // page 1

                        if (result.getOrders().isEmpty()) {
                            io.printMessage("You have no orders yet.");
                        } else {
                            for (Order order : result.getOrders()) {
                                io.printObject(order);
                            }
                        }
                        break;
                    case "5":
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
