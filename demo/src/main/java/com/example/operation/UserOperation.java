package com.example.operation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import com.example.model.Admin;
import com.example.model.Customer;
import com.example.model.User;

public class UserOperation {
    private static UserOperation instance;
    private final String USER_FILE = "demo/data/users.txt";
    private final Random random = new Random();

    private UserOperation() {}

    public static UserOperation getInstance() {
        if (instance == null) {
            instance = new UserOperation();
        }
        return instance;
    }

    public String generateUniqueUserId() {
        List<JSONObject> users = loadUsersFromFile();
        int maxId = 0;
        for (JSONObject user : users) {
            String idStr = user.getString("user_id").replace("u_", "");
            int id = Integer.parseInt(idStr);
            if (id > maxId) maxId = id;
        }
        return String.format("u_%010d", maxId + 1);
    }

    public String encryptPassword(String userPassword) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder randomStr = new StringBuilder();
        for (int i = 0; i < userPassword.length() * 2; i++) {
            randomStr.append(chars.charAt(random.nextInt(chars.length())));
        }

        StringBuilder encrypted = new StringBuilder("^^");
        for (int i = 0; i < userPassword.length(); i++) {
            encrypted.append(randomStr.charAt(i * 2));
            encrypted.append(randomStr.charAt(i * 2 + 1));
            encrypted.append(userPassword.charAt(i));
        }
        encrypted.append("$$");
        return encrypted.toString();
    }

    public String decryptPassword(String encryptedPassword) {
        if (!encryptedPassword.startsWith("^^") || !encryptedPassword.endsWith("$$")) return null;
        String core = encryptedPassword.substring(2, encryptedPassword.length() - 2);
        StringBuilder original = new StringBuilder();
        for (int i = 2; i < core.length(); i += 3) {
            original.append(core.charAt(i));
        }
        return original.toString();
    }

    public boolean checkUsernameExist(String userName) {
        List<JSONObject> users = loadUsersFromFile();
        for (JSONObject user : users) {
            if (user.getString("user_name").equals(userName)) return true;
        }
        return false;
    }

    public boolean validateUsername(String userName) {
        return userName != null && userName.matches("[a-zA-Z_]{5,}");
    }

    public boolean validatePassword(String userPassword) {
        return userPassword != null &&
        userPassword.length() >= 4 &&
        (
            userPassword.matches(".*[a-z].*") ||      
            userPassword.matches(".*[A-Z].*") ||      
            userPassword.matches(".*\\d.*")   ||      
            userPassword.matches(".*[^a-zA-Z0-9].*")  
        );
    }

    public User login(String userName, String userPassword) {
        List<JSONObject> users = loadUsersFromFile();
        for (JSONObject userJson : users) {
            if (userJson.getString("user_name").equals(userName)) {
                String encryptedStored = userJson.getString("user_password");
                
                if (decryptPassword(encryptedStored).equals(userPassword)) {
                    return parseUser(userJson);
                }
            }
        }
        return null;
    }

    private User parseUser(JSONObject obj) {
        String role = obj.getString("user_role");
        if (role.equalsIgnoreCase("admin")) {
            return new Admin(
                obj.getString("user_id"),
                obj.getString("user_name"),
                obj.getString("user_password"),
                obj.getString("user_register_time"),
                obj.getString("user_role")
            );
        } else {
            return new Customer(
                obj.getString("user_id"),
                obj.getString("user_name"),
                obj.getString("user_password"),
                obj.getString("user_register_time"),
                obj.getString("user_role"),
                obj.optString("user_email"),
                obj.optString("user_mobile")
            );
        }
    }


    private List<JSONObject> loadUsersFromFile() {
        List<JSONObject> users = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(USER_FILE));
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    users.add(new JSONObject(line));
                }
            }
        } catch (IOException e) {

        }
        return users;
    }
}
    