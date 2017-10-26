package com.codename26.geofenceapplication;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TaskEditActivity extends AppCompatActivity {
    private GeoTask mGeoTask;
    private EditText mEditTextName;
    private EditText mEditTextDesc;
    private TextView tvRadius;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);

        Intent intent = getIntent();
        mGeoTask = (GeoTask) intent.getSerializableExtra(MainActivity.NEW_TASK);

        mEditTextName = (EditText) findViewById(R.id.editTextName);
        mEditTextDesc = (EditText) findViewById(R.id.editTextDesc);
        tvRadius = (TextView) findViewById(R.id.textViewRadius);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                Intent intent = new Intent(TaskEditActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.RETURNED_TASK, mGeoTask);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        inittextFields();

    }

    private void saveData() {
        mGeoTask.setTaskName(String.valueOf(mEditTextName.getText()));
        mGeoTask.setTaskDescription(String.valueOf(mEditTextDesc.getText()));
        mGeoTask.setTaskRadius(Float.parseFloat(String.valueOf(tvRadius.getText())));
    }

    private void inittextFields() {
        if (mGeoTask.getTaskName().length() > 0){
            mEditTextName.setText(mGeoTask.getTaskName());
        }
        if (mGeoTask.getTaskDescription() != null){
            mEditTextDesc.setText(mGeoTask.getTaskDescription());
        }
        if (mGeoTask.getTaskRadius() != 200){
            tvRadius.setText(String.valueOf(mGeoTask.getTaskRadius()));
        }
    }
}
