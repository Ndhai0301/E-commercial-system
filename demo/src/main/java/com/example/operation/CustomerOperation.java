package com.example.operation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import com.example.model.Customer;
import com.example.operation.CustomerOperation.CustomerListResult;

public class CustomerOperation {
    public static CustomerOperation instance;
    private static final String USER_FILE = "demo/data/users.txt";
    UserOperation userOp = UserOperation.getInstance();
    public CustomerOperation(){}

    public static CustomerOperation getInstance(){

        if (instance == null){
            instance = new CustomerOperation();
        }

        return instance;
   }

    public boolean validateEmail (String userEmail){
        return userEmail != null && userEmail.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    public boolean validateMobile(String userMobile){
        return userMobile != null && userMobile.matches("\\d{8,12}"); 
    }

    public boolean registerCustomer(String userName,String userPassword,String userEmail,String userMobile){
        File file = new File("demo/data/users.txt");

        if (!userOp.validateUsername(userName) || !userOp.validatePassword(userPassword)
                || !validateEmail(userEmail) || !validateMobile(userMobile)) {
            return false;
        }

        if (userOp.checkUsernameExist(userName) ){
            return false;
        }
        
        JSONObject newUser = new JSONObject();
        newUser.put("user_id", userOp.generateUniqueUserId());
        newUser.put("user_name", userName);
        newUser.put("user_password", userOp.encryptPassword(userPassword));
        newUser.put("user_register_time", new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss").format(new Date()));
        newUser.put("user_role", "customer");
        newUser.put("user_email", userEmail);
        newUser.put("user_mobile", userMobile);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,true))) { 
            writer.write(newUser.toString());
            writer.newLine();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProfile(String attributeName, String value, Customer customerObject) {
        if (attributeName == null || value == null || customerObject == null) return false;
    
        try {
            List<String> lines = Files.readAllLines(Paths.get(USER_FILE));
            List<String> updatedLines = new ArrayList<>();
            boolean updated = false;
    
            for (String line : lines) {
                JSONObject user = new JSONObject(line);
                if (user.getString("user_id").equals(customerObject.getUserId())) {
                    switch (attributeName.toLowerCase()) {
                        case "user_name":
                            if (!userOp.validateUsername(value) || userOp.checkUsernameExist(value)) return false;
                            customerObject.setUserName(value);
                            user.put("user_name", value);
                            break;
                        case "user_email":
                            if (!validateEmail(value)) return false;
                            customerObject.setUserEmail(value);
                            user.put("user_email", value);
                            break;
                        case "user_mobile":
                            if (!validateMobile(value)) return false;
                            customerObject.setUserMobile(value);
                            user.put("user_mobile", value);
                            break;
                        case "user_password":
                            if (!userOp.validatePassword(value)) return false;
                            String encryptedPassword = userOp.encryptPassword(value);
                            customerObject.setUserPassword(encryptedPassword);
                            user.put("user_password", encryptedPassword);
                            break;
                        default:
                            return false;
                    }
                    updated = true;
                }
    
                updatedLines.add(user.toString());
            }
    
            if (updated) {
                Files.write(Paths.get(USER_FILE), updatedLines);
            }
    
            return updated;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteCustomer(String customerId) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(USER_FILE));
            List<String> updatedLines = lines.stream()
                    .filter(line -> !new JSONObject(line).getString("user_id").equals(customerId))
                    .collect(Collectors.toList());

            boolean deleted = lines.size() != updatedLines.size();
            if (deleted) {
                Files.write(Paths.get(USER_FILE), updatedLines);
            }
            return deleted;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Helper class for pagination
    public CustomerListResult getCustomerList(int pageNumber) {
        List<Customer> customers = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(USER_FILE));
            List<JSONObject> customerJsons = lines.stream()
                    .map(JSONObject::new)
                    .filter(obj -> obj.getString("user_role").equals("customer"))
                    .collect(Collectors.toList());

            int totalPages = (int) Math.ceil(customerJsons.size() / 10.0);
            int start = (pageNumber - 1) * 10;
            int end = Math.min(start + 10, customerJsons.size());

            for (int i = start; i < end; i++) {
                JSONObject user = customerJsons.get(i);
                customers.add(new Customer(
                        user.getString("user_id"),
                        user.getString("user_name"),
                        user.getString("user_password"),
                        user.getString("user_register_time"),
                        user.getString("user_role"),
                        user.optString("user_email"),
                        user.optString("user_mobile")
                ));
            }

            return new CustomerListResult(customers, pageNumber, totalPages);
        } catch (IOException e) {
            e.printStackTrace();
            return new CustomerListResult(Collections.emptyList(), 0, 0);
        }
    }

    public void deleteAllCustomers() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(USER_FILE));
            List<String> admins = lines.stream()
                    .filter(line -> new JSONObject(line).getString("user_role").equals("admin"))
                    .collect(Collectors.toList());
            Files.write(Paths.get(USER_FILE), admins);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class CustomerListResult {
        private final List<Customer> customers;
        private final int currentPage;
        private final int totalPages;

        public CustomerListResult(List<Customer> customers, int currentPage, int totalPages) {
            this.customers = customers;
            this.currentPage = currentPage;
            this.totalPages = totalPages;
        }

        public List<Customer> getCustomers() {
            return customers;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public int getTotalPages() {
            return totalPages;
        }
    }
    public Customer getCustomerById(String userId) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(USER_FILE));
            for (String line : lines) {
                JSONObject user = new JSONObject(line);
                if (user.getString("user_id").equals(userId) && user.getString("user_role").equals("customer")) {
                    return new Customer(
                        user.getString("user_id"),
                        user.getString("user_name"),
                        user.getString("user_password"),
                        user.getString("user_register_time"),
                        user.getString("user_role"),
                        user.optString("user_email"),
                        user.optString("user_mobile")
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
