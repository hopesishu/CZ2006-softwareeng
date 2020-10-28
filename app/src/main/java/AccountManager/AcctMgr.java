package AccountManager;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.connectme.CustomMessageEvent;
import com.example.connectme.LoginActivity;
import com.example.connectme.MainActivity;
import com.example.connectme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

/**
 * implements AccountMgrInterface
 * allows the app to interact with firebase database, acting as an control class for Account (entity class)
 */
public class AcctMgr implements AccountMgrInterface {

    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    private String userID;
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;

    public AcctMgr()
    {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
    }


    /**
     * This method is called when user sign in. Will go to VaccinationHistoryFragment in MainActivity if authentication is successful
     * @param email
     * @param password
     * @param activity
     */

    public void signIn(String email, String password, final Activity activity)
    {
        final ProgressBar progressBar = activity.findViewById(R.id.loading);
        final EditText emailEditText = activity.findViewById(R.id.username);
        final EditText passwordEditText = activity.findViewById(R.id.password);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            //set first profile to be default profile
                            setDefaultProfile(user.getUid());

                            //send userid on event bus
                            onEventBus(user.getUid());

                            //clear input
                            clearInput(emailEditText);
                            clearInput(passwordEditText);

                            //updateUI(user);
                            setDefaultProfile(user.getUid());

                            Intent intent = new Intent(activity, MainActivity.class);
                            activity.startActivity(intent);


                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    }
                });
    }


    /**
     * this method is called to create account for new user
     * @param email
     * @param password
     * @param firstName
     * @param lastName
     * @param dob
     * @param activity
     */
    @Override
    public void createAccount(final String email, final String password, final String firstName, final String lastName, final String dob, final Activity activity)
    {
        if (!email.equals("") && !password.equals("") && !firstName.equals("") && !lastName.equals("")) {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(activity, "User Created.", Toast.LENGTH_SHORT).show();
                        userID = mAuth.getCurrentUser().getUid();
                        System.out.println("userid" + userID);
                        Account user = new Account(email, password, firstName, lastName, dob);
                        myRef.child("users").child(userID).setValue(user);

                    }else {
                        Toast.makeText(activity, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


            activity.startActivity(new Intent(activity.getApplicationContext(), LoginActivity.class));
        }
    }

    /**
     * change password
     * @param password
     * @param uId
     */
    @Override
    public void changePassword(String password, String uId) {
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        System.out.println("uid in change password"+uId);
        userRef.child(uId).child("password").setValue(password);
    }

    /**
     * get current profile's name
     * @param myCallback
     * @param Uid
     */
    @Override
    public void retrieveCurrentProfileName(final MyCallbackString myCallback, final String Uid) {
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
     * retrieve current account's email address
     * @param myCallback
     * @param Uid
     */
    @Override
    public void retrieveEmailAdress(final MyCallbackString myCallback, final String Uid) {
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child(Uid).child("email").getValue(String.class);
                myCallback.onCallback(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * retrieve sub profile name and ID
     * @param myCallback
     * @param Uid
     */
    @Override
    public void retrieveSubprofileNameAndID(final MyCallbackHashMap myCallback, final String Uid) {
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
     * delete account
     * @param pw
     * @param view
     * @param activity
     * @param uId
     */
    @Override
    public void deleteAcc(final String pw, View view, final Activity activity, String uId)
    {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        final EditText passwordEditText =  view.findViewById(R.id.passwordDeleteAcc);

        retrieveEmailAdress(new MyCallbackString() {
            @Override
            public void onCallback(String value) {
                String email;
                email = value;
                AuthCredential credential = EmailAuthProvider.getCredential(email, pw);
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.delete();

                                    Intent intent = new Intent(activity, LoginActivity.class);
                                    activity.startActivity(intent);
                                } else {
                                    passwordEditText.setError("incorrect password entered");

                                }
                            }
                        });
            }
        }, uId);
    }


    /**
     * set first profile to be the default profile
     * @param Uid
     */
    public void setDefaultProfile(final String Uid) {
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
     * Clears any form of input that the user has typed
     * @param editText EditText id
     */
    private void clearInput(EditText editText){
        editText.getText().clear();
    }


    /**
     * Based on Observer Pattern design.
     * send userID through event bus
     * @param userID
     */
    private void onEventBus(String userID){
        CustomMessageEvent event = new CustomMessageEvent(userID);
        EventBus.getDefault().postSticky(event);
    }


    public interface MyCallbackHashMap{
        void onCallback(HashMap<String,String> value);
    }

    public interface MyCallbackString {
        void onCallback(String value);
    }

}
