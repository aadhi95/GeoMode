package team13.geomode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;

public class Main3Activity extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback, GoogleMap.OnMapClickListener {
    RadioButton r1, r2;
    LatLng center, radius;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
        r1 = (RadioButton) findViewById(R.id.radio_center);
        r2 = (RadioButton) findViewById(R.id.radio_radius);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (r1.isChecked()) {
            center = latLng;
        } else if (r2.isChecked()) {
            radius = latLng;
        } else {
            Toast.makeText(this, "please select either center or radius", Toast.LENGTH_SHORT).show();
        }
        updatemap();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng ll = new LatLng(14.350817990, 77.6325169);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ll));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        mMap.setOnMapClickListener(this);
        updatemap();
    }

    public void accept(View v) {
        if (center != null && radius != null) {
            Double radus = CalculationByDistance(center, radius);
            Intent i = new Intent(this, AddData.class);
            i.putExtra("latitude", center.latitude + "");
            i.putExtra("longitude", center.longitude + "");
            i.putExtra("radius", radus + "");
            Log.i("intentt", "creates");
            startActivityForResult(i, 11);
        } else {
            Toast.makeText(this, "please check if radius and center is selected ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
    }

    private void updatemap() {
        mMap.clear();
        if (center != null) {
            mMap.addMarker(new MarkerOptions().position(center).title("Center"));
        }
        if (radius != null) {
            mMap.addMarker(new MarkerOptions().position(radius).title("Radius"));
        }
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
}
