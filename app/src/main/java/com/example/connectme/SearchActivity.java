package com.example.connectme;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

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
    Random r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String select = getIntent().getStringExtra("Search Option");
        if (select == "park") {

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        parkDatabaseList = Park.getParkDatabase();
        ArrayList<ArrayList<String>> listOfLists = new ArrayList<ArrayList<String>>();
//        if (select == "hawker"){
//            //call hawker list, listOfLists = function
//        }
//        else if (select == "park"){
//            isHawker = false;
//            //call park list, listOfLists = function
//            for (int i=0; i<listOfLists.size(); i++) {
//                r = new Random();
//                int ranNum = r.nextInt(20);
//                listOfLists.get(i).set(2, parkDatabaseList.get(ranNum));
//            }
//        }

        ArrayList<String> list1 = new ArrayList<String>();
        list1.add("Jurong West Hawker center");
        list1.add("Jurong West st 100");
        list1.add("https://www.nea.gov.sg/images/default-source/Hawker-Centres-Division/resize_1262155861677.jpg");
        list1.add("polyline");
        listOfLists.add(list1);

        ArrayList<String> anotherList = new ArrayList<String>();

        anotherList.add("Taman Jurong Hawker");
        anotherList.add("Yung Ho Road");
        anotherList.add("http://www.nea.gov.sg/images/default-source/Hawker-Centres-Division/resize_1267879560483.jpg");
        anotherList.add("polyline");
        listOfLists.add(anotherList);

        ArrayList<String> anotherList2 = new ArrayList<String>();

        anotherList2.add("Ellias Mall Hawker");
        anotherList2.add("Pasir Ris");
        anotherList2.add("http://www.nea.gov.sg/images/default-source/Hawker-Centres-Division/resize_1262154766447.jpg");
        anotherList2.add("polyline");
        listOfLists.add(anotherList2);

        ArrayList<String> anotherList3 = new ArrayList<String>();

        anotherList3.add("Tampines Hawker");
        anotherList3.add("Tampines");
        anotherList3.add("http://www.nea.gov.sg/images/default-source/Hawker-Centres-Division/resize_1267846802175.jpg");
        anotherList3.add("polyline");
        listOfLists.add(anotherList3);

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
        // add in list for polyline




        moviesList = new ArrayList<>();
        moviesList.add("Iron Man");
        moviesList.add("The Incredible Hulk");
        moviesList.add("Iron Man 2");
        moviesList.add("Thor");
        moviesList.add("Captain America: The First Avenger");
        moviesList.add("The Avengers");
        moviesList.add("Iron Man 3");
        moviesList.add("Thor: The Dark World");
        moviesList.add("Captain America: The Winter Soldier");
        moviesList.add("Guardians of the Galaxy");
        moviesList.add("Avengers: Age of Ultron");
        moviesList.add("Ant-Man");
        moviesList.add("Captain America: Civil War");
        moviesList.add("Doctor Strange");
        moviesList.add("Guardians of the Galaxy Vol. 2");
        moviesList.add("Spider-Man: Homecoming");
        moviesList.add("Thor: Ragnarok");
        moviesList.add("Black Panther");
        moviesList.add("Avengers: Infinity War");
        moviesList.add("Ant-Man and the Wasp");
        moviesList.add("Captain Marvel");
        moviesList.add("Avengers: Endgame");
        moviesList.add("Spider-Man: Far From Home");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerAdapter = new RecyclerAdapter(nameList, addressList, urlList, isHawker);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
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