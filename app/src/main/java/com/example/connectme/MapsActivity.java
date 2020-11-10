package com.example.connectme;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.data.kml.KmlContainer;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlLineString;
import com.google.maps.android.data.kml.KmlPlacemark;
import com.google.maps.android.data.kml.KmlPoint;
import com.thecodecity.mapsdirection.directionhelpers.FetchURL;
import com.thecodecity.mapsdirection.directionhelpers.TaskLoadedCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import control.ProfileMgr;
import entity.LocationData;

/*
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback{

    private GoogleMap mMap;

    private FloatingActionButton button_location;
    private Chip chip_pcn;
    private Chip chip_hawker;

    private KmlLayer layer_pcn;
    private KmlLayer layer_hawker;

    private ArrayList<Marker> arrayList_hawker = new ArrayList<Marker>();
    private ArrayList<Polyline> arrayList_pcn = new ArrayList<Polyline>();
    private boolean isPcnShown = false;
    private boolean isHawkerShown = false;
    private ArrayList<Polyline> arrayList_navigation = new ArrayList<Polyline>();

    public static LocationData database_pcn = new LocationData();
    public static LocationData database_hawker =  new LocationData();

    private Button button_navigate;

    private String navigation_target;
    private LatLng navigation_LatLng;

    private static final int ACCESS_FINE_LOCATION_CODE = 1;
    private boolean permission_acquired = false;

    private FusedLocationProviderClient fusedLocationClient;

    private Location current_location;
    private LatLng current_LatLng = new LatLng(1.2903, 103.85);

    private Polyline current_polyline; //Ignore this variable

    private Marker polyline_temp_marker;
    private Marker intent_temp_marker;
    private Polyline intent_temp_polyline;

    //Profile Management
    private ProfileMgr mProfileMgr = new ProfileMgr();
    private String uId;

    //Intent Management
    private boolean hasIntent;
    private String intent_name;
    private boolean intent_isHawker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        intent_isHawker = getIntent().getBooleanExtra("isHawker", true);
        intent_name = getIntent().getStringExtra("name");
        hasIntent = (intent_name != null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //linking bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigationMap);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.navigationMap:
                        return true;
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

        EventBus.getDefault().register(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);


        button_navigate = (Button)findViewById(R.id.button_navigate);
        button_navigate.setVisibility(View.INVISIBLE);
        button_navigate.setEnabled(false);

        chip_pcn = findViewById(R.id.chip_pcn);
        chip_pcn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    showPCN();
                }
                else{
                    hidePCN();
                }
            }
        });

        chip_hawker = findViewById(R.id.chip_hawker);
        chip_hawker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    showHawker();
                }
                else{
                    hideHawker();
                }
            }
        });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng singapore = new LatLng(1.2903, 103.85);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(singapore));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10));

        //Somehow mysteriously this method doesn't work HERE

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.i("clicked", "Marker");
                hideNavigation();
                button_navigate.setVisibility(View.VISIBLE);
                button_navigate.setEnabled(true);
                navigation_target = marker.getTitle();
                navigation_LatLng = marker.getPosition();
                //removeTemp();
                return true;
            }
        });




        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.i("clicked", "Map");
                removeTemp();
                boolean isOnPolyline = false;
                boolean isOnMarker = false;
                if(isPcnShown) {
                    for (Polyline polyline : arrayList_pcn) {
                        if (PolyUtil.isLocationOnPath(latLng, polyline.getPoints(), true, 40)) {
                            hideNavigation();
                            button_navigate.setVisibility(View.VISIBLE);
                            button_navigate.setEnabled(true);
                            navigation_target = ((ArrayList<String>) polyline.getTag()).get(0) + ", " + ((ArrayList<String>) polyline.getTag()).get(1);
                            double min_distance = 5; //Big enough number;


                            for (LatLng poly_latlng : polyline.getPoints()) {
                                double lat_diff, lng_diff;
                                lat_diff = current_LatLng.latitude - poly_latlng.latitude;
                                lng_diff = current_LatLng.longitude - poly_latlng.longitude;
                                double distance = lat_diff * lat_diff + lng_diff * lng_diff;
                                if (distance < min_distance) {
                                    min_distance = distance;
                                    navigation_LatLng = poly_latlng;
                                }
                            }

                            polyline_temp_marker = mMap.addMarker(new MarkerOptions().position(latLng).title(navigation_target).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                            polyline_temp_marker.showInfoWindow();
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            isOnPolyline = true;
                            break;
                        }
                    }
                }
                if(isHawkerShown){
                    for(Marker marker : arrayList_hawker){
                        if (PolyUtil.isLocationOnPath(latLng, new ArrayList<LatLng>(Collections.singleton(marker.getPosition())), true, 40)){
                            hideNavigation();
                            button_navigate.setVisibility(View.VISIBLE);
                            button_navigate.setEnabled(true);
                            navigation_target = marker.getTitle();
                            navigation_LatLng = marker.getPosition();
                            marker.showInfoWindow();
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            isOnMarker = true;
                            break;
                        }
                    }
                }
                if (!isOnPolyline && !isOnMarker) {
                    button_navigate.setVisibility(View.INVISIBLE);
                    button_navigate.setEnabled(false);
                    hideNavigation();
                }
            }
        });

        try {
            createPcnPolylines();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        try {
            createHawkerMarkers();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }


        if (hasIntent){
            if(intent_isHawker){
                for(Marker marker: arrayList_hawker){
                    if (marker.getTitle() == intent_name){
                        intent_temp_marker = mMap.addMarker(new MarkerOptions().position(marker.getPosition()).title(intent_name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                        intent_temp_marker.showInfoWindow();
                        navigation_target = intent_name;
                        navigation_LatLng = marker.getPosition();
                        button_navigate.setVisibility(View.VISIBLE);
                        button_navigate.setEnabled(true);
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(navigation_LatLng));
                        break;
                    }
                }
            }
            else{
                for (Polyline polyline: arrayList_pcn){
                    if (intent_name.indexOf((String)polyline.getTag()) == 0){
                        intent_temp_polyline = mMap.addPolyline(new PolylineOptions().addAll(polyline.getPoints()).width(10).clickable(false));
                        break;
                    }
                }
            }
        }
    }


    public void removeTemp(){
        if (polyline_temp_marker != null) {
            polyline_temp_marker.remove();
        }
        if (intent_temp_marker != null) {
            intent_temp_marker.remove();
        }
        if (intent_temp_polyline != null) {
            intent_temp_polyline.remove();
        }
    }

    public void onGetMyLocationClick(View view){
        if (permission_acquired == false) {
            checkPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    ACCESS_FINE_LOCATION_CODE);
        }
        if (permission_acquired == true){
            moveCameraToUser();
        }
    }

    public void onNavigationClick(View view){
        Log.i("navigate", navigation_target);
        String url = getUrl(current_LatLng, navigation_LatLng, "driving");
        Log.i("url", url);
        new FetchURL(MapsActivity.this).execute(url, "driving");
        //mProfileMgr.editHistory(navigation_target, uId);
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    public void onTaskDone(Object... values){
        Log.i("OnTaskDone", navigation_target);
        if(current_polyline != null) current_polyline.remove();
        current_polyline = mMap.addPolyline((PolylineOptions)values[0]);
        arrayList_navigation.add(current_polyline);

    }

    public void hideNavigation(){
        for(Polyline polyline: arrayList_navigation){
            polyline.remove();
        }
        arrayList_navigation = new ArrayList<Polyline>();
    }


    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(CustomMessageEvent event) {
        //Log.d("HOMEFRAG EB RECEIVER", "Username :\"" + event.getCustomMessage() + "\" Successfully Received!");
        uId = event.getCustomMessage();
        //DisplayName.setText(usernameImported);
    }



    public void checkPermission(String permission, int requestCode)
    {
        //Acquire permission
        if (ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { permission },
                    requestCode);
        }
        else {
            permission_acquired = true;
            /*
            Toast.makeText(this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();
            */
        }
    }

    @SuppressLint("MissingPermission")

    public void moveCameraToUser(){
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            current_location = location;
                            current_LatLng =  new LatLng(current_location.getLatitude(), current_location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(current_LatLng).title("You Are Here!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current_LatLng, 12));
                        }
                    }
                });
    }

    public void showPCN(){
        for(Polyline polyline: arrayList_pcn) {
            polyline.setVisible(true);
        }
        isPcnShown = true;
    }


    public void hidePCN(){
        for(Polyline polyline: arrayList_pcn){
            polyline.setVisible(false);
        }
        isPcnShown = false;
    }


    public void createPcnPolylines()  throws IOException, XmlPullParserException {
        layer_pcn = new KmlLayer(mMap, R.raw.park_connector_loop_kml, this);
        layer_pcn.addLayerToMap();
        //.i("Has containers?", String.valueOf(layer_pcn.hasContainers()));
        layer_pcn.hasContainers();
        createPcn(layer_pcn.getContainers());
        layer_pcn.removeLayerFromMap();
    }

    public void createPcn(Iterable<KmlContainer> containers){
        for (KmlContainer container : containers) {
            //Log.i("Hi I am here!", "240");
            if (container.hasContainers()) {
                //Log.i("Hi I am here!", "242");
                createPcn(container.getContainers());
            }
            else {
                //Log.i("Hi I am here!", "246");
                for (KmlPlacemark placemark : container.getPlacemarks()){
                    //Log.i("typeis", placemark.getGeometry().getGeometryType());

                    if(placemark.getGeometry().getGeometryType().equals("LineString")) {
                        KmlLineString lineString = (KmlLineString) placemark.getGeometry();
                        //LatLng latLng = new LatLng(point.getGeometryObject().latitude, point.getGeometryObject().longitude);
                        String description = placemark.getProperty("description");

                        int start = description.indexOf("<th>PARK</th>") + "<th>PARK</th>".length() + 2;
                        int end = description.indexOf("</td>", start);
                        start += "<td>".length();
                        String parkName = description.substring(start, end);

                        start = description.indexOf("<th>PCN_LOOP</th>") + "<th>PCN_LOOP</th>".length() + 2;
                        end = description.indexOf("</td>", start);
                        start += "<td>".length();
                        String pcnLoopName = description.substring(start, end);

                        Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(lineString.getGeometryObject()).width(10).clickable(false));
                        ArrayList<String> tag = new ArrayList<String>();
                        tag.add(parkName);
                        tag.add(pcnLoopName);
                        polyline.setTag(tag);
                        arrayList_pcn.add(polyline);
                        polyline.setVisible(false);

                        ArrayList<String> item = new ArrayList<>();
                        item.add(parkName);
                        item.add(pcnLoopName);
                        item.add(" ");
                        database_pcn.add(item);
                    }
                }
            }
        }
    }

    public void showHawker(){
        for(Marker marker : arrayList_hawker) {
            marker.setVisible(true);
        }
        isHawkerShown = true;
    }


    public void hideHawker(){
        for(Marker marker : arrayList_hawker) {
            marker.setVisible(false);
        }
        isHawkerShown = false;
    }

    public void createHawkerMarkers() throws IOException, XmlPullParserException {
        layer_hawker = new KmlLayer(mMap, R.raw.hawker_centres_kml, this);
        layer_hawker.addLayerToMap();
        createHawker(layer_hawker.getContainers());
        layer_hawker.removeLayerFromMap();
    }

    public void createHawker(Iterable<KmlContainer> containers) {
        for (KmlContainer container : containers) {
            if (container.hasContainers()) {
                createHawker(container.getContainers());
            }
            else for (KmlPlacemark placemark : container.getPlacemarks()){
                if(placemark.getGeometry().getGeometryType().equals("Point")) {
                    KmlPoint point = (KmlPoint) placemark.getGeometry();
                    LatLng latLng = new LatLng(point.getGeometryObject().latitude, point.getGeometryObject().longitude);
                    String description = placemark.getProperty("description");

                    int start = description.indexOf("<th>NAME</th>") + "<th>NAME</th>".length() + 2;
                    int end = description.indexOf("</td>", start);
                    start += "<td>".length();
                    String hawkerName = description.substring(start, end);


                    start = description.indexOf("<th>ADDRESSSTREETNAME</th>") + "<th>ADDRESSSTREETNAME</th>".length() + 2;
                    end = description.indexOf("</td>", start);
                    start += "<td>".length();
                    String streetName = description.substring(start, end);

                    start = description.indexOf("<th>PHOTOURL</th>") + "<th>PHOTOURL</th>".length() + 2;
                    end = description.indexOf("</td>", start);
                    start += "<td>".length();
                    String url = description.substring(start, end);
                    Log.i("URL", url);


                    Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(hawkerName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    arrayList_hawker.add(marker);
                    marker.setVisible(false);

                    ArrayList<String> item = new ArrayList<>();
                    item.add(hawkerName);
                    item.add(streetName);
                    item.add(url);
                    database_hawker.add(item);
                }
            }
        }
    }
}