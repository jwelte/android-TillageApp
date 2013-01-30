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
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

public class TillageAppActivity<Main> extends MapActivity {

	MapView map;
	MyLocationOverlay compass;
	MapController controller;
	Drawable d;
	LocationManager lm;
	String towers;
	int lat = 0;
	int longi = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tillage_app);
		map = (MapView) findViewById(R.id.mvMain);
		map.setBuiltInZoomControls(true);

		Touch t = new Touch();
		List<Overlay> overlayList = map.getOverlays();
		overlayList.add(t);
		compass = new MyLocationOverlay(TillageAppActivity.this, map);
		overlayList.add(compass);
		controller = map.getController();
		map.setSatellite(true);
		
		// GeoPoint point = new GeoPoint(*1E6);
		/*lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria crit = new Criteria();
		towers = lm.getBestProvider(crit, false);
		Location location = lm.getLastKnownLocation(towers);
		if (location != null) {
			lat = (int) (location.getLatitude() * 1E6);
			longi = (int) (location.getLongitude() * 1E6);
			GeoPoint ourLocation = new GeoPoint(lat, longi);
			OverlayItem overlayItem = new OverlayItem(ourLocation, "What's up",
					"2nd Sting");
			CustomPinpoint custom = new CustomPinpoint(d,
					TillageAppActivity.this);
			custom.insertPinpoint(overlayItem);
			overlayList.add(custom);
			controller.animateTo(ourLocation);
			controller.setZoom(17);
		} else {
			Toast.makeText(getBaseContext(), "Couldn't Get Provider",
					Toast.LENGTH_SHORT).show();
		}*/
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		compass.disableCompass();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		compass.enableCompass();
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_tillage_app, menu);

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	class Touch extends Overlay {
		public boolean onTouchEvent(MotionEvent e, MapView m) {

			return false;
		}
	}
}
