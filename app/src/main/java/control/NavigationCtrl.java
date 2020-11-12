package control;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.thecodecity.mapsdirection.directionhelpers.FetchURL;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NavigationCtrl {
    private Polyline route;
    private LatLng current_latlng = new LatLng(1.2903, 103.85);
    private String target_name;
    private LatLng target_latlng;
    private ProfileMgr mProfileMgr = new ProfileMgr();
    private int database_update_count;

    public LatLng getCurrent_latlng(){
        return current_latlng;
    }

    public void setCurrent_latlng(LatLng current_latlng){
        this.current_latlng = current_latlng;
    }

    public void setName(String name){
        target_name = name;
    }

    public String getName(){
        return target_name;
    }

    public void setLatLng(LatLng latLng){
        target_latlng = latLng;
    }

    public void navigate(Context context, String uId, String API_key){
        String url = getUrl(current_latlng, target_latlng, "driving", API_key);
        new FetchURL(context).execute(url, "driving");
        database_update_count = 0;
        mProfileMgr.retrieveHistory(value -> {
            if (database_update_count > 0) return;
            database_update_count++;
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            value += formatter.format(date) + ", " + target_name + ".\n\n";
            mProfileMgr.editHistory(uId, value);
        }, uId);
    }

    public void createNavigation(GoogleMap map, Object... values){
        route = map.addPolyline(((PolylineOptions)values[0]).color(0xBB8A2BE2));
    }

    public void removeNavigation(){
        route.remove();
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode, String API_key) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=" + directionMode;
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + API_key;
        return url;
    }
}
