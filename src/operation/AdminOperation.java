package operation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.Admin;

public class AdminOperation {
    private static final String userDataFile = "data/users.txt";
    private static AdminOperation instance;
    

    private AdminOperation(){}

    public static AdminOperation getInstance(){
        if (instance == null){
            instance = new AdminOperation();
        }        
        return instance;
    }

    public void registerAdmin() {
        File inputFile = new File(userDataFile);
        String defaultAdminName = "admin";
        String defaultAdminPassword = "753admin753";

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("\"user_name\":\"" + defaultAdminName + "\"") &&
                    line.contains("\"user_role\":\"admin\"")) {
                    System.out.println("This admin already existed");
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        
        UserOperation uo = UserOperation.getInstance();
        String userId = uo.generateUniqueUserId();
        String encryptedPassword = uo.encryptPassword(defaultAdminPassword);
        String registerTime = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss").format(new Date());

        Admin admin = new Admin(userId, defaultAdminName, encryptedPassword, registerTime, "admin");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile, true))) {
            writer.write(admin.toString());
            writer.newLine();
            System.out.println("Create Admin Successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
        }

}
