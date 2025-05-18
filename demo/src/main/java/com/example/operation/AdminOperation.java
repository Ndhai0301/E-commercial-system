package com.example.operation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.json.JSONObject;

public class AdminOperation {

    public static AdminOperation instance;
    UserOperation userOp = UserOperation.getInstance();
    private AdminOperation(){}
    
    public static AdminOperation getInstance(){
        if(instance == null){
            instance = new AdminOperation();
        }
        return instance;
    }
    public void registerAdmin(){
        File file = new File("demo/data/users.txt");
        JSONObject newUser = new JSONObject();
        newUser.put("user_id", userOp.generateUniqueUserId());
        newUser.put("user_name", createAdminName());
        newUser.put("user_password", userOp.encryptPassword(createPassword()));
        newUser.put("user_register_time", new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss").format(new Date()));
        newUser.put("user_role", "admin");

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,true))) { 
            writer.write(newUser.toString());
            writer.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String createAdminName(){
        int count = 0;
        File file = new File("demo/data/users.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                JSONObject obj = new JSONObject(line);
                if (obj.has("user_role") && obj.getString("user_role").equalsIgnoreCase("admin")) {
                    count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "admin" + (count + 1);
    }

    public String createPassword() {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rand = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            password.append(alphabet.charAt(rand.nextInt(alphabet.length())));
        }
        return password.toString();
    }
}
