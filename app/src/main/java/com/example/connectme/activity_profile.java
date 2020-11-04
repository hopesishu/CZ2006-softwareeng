package com.example.connectme;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        //@yuchen something wrong with the logic
        //app always crashes when this code is uncommented

//        profileMgr = new ProfileMgr();
//        String apple = "2";
//
//        TextView profile_name = (TextView) findViewById(R.id.profile_name);
//        TextView dateOfBirth = (TextView) findViewById(R.id.date_of_birth);
//
//        profileMgr.retrieveCurrentProfileName(value -> profile_name.setText(value), "1TYWJLwtCWglNM5wm2pRFtPcneU2");
//        dateOfBirth.setText("Date Of Birth: " + apple);

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
}
