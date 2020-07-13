package cs7455.team5.dashyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import cs7455.team5.dashyy.GeoTag;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private TextView tvSpeed;

    final static int PERMISSION_ALL = 1;
    final static String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private GoogleMap mMap;
    MarkerOptions markerOptions;
    boolean mapReady = false;
    Marker prevMarker;
    private String displayName;
    FirebaseDatabase firebaseDatabase;
    LocationManager locationManager;
    double prevLat, prevLong;
    float zoomLevel = 17.0f;
    private static final String TAG = "MapsActivity";

    // TODO: these will have to passed to the intent
    boolean isParent = true;
    String userID = "";
    DatabaseReference dbGps, dbLogGPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        tvSpeed = findViewById(R.id.speed_TV);
        // init firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        displayName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        dbGps = firebaseDatabase.getReference("last-login");
        dbLogGPS = firebaseDatabase.getReference("gps-log");

        if(isParent) {
            dbLogGPS.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getKey().equals("gps-log")) {
                            GeoTag childGeoTag = snapshot.child(userID).getValue(GeoTag.class);
                            if (childGeoTag != null) {
                                updateMap(childGeoTag);
                                snapshot.child(userID).getRef().removeValue();
                                int speed = childGeoTag.getSpeed();
                                updateSpeedDisplay(speed);
                                // Toast.makeText(getApplicationContext(), childGeoTag.getSpeed() + " MPH", Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, "Exception: " + ex.getMessage());
                        Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // todo: alert mom & dad or something
                }
            });
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LatLng curr = getLocation(locationManager);
        if(curr != null)
            markerOptions = new MarkerOptions().position(curr).title(displayName);

        if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted())
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        else requestLocation();
        if (!isLocationEnabled()) showAlert(false);
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
        mMap.addMarker(markerOptions);
        mapReady = true;
        if(prevMarker == null) {
            LatLng currLocation = getLocation((LocationManager) getSystemService(Context.LOCATION_SERVICE));
            if (currLocation != null) {
                prevMarker = mMap.addMarker(new MarkerOptions().position(currLocation).title(displayName));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLocation, zoomLevel));
                prevLat = currLocation.latitude;
                prevLong = currLocation.longitude;
            }
        }
    }

    public static LatLng getLocation(LocationManager locationManager) {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        try {
            if (bestLocation != null) {
                return new LatLng(bestLocation.getLatitude(), bestLocation.getLongitude());
            } else return null;
        } catch (Exception ex) {
            Log.i("Location Error", ex.getMessage());
            return null;
        }
    }

    protected void updateMap(GeoTag geoTag){
        LatLng curr = new LatLng(geoTag.getLatitude(), geoTag.getLongitude());
        prevMarker = mMap.addMarker(new MarkerOptions().position(curr).title(displayName));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curr, zoomLevel));
    }

    protected void setLocation(@NonNull Location location) {
        try {
            //LatLng coords = new LatLng(location.getLatitude(), location.getLongitude());
            //markerOptions = new MarkerOptions().position(coords).title("Current Location");
            if (mapReady) {
                GeoTag geoTag = null;
                boolean moved = true;
                if(location != null) {
                    if(location.equals(new LatLng(prevLat, prevLong)))
                        moved = false;
                    else{
                        prevMarker.remove();
                        geoTag = new GeoTag(location.getLatitude(), location.getLongitude());
                        geoTag.setID(userID);
                        prevLat = location.getLatitude();
                        prevLong = location.getLongitude();
                        // TODO Compute speed
                        int speed = (int)(location.getSpeed() * 2.236936);
                        geoTag.setSpeed(speed);
                        updateSpeedDisplay(speed);
                        moved = true;
                    }
                }
                else{
                    // todo: alert mom & dad?
                }
                if(moved) { // only update map and database if location changed
                    final DatabaseReference child = dbLogGPS.child(geoTag.getID());
                    child.setValue(geoTag);
                    updateMap(geoTag);
                }
            }
        } catch (Exception ex) {
            Log.d(TAG, "Exception: " + ex.getMessage());
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(!isParent) // prevent parent from writing to db
            setLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    private String getProvider() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        return locationManager.getBestProvider(criteria, true);
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocation();
            locationManager.requestLocationUpdates(getProvider(), 10000, 10, this);
        }
        locationManager.requestLocationUpdates(getProvider(), 10000, 10, this);
    }

    private void showAlert(final boolean locationEnabled) {
        String message, title, text;
        if (!locationEnabled) {
            message = "Your location settings is set to off. You must enabled location to use this app.";
            title = "Enable Location";
            text = "Location Settings";
        } else {
            message = "Please allow this app to access location.";
            title = "Permissions access";
            text = "Grant";
        }
        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle(title).setMessage(message)
                .setPositiveButton(text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!locationEnabled) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        } else {
                            requestPermissions(PERMISSIONS, PERMISSION_ALL);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean isPermissionGranted() {
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permissions granted.");
            return true;
        } else {
            Log.v(TAG, "Permissions NOT granted.");
            return false;
        }
    }

    private void updateSpeedDisplay(int speed) {
        if (speed > 60) {// todo: make a setting for max speed
            tvSpeed.setTextColor(Color.RED);
            tvSpeed.setBackgroundColor(Color.YELLOW);
        } else {
            tvSpeed.setBackgroundColor(Color.WHITE);
            tvSpeed.setTextColor(Color.BLACK);
        }
        tvSpeed.setText(Integer.toString(speed));
    }

    private LatLng LocationToLatLng(Location l){
        return new LatLng(l.getLatitude(), l.getLongitude());
    }
}
