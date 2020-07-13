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

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class GeoTag implements Serializable {
    private double longitude;
    private double latitude;
    private String timestamp;
    private String id;
    private int speed = 0;

    public GeoTag() { }

    public GeoTag(double _latitude, double _longitude) {
        this.longitude = _longitude;
        this.latitude = _latitude;
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date());
    }

    public double getLongitude(){
        return this.longitude;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public String getTimestamp(){
        return this.timestamp;
    }

    public String getID(){
        return this.id;
    }
    public void setID(String _id){
        this.id = _id;
    }

    public int getSpeed() { return (int)speed; }
    public void setSpeed(int _speed) { this.speed = _speed; }
}