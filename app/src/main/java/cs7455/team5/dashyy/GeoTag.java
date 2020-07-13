/*
Mobile App Development - W01
Team 5
Summer 2020
Semester Project - Dashyy

Any use of the following code is forbidden without prior consent.
*/

package cs7455.team5.dashyy;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class GeoTag implements Serializable {
    private double mLongitude;
    private double mLatitude;
    private String mTimestamp;
    private String mID;

    public GeoTag() {
    }

    public GeoTag(double latitude, double longitude) {
        this.mLongitude = longitude;
        this.mLatitude = latitude;
        this.mTimestamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date());
        //String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
    }

    public double getLongitude(){
        return this.mLongitude;
    }

    public double getLatitude(){
        return this.mLatitude;
    }

    public String getTimestamp(){
        return this.mTimestamp;
    }

    public String getID(){
        return this.mID;
    }
    public void setID(String id){
        this.mID = id;
    }
}