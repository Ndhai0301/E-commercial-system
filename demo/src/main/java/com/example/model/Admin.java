package com.example.model;


public class Admin extends User {
    public Admin(String userId, String userName, String userPassword, String userRegisterTime, String userRole) {
        super(userId, userName, userPassword, userRegisterTime, userRole);
    }
    public Admin(){
        super();
    }
    public String toString() {
        return super.toString();
    }
}

