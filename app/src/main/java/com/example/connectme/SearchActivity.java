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


public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    List<String> moviesList;
    List<String> nameList;
    List<String> addressList;
    List<String> urlList;
    List<Object> polylineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        List<ArrayList<String>> listOfLists = new ArrayList<ArrayList<String>>();
        ArrayList<String> list1 = new ArrayList<String>();
        list1.add("Jurong West Hawker center");
        list1.add("Jurong West st 100");
        list1.add("url");
        list1.add("polyline");
        listOfLists.add(list1);

        ArrayList<String> anotherList = new ArrayList<String>();

        anotherList.add("Taman Jurong Hawker");
        anotherList.add("Yung Ho Road");
        anotherList.add("url");
        anotherList.add("polyline");
        listOfLists.add(anotherList);

        nameList = new ArrayList<>();
        addressList = new ArrayList<>();
        urlList = new ArrayList<>();

        for (List<String> listitem : listOfLists) {
            for (String item : listitem ) {
                if (item == listitem.get(0)) {
                    nameList.add(item);
                }
                else if (item == listitem.get(1)) {
                    addressList.add(item);
                }
                else if (item == listitem.get(3)) {
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
        recyclerAdapter = new RecyclerAdapter(nameList, addressList);
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