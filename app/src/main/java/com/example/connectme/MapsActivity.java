package com.example.connectme;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.data.kml.KmlLineString;
import com.google.maps.android.data.kml.KmlPlacemark;
import com.google.maps.android.data.kml.KmlPoint;
import com.thecodecity.mapsdirection.directionhelpers.TaskLoadedCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import control.HawkerCtrl;
import control.KMLUtil;
import control.NavigationCtrl;
import control.PCNCtrl;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    //View
    private GoogleMap mMap;
    private Chip chip_pcn;
    private Chip chip_hawker;
    private FloatingActionButton button_navigate;
    private ArrayList<Object> temp_map_objects = new ArrayList<>();

    //Permission
    private static final int ACCESS_FINE_LOCATION_CODE = 1;
    private boolean permission_acquired = false;
    private FusedLocationProviderClient fusedLocationClient;

    //Incoming Intent Management
    private boolean hasIntent;
    private String intent_name;
    private boolean intent_isHawker;

    //Outgoing Intent Management
    private String nearest_pcn_name;

    //Manager Instances
    private HawkerCtrl mHawkerCtrl;
    private PCNCtrl mPCNCtrl;
    private NavigationCtrl mNavigationCtrl;

    //uId
    private String uId;

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
                        Intent home_intent = new Intent(getApplicationContext(), MainActivity.class);
                        if (nearest_pcn_name != null) {
                            home_intent.putExtra("nearest", nearest_pcn_name);
                        }
                        startActivity(home_intent);
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


        button_navigate = (FloatingActionButton)findViewById(R.id.button_navigate);
        button_navigate.setVisibility(View.INVISIBLE);
        button_navigate.setEnabled(false);


        chip_pcn = findViewById(R.id.chip_pcn);
        chip_pcn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mPCNCtrl.show();
                }
                else{
                    mPCNCtrl.hide();
                }
            }
        });

        chip_hawker = findViewById(R.id.chip_hawker);
        chip_hawker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mHawkerCtrl.show();
                }
                else{
                    mHawkerCtrl.hide();
                }
            }
        });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(1.2903, 103.85), 10));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mNavigationCtrl.removeNavigation();
                removeTempMapObject();
                boolean isOnPcn = false;
                boolean isOnHawker = false;
                if(mPCNCtrl.isPCNShown()) {
                    for (Polyline polyline : PCNCtrl.getArrayList_pcn()) {
                        if (PolyUtil.isLocationOnPath(latLng, polyline.getPoints(), true, 40)) {
                            double min_distance = 10; //Big enough number;
                            LatLng nearest_latlng = null;
                            for (LatLng poly_latlng : polyline.getPoints()) {
                                double distance = distance(poly_latlng, mNavigationCtrl.getCurrent_latlng());
                                if (distance < min_distance) {
                                    min_distance = distance;
                                    nearest_latlng = poly_latlng;
                                }
                            }
                            createTempMarker(nearest_latlng, (String)polyline.getTag(), BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                            isOnPcn = true;
                            break;
                        }
                    }
                }
                if(mHawkerCtrl.isHawkerShown()){
                    for(GroundOverlay groundOverlay : HawkerCtrl.getArrayList_hawker()){
                        if (PolyUtil.isLocationOnPath(latLng, new ArrayList<LatLng>(Collections.singleton(groundOverlay.getPosition())), true, 300)){
                            createTempMarker(latLng, (String)groundOverlay.getTag(), BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            isOnHawker = true;
                            break;
                        }
                    }
                }
                if (!isOnPcn && !isOnHawker) {
                    button_navigate.setVisibility(View.INVISIBLE);
                    button_navigate.setEnabled(false);
                }
            }
        });

        mHawkerCtrl = new HawkerCtrl(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name), (float)0.3, 500);
        mPCNCtrl =  new PCNCtrl(0xff008080, 10);
        mNavigationCtrl = new NavigationCtrl();

        try {
            createPcn();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        try {
            createHawker();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }


        if (hasIntent){
            if(intent_isHawker){
                for(GroundOverlay groundOverlay: HawkerCtrl.getArrayList_hawker()){
                    if (((String)groundOverlay.getTag()).equals(intent_name)){
                        createTempMarker(groundOverlay.getPosition(), intent_name, BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        break;
                    }
                }
            }
            else{
                boolean isMarkerCreated = false;
                for (Polyline polyline: PCNCtrl.getArrayList_pcn()){
                    if (intent_name.equals(((ArrayList<String>)polyline.getTag()).get(0))){
                        List<LatLng> latlng_list = polyline.getPoints();
                        createTempPolyline(new ArrayList<>(latlng_list), 0xff008080);
                        if (isMarkerCreated == false){
                            LatLng midpoint_latlng = latlng_list.get(latlng_list.size() / 2);
                            createTempMarker(midpoint_latlng, intent_name, BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                            isMarkerCreated = true;
                        }
                    }
                }
            }
        }
    }

    //Subscribe for uId
    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(CustomMessageEvent event) {
        uId = event.getCustomMessage();
    }




    //Creation and destruction methods of temporary map objects
    public void createTempMarker(LatLng latLng, String title, BitmapDescriptor icon){
        Marker temp_marker = mMap.addMarker(new MarkerOptions().position(latLng).title(title).icon(icon));
        temp_marker.showInfoWindow();
        mNavigationCtrl.setName(title);
        mNavigationCtrl.setLatLng(latLng);
        button_navigate.setVisibility(View.VISIBLE);
        button_navigate.setEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        temp_map_objects.add(temp_marker);
    }

    public void createTempPolyline(ArrayList<LatLng> latLngs, int color){
        Polyline intent_temp_polyline = mMap.addPolyline(new PolylineOptions().addAll(latLngs).width(10).clickable(false).color(color));
        temp_map_objects.add(intent_temp_polyline);
    }

    public void removeTempMapObject(){
        for(Object item : temp_map_objects){
            if (item instanceof Marker)
                ((Marker)item).remove();
            else
                ((Polyline)item).remove();
        }
    }




    //Handles my location button click & permission
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

    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { permission },
                    requestCode);
        }
        else {
            permission_acquired = true;
        }
    }

    @SuppressLint("MissingPermission")
    public void moveCameraToUser(){
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng current_LatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mNavigationCtrl.setCurrent_latlng(current_LatLng);
                            mMap.addMarker(new MarkerOptions().position(current_LatLng).title("You Are Here!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current_LatLng, 12));
                            calculateNearestPcn();
                        }
                    }
                });
    }





    //Handles Navigation button click
    public void onNavigationClick(View view){
        mNavigationCtrl.navigate(this, uId, getString(R.string.google_api_key));
    }

    public void onTaskDone(Object... values){
        mNavigationCtrl.createNavigation(mMap, values);
    }

    public void calculateNearestPcn(){
        double min_distance = 10;

        for (Polyline polyline : PCNCtrl.getArrayList_pcn()){
            for (LatLng latlng : polyline.getPoints()){
                double distance = distance(latlng, mNavigationCtrl.getCurrent_latlng());
                if (distance < min_distance) {
                    min_distance = distance;
                    nearest_pcn_name = ((ArrayList<String>)polyline.getTag()).get(0);
                }
            }
        }
    }





    //Create Pcn objects and set to invisible
    public void createPcn()  throws IOException, XmlPullParserException {
        for (KmlPlacemark placemark : KMLUtil.unwrap(R.raw.park_connector_loop_kml, mMap,this)){
            if(placemark.getGeometry().getGeometryType().equals("LineString")) {

                KmlLineString lineString = (KmlLineString) placemark.getGeometry();

                ArrayList<String> attributes = new ArrayList<>();
                attributes.add("PARK");
                attributes.add("PCN_LOOP");
                ArrayList<String> v = KMLUtil.getAttributesValue(placemark, attributes);

                mPCNCtrl.add(mMap, lineString.getGeometryObject(), v.get(0), v.get(1));
            }
        }
    }




    //Create Hawker objects and set to invisible
    public void createHawker() throws IOException, XmlPullParserException {
        for (KmlPlacemark placemark : KMLUtil.unwrap(R.raw.hawker_centres_kml, mMap, this)){
            if(placemark.getGeometry().getGeometryType().equals("Point")) {

                KmlPoint point = (KmlPoint) placemark.getGeometry();
                LatLng latLng = new LatLng(point.getGeometryObject().latitude, point.getGeometryObject().longitude);

                ArrayList<String> attributes = new ArrayList<>();
                attributes.add("NAME");
                attributes.add("ADDRESSSTREETNAME");
                attributes.add("PHOTOURL");
                ArrayList<String> v = KMLUtil.getAttributesValue(placemark, attributes);

                mHawkerCtrl.add(mMap, latLng, v.get(0), v.get(1), v.get(2));
            }
        }
    }





    //Calculate distance between LatLngs
    public double distance(LatLng latLng1, LatLng latLng2){
        double lat_diff, lng_diff;
        lat_diff = latLng1.latitude - latLng2.latitude;
        lng_diff = latLng1.longitude - latLng2.longitude;
        return lat_diff * lat_diff + lng_diff * lng_diff;
    }
}