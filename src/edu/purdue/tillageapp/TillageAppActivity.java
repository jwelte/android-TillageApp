package edu.purdue.tillageapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TillageAppActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tillage_app);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_tillage_app, menu);
        return true;
    }
}
