package com.example.timct.timekeeper0823;

import android.os.Bundle;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;

public class Nine_axis_record  implements SensorEventListener {
    public SensorManager sensorManager;
    public Sensor magneticSensor;
    public Sensor accelerometerSensor;
    public Sensor gyroscopeSensor;
    // 將納秒轉化為秒
    public static final float NS2S = 1.0f / 1000000000.0f;
    //public static long start_time;
    //public int timestamp;
    public static boolean state;
    public static float x;
    public static float y;
    public static float z;

    public void start_record(SensorManager sensorManager,Boolean state) {
        //historylist = new ArrayList();
        //sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.state = state;
        //magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        //sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        //sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if(this.state == false){
            Log.d("九軸","關閉九軸感應");
            sensorManager.unregisterListener(this);
        }
        if(accelerometerSensor == null){
            Log.d("九軸","註冊失敗");
        }else{
            Log.d("九軸","註冊成功");
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        //Log.d("九軸","狀態(state)："+state);
        //timestamp = (int)((System.currentTimeMillis() - start_time)/1000) ;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && state == true) {
            //Log.d("九軸","成功");
            // x,y,z分別存儲坐標軸x,y,z上的加速度
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            // 根據三個方向上的加速度值得到總的加速度值a
            //Log.d("九軸","X軸："+x+"Y軸："+y+"Z軸："+z);
//            System.out.println("x---------->" + x);
//            System.out.println("y---------->" + y);
//            System.out.println("z---------->" + z);
            //System. out .println( "時間戳記---------->" +timestamp);
            // 傳感器從外界採集數據的時間間隔為10000微秒
//            System.out.println("magneticSensor.getMinDelay()-------->" + magneticSensor.getMinDelay());
            // 加速度傳感器的最大量程
            //           System.out.println("event.sensor.getMaximumRange()-------->" + event.sensor.getMaximumRange());
            //historylist.add("陀螺儀x軸："+String.format("%.02f", x)+"，陀螺儀y軸："+String.format("%.02f", y)+"，陀螺儀z軸："+String.format("%.02f", z));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //    TODO Auto-generated method stub
    }
}
