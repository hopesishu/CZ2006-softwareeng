package control;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import entity.LocationData;

public class HawkerCtrl {
    private static ArrayList<GroundOverlay> arrayList_hawker = new ArrayList<GroundOverlay>();
    private static LocationData database_hawker =  new LocationData();
    private boolean is_hawker_shown;
    private BitmapDescriptor icon;
    private float transparency;
    private float radius;

    public HawkerCtrl(BitmapDescriptor icon, float transparency, float radius){
        is_hawker_shown = false;
        this.icon = icon;
        this. transparency = transparency;
        this.radius = radius;
    }

    public boolean isHawkerShown(){
        return is_hawker_shown;
    }

    public static LocationData getDatabase_hawker(){
        return database_hawker;
    }

    public static ArrayList<GroundOverlay> getArrayList_hawker(){
        return arrayList_hawker;
    }

    public void add(GoogleMap map, LatLng latlng, String name, String streetName, String url){
        GroundOverlay groundOverlay = map.addGroundOverlay(new GroundOverlayOptions().position(latlng, radius).image(icon));
        groundOverlay.setTag(name);
        arrayList_hawker.add(groundOverlay);
        groundOverlay.setVisible(false);
        groundOverlay.setClickable(false);
        groundOverlay.setTransparency((float)transparency);

        ArrayList<String> item = new ArrayList<>();
        item.add(name);
        item.add(streetName);
        item.add(url);
        database_hawker.add(item);
    }

    public void show(){
        for(GroundOverlay groundOverlay : arrayList_hawker) {
            groundOverlay.setVisible(true);
        }
        is_hawker_shown = true;
    }

    public void hide(){
        for(GroundOverlay groundOverlay : arrayList_hawker) {
            groundOverlay.setVisible(false);
        }
        is_hawker_shown = false;
    }
}
