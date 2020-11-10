package com.example.connectme;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import control.AcctMgr;
import entity.AccountMgrInterface;

public class activity_feedback extends AppCompatActivity {

    private AccountMgrInterface accountMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        accountMgr = new AcctMgr();

        final EditText feedbackText = findViewById(R.id.feedback_text);
        final Button submitFeedback = findViewById(R.id.submit_feedback);


        submitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "onClick: Submit pressed.");
                String feedback = activity_feedback.this.getInput(feedbackText);
                accountMgr.addFeedback(feedback);
                Toast.makeText(activity_feedback.this, "Feedback submitted!", Toast.LENGTH_SHORT).show();
                feedbackText.setText("");
            }
        });

        //linking toolbar
        Toolbar toolbar = findViewById(R.id.feedback_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed(); // Implemented by activity
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
    public void onBackPressed() {
        startActivity(new Intent(this, activity_settings.class));
        finish();
    }

}
