package team13.geomode;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;

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
        db.execSQL("create table if not exists coordtab(coord_nm text not null,lat text not null,longi text not null,radius text not null,mode text not null ,FOREIGN KEY(mode) REFERENCES modetab(mode_nm))");
        db.execSQL("insert or ignore into modetab values('lownoise','2','0','2','2')");
        db.execSQL("insert or ignore into modetab values('fullsound','7','on','7','15')");
    }

    public void addmode(String modename, String ringer_volume, String w_state, String alrm_vol, String med_vol) {
        int max_r = Integer.parseInt(ringer_volume), max_al = Integer.parseInt(alrm_vol), max_med = Integer.parseInt(med_vol);
        if (max_r >= 0 && max_r < 8 && max_al >= 0 && max_al < 8 && max_med >= 0 && max_med < 16)
        db.execSQL("insert or ignore into modetab values('" + modename + "','" + ringer_volume + "','" + w_state + "','" + alrm_vol + "','" + med_vol + "')");
    }

    public void addcoord(String nm, String lat, String longi, String rad, String mode) {
        db.execSQL("insert or ignore into coordtab values('" + nm + "','" + lat + "','" + longi + "','" + rad + "','" + mode + "')");
    }

    public String[] getCoordMode(Double lat, Double longi) {
        Cursor c = db.rawQuery("Select * from coordtab", null);
        String mode = "fullsound", place = null;
        while (c.moveToNext()) {
            if (CalculationByDistance(new LatLng(lat, longi), new LatLng(Double.parseDouble(c.getString(c.getColumnIndex("lat"))), Double.parseDouble(c.getString(c.getColumnIndex("longi"))))) < Double.parseDouble(c.getString(c.getColumnIndex("radius")))) {
                mode = c.getString(c.getColumnIndex("mode"));
                place = c.getString(c.getColumnIndex("coord_nm"));
            }
        }
        if (mode != null) {
        Cursor c1 = db.rawQuery("Select * from modetab where mode_nm=?", new String[]{mode});
        if (c1.moveToFirst()) {
            String[] attr = new String[]{c1.getString(c1.getColumnIndex("mode_nm")), c1.getString(c1.getColumnIndex("wifi_state")), c1.getString(c1.getColumnIndex("ringr_vol")), c1.getString(c1.getColumnIndex("alarm_vol")), c1.getString(c1.getColumnIndex("media_vol")), place};
            return attr;
        }
        }
        return null;
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
        double meter = valueResult * 1000;
        return meter;
    }

}
