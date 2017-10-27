package com.codename26.geofenceapplication;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_CAMERA_POSITION = "key_camera_position";
    public static final String KEY_LOCATION = "key_location";
    public static final String TASK_ARRAY = "task_array";
    public static final String NEW_TASK = "new_task";
    public static final String EDIT_TASK = "edit_task";
    public static final String RETURNED_TASK = "returned_task";
    public static final int NEW_TASK_REQUEST_CODE = 1;
    public static final int EDIT_TASK_REQUEST_CODE = 2;
    private MapFragment mMapFragment;
    private ArrayList<GeoTask> mGeoTasks = new ArrayList<>();
    DataBaseHelper helper;


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
       helper = new DataBaseHelper(this);
       initMapFragment();
    }

   private PlaceholderFragment.SendGeoListener mSendGeoListener = new PlaceholderFragment.SendGeoListener() {
       @Override
       public void sendGeoToService(MyGeofence myGeofence) {
           Intent geofencingService = new Intent(MainActivity.this, GeofencingService.class);
           geofencingService.setAction(String.valueOf(Math.random()));
           geofencingService.putExtra(GeofencingService.EXTRA_ACTION, GeofencingService.Action.ADD);
           geofencingService.putExtra(GeofencingService.EXTRA_GEOTASK, myGeofence);

           startService(geofencingService);
       }
   };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void initMapFragment(){
        mMapFragment = new MapFragment();
        mGeoTasks = helper.getTasks();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TASK_ARRAY, (Serializable) mGeoTasks);
        mMapFragment.setArguments(bundle);
        //mMapFragment.setDeleteTaskListener(mDeleteTaskListener);
        //mMapFragment.setCreateTaskListener(mCreateTaskListener);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mMapFragment)
                .commit();
        //mMapFragment.setEditTaskListener(mEditTaskListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
