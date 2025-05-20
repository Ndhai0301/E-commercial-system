package operation;
import model.Admin;
import model.Customer;
import model.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.*;

import data.databaseWork;

public class UserOperation {

    private static UserOperation instance;
    private static final Set<String> registeredUsernames = new HashSet<>();
    private static final Map<String, String> userDatabase = new HashMap<>(); // username -> encryptedPassword
    private static final Map<String, User> userObjects = new HashMap<>(); // username -> User object
    private static final Random random = new Random();
    private static final String userDataFile = "src/data/users.txt";

    private UserOperation() {}

    public static UserOperation getInstance() {
        if (instance == null) {
            instance = new UserOperation();
        }
        return instance;
    }

    public String generateUniqueUserId() {
        long number = (long)(Math.random() * 1_000_000_0000L);
        return "u_" + String.format("%010d", number);
    }

    public String encryptPassword(String userPassword) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder randomStr = new StringBuilder();

        for (int i = 0; i < userPassword.length()*2; i++) {
            randomStr.append(chars.charAt(random.nextInt(chars.length())));
        }

        StringBuilder encrypted = new StringBuilder("^^");
        for (int i = 0, j = 0; i < userPassword.length(); i++, j += 2) {
            encrypted.append(randomStr.charAt(j))          
            .append(randomStr.charAt(j + 1))      
            .append(userPassword.charAt(i)); 
        }
        encrypted.append("$$");
        return encrypted.toString();
    }

    public String decryptPassword(String encryptedPassword) {
        if (!encryptedPassword.startsWith("^^") || !encryptedPassword.endsWith("$$")) {
            return null;
        }

        String core = encryptedPassword.substring(2, encryptedPassword.length() - 2);
        StringBuilder original = new StringBuilder();

        for (int i = 2; i < core.length(); i += 3) {
            original.append(core.charAt(i));
        }
        return original.toString();
    }

     public boolean checkUsernameExist(String userName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(userDataFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("\"user_name\":\"" + userName + "\"")) {
                    return true;
                }
            }
        } catch (IOException e) {}
        return false;
    }

    public boolean validateUsername(String userName) {
        return userName.matches("[a-zA-Z0-9_]{5,}");
    }

    public boolean validatePassword(String userPassword) {
        return userPassword.matches("(?=.*[a-zA-Z])(?=.*\\d).{5,}");
    }

    public User login(String userName, String userPassword) {
        try (BufferedReader reader = new BufferedReader(new FileReader(userDataFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("\"user_name\":\"" + userName + "\"")) {
                    String encrypted =  databaseWork.extractField(line, "user_password");
                    String decrypted = decryptPassword(encrypted);
                    if (userPassword.equals(decrypted)) {
                        String userId = databaseWork.extractField(line, "user_id");
                        String regTime = databaseWork.extractField(line, "user_register_time");
                        String role = databaseWork.extractField(line, "user_role");

                        if (role.equals("admin")) {
                            return new Admin(userId, userName, encrypted, regTime, role);
                        } else {
                            String email = databaseWork.extractField(line, "user_email");
                            String mobile = databaseWork.extractField(line, "user_mobile");
                            return new Customer(userId, userName, encrypted, regTime, role, email, mobile);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Login error: " + e.getMessage());
        }
        return null;
    }

   
}