package control;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import entity.Profile;
import entity.ProfileMgrInterface;

/**
 * implements ProfileMgrInterface
 * allows the app to interact with firebase database, acting as an control class for Profile(entity class)
 */
public class ProfileMgr implements ProfileMgrInterface {
    private FirebaseDatabase database;
    private DatabaseReference userRef;

    /**
     * retrieve sub profile name and ID
     * @param myCallback
     * @param Uid
     */
    @Override
    public void retrieveSubprofileNameAndID(final MyCallbackHashMap myCallback, final String Uid)
    {
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, String> profileName = new HashMap<>();
                if (dataSnapshot.child(Uid).child("profiles").getChildrenCount()!=1) {
                    for (DataSnapshot data : dataSnapshot.child(Uid).child("profiles").getChildren()) {
                        if (!data.child("thisProfile").getValue(boolean.class)) {
                            profileName.put(data.child("name").getValue(String.class), data.getKey());
                        }
                    }
                    myCallback.onCallback(profileName);
                }
                else
                    myCallback.onCallback(profileName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

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
     * retrieve particular profile with userID and profileID
     * @param myCallback
     * @param uId
     * @param pId
     */
    @Override
    public void retrieveProfile(MyCallbackProfile myCallback, String uId, String pId) {
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child(uId).child("profiles").child(pId).child("name").getValue(String.class);
                String dob = dataSnapshot.child(uId).child("profiles").child(pId).child("dateOfBirth").getValue(String.class);

                myCallback.onCallback(name, dob);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * edit current profile name with userID
     * @param uId
     * @param name
     */
    @Override
    public void editProfile(String uId, String name) {
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.child(uId).child("profiles").getChildren()) {
                    if (data.child("thisProfile").getValue(boolean.class)) {
                        userRef.child(uId).child("profiles").child(data.getKey()).child("name").setValue(name);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * edit particular profile details with userID and profileID
     * @param uId
     * @param pId
     * @param name
     * @param dob
     */
    @Override
    public void editProfile(String uId, String pId, String name, String dob) {
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        userRef.child(uId).child("profiles").child(pId).child("name").setValue(name);
        userRef.child(uId).child("profiles").child(pId).child("dateOfBirth").setValue(dob);
    }


    /**
     * set first profile in database to be default profile
     * @param Uid
     */
    @Override
    public void setDefaultProfile(String Uid) {
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean flag=false;
                String profileId;
                int numProfiles = (int) dataSnapshot.child(Uid).child("profiles").getChildrenCount();

                for (DataSnapshot data: dataSnapshot.child(Uid).child("profiles").getChildren())
                {
                    if (!flag)
                    {
                        userRef.child(Uid).child("profiles").child(data.getKey()).child("thisProfile").setValue(true);
                        flag = true;
                        continue;
                    }
                    else
                    {
                        profileId = data.getKey();
                        userRef.child(Uid).child("profiles").child(profileId).child("thisProfile").setValue(false);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * add profile to a existing account
     * @param Uid
     * @param name
     * @param dob
     */
    @Override
    public void addProfile(String Uid, String name, String dob) {
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        final Profile profile = new Profile(name, dob);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int maxProfileId=0;
                int numProfile = (int) dataSnapshot.child(Uid).child("profiles").getChildrenCount();
                for (DataSnapshot data: dataSnapshot.child(Uid).child("profiles").getChildren())
                {
                    if (Integer.parseInt(data.getKey())>maxProfileId)
                        maxProfileId = Integer.parseInt(data.getKey());
                }

                userRef.child(Uid).child("profiles").child(Integer.toString(maxProfileId+1)).setValue(profile);
                switchProfile(Uid,Integer.toString(maxProfileId+1 ));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * switching of profiles
     * @param Uid
     * @param profileId
     */
    @Override
    public void switchProfile(String Uid, String profileId) {
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        System.out.println("change profile to " + profileId);
        userRef.child(Uid).child("profiles").child(profileId).child("thisProfile").setValue(true);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numProfiles = (int) dataSnapshot.child(Uid).child("profiles").getChildrenCount();
                if (numProfiles!=1)
                {
                    for (DataSnapshot data:dataSnapshot.child(Uid).child("profiles").getChildren() )
                    {
                        if (data.getKey()!=profileId)
                        {
                            System.out.println("profiles that are set to false" + data.getKey());
                            userRef.child(Uid).child("profiles").child(data.getKey()).child("thisProfile").setValue(false);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * delete profile
     * @param Uid
     * @param profileId
     */
    @Override
    public void deleteProfile(String Uid, String profileId) {
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");

        //delete profile
        userRef.child(Uid).child("profiles").child(profileId).removeValue();

        //change back to default profile
        setDefaultProfile(Uid);

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
