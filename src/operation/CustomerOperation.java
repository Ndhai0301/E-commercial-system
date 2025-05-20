package operation;

import model.Customer;
import util.CustomerListResult;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.*;

import data.databaseWork;

public class CustomerOperation {

    private static final String userDataFile = "src/data/users.txt";
    private static CustomerOperation instance;
    private static final int PAGE_SIZE = 10;

    private CustomerOperation(){}

    public static CustomerOperation getInstance(){
        if (instance == null){
            instance = new CustomerOperation();
        }
        return instance;
    }

    public boolean validateEmail(String userMail){
        return userMail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public boolean validateMobile(String userMobile){
        return userMobile.matches("^(03|04)\\d{8}$");
    }

    public boolean registerCustomer(String userName, String userPassword, String userEmail, String userMobile){
        UserOperation uo= UserOperation.getInstance();
        //Check username
        if (!uo.validateUsername(userName) || !uo.validatePassword(userPassword)
                || !validateEmail(userEmail) || !validateMobile(userMobile)
                || uo.checkUsernameExist(userName)) {
            return false;
        }
        String registerTime = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss").format(new Date());
        String encryptedPassword = uo.encryptPassword(userPassword);
        // String decryptedPassword = uo.decryptPassword(encryptedPassword);
        
        Customer customer = new Customer(uo.generateUniqueUserId(),userName, encryptedPassword, registerTime, "customer", userEmail, userMobile);
        
        return databaseWork.addOneLine(customer.toString(), userDataFile);
    }

    public boolean updateProfile(String attributeName, String value, Customer customerObject) {
        UserOperation userOp = UserOperation.getInstance();

        switch (attributeName.toLowerCase()) {
            case "user_name":
                if (!userOp.validateUsername(value)) {
                    return false;
                }
                customerObject.setUserName(value);
                break;

            case "user_email":
                if (!validateEmail(value)) {
                    return false;
                }
                customerObject.setUserEmail(value);
                break;

            case "user_mobile":
                if (!validateMobile(value)) {
                    return false;
                }
                customerObject.setUserMobile(value);
                break;

            case "user_password":
                if (!userOp.validatePassword(value)) {
                    return false;
                }
                String encryptedPassword = userOp.encryptPassword(value);
                customerObject.setUserPassword(encryptedPassword);
                break;

            default:
                return false;
        }

        File inputFile = new File("src/data/users.txt");
        List<String> updatedLines = new ArrayList<>();
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("\"user_id\":\"" + customerObject.getUserId() + "\"") &&
                    line.contains("\"user_role\":\"customer\"")) {
                    updatedLines.add(customerObject.toString());
                    updated = true;
                } else {
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!updated) return false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile, false))) {
            for (String l : updatedLines) {
                writer.write(l);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean deleteCustomer(String customerId) {
        File inputFile = new File("src/data/users.txt");

        List<String> remainingLines = new ArrayList<>();
        boolean deleted = false;


        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("\"user_id\":\"" + customerId + "\"") &&
                    line.contains("\"user_role\":\"customer\"")) {
                    deleted = true; 
                    continue;
                }
                remainingLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!deleted) {
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile, false))) {
            for (String i : remainingLines) {
                writer.write(i);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public CustomerListResult getCustomerList(int pageNumber) {
        List<Customer> allCustomers = new ArrayList<>();
        int customersPerPage = 10;

        File inputFile = new File("src/data/users.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("\"user_role\":\"customer\"")) {
                    Customer customer = parseCustomerFromLine(line);
                    if (customer != null) {
                        allCustomers.add(customer);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int totalCustomers = allCustomers.size();
        int totalPages = (int) Math.ceil(totalCustomers / 10.0);

        // Điều chỉnh pageNumber nếu vượt giới hạn
        if (pageNumber < 1) pageNumber = 1;
        if (pageNumber > totalPages) pageNumber = totalPages;

        int startIndex = (pageNumber - 1) * customersPerPage;
        int endIndex = Math.min(startIndex + customersPerPage, totalCustomers);

        List<Customer> customersOnPage = new ArrayList<>();
        if (startIndex < totalCustomers) {
            customersOnPage = allCustomers.subList(startIndex, endIndex);
        }

        return new CustomerListResult(customersOnPage, pageNumber, totalPages);
    }
    
    private Customer parseCustomerFromLine(String line) {

        try {
            String userId = databaseWork.extractField(line, "user_id");
            String userName = databaseWork.extractField(line, "user_name");
            String userPassword = databaseWork.extractField(line, "user_password");
            String userRegisterTime = databaseWork.extractField(line, "user_register_time");
            String userRole = databaseWork.extractField(line, "user_role");
            String userEmail = databaseWork.extractField(line, "user_email");
            String userMobile = databaseWork.extractField(line, "user_mobile");

            return new Customer(userId, userName, userPassword, userRegisterTime,
                                userRole, userEmail, userMobile);
        } catch (Exception e) {
            return null;
        }
    }

    public void deleteAllCustomers() {
        File inputFile = new File(userDataFile);
        List<String> remainingLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Chỉ giữ lại những dòng KHÔNG phải là customer
                if (!line.contains("\"user_role\":\"customer\"")) {
                    remainingLines.add(line);
                }
            }
        } catch (IOException e) {
            return;
        }

        // Ghi đè file chỉ với admin còn lại
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile, false))) {
            for (String line : remainingLines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("ERROR WRITING FILE: " + e.getMessage());
        }
    }

}
