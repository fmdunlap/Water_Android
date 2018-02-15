package itp341.dunlap.forrest.water.singletons;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import itp341.dunlap.forrest.water.models.User;

/**
 * Created by FDUNLAP on 5/6/2017.
 */

public class UserManager {
    private static final UserManager ourInstance = new UserManager();

    private DatabaseReference ref = null;

    public static UserManager getInstance() {
        return ourInstance;
    }

    boolean loggedIn;
    User currentUser;
    String currentLocation;

    private UserManager() {
        ref = FirebaseDatabase.getInstance().getReference();
    }

    public void setUser(User user) {
        this.currentUser = user;
        loggedIn = true;

        ref.child("user").child(currentUser.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("email").exists()){
                    addFirstTimeUser(currentUser);
                } else
                    currentUser.setDrops(dataSnapshot.child("drops").getValue(Integer.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addFirstTimeUser(User currentUser) {
        ref.child("user").child(getUserId()).setValue(currentUser);
    }

    public boolean isLoggedIn(){
        return loggedIn;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public String getUserId(){
        return currentUser.getKey();
    }

    public void logOut(){
        currentUser = null;
        loggedIn = false;
    }

    public void addDrops(int toAdd){
        currentUser.setDrops(currentUser.getDrops() + toAdd);

        ref.child("user").child(currentUser.getKey()).child("drops").setValue(currentUser.getDrops());
        incrementCounter(currentLocation);
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void incrementCounter(String state) {
        FirebaseDatabase.getInstance().getReference().child("region").child(state).child("drops").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(final MutableData currentData) {
                if (currentData.getValue() == null) {
                    currentData.setValue(1);
                } else {
                    currentData.setValue((Long) currentData.getValue() + 1);
                }

                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                if (databaseError != null) {
                    Log.d("Firebase", "Firebase counter increment failed.");
                } else {
                    Log.d("Firebase", "Firebase counter increment succeeded.");
                }
            }
        });
    }
}
