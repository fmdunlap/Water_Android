package itp341.dunlap.forrest.water.models;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by FDUNLAP on 5/6/2017.
 */

@IgnoreExtraProperties
public class User {

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String username;
    public String email;
    public String key;

    public int getDrops() {
        return drops;
    }

    public void setDrops(int drops) {
        this.drops = drops;
    }

    public int drops = 0;

    public User() {

    }

    public User(FirebaseUser u){
        username = u.getDisplayName();
        email = u.getEmail();
        key = u.getUid();
    }

    public User(String username, String email, String key) {
        this.username = username;
        this.email = email;
        this.key = key;
    }

}
