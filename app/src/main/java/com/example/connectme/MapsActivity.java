package com.example.connectme;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
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

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    //add in on create call haoran's function for list
    private GoogleMap mMap;

    private FloatingActionButton button_location;
    private Chip chip_pcn;
    //    private Chip chip_carpark;
    private Chip chip_hawker;

    private KmlLayer layer_pcn;
    //    private KmlLayer layer_carpark;
    private KmlLayer layer_hawker;

    private ArrayList<Marker> arrayList_hawker = new ArrayList<Marker>();
    //    private ArrayList<Marker> arrayList_carpark = new ArrayList<Marker>();
    private ArrayList<Polyline> arrayList_pcn = new ArrayList<Polyline>();
    private ArrayList<Polyline> arrayList_navigation = new ArrayList<Polyline>();

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

    private DatabaseReference myRef;
    //private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    //private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private String userID;

    //private HistoryMgr mHistoryMgr = new HistoryMgr();

    private String uId;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.connectme.R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);
        button_navigate = (Button) findViewById(com.example.connectme.R.id.button_navigate);
        button_navigate.setVisibility(View.INVISIBLE);
        button_navigate.setEnabled(false);

        /*
        button_location = findViewById(R.id.button_location);
        button_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (permission_acquired == false) {
                    checkPermission(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            ACCESS_FINE_LOCATION_CODE);
                }
                if (permission_acquired == true){
                    moveCameraToUser();
                }

            }
        });
        */

        chip_pcn = findViewById(com.example.connectme.R.id.chip_pcn);
        chip_pcn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        showPCN();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }
                } else {
                    hidePCN();
                }
            }
        });


//        chip_carpark = findViewById(com.example.connectme.R.id.chip_carpark);
//        chip_carpark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    try {
//                        showCarpark();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (XmlPullParserException e) {
//                        e.printStackTrace();
//                    }
//                }
//                else{
//                    hideCarpark();
//                }
//            }
//        });


        chip_hawker = findViewById(com.example.connectme.R.id.chip_hawker);
        chip_hawker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        showHawker();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }
                } else {
                    hideHawker();
                }
            }
        });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng singapore = new LatLng(1.2903, 103.85);
        //mMap.addMarker(new MarkerOptions().position(singapore).title("Singapore"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(singapore));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //Log.i("click", "marker");
                hideNavigation();
                button_navigate.setVisibility(View.VISIBLE);
                button_navigate.setEnabled(true);
                navigation_target = marker.getTitle();
                navigation_LatLng = marker.getPosition();
                if (polyline_temp_marker != null) {
                    polyline_temp_marker.remove();
                }
                return false;
            }
        });
        /*
        mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick(Polyline polyline) {
                Log.i("click", "poly");
                button_navigate.setVisibility(View.VISIBLE);
                button_navigate.setEnabled(true);
                navigation_target = ((ArrayList<String>)polyline.getTag()).get(0) + " " + ((ArrayList<String>)polyline.getTag()).get(1);
                Log.i("polyline click", ((ArrayList<String>)polyline.getTag()).get(0) + " " + ((ArrayList<String>)polyline.getTag()).get(1));
            }
        });

         */

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (polyline_temp_marker != null) {
                    polyline_temp_marker.remove();
                }
                boolean isOnPolyline = false;
                for (Polyline polyline : arrayList_pcn) {
                    if (PolyUtil.isLocationOnPath(latLng, polyline.getPoints(), true, 40)) {
                        hideNavigation();
                        Log.i("click", "poly");
                        button_navigate.setVisibility(View.VISIBLE);
                        button_navigate.setEnabled(true);
                        navigation_target = ((ArrayList<String>) polyline.getTag()).get(0) + ", " + ((ArrayList<String>) polyline.getTag()).get(1);
                        Log.i("polyline click", ((ArrayList<String>) polyline.getTag()).get(0) + ", " + ((ArrayList<String>) polyline.getTag()).get(1));
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
                        //BitmapDescriptor transparent = BitmapDescriptorFactory.fromResource(R.drawable.transparent);
                        BitmapDescriptor transparent = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                        polyline_temp_marker = mMap.addMarker(new MarkerOptions().position(latLng).title(navigation_target).icon(transparent));
                        polyline_temp_marker.showInfoWindow();
                        isOnPolyline = true;
                        break;
                    }
                }
                if (!isOnPolyline) {
                    Log.i("click", "map");
                    button_navigate.setVisibility(View.INVISIBLE);
                    button_navigate.setEnabled(false);
                    hideNavigation();
                }
            }
        });
    }

    public void onGetMyLocationClick(View view) {
        if (permission_acquired == false) {
            checkPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    ACCESS_FINE_LOCATION_CODE);
        }
        if (permission_acquired == true) {
            moveCameraToUser();
        }
    }

    public void onNavigationClick(View view) {
        //myRef = mFirebaseDatabase.getReference();
        //myRef.child("users").child("1TYWJLwtCWglNM5wm2pRFtPcneU2"/mAuth.getCurrentUser().getUid()/).child("profiles").setValue(new_visited);
        Log.i("navigate", navigation_target);
        String url = getUrl(current_LatLng, navigation_LatLng, "driving");
        new FetchURL(MapsActivity.this).execute(url, "driving");
        //mHistoryMgr.addHistory(navigation_target, uId);

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
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(com.example.connectme.R.string.google_maps_key);
        return url;
    }

    public void onTaskDone(Object... values) {
        if (current_polyline != null) current_polyline.remove();
        current_polyline = mMap.addPolyline((PolylineOptions) values[0]);
        arrayList_navigation.add(current_polyline);

    }

    public void hideNavigation() {
        for (Polyline polyline : arrayList_navigation) {
            polyline.remove();
        }
        arrayList_navigation = new ArrayList<Polyline>();
    }

    /*
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

     */


    public void checkPermission(String permission, int requestCode) {
        //Acquire permission
        if (ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    requestCode);
        } else {
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

    public void moveCameraToUser() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            current_location = location;
                            current_LatLng = new LatLng(current_location.getLatitude(), current_location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(current_LatLng).title("You Are Here!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current_LatLng, 12));
                        }
                    }
                });
    }

    public void showPCN() throws IOException, XmlPullParserException {

        layer_pcn = new KmlLayer(mMap, com.example.connectme.R.raw.park_connector_loop_kml, this);
        layer_pcn.addLayerToMap();
        //.i("Has containers?", String.valueOf(layer_pcn.hasContainers()));
        createPcnPolylines(layer_pcn.getContainers());
        layer_pcn.removeLayerFromMap();
    }


    public void hidePCN() {
        for (Polyline polyline : arrayList_pcn) {
            polyline.remove();
        }
        arrayList_pcn = new ArrayList<Polyline>();
    }


    public void createPcnPolylines(Iterable<KmlContainer> containers) {
        for (KmlContainer container : containers) {
            //Log.i("Hi I am here!", "240");
            if (container.hasContainers()) {
                //Log.i("Hi I am here!", "242");
                createPcnPolylines(container.getContainers());
            } else {
                //Log.i("Hi I am here!", "246");
                for (KmlPlacemark placemark : container.getPlacemarks()) {
                    //Log.i("typeis", placemark.getGeometry().getGeometryType());

                    if (placemark.getGeometry().getGeometryType().equals("LineString")) {
                        KmlLineString lineString = (KmlLineString) placemark.getGeometry();
                        //LatLng latLng = new LatLng(point.getGeometryObject().latitude, point.getGeometryObject().longitude);
                        String description = placemark.getProperty("description");

                        int start = description.indexOf("<th>PARK</th>\n") + "<th>PARK</th>\n".length();
                        int end = description.indexOf("</td>", start);
                        start += "<td>".length();
                        String parkName = description.substring(start, end);

                        start = description.indexOf("<th>PCN_LOOP</th>\n") + "<th>PCN_LOOP</th>\n".length();
                        end = description.indexOf("</td>", start);
                        start += "<td>".length();
                        String pcnLoopName = description.substring(start, end);

                        Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(lineString.getGeometryObject()).width(10).clickable(false));
                        ArrayList<String> tag = new ArrayList<String>();
                        tag.add(parkName);
                        tag.add(pcnLoopName);
                        polyline.setTag(tag);
                        arrayList_pcn.add(polyline);

                    }
                }
            }
        }
    }


