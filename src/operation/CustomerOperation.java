package operation;

import model.Customer;
import util.CustomerListResult;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.*;

public class CustomerOperation {

    private static final String userDataFile = "data/users.txt";
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

    public boolean validateMoblie(String userMobile){
        return userMobile.matches("^(03|04)\\d{8}$");
    }

    public boolean registerCustomer(String userName, String userPassword, String userEmail, String userMobile){
        UserOperation up = UserOperation.getInstance();
        //Check username
        if (!up.validateUsername(userName) || !up.validatePassword(userPassword)
                || !validateEmail(userEmail) || !validateMoblie(userMobile)
                || up.checkUsernameExist(userName)) {
            return false;
        }

        String userId = up.generateUniqueUserId();
        String registerTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
        String encryptedPassword = up.encryptPassword(userPassword);

        Customer customer = new Customer(userId, userName, userPassword, registerTime, "Customer", userEmail, userMobile);
        boolean writeCustomerToFile = importData(customer);
        if (writeCustomerToFile){
            up.registerUser(userName, userPassword, customer);
            return true;
        }
        return false;

    }

    private boolean importData(Customer customer){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userDataFile, true))) {
            writer.write(customer.toString());
            writer.newLine();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean updateProfile(String attributeName,String value,Customer customerObject){
        switch (attributeName.toLowerCase()){
            case "userEmail":
                if (validateMoblie(value)){
                    customerObject.setUserEmail(value);
                }
                break;
            case "userMobile":
                if (validateMoblie(value)) {
                    customerObject.setUserMobile(value);
                }
                break;
            default: //If attributeName does not match with userEmail and userMobile
                return false;
        }
        return false;
    }

    public boolean deleteCustomer(String customerId){
        try {
            File inputFile = new File(userDataFile);
            File tempFile = new File("data/temp_users.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            boolean deleted = false;

            while ((line=reader.readLine()) != null){
                if (line.startsWith(customerId+",")){
                    deleted = true;
                } else{
                    writer.write(line);
                    writer.newLine();
                }
            }
            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) return false;

            writer.close();
            reader.close();

            return deleted;
        } catch (IOException e) {
            return false;
        }
    }

    public CustomerListResult getCustomerList(int pageNumber) {
        List<Customer> allCustomers = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(userDataFile))) {
            String line;
            //***********************IT'S SO HARD TO UNDERSTAND****************
            while ((line = reader.readLine()) != null) {
                Customer customer = new Customer();
                if (customer.toString() != null) allCustomers.add(customer);
            }
            //*****************************************************************
        } catch (IOException e) {
            return new CustomerListResult(Collections.emptyList(), 0, 0);
        }

        int totalPages = (int) Math.ceil((double) allCustomers.size() / PAGE_SIZE);
        if (pageNumber > totalPages || pageNumber < 1) {
            return new CustomerListResult(Collections.emptyList(), pageNumber, totalPages);
        }

        int startIndex = (pageNumber - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, allCustomers.size());
        List<Customer> pageCustomers = allCustomers.subList(startIndex, endIndex);

        return new CustomerListResult(pageCustomers, pageNumber, totalPages);
    }

    public void deleteAllCustomers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(userDataFile))) {
            // Just overwrite with nothing
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
