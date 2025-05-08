package model;

public class Customer extends User {
    private String userEmail;
    private String userMobile;

    public Customer(String userId,String userName,String userPassword, String userRegisterTime,String userRole,String userEmail,String userMobile){
        super(userId,userName,userPassword,userRegisterTime,userRole);
        this.userEmail = userEmail;
        this.userMobile = userMobile;
    }
    public Customer(){
        super();
        this.userEmail = "default@example.com";
        this.userMobile = "0000000000";
    }
    public String getUserEmail() {
        return userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    @Override
    public String toString() {
        return super.toString().replace("}", "") +  
            ", userEmail='" + userEmail + '\'' +
            ", userMobile='" + userMobile + '\'' +
            '}';
    }
}
