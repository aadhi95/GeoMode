package team13.geomode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    }

    public void stopser(View v) {

    }

}
