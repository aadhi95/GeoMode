package team13.geomode;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddData extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    SQLiteDatabase db;
    String lat, lon, rad, mod;
    String[] arr;
    dbaccess d1;
    Spinner s1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        Log.i("adddata", "radius");
        try {
            db = openOrCreateDatabase("geomode", Context.MODE_PRIVATE, null);
            Log.i("adddata", "dbopened");
            Intent i = getIntent();
            TextView t = (TextView) findViewById(R.id.lat);
            lat = i.getStringExtra("latitude");
            t.setText(i.getStringExtra("latitude"));
            t = (TextView) findViewById(R.id.lon);
            t.setText(i.getStringExtra("longitude"));
            lon = i.getStringExtra("longitude");
            t = (TextView) findViewById(R.id.rad);
            t.setText(i.getStringExtra("radius"));
            rad = i.getStringExtra("radius");
            Log.i("adddata", "radius");
            d1 = new dbaccess(db);
            arr = d1.getModes();
            Log.i("adddata", "created");
            s1 = (Spinner) findViewById(R.id.mode_spin);
            ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arr);
            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            s1.setAdapter(ad);
            s1.setOnItemSelectedListener(this);
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mod = arr[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void addcoord(View v) {
        if (mod != null && d1 != null) {
            try {
                d1.addcoord(lat, lon, rad, mod);
                Toast.makeText(this, "row addded", Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        setResult(12, null);
        finish();
    }
}
