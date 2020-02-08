package it.guaraldi.to_dotaskmanager.data;

import com.google.firebase.auth.FirebaseUser;

public class User {


    private String uId;
    private String displayName;
    private String email;
    private String phoneNumber;

    public User(String uId, String displayName, String email, String phoneNumber) {
        this.uId = uId;
        this.displayName = displayName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public User (FirebaseUser firebaseUser){
        this.uId = firebaseUser.getUid();
        this.displayName = firebaseUser.getDisplayName();
        this.email = firebaseUser.getEmail();
        this.phoneNumber = firebaseUser.getPhoneNumber();
    }


    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
