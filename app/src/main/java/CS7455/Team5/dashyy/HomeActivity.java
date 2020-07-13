/*
Mobile App Development - W01
Team 5
Summer 2020
Semester Project - Dashyy

Any use of the following code is forbidden without prior consent.
*/

package cs7455.team5.dashyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    //Class Variables
    private static final int SEND_SMS_CODE = 23;
    private static final String TAG = "Home Activity";
    // for callback
    private boolean hasPermissionResult = false;


    //Component Variables
    ImageView maps_IV;
    Button track_BTN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Component Setup
        initView();
    }

    private void checkPermissions() {
        boolean canReadSend = (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED)
                &&  (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED);
        if(!canReadSend) {
            requestPermissionReadSend();
            hasPermissionResult = true;
        }
        Log.d(TAG, "checkPermissions");
    }

        Log.d(TAG, "requestPermissionReadWrite");
    private void requestPermissionReadSend(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS}, 120);

    @Override
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 120) {
        }
            postPermissionInit();
        Log.d(TAG, "onRequestPermissionsResult");
    }

    private void initView() {
        track_BTN = findViewById(R.id.childOneMaps_PB);
        track_BTN.setEnabled(false);
        checkPermissions();
        maps_IV = findViewById(R.id.map_PB);
        maps_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), cs7455.team5.dashyy.MapsActivity.class);
                startActivity(intent);
            }
        });
        Log.d(TAG, "View initialized");
    }

    public void MapClick(View view)
    private void postPermissionInit() {
        track_BTN.setEnabled(true);
    }

    //SCREEN NAVIGATION

    //Maps Screen
    {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    //Requesting permission
    private void requestSmsSendPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            Toast.makeText(getApplicationContext(), "SMS permissions required for notifications.", Toast.LENGTH_LONG).show();
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.SEND_SMS },
                SEND_SMS_CODE);
    }

    public void childOneMaps_PB_onClick(View view) {
        SmsManager sms = SmsManager.getDefault();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("SMS_SENT"), 0);
        ArrayList<String> parts = sms.divideMessage("DEPARTING");
        sms.sendTextMessage("+17068315347", null, "DEPARTING!", pendingIntent, null);
        Toast.makeText(getApplicationContext(), "SMS Sent", Toast.LENGTH_LONG).show();
        track_BTN.setText("TRACKING..");
    }
}