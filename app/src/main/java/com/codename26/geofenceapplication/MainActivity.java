package com.codename26.geofenceapplication;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_CAMERA_POSITION = "key_camera_position";
    public static final String KEY_LOCATION = "key_location";
    public static final String TASK_ARRAY = "task_array";
    private MapFragment mMapFragment;
    private ArrayList<GeoTask> mGeoTasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION }, 1);
       /* if (savedInstanceState == null) {
            PlaceholderFragment mPlaceholderFragment = new PlaceholderFragment();
            mPlaceholderFragment.setSendGeoListener(mSendGeoListener);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mPlaceholderFragment)
                    .commit();
        }*/
       initMapFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, mMapFragment)
                .commit();
    }

   private PlaceholderFragment.SendGeoListener mSendGeoListener = new PlaceholderFragment.SendGeoListener() {
       @Override
       public void sendGeoToService(MyGeofence myGeofence) {
           Intent geofencingService = new Intent(MainActivity.this, GeofencingService.class);
           geofencingService.setAction(String.valueOf(Math.random()));
           geofencingService.putExtra(GeofencingService.EXTRA_ACTION, GeofencingService.Action.ADD);
           geofencingService.putExtra(GeofencingService.EXTRA_GEOFENCE, myGeofence);

           startService(geofencingService);
       }
   };

   private MapFragment.CreateTaskListener mCreateTaskListener = new MapFragment.CreateTaskListener() {
       @Override
       public void createTask(MyGeofence myGeofence) {
           System.out.println("***************" + myGeofence.toString());
           Intent geofencingService = new Intent(MainActivity.this, GeofencingService.class);
           geofencingService.setAction(String.valueOf(Math.random()));
           geofencingService.putExtra(GeofencingService.EXTRA_ACTION, GeofencingService.Action.ADD);
           geofencingService.putExtra(GeofencingService.EXTRA_GEOFENCE, myGeofence);

           startService(geofencingService);
       }
   };

    private void initMapFragment(){
        mMapFragment = new MapFragment();
        DataBaseHelper helper = new DataBaseHelper(this);
        mGeoTasks = helper.getTasks();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TASK_ARRAY, (Serializable) mGeoTasks);
        mMapFragment.setArguments(bundle);
        //mMapFragment.setDeleteTaskListener(mDeleteTaskListener);
        mMapFragment.setCreateTaskListener(mCreateTaskListener);
        //mMapFragment.setEditTaskListener(mEditTaskListener);
    }
}
