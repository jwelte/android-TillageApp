package edu.purdue.tillageapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class TillageAppListActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tillage_app_list);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_tillage_app_list, menu);

		return true;
		//return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean result = true;
		switch(item.getItemId()) {
		case R.id.viewChangeToMap:
			Intent changeViewToMap = new Intent(this, TillageAppActivity.class);
			startActivity(changeViewToMap);
		break;
		case R.id.menu_settings:
			Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show();
		break;
		case R.id.menu_add_field:
			Toast.makeText(this, "ADD NEW FILED TO LIST VIEW", Toast.LENGTH_LONG).show();
		break;
		case R.id.menu_plan:
			Toast.makeText(this, "Plan", Toast.LENGTH_LONG).show();
		break;
		case R.id.menu_help:
			Toast.makeText(this, "OPEN HELP", Toast.LENGTH_LONG).show();
		break;
	}
		// If we didn't handle, let the super version try
				return result | super.onOptionsItemSelected(item);
	}
	
	
}
