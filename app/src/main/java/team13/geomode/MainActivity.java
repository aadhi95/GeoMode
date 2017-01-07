package team13.geomode;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    AlarmManager a;
    PendingIntent p;
    Boolean th;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.hope);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        th = false;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.VIBRATE}, 11);
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 11);
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 11);
        }

    }

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

    public void usecurloc(View v){
        Intent i = new Intent(this, Main2Activity.class);
        startActivity(i);
    }
    public void usegmap(View v)
    {
        Intent i = new Intent(this, Main3Activity.class);
        startActivity(i);

    }

    public void check(View v) {
        Intent i = new Intent(this, Main4Activity.class);
        startActivity(i);
    }

    public void startser(View v) {
        i = new Intent(MainActivity.this, LocSer.class);
        i.setAction("start");
        startService(i);

    }

    public void stopser(View v) {
        i = new Intent(MainActivity.this, LocSer.class);
        i.setAction("stop");
        stopService(i);

    }

    public void vall(View v) {
        Intent i1 = new Intent(this, VIewAll.class);
        startActivity(i1);
    }



}
