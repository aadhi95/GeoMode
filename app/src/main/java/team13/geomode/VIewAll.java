package team13.geomode;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class VIewAll extends AppCompatActivity implements LocationListener, OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {
    protected LocationManager locationManager;
    protected Context context;
    GoogleMap gMap;
    String provider;
    Location l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.hope);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map5);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria c = new Criteria();
        locationManager.requestLocationUpdates(1, 1, c, this, null);
        provider = locationManager.getBestProvider(c, true);
        //Toast.makeText(this, provider, Toast.LENGTH_SHORT).show();
        l = locationManager.getLastKnownLocation(provider);
        Log.i("vall", "on create over");
    }

    @Override
    public void onLocationChanged(Location location) {
        l = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("vall", "map is ready");
        gMap = googleMap;
        SQLiteDatabase db = openOrCreateDatabase("geomode", Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("Select * from coordtab", null);
        while (c.moveToNext()) {
            drawCircle(new LatLng(Double.parseDouble(c.getString(c.getColumnIndex("lat"))), Double.parseDouble(c.getString(c.getColumnIndex("longi")))), Double.parseDouble(c.getString(c.getColumnIndex("radius"))));
        }
        db.close();
        Log.i("vall", "db closed");
        while (l == null) {
        }
        Log.i("vall", "past l null");
        LatLng l1 = new LatLng(l.getLatitude(), l.getLongitude());
        gMap.addMarker(new MarkerOptions().position(l1).title("Current Location"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(l1));
        gMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);
    }

    private void drawCircle(LatLng cener, Double rad) {

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(cener);

        // Radius of the circle
        circleOptions.radius(rad);

        // Border color of the circle
        circleOptions.strokeColor(Color.RED);

        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        gMap.addCircle(circleOptions);

    }
}
