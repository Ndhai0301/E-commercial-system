package com.example;
import com.example.model.Customer;
import com.example.operation.CustomerOperation;
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

        Customer customer = customerOp.getCustomerById("u_0000000002");
        if (customer != null) {
            boolean updateEmailSuccess = customerOp.updateProfile("user_email", "john.new@example.com", customer);
            System.out.println("Update email status: " + (updateEmailSuccess ? "Success" : "Failed"));
        } else {
        System.out.println("Customer not found.");
        }
    }
}
