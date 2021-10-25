package comp5216.sydney.edu.au.timebank.model;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;

public class User {

    private String userId;
    private String userName;
    private boolean gender;
    private int coins;
    private ArrayList<String> taskPublished;
    private ArrayList<String> taskWorkingOn;
    private ArrayList<String> taskFinished;
    private ArrayList<String> messageBox;
    private @ServerTimestamp Date timestamp;


    public User()
    {}

    public User(FirebaseUser user, boolean gender, int coins){

        this.userId=user.getUid();
        this.userName=user.getDisplayName();
        if(TextUtils.isEmpty(this.userName)){
            this.userName=user.getEmail();
        }
        this.gender=gender;
        this.coins=coins;

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public int getCoins() {
        return coins;
    }

    public void setText(int coins) {
        this.coins = coins;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


}
