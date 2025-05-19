package model;

public class User {
    private String userId;
    private String userName;
    private String userPassword;
    private String userRegisterTime;
    private String userRole;

    public User(String userId, String userName, String userPassword, String userRegisterTime, String userRole ){
        // if ( !userId.matches("u_\\d{10}")){
        //     throw new IllegalArgumentException("Invalid userId format. Expected: u_1234567890");
        // }
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userRegisterTime = userRegisterTime;
        this.userRole = userRole;
    }
    public User(){
        this.userId = "u_0000000000";
        this.userName = "Default User";
        this.userPassword = "password";
        this.userRegisterTime = "01-01-2000_00:00:00";
        this.userRole = "customer";
    }
    
    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserRegisterTime() {
        return userRegisterTime;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
    @Override
    public String toString() {
        return  "{"+ "\"user_id\":\"" + userId + '\"' +
                ",\"user_name\":\"" + userName + '\"' +
                ",\"user_password\":\"" + userPassword + '\"' +
                ",\"user_register_Time\":\"" + userRegisterTime + '\"' +
                ",\"user_role\":\"" + userRole + '\"' +
                '}';
    }

}
