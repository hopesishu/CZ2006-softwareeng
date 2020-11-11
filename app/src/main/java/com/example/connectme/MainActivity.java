package com.example.connectme;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private CardView parkConnectorCard, hawkerCentreCard;
    private String nearestPark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        nearestPark = getIntent().getStringExtra("nearest");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //linking cards to other activities
        parkConnectorCard = (CardView) findViewById(R.id.cardParkConnectors);
        hawkerCentreCard = (CardView) findViewById(R.id.cardHawkerCentres);

        parkConnectorCard.setOnClickListener(this);
        hawkerCentreCard.setOnClickListener(this);

        //linking bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigationHome);

        TextView pcn_name = (TextView) findViewById(R.id.PCNname);
        pcn_name.setText(nearestPark);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.navigationMap:
                        startActivity(new Intent(MainActivity.this, MapsActivity.class));
//                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigationInfo:
                        startActivity(new Intent(MainActivity.this, activity_settings.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigationHome:
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent i;

        switch(view.getId()) {
            case R.id.cardParkConnectors:
                i = new Intent(this, SearchActivity.class);
                i.putExtra("SEARCH_OPTION", 2);
                startActivity(i);
                break;

            case R.id.cardHawkerCentres:
                i = new Intent(this, SearchActivity.class);
                i.putExtra("SEARCH_OPTION", 1);
                startActivity(i);
                break;
        }
    }
}