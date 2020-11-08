package com.example.connectme;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import control.HistoryMgr;
import control.ProfileMgr;
import entity.ProfileMgrInterface;

public class activity_profile extends AppCompatActivity {
    //vars
    private ProfileMgrInterface profileMgr;
    private String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        profileMgr = new ProfileMgr();
        HistoryMgr historyMgr = new HistoryMgr();
        String apple = "2";

        TextView profile_name = (TextView) findViewById(R.id.profile_name);
        TextView dateOfBirth = (TextView) findViewById(R.id.date_of_birth);
        TextView park_history = (TextView) findViewById(R.id.park_history);
        final EditText editHistoryText = findViewById(R.id.edit_history_str);

        final Button edit_history = findViewById(R.id.edit_history);

        profileMgr.retrieveCurrentProfileName(value -> profile_name.setText(value), uId);
        profileMgr.retrieveCurrentDOB(value -> dateOfBirth.setText(value), uId);
        profileMgr.retrieveHistory(value -> park_history.setText(value), uId);

        edit_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "onClick: Submit pressed.");
                String history = activity_profile.this.getInput(editHistoryText);
                history = park_history.getText() + "\n" + history + ".";
                profileMgr.editHistory(uId, history);
            }
        });


        //linking bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigationInfo);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.navigationMap:
                        //startActivity(new Intent(getApplicationContext(), activity_map.class));
                        //overridePendingTransition(0,0);
                        //return true;
                    case R.id.navigationInfo:
                        startActivity(new Intent(getApplicationContext(), activity_settings.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigationHome:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }
    public String getInput(EditText editText) {
        return editText.getText().toString().trim();
    }

    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(CustomMessageEvent event) {
        //Log.d("HOMEFRAG EB RECEIVER", "Username :\"" + event.getCustomMessage() + "\" Successfully Received!");
        uId = event.getCustomMessage();
        //DisplayName.setText(usernameImported);

    }
}
