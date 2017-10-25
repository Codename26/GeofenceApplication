package com.codename26.geofenceapplication;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.location.Geofence;

import java.io.Serializable;

/**
 * Created by Dell on 01.10.2017.
 */

public class GeoTask implements Serializable {
    public static final String TABLE_NAME = "Tasks";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TASK_NAME = "TaskName";
    public static final String COLUMN_TASK_DESCRIPTION = "TaskDescription";
    public static final String COLUMN_TASK_LATITUDE = "TaskLatitude";
    public static final String COLUMN_TASK_LONGITUDE = "TaskLongitude";
    public static final String COLUMN_TASK_RADIUS = "TaskRadius";
    private static final int ONE_MINUTE = 60000;
    private String mTaskName;
    private String mTaskDescription;
    private double mTaskLatitude;
    private double mTaskLongitude;
    private float mTaskRadius;
    private long mTaskId;

    public GeoTask() {
    }

    public GeoTask(String taskName, double taskLatitude, double taskLongitude, float taskRadius) {
        mTaskName = taskName;
        mTaskLatitude = taskLatitude;
        mTaskLongitude = taskLongitude;
        mTaskRadius = taskRadius;
    }

    public GeoTask(double taskLatitude, double taskLongitude) {
        mTaskLatitude = taskLatitude;
        mTaskLongitude = taskLongitude;
    }

    public String toString(){
        return String.format("Name = %s, Desc = %s, Latitude = %f, Longitude = %f",
                mTaskName, mTaskDescription, mTaskLatitude, mTaskLongitude);
    }

    public Geofence taskToGeofence(){
        return new Geofence.Builder()
                .setRequestId(String.valueOf(mTaskId))
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setCircularRegion(mTaskLatitude, mTaskLongitude, mTaskRadius)
                .setExpirationDuration(ONE_MINUTE)
                .build();
    }

    public String getTaskName() {
        return mTaskName;
    }

    public void setTaskName(String taskName) {
        mTaskName = taskName;
    }

    public String getTaskDescription() {
        return mTaskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        mTaskDescription = taskDescription;
    }

    public double getTaskLatitude() {
        return mTaskLatitude;
    }

    public void setTaskLatitude(double taskLatitude) {
        mTaskLatitude = taskLatitude;
    }

    public double getTaskLongitude() {
        return mTaskLongitude;
    }

    public void setTaskLongitude(double taskLongitude) {
        mTaskLongitude = taskLongitude;
    }

    public float getTaskRadius() {
        return mTaskRadius;
    }

    public void setTaskRadius(float taskRadius) {
        mTaskRadius = taskRadius;
    }

    public long getTaskId() {
        return mTaskId;
    }

    public void setTaskId(long taskId) {
        mTaskId = taskId;
    }
}


