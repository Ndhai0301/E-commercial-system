package com.example;
import com.example.model.User;
import com.example.operation.UserOperation;

public class Main {
    public static void main(String[] args) {
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
    }
}
