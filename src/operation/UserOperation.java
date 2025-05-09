package operation;
import model.User;
import java.util.*;
import java.util.regex.*;

public class UserOperation {

    private static UserOperation instance;
    private static final Set<String> registeredUsernames = new HashSet<>();
    private static final Map<String, String> userDatabase = new HashMap<>(); // username -> encryptedPassword
    private static final Map<String, User> userObjects = new HashMap<>(); // username -> User object
    private static final Random random = new Random();

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

        for (int i = 0; i < userPassword.length() * 2; i++) {
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
        return registeredUsernames.contains(userName);
    }

    public boolean validateUsername(String userName) {
        return userName.matches("[a-zA-Z_]{5,}");
    }

    public boolean validatePassword(String userPassword) {
        return userPassword.matches("(?=.*[a-zA-Z])(?=.*\\d).{5,}");
    }

    public User login(String userName, String userPassword) {
        if (!checkUsernameExist(userName)) return null;

        String encryptedStored = userDatabase.get(userName);
        String decrypted = decryptPassword(encryptedStored);

        if (decrypted != null && decrypted.equals(userPassword)) {
            return userObjects.get(userName); 
        }
        return null;
    }

   
    public boolean registerUser(String userName, String userPassword, User userObj) {
        if (checkUsernameExist(userName) || !validateUsername(userName) || !validatePassword(userPassword)) {
            return false;
        }
        String encrypted = encryptPassword(userPassword);
        registeredUsernames.add(userName);
        userDatabase.put(userName, encrypted);
        userObjects.put(userName, userObj);
        return true;
    }
}