package com.example.connectme;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import control.ProfileMgr;
import entity.ProfileMgrInterface;

public class activity_profile extends AppCompatActivity {
    //vars
    private ProfileMgrInterface profileMgr;
    private String uId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileMgr = new ProfileMgr();
        String apple = "2";
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        TextView profile_name = (TextView) findViewById(R.id.profile_name);
        TextView dateOfBirth = (TextView) findViewById(R.id.date_of_birth);

        profileMgr.retrieveCurrentProfileName(value -> profile_name.setText(value), "1TYWJLwtCWglNM5wm2pRFtPcneU2");
        dateOfBirth.setText("Date Of Birth: " + apple);


    }
}
