/*
Mobile App Development - W01
Team 5
Summer 2020
Semester Project - Dashyy

Any use of the following code is forbidden without prior consent.
*/

package CS7455.Team5.dashyy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class StartupActivity extends AppCompatActivity {

    //Class Variables


    //Component Variables


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        //Component Setup


        //Startup Logic
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
