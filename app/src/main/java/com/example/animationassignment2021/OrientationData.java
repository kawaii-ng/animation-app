package com.example.animationassignment2021;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.nfc.Tag;
import android.provider.SyncStateContract;
import android.util.Log;

/*
*
* OrientationData is a class to get the orientation data
* it set up the sensor to detect the rotation of the mobile device
*
* */

public class OrientationData implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor aSensor;
    private Sensor mSensor;

    private float[] aOutput;
    private float[] mOutput;

    private float[] orientation = new float[3];

    public OrientationData(Context context) {
        // create instances of Sensor Manger
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        // define type of specific sensor
        aSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void register() {
        // register a event listener
        sensorManager.registerListener(this, aSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void unregister(){
        sensorManager.unregisterListener(this, aSensor);
        sensorManager.unregisterListener(this, mSensor);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            aOutput = event.values;

        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mOutput = event.values;

        if(aOutput != null && mOutput != null) {

            float[] R = new float[9];
            float[] I = new float[9];

            if(SensorManager.getRotationMatrix(R, I, aOutput, mOutput)) {
                SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X, SensorManager.AXIS_Y, R);
                SensorManager.getOrientation(R, orientation);
            }
        }

    }

    public float[] getOrientation() {
        return orientation;
    }

}