import model.Customer;
import model.User;
import operation.CustomerOperation;
import operation.UserOperation;

public class App {
    public static void main(String[] args) throws Exception {
       
        UserOperation userOp = UserOperation.getInstance();
        CustomerOperation CustomerOp = CustomerOperation.getInstance();

        // Test 1: Generate Unique User ID
        String id1 = userOp.generateUniqueUserId();
        System.out.println("Generated ID: " + id1);

        // Test 2: Encrypt and Decrypt Password
        String password = "Test123";
        String encrypted = userOp.encryptPassword(password);
        String decrypted = userOp.decryptPassword(encrypted);
        System.out.println("Original: " + password);
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypted);

        // Test 3: Validate Username
        String username = "abc_chau";
        // System.out.println("Username valid? " + userOp.validateUsername(username));

        // Test 4: Validate Password
        // System.out.println("Password valid? " + userOp.validatePassword(password));

        // Test 5: Check if username exists
        // System.out.println("Username exists? " + userOp.checkUsernameExist("john_doe"));
        
        // // Test 6: Login with existing username and password
        // User loggedInUser = userOp.login("john_doe", "Test123");
        // if (loggedInUser != null) {
        //     System.out.println("Login successful!");
        //     System.out.println(loggedInUser.toString());
        // } else {
        //     System.out.println("Login failed.");
        // } 

        
        String userEmail = "minh.chau_753+test@gmail-domain.com";
        String userPhone = "0371746837";
        CustomerOp.registerCustomer(username, encrypted, userEmail,userPhone);
    }
}
