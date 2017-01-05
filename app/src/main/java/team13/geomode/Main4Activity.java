package team13.geomode;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Main4Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner s;
    SQLiteDatabase db;
    dbaccess d1;
    TextView mdnm, rvol, avol, mvol;
    String wist;
    String[] arr;
    Spinner s1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        mdnm = (EditText) findViewById(R.id.mode_nm);
        rvol = (EditText) findViewById(R.id.rngr_vol);
        avol = (EditText) findViewById(R.id.alr_vol);
        mvol = (EditText) findViewById(R.id.media_vol);
        s1 = (Spinner) findViewById(R.id.wifi_spin);
        wist = "off";
        arr = new String[]{"on", "off"};
        ArrayAdapter ad = new ArrayAdapter(this, R.layout.spin, arr);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(ad);
        s1.setOnItemSelectedListener(this);
        try {
            db = openOrCreateDatabase("geomode", Context.MODE_PRIVATE, null);
            d1 = new dbaccess(db);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        wist = arr[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void addmd(View v) {
        if (!mdnm.getText().toString().equals("") && !rvol.getText().toString().equals("") && !wist.equals("") && !avol.getText().toString().equals("") && !mvol.getText().toString().equals("")) {
            try {
                d1.addmode(mdnm.getText().toString(), rvol.getText().toString(), wist, avol.getText().toString(), mvol.getText().toString());
                Toast.makeText(this, "row added", Toast.LENGTH_LONG).show();
                finish();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
