package com.example;
import java.util.List;

import com.example.model.Customer;
import com.example.operation.CustomerOperation;
import com.example.operation.CustomerOperation.CustomerListResult;
import com.example.operation.UserOperation;

public class Main {
    public static void main(String[] args) {
        /** test UserOperation
        UserOperation op = UserOperation.getInstance();
        String newUserId = op.generateUniqueUserId();
        System.out.println("Generated User ID: " + newUserId);

        String password = "pass123";
        String encrypted = op.encryptPassword(password);
        String decrypted = op.decryptPassword(encrypted);
        System.out.println("Original Password: " + password);
        System.out.println("Encrypted Password: " + encrypted);
        System.out.println("Decrypted Password: " + decrypted);
        System.out.println("Password matches: " + password.equals(decrypted));

        String testUsername = "john_doe";
        boolean exists = op.checkUsernameExist(testUsername);
        System.out.println("Username '" + testUsername + "' exists: " + exists);

        String validUsername = "user_test";
        
        String validPassword = "abc123";
        

        System.out.println("Valid username '" + validUsername + "': " + op.validateUsername(validUsername));
        
        System.out.println("Valid password '" + validPassword + "': " + op.validatePassword(validPassword));
       
        String loginUsername = "admin"; // chỉnh theo dữ liệu thật trong users.txt
        String loginPassword = "XtIa"; // chỉnh đúng với password gốc trước khi mã hoá

        User user = op.login(loginUsername, loginPassword);
        if (user != null) {
            System.out.println("Login success. User: " + user.getUserName() + ", Role: " + user.getUserRole());
        } else {
            System.out.println("Login failed for username: " + loginUsername);
        }
        /* */
        // Test CustomerOperation
        CustomerOperation customerOp = CustomerOperation.getInstance();
        UserOperation userOp = UserOperation.getInstance();
        String email = "duchai301@example.com";
        System.out.println("Valid email:" + customerOp.validateEmail(email));
        String mobile = "0904784715";
        System.out.println("Valid mobile: " + customerOp.validateMobile(mobile));
        String userName = "duc_hai";
        String userPassword = "duc123";

        customerOp.registerCustomer(userName, userPassword, email, mobile);
        System.out.println("Username exists: " + userOp.checkUsernameExist(userName));

        int pageToFetch = 1;
        CustomerListResult result = customerOp.getCustomerList(pageToFetch);
        System.out.println(" Trang hiện tại: " + result.getCurrentPage());
        System.out.println(" Tổng số trang: " + result.getTotalPages());

        List<Customer> customers = result.getCustomers();
        if (customers.isEmpty()) {
            System.out.println(" Không có khách hàng nào trong trang này.");
        } else {
            System.out.println(" Danh sách khách hàng:");
            for (Customer customer : customers) {
                System.out.println(" ID: " + customer.getUserId());
                System.out.println("    Tên: " + customer.getUserName());
                System.out.println("    Email: " + customer.getUserEmail());
                System.out.println("    SĐT: " + customer.getUserMobile());
                System.out.println("--------------");
            }
        }
        customerOp.deleteAllCustomers();
    }
}



