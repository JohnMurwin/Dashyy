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

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {

    //Class Variables


    //Component Variables


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Component Setup

        //SCREEN NAVIGATION
        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.Maps_Page:
                        Intent intent = new Intent(SettingsActivity.this, MapsActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.Home_Page:
                        Intent intent2 = new Intent(SettingsActivity.this, HomeActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.Settings_Page:

                        break;
                }
                return false;
            }
        });
    }
}