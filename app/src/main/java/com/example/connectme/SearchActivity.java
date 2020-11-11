package com.example.connectme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entity.Park;


public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    List<String> moviesList;
    List<String> nameList;
    List<String> addressList;
    List<String> urlList;
    ArrayList<String> parkDatabaseList;
    List<Object> polylineList;
    boolean isHawker = true;
    boolean flag = true;
    Random r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        int select = intent.getExtras().getInt("SEARCH_OPTION");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        parkDatabaseList = Park.getParkDatabase();
        ArrayList<ArrayList<String>> listOfLists = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> tempList = new ArrayList<ArrayList<String>>();

        if (select == 1){

            listOfLists = MapsActivity.database_hawker.getDatabase();
        }
        else if (select == 2){
            isHawker = false;
            listOfLists = MapsActivity.database_pcn.getDatabase();
            for (int i=0; i<listOfLists.size()-1; i++){
                for (int j=i+1; j<listOfLists.size(); j++) {
                    if (listOfLists.get(i).get(0).equals(listOfLists.get(j).get(0))) {
                        flag = false;
                    }
                }
                if (flag) {
                    tempList.add(listOfLists.get(i));

                }
                flag =true;
            }
            listOfLists = tempList;
            for (int i=0; i<listOfLists.size(); i++) {
                r = new Random();
                int ranNum = r.nextInt(60);
                listOfLists.get(i).set(2, parkDatabaseList.get(ranNum));
            }
        }
       // listOfLists = MapsActivity.database_hawker.getDatabase();


//        ArrayList<String> list1 = new ArrayList<String>();
//        list1.add("Jurong West Hawker center");
//        list1.add("Jurong West st 100");
//        list1.add("https://www.nea.gov.sg/images/default-source/Hawker-Centres-Division/resize_1262155861677.jpg");
//        list1.add("polyline");
//        listOfLists.add(list1);
//
//        ArrayList<String> anotherList = new ArrayList<String>();
//
//        anotherList.add("Taman Jurong Hawker");
//        anotherList.add("Yung Ho Road");
//        anotherList.add("http://www.nea.gov.sg/images/default-source/Hawker-Centres-Division/resize_1267879560483.jpg");
//        anotherList.add("polyline");
//        listOfLists.add(anotherList);
//
//        ArrayList<String> anotherList2 = new ArrayList<String>();
//
//        anotherList2.add("Ellias Mall Hawker");
//        anotherList2.add("Pasir Ris");
//        anotherList2.add("http://www.nea.gov.sg/images/default-source/Hawker-Centres-Division/resize_1262154766447.jpg");
//        anotherList2.add("polyline");
//        listOfLists.add(anotherList2);
//
//        ArrayList<String> anotherList3 = new ArrayList<String>();
//
//        anotherList3.add("Tampines Hawker");
//        anotherList3.add("Tampines");
//        anotherList3.add("http://www.nea.gov.sg/images/default-source/Hawker-Centres-Division/resize_1267846802175.jpg");
//        anotherList3.add("polyline");
//        listOfLists.add(anotherList3);

        nameList = new ArrayList<>();
        addressList = new ArrayList<>();
        urlList = new ArrayList<>();

        for (ArrayList<String> listitem : listOfLists) {
            for (String item : listitem ) {
                if (item == listitem.get(0)) {
                    nameList.add(item);
                }
                else if (item == listitem.get(1)) {
                    addressList.add(item);
                }
                else if (item == listitem.get(2)) {
                    urlList.add((String) item);
                }
            }
        }


        recyclerView = findViewById(R.id.recyclerView);
        recyclerAdapter = new RecyclerAdapter(nameList, addressList, urlList, isHawker);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


        //linking bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigationHome);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }
}