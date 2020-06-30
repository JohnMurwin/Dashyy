package CS7455.Team5.dashyy;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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


        initLocation();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbGps = firebaseDatabase.getReference("last-login");
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        LatLng currLocation = getLocation((LocationManager) getSystemService(Context.LOCATION_SERVICE));
        if(currLocation!=null){
            mMap.addMarker(new MarkerOptions().position(currLocation).title("Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currLocation));
            GeoTag geoTag = new GeoTag(currLocation.latitude, currLocation.longitude);
            geoTag.setID(id);
            dbGps.child(geoTag.getID()).setValue(geoTag);
        }
        else{
            // Add a marker to KSU's Atrium building
            LatLng ksuAtriumBldg = new LatLng(33.9357641, -84.5208089);
            mMap.addMarker(new MarkerOptions().position(ksuAtriumBldg).title("Atrium Building (KSU)"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(ksuAtriumBldg));
            dbGps.child(id).setValue(ksuAtriumBldg);
        }
    }

    protected void initLocation(){
        final Context context = getApplicationContext();
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("GPS not enabled");
            builder.setNegativeButton("Cancel", null);
            builder.setPositiveButton("Open Location settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            builder.create();
            builder.show();
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
        // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
        //    txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        try {
            if (bestLocation != null) {
                return new LatLng(bestLocation.getLatitude(), bestLocation.getLongitude());
            } else return null;
        } catch (Exception ex) {
            Log.i("Location Error", ex.getMessage());
            return null;
        }
    }
}
