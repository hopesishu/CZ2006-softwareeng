package com.example.connectme;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    }
    public String getInput(EditText editText) {
        return editText.getText().toString().trim();
    }

}
