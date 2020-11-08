package com.example.connectme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import entity.AccountMgrInterface;
import control.AcctMgr;

/**
 * create new account
 * Called by: {@link LoginActivity}
 * Calls: {@link LoginActivity}
 */
public class CreateAccountActivity extends AppCompatActivity {

    public static final String TAG = "CreateAccount";
    private AccountMgrInterface accountMgr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        accountMgr = new AcctMgr();

        final EditText emailText = findViewById(R.id.emailText);
        final EditText passwordText = findViewById(R.id.password);
        final EditText confirmPasswordText = findViewById(R.id.confirmPassword);
        final EditText firstNameText = findViewById(R.id.firstName);
        final EditText lastNameText = findViewById(R.id.lastName);
        final EditText dateOfBirthText = findViewById(R.id.DOB);
        final Button createAccount = findViewById(R.id.create_account_button);

        Toolbar toolbar = findViewById(R.id.create_account_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);


        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Submit pressed.");
                final String password = CreateAccountActivity.this.getInput(passwordText);
                final String email = CreateAccountActivity.this.getInput(emailText);
                final String confirmPassword = CreateAccountActivity.this.getInput(confirmPasswordText);
                final String firstName = CreateAccountActivity.this.getInput(firstNameText);
                final String lastName = CreateAccountActivity.this.getInput(lastNameText);
                final String dob = CreateAccountActivity.this.getInput(dateOfBirthText);


                accountMgr.createAccount(email, confirmPassword, firstName, lastName, dob, CreateAccountActivity.this);
            }
        });

        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed(); // Implemented by activity
        });

        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed(); // Implemented by activity
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public String getInput(EditText editText) {
        return editText.getText().toString().trim();
    }

    /**
     * Based on Observer Pattern design.
     * Offers a subscription mechanism for each class Activity/Fragment to subscribe to
     * @param event Event to be sent through EventBus
     */
    @Subscribe
    public void onEvent(CustomMessageEvent event){
        Log.d("MAINACTIVITY EB SENDER","Username :\"" + event.getCustomMessage() + "\" Successfully Fired!");

    }

    public void onEventBus(String userId){
        CustomMessageEvent event = new CustomMessageEvent(userId);
        EventBus.getDefault().postSticky(event);
    }




}
