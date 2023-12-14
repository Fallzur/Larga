package com.example.larga;

import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.larga.databinding.ActivityUserDriverViewHistoryBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class USER_DRIVER_VIEW_HISTORY extends FragmentActivity implements OnMapReadyCallback, RoutingListener {

    private GoogleMap mMap;

    LatLng pointB,pointA;
    Intent i;
    Button cancel ,accept,arrived,startTrip,cancelTrip,finishTripe,cash;
    String Sid, Sname, Scontact, SlocationA, SlatA, SlonA, SlocationB, SlatB, SlonB,Spayment,Stype,SpapalitInfo,Stotal;
    TextView tvname, tvnumber, tvlocationA, tvlocationB,papalitInfo;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    String driverName,driverContact;
    RelativeLayout relativeInfo;
    TextView total,type;

    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.black};


    private ActivityUserDriverViewHistoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);

        binding = ActivityUserDriverViewHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        polylines = new ArrayList<>();

        total = findViewById(R.id.histotalcost);
        type = findViewById(R.id.historyTittle2);



        tvname = findViewById(R.id.his_name);
        tvnumber = findViewById(R.id.his_number);
        tvlocationA = findViewById(R.id.his_locationA);
        tvlocationB = findViewById(R.id.his1_locationB);

        i = getIntent();
        Sid = i.getStringExtra("id");
        Sname = i.getStringExtra("name");
        tvname.setText(Sname);
        Scontact = i.getStringExtra("number");
        tvnumber.setText(Scontact);

        SlocationA = i.getStringExtra("locationA");
        tvlocationA.setText(SlocationA);
        SlatA = i.getStringExtra("latA");
        SlonA = i.getStringExtra("lonA");

        SlocationB = i.getStringExtra("locationB");
        tvlocationB.setText(SlocationB);
        SlatB = i.getStringExtra("latB");
        SlonB = i.getStringExtra("lonB");
        Stype = i.getStringExtra("type");
        type.setText(Stype);
        Spayment = i.getStringExtra("Paymenttype");
        SpapalitInfo = i.getStringExtra("papalitInfo");

        Stotal = i.getStringExtra("tol");
        total.setText(Stotal+" "+"Pesos");

        papalitInfo = findViewById(R.id.his_InformationOfPapalit);
        papalitInfo.setText(i.getStringExtra("papalitInfo"));

        if (SpapalitInfo.isEmpty()){
            relativeInfo = findViewById(R.id.history_RL_information);
            relativeInfo.setVisibility(View.INVISIBLE);
        }else {
            relativeInfo = findViewById(R.id.history_RL_information);
            relativeInfo.setVisibility(View.VISIBLE);
        }
        if (Stype.equals("Motour Set A") || Stype.equals("Motour Set B")){
            tvlocationB.setText(Stype);
        }
        if (Stype.equals("withmotor")){
            type.setText("");
        }





        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        // Check if latitude and longitude values are valid for both points
        if (isValidLatLng(SlatA, SlonA) && isValidLatLng(SlatB, SlonB)) {
            // LatLng for Point A
            pointA = new LatLng(Double.parseDouble(SlatA), Double.parseDouble(SlonA));

            // LatLng for Point B
             pointB = new LatLng(Double.parseDouble(SlatB), Double.parseDouble(SlonB));

            // Marker for Point A
            mMap.addMarker(new MarkerOptions().position(pointA).title(SlocationA));

            // Marker for Point B
            mMap.addMarker(new MarkerOptions().position(pointB).title(SlocationB));



            // Move the camera to a location that shows both markers and the highlighted route
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(pointA);
            builder.include(pointB);
            LatLngBounds bounds = builder.build();

            // Delay the camera movement until the map is fully ready
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100)); // 100 is padding
                    getRoute(pointB);
                }
            });
        } else {

            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {



                    if (tvlocationB.getText().toString().trim().equals("Motour Set A")){

                        // Existing code for adding markers
                        mMap.addMarker(new MarkerOptions().position(new LatLng(8.952099159587851, 125.492689851965)).title("")).setTitle("location1 ");
                        mMap.addMarker(new MarkerOptions().position(new LatLng(8.954267737594224, 125.4962628878145)).title("")).setTitle("location2 ");
                        mMap.addMarker(new MarkerOptions().position(new LatLng(8.999672422961002, 125.48435925381695)).title("")).setTitle("location3 ");
// LatLngs for the markers
                        LatLng MARKA = new LatLng(8.952099159587851, 125.492689851965);
                        LatLng MARKB = new LatLng(8.954267737594224, 125.4962628878145);
                        LatLng MARKC = new LatLng(8.999672422961002, 125.48435925381695);
// Create a LatLngBounds that includes all markers
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(MARKA);
                        builder.include(MARKB);
                        builder.include(MARKC);
                        LatLngBounds bounds = builder.build();
// Move the camera to include all markers with padding
                        int padding = 100; // Adjust the padding as needed
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
// Routing code
                        Routing routing = new Routing.Builder()
                                .key("AIzaSyC1z9hkQLxjjLIspZP_8WwmPC6hfCVSSng")
                                .travelMode(AbstractRouting.TravelMode.DRIVING)
                                .withListener(USER_DRIVER_VIEW_HISTORY.this)
                                .alternativeRoutes(false)
                                .waypoints(MARKA, MARKB, MARKC)
                                .build();
                        routing.execute();
                    }else {
                        // Existing code for adding markers
                        mMap.addMarker(new MarkerOptions().position(new LatLng(8.972173444744817, 125.5383803519654)).title("")).setTitle("location1 ");
                        mMap.addMarker(new MarkerOptions().position(new LatLng(8.954235966175625, 125.49629509614462)).title("")).setTitle("location2 ");
                        mMap.addMarker(new MarkerOptions().position(new LatLng(8.947834442747997, 125.54060193847276)).title("")).setTitle("location3 ");
// LatLngs for the markers
                        LatLng MarkA = new LatLng(8.972173444744817, 125.5383803519654);
                        LatLng MarkB = new LatLng(8.954235966175625, 125.49629509614462);
                        LatLng MarkC = new LatLng(8.947834442747997, 125.54060193847276);
// Create a LatLngBounds that includes all markers
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(MarkA);
                        builder.include(MarkB);
                        builder.include(MarkC);
                        LatLngBounds bounds = builder.build();
// Move the camera to include all markers with padding
                        int padding = 100; // Adjust the padding as needed
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
// Routing code
                        Routing routing = new Routing.Builder()
                                .key("AIzaSyC1z9hkQLxjjLIspZP_8WwmPC6hfCVSSng")
                                .travelMode(AbstractRouting.TravelMode.DRIVING)
                                .withListener(USER_DRIVER_VIEW_HISTORY.this)
                                .alternativeRoutes(false)
                                .waypoints(MarkA, MarkB, MarkC)
                                .build();
                        routing.execute();

                    }

                }
            });
        }
    }


    // Helper method to check if latitude and longitude values are valid
    private boolean isValidLatLng(String latitude, String longitude) {
        try {
            double lat = Double.parseDouble(latitude);
            double lon = Double.parseDouble(longitude);
            return (-90 <= lat && lat <= 90) && (-180 <= lon && lon <= 180);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
       exitApp();
    }

    public void exitApp() {
        System.exit(0);
    }














    private void getRoute(LatLng destinationLatLng) {

        Routing routing = new Routing.Builder()
                .key("AIzaSyC1z9hkQLxjjLIspZP_8WwmPC6hfCVSSng")
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(pointA, destinationLatLng)
                .build();
        routing.execute();
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingCancelled() {

    }

}