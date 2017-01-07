package team13.geomode;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Main4Activity extends AppCompatActivity {
    SQLiteDatabase db;
    dbaccess d1;
    TextView mdnm, rvol, avol, mvol;
    String wist;
    Switch s2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.hope);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        mdnm = (EditText) findViewById(R.id.mode_nm);
        rvol = (EditText) findViewById(R.id.rngr_vol);
        avol = (EditText) findViewById(R.id.alr_vol);
        mvol = (EditText) findViewById(R.id.media_vol);
        //s1 = (Spinner) findViewById(R.id.wifi_spin);
        s2 = (Switch) findViewById(R.id.tog);
        wist = "off";

        try {
            db = openOrCreateDatabase("geomode", Context.MODE_PRIVATE, null);
            d1 = new dbaccess(db);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public void addmd(View v) {
        if (s2.isChecked())
            wist = "on";
        else
            wist = "off";
        if (!mdnm.getText().toString().equals("") && !rvol.getText().toString().equals("") && !wist.equals("") && !avol.getText().toString().equals("") && !mvol.getText().toString().equals("")) {
            try {
                if (d1.addmode(mdnm.getText().toString(), rvol.getText().toString(), wist, avol.getText().toString(), mvol.getText().toString()) == 1) {
                    Toast.makeText(this, "Mode Added ", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(this, "Mode not added. Check Volumes!!", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Mode not added", Toast.LENGTH_SHORT).show();
        }
    }
}
