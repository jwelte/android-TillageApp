package edu.purdue.tillageapp;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.MapView.ReticleDrawMode;

import edu.purdue.FieldNotebook.shape.GeoPolygon;
import edu.purdue.FieldNotebook.shape.ScreenPolygon;
import edu.purdue.FieldNotebook.view.PolygonOverlay;
import edu.purdue.FieldNotebook.view.PolygonSurfaceView;
import edu.purdue.libwaterapps.note.Object;
import edu.purdue.libwaterapps.view.maps.RockMapOverlay;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class TillageAppActivity<Main> extends MapActivity implements LocationListener{

	private MapView map;
	private MyLocationOverlay mvMyLocationOverlay;
	private MapController mvMapController;
	private Drawable d;
	private List<Overlay> overlayList;
	private LocationManager lm;
	private String towers;
	private int lat = 0;
	private int longi = 0;
	private RockMapOverlay mRockOverlay;
	private PolygonOverlay mPolygonOverlay;
	private PolygonSurfaceView mPolygonView;
	private int lastType;
	private int lastColor;

	//private LocationListener ll;
	private GeoPoint ourLocation;
	static final int SPAN_LAT = 2000;
	static final int SPAN_LONG = 2000;
	
	// An array of strings to populate drop down navigation list //
    String[] actions = new String[] {
        "Tillage",
        "Chisel 2012",
        "Disc 2013"
    };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Hide the application name in the action bar
		getActionBar().setDisplayShowTitleEnabled(false);
		
		//Give the activity a view
		setContentView(R.layout.activity_tillage_app);
		
		// Get the MapView, turn on satellite, and show the current position
		map = (MapView) findViewById(R.id.mvMain);
		map.setSatellite(true);
		map.setBuiltInZoomControls(true);
		map.setReticleDrawMode(ReticleDrawMode.DRAW_RETICLE_OVER);
		
		// Get the mMap controller
		mvMapController = map.getController();
		
		// Make a location overlay to track device	
		Touch t = new Touch();
		overlayList = map.getOverlays();
		overlayList.add(t);
		mvMyLocationOverlay = new MyLocationOverlay(TillageAppActivity.this, map);
		overlayList.add(mvMyLocationOverlay);
		
		//Placing icon at user location
		d = getResources().getDrawable(R.drawable.ic_device_access_location_searching);
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria crit = new Criteria();
		towers = lm.getBestProvider(crit, false);
		Location location = lm.getLastKnownLocation(towers);
		if (location != null) {
			lat = (int) (location.getLatitude() * 1E6);
			longi = (int) (location.getLongitude() * 1E6);
			ourLocation = new GeoPoint(lat, longi);
			//OverlayItem overlayItem = new OverlayItem(ourLocation, "What's up","2nd Sting");
			//CustomPinpoint custom = new CustomPinpoint(d, TillageAppActivity.this);
			//custom.insertPinpoint(overlayItem);
			//overlayList.add(custom);
			mvMapController.animateTo(ourLocation);
			mvMapController.setZoom(20);
		} else {
			Toast.makeText(getBaseContext(), "Couldn't Get Provider", Toast.LENGTH_SHORT).show();
		}
		// Make a PolygonOverlay
		mPolygonOverlay = new PolygonOverlay(getResources().getDrawable(R.drawable.ic_launcher));
		map.getOverlays().add(mPolygonOverlay);
								
		// Get a hold of the polygon surface
		mPolygonView = (PolygonSurfaceView)findViewById(R.id.polySurface);
		
	    // Create an array adapter to populate drop down list //
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, actions);
 
        // Enabling drop down list navigation for the action bar //
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
 
        // Defining Navigation listener //
        ActionBar.OnNavigationListener navigationListener = new OnNavigationListener() {
 
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                Toast.makeText(getBaseContext(), "You selected : " + actions[itemPosition]  , Toast.LENGTH_SHORT).show();
                return false;
            }
        };
        /** Setting drop down items and item navigation listener for the action bar */
        getActionBar().setListNavigationCallbacks(adapter, navigationListener);
	}

    
	@Override
	protected void onPause() {
		super.onPause();
		lm.removeUpdates(this);
		disableLocation();
	}

	@Override
	protected void onResume() {
		enableLocation();
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
		
		MenuItem importRocks = menu.findItem(R.id.menu_layer_rocks);
		
		if(mRockOverlay == null) {
			importRocks.setTitle(getString(R.string.menu_layer_rocks_show));
		} else {
			importRocks.setTitle(getString(R.string.menu_layer_rocks_hide));
		}
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
			Toast.makeText(this, "SETTINGS", Toast.LENGTH_LONG).show();
		break;
		case R.id.menu_plan:
			Toast.makeText(this, "OPEN PLAN VIEW", Toast.LENGTH_LONG).show();
		break;
		case R.id.menu_add_field:
			//Toast.makeText(this, "OPEN FIELD DRAW VIEW", Toast.LENGTH_LONG).show();
			startPolygon(Object.TYPE_POLYGON);
			lastType = Object.TYPE_POLYGON;
		break;
		case R.id.menu_move_to_gps:
			moveToGPSLocation();
			result = true; 
		break;
		case R.id.menu_layer_rocks:
			if(mRockOverlay == null) {
				// Make a overlay for the rocks
				mRockOverlay = new RockMapOverlay(this, map);
				map.getOverlays().add(mRockOverlay);
				map.postInvalidate();
			} else {
				map.getOverlays().remove(mRockOverlay);
				map.postInvalidate();
				mRockOverlay = null;
			}
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

	public void moveToGPSLocation(){
		mvMapController.animateTo(ourLocation);
		mvMapController.setZoom(20);
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
	
	// Stop tracking the GPS
	private void disableLocation() {
		mvMyLocationOverlay.disableMyLocation();
	}
	
	public void startPolygon(int type) {
		//ArrayList<Note> notes = mNotesScrollView.getNotes();
		//Note note = notes.get(notes.size()-1);
		
		//lastColor = note.getColor();
		
		if(!mPolygonView.isRunning()) {
			mPolygonView.startDrawing(lastColor, type);
			startActionMode(new DrawAcceptActionModeCallback());
		}
	}
private class DrawAcceptActionModeCallback implements ActionMode.Callback {
		
		// Call when startActionMode() is called
		// Should inflate the menu
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.field_accept, menu);
				
			return true;
		}
		
		// Called when the mode is invalidated
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}
		
		// Called when the user selects a menu item
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			boolean result;
			
			switch(item.getItemId()) {
				case R.id.accept:
					finishPolygon();
					
					mode.finish();
					result = true;
				break;
				
				case R.id.reject:
					clearPolygon();
					
					// No longer need to show the action bar after taking a new picture
					mode.finish();
					result = true;
				break;
				
				default:
					result = false;
				break;
			}
			
			return result;
		}

		// Called when the user exists the action mode
		public void onDestroyActionMode(ActionMode mode) {
			clearPolygon();
		}
	}
public void finishPolygon() {
	ScreenPolygon polygon = null;
	
	if(mPolygonView.isRunning()) {
		polygon = mPolygonView.stopDrawing();
		
		GeoPolygon geoPolygon = new GeoPolygon(polygon, map.getProjection());
		
		
		Object obj = new Object(this, Object.getNewGroupId(this), lastType, geoPolygon.getPoints(), lastColor);
		obj.save();
	
//		mPolygonOverlay.addPolygon(geoPolygon);
		mPolygonOverlay.addPolygon(obj);
		
		map.postInvalidate();
	}
}

public void clearPolygon() {
	if(mPolygonView.isRunning()) {
		mPolygonView.stopDrawing();
	}
	
}
	// Ask location to track GPS and display on the map
	private void enableLocation() {
		mvMyLocationOverlay.enableMyLocation();
		
		// Animate the map to the current location
		mvMyLocationOverlay.runOnFirstFix(new Runnable() {
			public void run() {
				mvMapController.animateTo(mvMyLocationOverlay.getMyLocation());
				mvMapController.zoomToSpan(SPAN_LAT, SPAN_LONG);
			}
		});
	}
}
