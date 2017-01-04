package team13.geomode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void usecurloc(View v){
        Intent i=new Intent(this,MapsActivity.class);
        startActivity(i);
        //hello
    }
    public void usegmap(View v)
    {


    }
}
