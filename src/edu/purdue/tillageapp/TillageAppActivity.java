package edu.purdue.tillageapp;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

public class TillageAppActivity<Main> extends MapActivity implements LocationListener{

	MapView map;
	MyLocationOverlay compass;
	MapController controller;
	Drawable d;
	List<Overlay> overlayList;
	LocationManager lm;
	String towers;
	int lat = 0;
	int longi = 0;
	LocationListener ll;
	
	//GEOCODER AND GEOPOINT

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tillage_app);
		map = (MapView) findViewById(R.id.mvMain);
		map.setBuiltInZoomControls(true);

		Touch t = new Touch();
		overlayList = map.getOverlays();
		overlayList.add(t);
		compass = new MyLocationOverlay(TillageAppActivity.this, map);
		overlayList.add(compass);
		controller = map.getController();
		map.setSatellite(true);

		
		//Placing icon at user location
		d = getResources().getDrawable(R.drawable.ic_device_access_location_searching);
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria crit = new Criteria();
		towers = lm.getBestProvider(crit, false);
		Location location = lm.getLastKnownLocation(towers);
		if (location != null) {
			lat = (int) (location.getLatitude() * 1E6);
			longi = (int) (location.getLongitude() * 1E6);
			GeoPoint ourLocation = new GeoPoint(lat, longi);
			OverlayItem overlayItem = new OverlayItem(ourLocation, "What's up","2nd Sting");
			CustomPinpoint custom = new CustomPinpoint(d, TillageAppActivity.this);
			custom.insertPinpoint(overlayItem);
			overlayList.add(custom);
			controller.animateTo(ourLocation);
			controller.setZoom(20);
		} else {
			Toast.makeText(getBaseContext(), "Couldn't Get Provider", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onPause() {
		compass.disableCompass();
		super.onPause();
		lm.removeUpdates(this);
	}

	@Override
	protected void onResume() {
		compass.enableCompass();
		super.onResume();
		lm.requestLocationUpdates(towers, 500, 1, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_tillage_app, menu);

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean result = true;
		switch(item.getItemId()) {
		case R.id.viewChangeToList:
			Intent changeViewToList = new Intent(this, TillageAppListActivity.class);
			startActivity(changeViewToList);
		break;
		case R.id.menu_settings:
			Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show();
		break;
		case R.id.menu_plan:
			Toast.makeText(this, "Plan", Toast.LENGTH_LONG).show();
		break;
		case R.id.tillage_type_Tillage:
			Toast.makeText(this, "Tillage", Toast.LENGTH_LONG).show();
		break;
		case R.id.tillage_type_Chisel2012:
			Toast.makeText(this, "Chisel 2012", Toast.LENGTH_LONG).show();
		break;
		case R.id.tillage_type_disc2012:
			Toast.makeText(this, "Disc 2012", Toast.LENGTH_LONG).show();
		break;
		case R.id.menu_add_field:
			Toast.makeText(this, "OPEN FIELD DRAW VIEW", Toast.LENGTH_LONG).show();
		break;
		case R.id.menu_move_to_gps:
			Toast.makeText(this, "MOVE TO USER'S LOCATION", Toast.LENGTH_LONG).show();
		break;
		case R.id.menu_layer_rocks:
			Toast.makeText(this, "IMPORT ROCKS TO MAP", Toast.LENGTH_LONG).show();
		break;
		case R.id.menu_layer_hazards:
			Toast.makeText(this, "IMPORT HAZARDS TO MAP", Toast.LENGTH_LONG).show();
		break;
		case R.id.menu_layer_tilerisers:
			Toast.makeText(this, "IMPORT TILE RISERS TO MAP", Toast.LENGTH_LONG).show();
		break;
		case R.id.menu_help:
			Toast.makeText(this, "OPEN HELP", Toast.LENGTH_LONG).show();
		break;
	}
		// If we didn't handle, let the super version try
				return result | super.onOptionsItemSelected(item);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	class Touch extends Overlay {
		public boolean onTouchEvent(MotionEvent e, MapView m) {

			return false;
		}
	}

	@Override
	public void onLocationChanged(Location l) {
		lat = (int) l.getLatitude();
		longi = (int) l.getLongitude();
		GeoPoint ourLocation = new GeoPoint(lat, longi);
		OverlayItem overlayItem = new OverlayItem(ourLocation, "What's up","2nd Sting");
		CustomPinpoint custom = new CustomPinpoint(d, TillageAppActivity.this);
		custom.insertPinpoint(overlayItem);
		overlayList.add(custom);
	}

	@Override
	public void onProviderDisabled(String arg0) {
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		
	}
}
