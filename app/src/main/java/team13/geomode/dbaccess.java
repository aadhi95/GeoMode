package team13.geomode;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;

/**
 * Created by aadhithya on 1/5/17.
 */

public class dbaccess {
    SQLiteDatabase db;

    dbaccess(SQLiteDatabase d) {
        db = d;
        init();
    }

    private void init() {
        db.execSQL("create table if not exists modetab(mode_nm text primary key,ringr_vol text not null,wifi_state text not null,alarm_vol text not null,media_vol text not null)");
        db.execSQL("create table if not exists coordtab(lat text not null,longi text not null,radius text not null,mode text not null ,FOREIGN KEY(mode) REFERENCES modetab(mode_nm))");
        db.execSQL("insert or ignore into modetab values('lownoise','2','0','2','2')");
        db.execSQL("insert or ignore into modetab values('fullsound','9','0','2','2')");
    }

    public void addmode(String modename, String ringer_volume, String w_state, String alrm_vol, String med_vol) {
        db.execSQL("insert or ignore into modetab values('" + modename + "','" + ringer_volume + "','" + w_state + "','" + alrm_vol + "','" + med_vol + "')");
    }

    public void addcoord(String lat, String longi, String rad, String mode) {
        db.execSQL("insert or ignore into coordtab values('" + lat + "','" + longi + "','" + rad + "','" + mode + "')");
    }

    public String getCoordMode(Double lat, Double longi) {
        Cursor c = db.rawQuery("Select * from corrdtab", null);
        String mode = null;
        while (c.moveToNext()) {
            if (CalculationByDistance(new LatLng(lat, longi), new LatLng(Double.parseDouble(c.getString(c.getColumnIndex("lat"))), Double.parseDouble(c.getString(c.getColumnIndex("longi"))))) < Double.parseDouble(c.getString(c.getColumnIndex("radius")))) {
                mode = c.getString(c.getColumnIndex("mode"));
            }
        }
        return mode;
    }

    public String[] getModes() {
        String[] arr;
        Cursor c = db.rawQuery("Select * from modetab", null);
        c.moveToFirst();
        arr = new String[c.getCount()];
        arr[0] = c.getString(c.getColumnIndex("mode_nm"));
        for (int i = 1; i < c.getCount(); i++) {
            c.moveToNext();
            arr[i] = c.getString(c.getColumnIndex("mode_nm"));
        }
        return arr;
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

    public void close() {
        db.close();
    }
}
