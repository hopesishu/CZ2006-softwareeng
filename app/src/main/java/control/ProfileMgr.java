package control;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import entity.ProfileMgrInterface;

/**
 * implements ProfileMgrInterface
 * allows the app to interact with firebase database, acting as an control class for Profile(entity class)
 */
public class ProfileMgr implements ProfileMgrInterface {
    private FirebaseDatabase database;
    private DatabaseReference userRef;


    /**
     * retrieve name of current profile
     * @param myCallback
     * @param Uid
     */
    @Override
    public void retrieveCurrentProfileName(final MyCallbackString myCallback, final String Uid)
    {
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String profileName;
                for (DataSnapshot data : dataSnapshot.child(Uid).child("profiles").getChildren()) {
                    if (data.child("thisProfile").getValue(boolean.class)) {
                        profileName = data.child("name").getValue(String.class);
                        myCallback.onCallback(profileName);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * retrieve DOB of current profile
     * @param myCallback
     * @param Uid
     */
    @Override
    public void retrieveCurrentDOB(final MyCallbackString myCallback, final String Uid)
    {
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String dateOfBirth;
                for (DataSnapshot data : dataSnapshot.child(Uid).child("profiles").getChildren()) {
                    if (data.child("thisProfile").getValue(boolean.class)) {
                        dateOfBirth = data.child("dateOfBirth").getValue(String.class);
                        myCallback.onCallback(dateOfBirth);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * retrieve History of current profile
     * @param myCallback
     * @param Uid
     */
    @Override
    public void retrieveHistory(final MyCallbackString myCallback, final String Uid)
    {
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String historyStr;
                for (DataSnapshot data : dataSnapshot.child(Uid).child("profiles").getChildren()) {
                    if (data.child("thisProfile").getValue(boolean.class)) {
                        historyStr = data.child("history").getValue(String.class);
                        myCallback.onCallback(historyStr);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    /**
     * edit history of park connector
     * @param uId
     * @param history
     */
    @Override
    public void editHistory(String uId, String history) {
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.child(uId).child("profiles").getChildren()) {
                    if (data.child("thisProfile").getValue(boolean.class)) {
                        userRef.child(uId).child("profiles").child(data.getKey()).child("history").setValue(history);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public interface MyCallbackProfile{
        void onCallback(String name, String dob);
    }

    public interface MyCallbackString {
        void onCallback(String value);
    }

    public interface MyCallbackHashMap{
        void onCallback(HashMap<String,String> value);
    }


}
