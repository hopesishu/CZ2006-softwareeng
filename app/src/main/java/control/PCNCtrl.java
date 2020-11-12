package control;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import entity.LocationData;

public class PCNCtrl {
    private static ArrayList<Polyline> arrayList_pcn = new ArrayList<>();
    private static LocationData database_pcn = new LocationData();
    private boolean is_pcn_shown;
    private int color;
    private float width;

    public PCNCtrl(int color, float width) {
        this.color = color;
        this.width = width;
    }

    public boolean isPCNShown() {
        return is_pcn_shown;
    }

    public static LocationData getDatabase_pcn() {
        return database_pcn;
    }

    public static ArrayList<Polyline> getArrayList_pcn() {
        return arrayList_pcn;
    }

    public void add(GoogleMap map, ArrayList<LatLng> latLngs, String name, String loopName) {
        Polyline polyline = map.addPolyline(new PolylineOptions().addAll(latLngs).width(10).clickable(false).color(0xff008080));
        polyline.setTag(name + ", " + loopName);
        arrayList_pcn.add(polyline);
        polyline.setVisible(false);

        ArrayList<String> item = new ArrayList<>();
        item.add(name);
        item.add(loopName);
        item.add(" ");
        database_pcn.add(item);
    }

    public void show() {
        for (Polyline polyline : arrayList_pcn) {
            polyline.setVisible(true);
        }
        is_pcn_shown = true;
    }

    public void hide() {
        for (Polyline polyline : arrayList_pcn) {
            polyline.setVisible(false);
        }
        is_pcn_shown = false;
    }
}
