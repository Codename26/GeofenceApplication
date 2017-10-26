package com.codename26.geofenceapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerClickListener {
    private final LatLng mDefaultLocation = new LatLng(50.450794, 30.448814);
    private final float DEFAULT_RADIUS = 200;
    private GoogleMap mMap;
    private FloatingActionButton fab;
    private ArrayList<GeoTask> mGeoTasks = new ArrayList<>();
    boolean longClickPressed = false;
    private Snackbar mSnackbar;
    private GeoTask geoTask;
    private GeoTask newGeoTask;
    private HashMap<Double, Double> coordinates;
    private Menu mMenu;
    private ArrayList<Marker> mMarkers;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private LocationManager mLocationManager;
    private CameraPosition mCameraPosition;
    private BroadcastReceiver mBroadcastReceiver;
    private Criteria mCriteria;
    private MyGeofence myGeofence;



    public MapFragment() {
        // Required empty public constructor

    }

    public static MapFragment newInstance(){
        return new MapFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //allow fragment to handle different menu items than it's root Activity
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(MainActivity.KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(MainActivity.KEY_CAMERA_POSITION);
        }
        Bundle arguments = getArguments();
/*        if (arguments != null && arguments.containsKey(MainActivity.NEW_TASK_KEY)){
            geoTask = arguments.getParcelable(MainActivity.NEW_TASK_KEY);
        }*/
        if (arguments != null && arguments.containsKey(MainActivity.TASK_ARRAY)){
            mGeoTasks = (ArrayList<GeoTask>) arguments.getSerializable(MainActivity.TASK_ARRAY);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment  = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCreateTaskListener != null){
                    Intent intent = new Intent(getActivity(), TaskEditActivity.class);
                    intent.putExtra(MainActivity.NEW_TASK, newGeoTask);
                    startActivityForResult(intent, MainActivity.NEW_TASK_REQUEST_CODE);
                    //mCreateTaskListener.createTask(myGeofence);

                }

            }
        });
        fab.setVisibility(View.GONE);
        return view;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                       @Override
                                       public void onMapClick(LatLng latLng) {
                                           if (longClickPressed) {
                                               drawMap();
                                              fab.setVisibility(View.GONE);
                                               if (mSnackbar.isShown()) {
                                                   mSnackbar.dismiss();
                                               }
                                               longClickPressed = false;
                                           }
                                          /* mMenu.findItem(R.id.action_delete).setVisible(false);
                                           mMenu.findItem(R.id.action_edit).setVisible(false);*/
                                       }
                                   });
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                drawMap();
            }
        });

    }

    public void drawMap() {
        mMarkers = new ArrayList<>();
        mMap.clear();
        for (int i = 0; i < mGeoTasks.size(); i++) {
            GeoTask geoTask = mGeoTasks.get(i);
      Marker m =  mMap.addMarker(new MarkerOptions().position(new LatLng(geoTask.getTaskLatitude(), geoTask.getTaskLongitude()))
                    .title(geoTask.getTaskName()));
            m.setTag(geoTask);
            mMarkers.add(m);
            mMap.addCircle(new CircleOptions()
                    .center(new LatLng(geoTask.getTaskLatitude(), geoTask.getTaskLongitude()))
                    .radius(200)
                    .strokeWidth(2)
                    .strokeColor(Color.argb(153, 117, 200, 242))
                    .fillColor(Color.argb(153, 117, 200, 242)));

        }

        LatLngBounds.Builder bld = new LatLngBounds.Builder();
        if (mGeoTasks.size() > 1) {
            for (int i = 0; i < mGeoTasks.size(); i++) {
                LatLng ll = new LatLng(mGeoTasks.get(i).getTaskLatitude(), mGeoTasks.get(i).getTaskLongitude());
                bld.include(ll);
            }
            LatLngBounds bounds = bld.build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 230));
        }
        if (mGeoTasks.size() == 1){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mGeoTasks.get(0).getTaskLatitude(),
                            mGeoTasks.get(0).getTaskLongitude()), 15.5f) );
        } else if (mGeoTasks.size() < 1){
            //Move camera to current location
            moveCameraToCurrentLocation();
        }
    }

    private void moveCameraToCurrentLocation() {
            mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            mCriteria = new Criteria();
            String bestProvider = String.valueOf(mLocationManager.getBestProvider(mCriteria, true));

            @SuppressWarnings("MissingPermission") Location mLocation = mLocationManager.getLastKnownLocation(bestProvider);
            if (mLocation != null) {
                final double currentLatitude = mLocation.getLatitude();
                final double currentLongitude = mLocation.getLongitude();
                LatLng loc1 = new LatLng(currentLatitude, currentLongitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude,
                        currentLongitude), 15.5f), 2000, null );
            }

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        longClickPressed = true;
            mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(200)
                .strokeWidth(2)
                .strokeColor(Color.argb(153, 117, 200, 242))
                .fillColor(Color.argb(153, 117, 200, 242)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.5f));
        newGeoTask = new GeoTask("", latLng.latitude, latLng.longitude, DEFAULT_RADIUS);
       // myGeofence = new MyGeofence(1, latLng.latitude, latLng.longitude, 300, Geofence.GEOFENCE_TRANSITION_ENTER);
        fab.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.appear);
        fab.setAnimation(anim);
        mSnackbar = Snackbar.make(fab, "Add geoTask at this location", Snackbar.LENGTH_INDEFINITE);
        mSnackbar.show();
        }



    @Override
    public boolean onMarkerClick(Marker marker) {
       /* mMenu.findItem(R.id.action_delete).setVisible(true);
        mMenu.findItem(R.id.action_edit).setVisible(true);
*/
        geoTask = (GeoTask) marker.getTag();

        return false;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mMenu = menu;
    }

    private DeleteTaskListener mDeleteTaskListener;
    private CreateTaskListener mCreateTaskListener;
    private EditTaskListener mEditTaskListener;
    public void setDeleteTaskListener(DeleteTaskListener listener){
        mDeleteTaskListener = listener;
    }

    public void setCreateTaskListener(CreateTaskListener listener){
        mCreateTaskListener = listener;
    }

    public void setEditTaskListener(EditTaskListener listener){
        mEditTaskListener = listener;
    }

    public interface DeleteTaskListener{
        void deleteGeoTask(long id);
    }

    public interface EditTaskListener{
        void editTask(GeoTask geoTask);
    }

    public interface CreateTaskListener{
        void createTask(MyGeofence myGeofence);
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_delete) {
           if (mDeleteTaskListener != null){
               mDeleteTaskListener.deleteGeoTask(geoTask.getTaskId());
               mGeoTasks.remove(geoTask);
               drawMap();
           }
            return true;
        }
        if (id == R.id.action_edit) {

            if(mEditTaskListener != null){
                mEditTaskListener.editTask(geoTask);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

private boolean isLocationPermissionGranted(){
    return ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED;

}

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (isLocationPermissionGranted()) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(MainActivity.KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(MainActivity.KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }
}
