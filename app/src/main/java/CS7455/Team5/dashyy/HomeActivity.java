/*
Mobile App Development - W01
Team 5
Summer 2020
Semester Project - Dashyy

Any use of the following code is forbidden without prior consent.
*/

package CS7455.Team5.dashyy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    //Class Variables


    //Component Variables


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Component Setup
    }

    //SCREEN NAVIGATION
    //GPS Screen
    public void onGPSClick (View view)
    {
        Intent intent = new Intent(this, GPSActivity.class);
        startActivity(intent);
    }
    //Settings Screen
    public void onSettingsClick (View view)
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
