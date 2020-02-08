package it.guaraldi.to_dotaskmanager.auth;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

public class User implements Parcelable {

    private String firstName;
    private String lastName;
    private String username;
    private String phone;
    private String objectId;
    public String sessionToken;
    private String gravatarId;
    private String avatarUrl;

    public User(String firstName, String lastName, String username, String phone, String objectId, String sessionToken, String gravatarId, String avatarUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.phone = phone;
        this.objectId = objectId;
        this.sessionToken = sessionToken;
        this.gravatarId = gravatarId;
        this.avatarUrl = avatarUrl;
    }

    public User(FirebaseUser user,String token){
        this.firstName = user.getDisplayName();
        this.lastName = "";
        this.phone = user.getPhoneNumber();
        this.objectId = user.getUid();
        this.username = user.getEmail();
        this.sessionToken = token;
        this.gravatarId = "";
        this.avatarUrl = "";
    }

    public User(String username, String password, String token){
        this.firstName ="";
        this.lastName = "";
        this.phone = "";
        this.objectId = "";
        this.username = username;
        this.sessionToken = token;
        this.gravatarId = "";
        this.avatarUrl = "";
    }

    protected User(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        username = in.readString();
        phone = in.readString();
        objectId = in.readString();
        sessionToken = in.readString();
        gravatarId = in.readString();
        avatarUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public String getGravatarId() {
        return gravatarId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(username);
        parcel.writeString(phone);
        parcel.writeString(objectId);
        parcel.writeString(sessionToken);
        parcel.writeString(gravatarId);
        parcel.writeString(avatarUrl);
    }
}
