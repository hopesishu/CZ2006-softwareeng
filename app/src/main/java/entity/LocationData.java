package entity;

import java.util.ArrayList;

public class LocationData {
    private ArrayList<ArrayList<String>> data;

    public LocationData(){
        data = new ArrayList<>();
    }

    public void add(ArrayList<String> item){
        data.add(item);
    }

    public ArrayList<ArrayList<String>> getDatabase() {
        return data;
    }
}
