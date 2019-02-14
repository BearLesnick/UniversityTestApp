package com.pet.lesnick.letterappwithfragments.activities;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pet.lesnick.letterappwithfragments.R;

public class SensorTestActivity extends Activity { 
    SensorEventListener listener;
    Sensor sensor;
    SensorManager manager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_test_activity);
        SensorManager manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        assert manager != null;
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                Log.e("Sensor","" +event.accuracy);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                Log.e("Sensor accuracy",""+accuracy);
            }
        };


    }

    @Override
    protected void onPause() {
        manager.unregisterListener(listener);
        super.onPause();
    }

    @Override
    protected void onResume() {
        manager.registerListener(listener, sensor,SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();

    }
}
