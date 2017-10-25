package com.codename26.geofenceapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {
    public DataBaseHelper(Context context) {
        super(context, "MapTasker.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + com.codename26.geofenceapplication.GeoTask.TABLE_NAME + "("
                + com.codename26.geofenceapplication.GeoTask.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_NAME + " TEXT NOT NULL,"
                + com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_DESCRIPTION + " TEXT,"
                + com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_RADIUS + " REAL,"
                + com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_LATITUDE + " REAL NOT NULL,"
                + com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_LONGITUDE + " REAL NOT NULL);");

        fillDB(sqLiteDatabase);

    }

    private void fillDB(SQLiteDatabase sqLiteDatabase) {
        double[] latArray = {50.516303, 50.515873, 50.515795, 50.512714};
        double[] lonArray = {30.455847, 30.442139, 30.432531, 30.417893};
        try {
            for (int i = 0; i < 4; i++) {
                ContentValues values = new ContentValues();
                values.put(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_NAME, "GeoTask " + i );
                values.put(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_DESCRIPTION, "Desc " + i );
                values.put(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_RADIUS, 200);
                values.put(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_LATITUDE, latArray[i]);
                values.put(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_LONGITUDE , lonArray[i]);

                sqLiteDatabase.insert(com.codename26.geofenceapplication.GeoTask.TABLE_NAME, null, values);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
     //   sqLiteDatabase.close();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean updateTask(GeoTask geoTask){
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_NAME, geoTask.getTaskName());
            values.put(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_DESCRIPTION, geoTask.getTaskDescription());
            values.put(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_RADIUS, geoTask.getTaskRadius());
            values.put(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_LATITUDE, geoTask.getTaskLatitude());
            values.put(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_LONGITUDE , geoTask.getTaskLongitude());

            db.update(com.codename26.geofenceapplication.GeoTask.TABLE_NAME, values, com.codename26.geofenceapplication.GeoTask.COLUMN_ID + "=" + geoTask.getTaskId(),null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
      //  db.close();

        return false;
    }

    public long insertTask(GeoTask geoTask){
        long id = 0;
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_NAME, geoTask.getTaskName());
            values.put(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_DESCRIPTION, geoTask.getTaskDescription());
            values.put(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_RADIUS, geoTask.getTaskRadius());
            values.put(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_LATITUDE, geoTask.getTaskLatitude());
            values.put(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_LONGITUDE , geoTask.getTaskLongitude());

            id = db.insert(com.codename26.geofenceapplication.GeoTask.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
      //  db.close();

        return id;
    }

    public long newTask(){
        long id = 0;
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_NAME, "");
            values.put(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_DESCRIPTION, "");
            values.put(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_RADIUS, 200);
            values.put(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_LATITUDE, 0);
            values.put(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_LONGITUDE , 0);

            id = db.insert(com.codename26.geofenceapplication.GeoTask.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
      //  db.close();

        return id;
    }

    public GeoTask getTask(long id){
        SQLiteDatabase db = getWritableDatabase();
        GeoTask geoTask = new GeoTask();
        Cursor cursor = null;

        try {
            cursor = db.query(com.codename26.geofenceapplication.GeoTask.TABLE_NAME, null, com.codename26.geofenceapplication.GeoTask.COLUMN_ID + "=" + id, null, null, null, null);

            if (cursor.moveToFirst()) {

                    geoTask.setTaskId(cursor.getLong(cursor.getColumnIndex(com.codename26.geofenceapplication.GeoTask.COLUMN_ID)));
                    geoTask.setTaskName(cursor.getString(cursor.getColumnIndex(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_NAME)));
                    geoTask.setTaskDescription(cursor.getString(cursor.getColumnIndex(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_DESCRIPTION)));
                    geoTask.setTaskRadius(cursor.getFloat(cursor.getColumnIndex(GeoTask.COLUMN_TASK_RADIUS)));
                    geoTask.setTaskLatitude(cursor.getDouble(cursor.getColumnIndex(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_LATITUDE)));
                    geoTask.setTaskLongitude(cursor.getDouble(cursor.getColumnIndex(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_LONGITUDE)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
           // db.close();
        }

        return geoTask;
    }

    public ArrayList<GeoTask> getTasks() {
        ArrayList<GeoTask> geoTasks = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(com.codename26.geofenceapplication.GeoTask.TABLE_NAME, null, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    GeoTask geoTask = new GeoTask();

                    geoTask.setTaskId(cursor.getLong(cursor.getColumnIndex(com.codename26.geofenceapplication.GeoTask.COLUMN_ID)));
                    geoTask.setTaskName(cursor.getString(cursor.getColumnIndex(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_NAME)));
                    geoTask.setTaskDescription(cursor.getString(cursor.getColumnIndex(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_DESCRIPTION)));
                    geoTask.setTaskRadius(cursor.getFloat(cursor.getColumnIndex(GeoTask.COLUMN_TASK_RADIUS)));
                    geoTask.setTaskLatitude(cursor.getDouble(cursor.getColumnIndex(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_LATITUDE)));
                    geoTask.setTaskLongitude(cursor.getDouble(cursor.getColumnIndex(com.codename26.geofenceapplication.GeoTask.COLUMN_TASK_LONGITUDE)));
                    geoTasks.add(geoTask);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
          //  db.close();
        }
        return geoTasks;
    }

    public boolean deleteTask(long id) {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();

        try {
            count = db.delete(com.codename26.geofenceapplication.GeoTask.TABLE_NAME, com.codename26.geofenceapplication.GeoTask.COLUMN_ID + "=" + id, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
       // db.close();

        return count > 0;
    }


}
