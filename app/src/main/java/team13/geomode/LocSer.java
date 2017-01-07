package team13.geomode;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

public class LocSer extends Service implements LocationListener {
    static Boolean ru;
    protected LocationManager lm;
    protected Context context;
    SharedPreferences s1;
    String provider;
    Location l;
    SQLiteDatabase db;
    dbaccess d1;
    String[] mode;
    Vibrator v;
    Intent ma;
    PendingIntent pma;
    WifiManager wifiManager;
    AudioManager audioManager;
    SharedPreferences.Editor se;

    public LocSer() {
    }

    @Override
    public void onCreate() {
        ru = true;
        Log.i("adas", "Service onCreate");
        super.onCreate();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        s1 = getSharedPreferences("GeoModeShared", Context.MODE_PRIVATE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        ma = new Intent(this, MainActivity.class);
        ma.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        pma = PendingIntent.getActivity(getApplicationContext(), 0, ma, 0);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria c = new Criteria();
        lm.requestLocationUpdates(3000, 12, c, this, null);
        provider = lm.getBestProvider(c, true);
        //Toast.makeText(this, provider, Toast.LENGTH_SHORT).show();
        l = lm.getLastKnownLocation(provider);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ru = true;
        Notification n = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.c)
                .setContentTitle("GeoMode Enabled")
                .setContentIntent(pma)
                .setContentText(s1.getString("mode", "Full Sound"))
                .build();
        startForeground(11, n);
        Log.i("adas", "Service onStartCommand");
        if (intent.getAction().equals("start")) {
            updatemode();
            n = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.c)
                    .setContentTitle("GeoMode Enabled")
                    .setContentIntent(pma)
                    .setContentText(mode[0] + "    " + mode[5])
                    .build();
            startForeground(11, n);
        } else if (intent.getAction().equals("stop")) {
            stopForeground(true);
            ru = false;
            Log.i("Service", "stopfore");
            stopSelf();
            Log.i("Service", "stopforefore");
        }
        //System.gc();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("Service", "In onDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void updatemode() {
        //System.gc();
        if (l != null) {
                /*StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
                        .permitDiskWrites()
                        .permitDiskReads()
                        .build());*/
            db = openOrCreateDatabase("geomode", Context.MODE_PRIVATE, null);
            d1 = new dbaccess(db);
            se = s1.edit();
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mode = d1.getCoordMode(l.getLatitude(), l.getLongitude());
            if (s1.getString("mode", "ddddd").equals("ddddd")) {
                se.clear();
                se.putString("mode", mode[0]);
                se.commit();
                v.vibrate(500);
                int vol_ring = Integer.parseInt(mode[2]), vol_med = Integer.parseInt(mode[4]), vol_ala = Integer.parseInt(mode[3]);
                audioManager.setStreamVolume(AudioManager.STREAM_RING, vol_ring, AudioManager.FLAG_ALLOW_RINGER_MODES | AudioManager.FLAG_PLAY_SOUND);
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM, vol_ala, AudioManager.FLAG_ALLOW_RINGER_MODES | AudioManager.FLAG_PLAY_SOUND);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol_med, AudioManager.FLAG_ALLOW_RINGER_MODES | AudioManager.FLAG_PLAY_SOUND);
                if (mode[1].equals("on")) {
                    Context context = getApplicationContext();
                    wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(true);
                } else {
                    Context context = getApplicationContext();
                    wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(false);
                }

            }
            if (!s1.getString("mode", mode[0]).equals(mode[0])) {
                se.clear();
                se.putString("mode", mode[0]);
                se.commit();
                v.vibrate(500);
                int vol_ring = Integer.parseInt(mode[2]), vol_med = Integer.parseInt(mode[4]), vol_ala = Integer.parseInt(mode[3]);
                audioManager.setStreamVolume(AudioManager.STREAM_RING, vol_ring, AudioManager.FLAG_ALLOW_RINGER_MODES | AudioManager.FLAG_PLAY_SOUND);
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM, vol_ala, AudioManager.FLAG_ALLOW_RINGER_MODES | AudioManager.FLAG_PLAY_SOUND);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol_med, AudioManager.FLAG_ALLOW_RINGER_MODES | AudioManager.FLAG_PLAY_SOUND);
                if (mode[1].equals("on")) {
                    Context context = getApplicationContext();
                    wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(true);
                } else {
                    Context context = getApplicationContext();
                    wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(false);
                }

            }
            //addNotification();

        }
        //addNotification();
        db.close();
                /*try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }*/

    }
    @Override
    public void onLocationChanged(Location location) {
        l = location;
        updatemode();
        Notification n = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.c)
                .setContentTitle("GeoMode Enabled")
                .setContentIntent(pma)
                .setContentText(s1.getString("mode", "fullnoise"))
                .build();
        startForeground(11, n);
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


}
