package team13.geomode;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    AlarmManager a;
    PendingIntent p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 11);
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
        startAlarm(getApplicationContext());

    }

    public void stopser(View v) {
        if (p != null) {
            a.cancel(p);
            p = null;
            Log.i("ser", "Service onStopCommand");
        }
    }

    public void startAlarm(Context context) {
        if (p == null) {
            Intent i = new Intent(context, LocationService.class);
            p = PendingIntent.getService(context, 0, i, 0);
            a = (AlarmManager) getSystemService(ALARM_SERVICE);
            a.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, 2000, p);
        }
    }



}