//    public void showCarpark() throws IOException, XmlPullParserException {
//        //Log.i("qwe", "success");
//
//        //CSVReader reader = new CSVReader(new FileReader("C:\\Users\\11219\\AndroidStudioProjects\\Gmap\\app\\src\\main\\res\\raw\\hdb_carpark.csv"));
//        //Log.i("qwe", "success");
//        InputStream is = getResources().openRawResource(com.example.connectme.R.raw.hdb_carpark);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
//        String nextLine = "";
//        nextLine = reader.readLine();
//        try {
//            while ((nextLine = reader.readLine()) != null) {
//                //Log.i("qwe", nextLine[0]);
//                String[] tokens = nextLine.split(",");
//                Double svy_x = Double.parseDouble(tokens[2].substring(1,tokens[2].length()-1));
//                Double svy_y = Double.parseDouble(tokens[3].substring(1,tokens[3].length()-1));
//                SVY21Coordinate svy = new SVY21Coordinate(svy_y, svy_x);
//                LatLonCoordinate latlon = SVY21.computeLatLon(svy);
//                LatLng latLng= new LatLng(latlon.getLatitude(), latlon.getLongitude());
//                String name = tokens[0].substring(1,tokens[0].length()-1) + ", " + tokens[1].substring(1,tokens[1].length()-1);
//                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
//                arrayList_carpark.add(marker);
//            }
//        } catch (IOException e) {
//
//
//        }
//    }
//
//
//    public void hideCarpark(){
//        for(Marker marker : arrayList_carpark){
//            marker.remove();
//        }
//        arrayList_carpark = new ArrayList<Marker>();
//    }


    public void showHawker() throws IOException, XmlPullParserException {
        layer_hawker = new KmlLayer(mMap, com.example.connectme.R.raw.hawker_centres_kml, this);
        layer_hawker.addLayerToMap();
        createHawkerMarkers(layer_hawker.getContainers());
        layer_hawker.removeLayerFromMap();
    }


    public void hideHawker() {
        for (Marker marker : arrayList_hawker) {
            marker.remove();
        }
        arrayList_hawker = new ArrayList<Marker>();
    }


    public void createHawkerMarkers(Iterable<KmlContainer> containers) {
        for (KmlContainer container : containers) {
            if (container.hasContainers()) {
                createHawkerMarkers(container.getContainers());
            } else for (KmlPlacemark placemark : container.getPlacemarks()) {
                if (placemark.getGeometry().getGeometryType().equals("Point")) {
                    KmlPoint point = (KmlPoint) placemark.getGeometry();
                    LatLng latLng = new LatLng(point.getGeometryObject().latitude, point.getGeometryObject().longitude);
                    String description = placemark.getProperty("description");
                    int start = description.indexOf("<th>NAME</th>\n") + "<th>NAME</th>\n".length();
                    int end = description.indexOf("</td>", start);
                    start += "<td>".length();
                    String hawkerName = description.substring(start, end);
                    Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(hawkerName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    arrayList_hawker.add(marker);

                }
            }
        }
    }
}