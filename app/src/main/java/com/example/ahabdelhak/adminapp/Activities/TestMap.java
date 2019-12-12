//package com.example.ahabdelhak.adminapp.Activities;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.drawable.Drawable;
//import android.location.Location;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.example.ahabdelhak.adminapp.Models.Locations;
//import com.example.ahabdelhak.adminapp.R;
//import com.firebase.geofire.GeoFire;
//import com.firebase.geofire.GeoLocation;
//import com.firebase.geofire.GeoQuery;
//import com.firebase.geofire.GeoQueryEventListener;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptor;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.navigation.NavigationView;
//import com.google.android.material.snackbar.Snackbar;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.EmailAuthProvider;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.iid.FirebaseInstanceId;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.ActionBarDrawerToggle;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.core.view.GravityCompat;
//import androidx.drawerlayout.widget.DrawerLayout;
//
//public class TestMap extends AppCompatActivity
//        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
//        GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener,
//        LocationListener {
//
//    SupportMapFragment mapFragment;
//    private GoogleMap mMap;
//    //play services
//    private static final int MY_PERMISSION_REQUEST_CODE = 7000;
//    private static final int PLAY_SERVICE_RES_REQUEST = 7001;
//
//    private LocationRequest mLocationRequest;
//    private GoogleApiClient mGoogleApiClient;
//    private Location mLastLocation;
//
//    private static int UPDATE_INTERVAL = 5000;
//    private static int FASTEST_INTERVAL = 3000;
//    private static int DISPLACEMENT = 10;
//
//    DatabaseReference Mechanics;
//    GeoFire geoFire;
//    Marker mUserMarker,MechanicMarker;
//
//    int radius = 1;//1 km
//    int Distance = 1;//3km
//    private static final int LIMIT = 3;
//
//    DatabaseReference MechanicsAvailable;
//
//    DrawerLayout drawer;
//
//    //INIT Image to convert Vector to Bitmap
//    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
//        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
//        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
//        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        vectorDrawable.draw(canvas);
//        return BitmapDescriptorFactory.fromBitmap(bitmap);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//
//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//
//        findViewById(R.id.drawer_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // open right drawer
//
//                if (drawer.isDrawerOpen(GravityCompat.END)) {
//                    drawer.closeDrawer(GravityCompat.END);
//                }
//                else
//                    drawer.openDrawer(GravityCompat.END);
//            }
//        });
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        toggle.setDrawerIndicatorEnabled(false);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        //Init  Views
//        /*
//        imagExpandable = (ImageView) findViewById(R.id.imgExpandable);
//        mbottomSheet = BottomSheetDriverFragment.newInstance("Driver Bottom Sheet");
//        imagExpandable.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mbottomSheet.show(getSupportFragmentManager(), mbottomSheet.getTag());
//            }
//        });
//        */
//        setUpLocation();
//        //updateFirebaseToken();
//    }
//
//
//    private void findMechanic() {
//        DatabaseReference Mechanics = FirebaseDatabase.getInstance().getReference("Locations");
//        GeoFire qfMechanics = new GeoFire(Mechanics);
//        final GeoQuery geoQuery = qfMechanics.queryAtLocation(new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()),
//                radius);
//        geoQuery.removeAllListeners();
//        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
//            @Override
//            public void onKeyEntered(String key, GeoLocation location) {
//                    Toast.makeText(getApplication(), "" + key, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onKeyExited(String key) {
//
//            }
//
//            @Override
//            public void onKeyMoved(String key, GeoLocation location) {
//
//            }
//
//            @Override
//            public void onGeoQueryReady() {
//                //if still not found Mechanic Then increased the radius
//                    findMechanic();
//            }
//
//            @Override
//            public void onGeoQueryError(DatabaseError error) {
//
//            }
//        });
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSION_REQUEST_CODE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (checkPlayServices()) {
//                        buildGoogleApiClient();
//                        creatLocationRequest();
//                        displayLocation();
//
//                    }
//                }
//        }
//    }
//
//    private void setUpLocation() {
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            //Request Runtime Permission
//            ActivityCompat.requestPermissions(this, new String[]{
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                    android.Manifest.permission.ACCESS_FINE_LOCATION
//            }, MY_PERMISSION_REQUEST_CODE);
//        } else {
//            if (checkPlayServices()) {
//                buildGoogleApiClient();
//                creatLocationRequest();
//                displayLocation();
//            }
//        }
//    }
//
//    private void displayLocation() {
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if(mLastLocation != null) {
//            //Presence System
//            MechanicsAvailable=FirebaseDatabase.getInstance().getReference("Locations");
//            MechanicsAvailable.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    //if have any change from Mechanics tble, we will reload all Mechanics available
//                    loadAllAvailableMechanics();
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//            final double latitud = mLastLocation.getLatitude();
//            final double logitude = mLastLocation.getLongitude();
//            if (mUserMarker != null) {
//                mUserMarker.remove();
//                mUserMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitud, logitude))
//                        .title("You"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud, logitude), 15.0f));
//                //Draw Animation rotate amrker
//                // rotateMarker(mCurrent, -360, mMap);
//                loadAllAvailableMechanics();
//            } else {
//                //Toast.makeText(Welcome.this, "Lun Hai Mera", Toast.LENGTH_SHORT).show();
//                mUserMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitud, logitude))
//                        .title("You"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud, logitude), 15.0f));
//                //Draw Animation rotate amrker
//                //rotateMarker(mCurrent, -360, mMap);
//                loadAllAvailableMechanics();
//            }
//
//        } else {
//           Snackbar.make(mapFragment.getView(), "Cannot Get Your Location", Snackbar.LENGTH_SHORT).show();
//        }
//    }
//
//    private void loadAllAvailableMechanics() {
//        //First, we will remove all markers on map included our marker
//        mMap.clear();
//        //After that just add our location marker
//        mMap.addMarker(new MarkerOptions().position(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()))
//                .title("You")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.idpi2))
//
//        );
//        //Load All Available Mechanics in Diatance 3km
//        DatabaseReference MechanicLocation = FirebaseDatabase.getInstance().getReference("Locations");
//        GeoFire geoFire = new GeoFire(MechanicLocation);
//        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), Distance);
//        geoQuery.removeAllListeners();
//        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
//            @Override
//            public void onKeyEntered(String key, final GeoLocation location) {
//                //Use Key to get  email from Table Users
//                //Table user is the Table of when Mechanic register and update information
//                DatabaseReference f_database = FirebaseDatabase.getInstance().getReference("Locations");
//                ValueEventListener listener = new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.exists()){
//                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
//
//                                Locations loc = snapshot.getValue(Locations.class);
//
//                                double lat = Double.parseDouble(loc.lat);
//                                double lng = Double.parseDouble(loc.lng);
//                                LatLng location = new LatLng(lat,lng);
//                                mMap.addMarker(new MarkerOptions().position(location).title(loc.email).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
//
//                            }
//                        }
//                    }
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        };
//                f_database.addListenerForSingleValueEvent(listener);
//            }
//
//            @Override
//            public void onKeyExited(String key) {
//
//            }
//
//            @Override
//            public void onKeyMoved(String key, GeoLocation location) {
//
//            }
//
//            @Override
//            public void onGeoQueryReady() {
//                    loadAllAvailableMechanics();
//            }
//
//            @Override
//            public void onGeoQueryError(DatabaseError error) {
//
//            }
//        });
//    }
//
//    private void creatLocationRequest() {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(UPDATE_INTERVAL);
//        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
//
//    }
//
//    private void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//        mGoogleApiClient.connect();
//    }
//
//    private boolean checkPlayServices() {
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICE_RES_REQUEST).show();
//            } else {
//                Toast.makeText(this, "This Device Is Not Supported", Toast.LENGTH_SHORT).show();
//            }
//            return false;
//        }
//        return true;
//    }
//
//    /*
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//    */
//
//    @Override
//    public void onBackPressed() {
//        if (drawer.isDrawerOpen(GravityCompat.END)) {
//            drawer.closeDrawer(GravityCompat.END);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//            Toast.makeText(getApplicationContext(), "Home is clicked", Toast.LENGTH_SHORT).show();
//
//        } else if (id == R.id.nav_gallery) {
//            Toast.makeText(getApplicationContext(), "Gps is clicked", Toast.LENGTH_SHORT).show();
//
//        } else if (id == R.id.nav_slideshow) {
//            Toast.makeText(getApplicationContext(), "view Map is clicked", Toast.LENGTH_SHORT).show();
//
//        } else if (id == R.id.nav_manage) {
//            Toast.makeText(getApplicationContext(), "other is clicked", Toast.LENGTH_SHORT).show();
//
//        }
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.END);
//        return true;
//    }
//
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        mMap.getUiSettings().setZoomControlsEnabled(true);
//        mMap.getUiSettings().setAllGesturesEnabled(true);
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        displayLocation();
//        startLocationUpdate();
//
//    }
//
//
//    private void startLocationUpdate() {
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        mLastLocation = location;
//        displayLocation();
//
//    }
//}
