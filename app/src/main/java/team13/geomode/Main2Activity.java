package team13.geomode;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;

public class Main2Activity extends AppCompatActivity implements
        LocationListener,
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMapClickListener {

    protected LocationManager locationManager;
    protected Context context;

    String provider;
    Location l;
    LatLng rad, cener;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 11);
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria c = new Criteria();
        locationManager.requestLocationUpdates(1, 1, c, this, null);
        provider = locationManager.getBestProvider(c, true);
        //Toast.makeText(this, provider, Toast.LENGTH_SHORT).show();
        l = locationManager.getLastKnownLocation(provider);
        cener = new LatLng(l.getLatitude(), l.getLongitude());

    }

    @Override
    public void onLocationChanged(Location location) {
        l = location;
        cener = new LatLng(l.getLatitude(), l.getLongitude());
        updatemap();
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
    public void onMapClick(LatLng latLng) {
        rad = latLng;
        updatemap();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        while (l == null) {
        }
        LatLng ll = new LatLng(l.getLatitude(), l.getLongitude());
        cener = new LatLng(l.getLatitude(), l.getLongitude());
        updatemap();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ll));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        mMap.setOnMapClickListener(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 11: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void updatemap() {
        mMap.clear();
        if (l != null) {
            LatLng ll = new LatLng(l.getLatitude(), l.getLongitude());
            mMap.addMarker(new MarkerOptions().position(ll).title("Current Location"));

        } else {
            Toast.makeText(this, "Current Location not Known", Toast.LENGTH_SHORT).show();
            return;
        }
        if (rad != null) {
            mMap.addMarker(new MarkerOptions().position(rad).title("Radius Location"));
        }
        if (rad != null && cener != null) {
            drawCircle();
        }
    }

    public void acpt(View v) {
        if (l != null && rad != null) {
        Double radus = CalculationByDistance(new LatLng(l.getLatitude(), l.getLongitude()), rad);
            Intent i = new Intent(this, AddData.class);
            i.putExtra("latitude", l.getLatitude() + "");
            i.putExtra("longitude", l.getLongitude() + "");
            i.putExtra("radius", radus + "");
            startActivityForResult(i, 11);
        } else {
            Toast.makeText(this, "please check if radius is selected and your current location is shown", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult * 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        return meter;
    }

    private void drawCircle() {

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(cener);

        // Radius of the circle
        circleOptions.radius(CalculationByDistance(cener, rad));

        // Border color of the circle
        circleOptions.strokeColor(Color.RED);

        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        mMap.addCircle(circleOptions);

    }
}
