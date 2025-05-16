package operation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import model.Admin;

public class AdminOperation {
    private static AdminOperation instance;
    private Admin adminAccount;

    private AdminOperation(){}

    public static AdminOperation getInstance(){
        if (instance == null){
            instance = new AdminOperation();
        }        
        return instance;
    }

    public void registerAdmin() {

        if (adminAccount == null){
            adminAccount = new Admin();
            System.out.println("The admin was created successfully");
        }
        else{
            System.out.println("The admin accoutn is available");
        }
    }

    public Admin getAdminAccount() {
        return adminAccount;
    }

    private void saveAdminToFile(Admin admin){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/users.txt", true))) {
            writer.write(admin.toString());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to save admin to file: " + e.getMessage());
        }
    }
}
