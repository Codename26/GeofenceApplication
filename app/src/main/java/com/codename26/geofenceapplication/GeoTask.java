package com.codename26.geofenceapplication;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dell on 01.10.2017.
 */

public class GeoTask implements Parcelable {
    public static final String TABLE_NAME = "Tasks";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TASK_NAME = "TaskName";
    public static final String COLUMN_TASK_DESCRIPTION = "TaskDescription";
    public static final String COLUMN_TASK_LATITUDE = "TaskLatitude";
    public static final String COLUMN_TASK_LONGITUDE = "TaskLongitude";
    public static final String COLUMN_TASK_TAG = "TaskTag";
    public static final String COLUMN_TASK_NOTIFICATION = "TaskNotification";



    private String mTaskName;
    private String mTaskDescription;
    private String mTaskTag;
    private double mTaskLatitude;
    private double mTaskLongitude;
    private long mTaskId;
    private int mTaskNotification;

    public GeoTask() {
    }

    public GeoTask(String taskName, double taskLatitude, double taskLongitude) {
        mTaskName = taskName;
        mTaskLatitude = taskLatitude;
        mTaskLongitude = taskLongitude;
    }

    public GeoTask(double taskLatitude, double taskLongitude) {
        mTaskLatitude = taskLatitude;
        mTaskLongitude = taskLongitude;
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

    public String getTaskTag() {
        return mTaskTag;
    }

    public void setTaskTag(String taskTag) {
        mTaskTag = taskTag;
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

    public long getTaskId() {
        return mTaskId;
    }

    public void setTaskId(long taskId) {
        mTaskId = taskId;
    }

    public int getTaskNotification() {
        return mTaskNotification;
    }

    public void setTaskNotification(int taskNotification) {
        mTaskNotification = taskNotification;
    }

    public String toString(){
        return String.format("Name = %s, Desc = %s, Latitude = %f, Longitude = %f",
                mTaskName, mTaskDescription, mTaskLatitude, mTaskLongitude);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTaskName);
        dest.writeString(this.mTaskDescription);
        dest.writeString(this.mTaskTag);
        dest.writeDouble(this.mTaskLatitude);
        dest.writeDouble(this.mTaskLongitude);
        dest.writeLong(this.mTaskId);
        dest.writeInt(this.mTaskNotification);
    }

    protected GeoTask(Parcel in) {
        this.mTaskName = in.readString();
        this.mTaskDescription = in.readString();
        this.mTaskTag = in.readString();
        this.mTaskLatitude = in.readDouble();
        this.mTaskLongitude = in.readDouble();
        this.mTaskId = in.readLong();
        this.mTaskNotification = in.readInt();
    }

    public static final Creator<GeoTask> CREATOR = new Creator<GeoTask>() {
        @Override
        public GeoTask createFromParcel(Parcel source) {
            return new GeoTask(source);
        }

        @Override
        public GeoTask[] newArray(int size) {
            return new GeoTask[size];
        }
    };
}


