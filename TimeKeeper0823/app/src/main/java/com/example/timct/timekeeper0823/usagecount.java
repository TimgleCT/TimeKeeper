package com.example.timct.timekeeper0823;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class usagecount extends AppCompatActivity {
    public static long start_time;
    public static long stop_record_time;
    long time, stopuse, usetime, totaltime;
    int min, hr, sec;
    static String state;
    static double volume;
    private StringBuilder sb;
    private DB_usage dbUsage;
    private DB_soundaxis dbSoundaxis;
    private IntentFilter theFilter;
    private ScreenBroadReceiver mScreenReceiver;
    public SensorManager sensorManager;
    Get_Sound sound_recorder;
    Nine_axis_record axis_recorder;
    public Date Today;
    public Timestamp timestamp;
    public SimpleDateFormat record_time;
    public static ArrayList id;
    public static ArrayList name;
    public static ArrayList date_time;
    public static ArrayList x_axis;
    public static ArrayList y_axis;
    public static ArrayList z_axis;
    public static ArrayList sound_db;
    ArrayList up_id,up_name,up_date_time,up_x_axis,up_y_axis,up_z_axis,up_sound_db;
    ArrayList up_id_u,up_date_time_u,up_period_u;
    String sql,sqltmp,sql_u,sql_u_tmp;
    Connect_To_Server connecting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        theFilter = new IntentFilter();
        theFilter.addAction(Intent.ACTION_SCREEN_ON);
        theFilter.addAction(Intent.ACTION_SCREEN_OFF);
        theFilter.addAction(Intent.ACTION_USER_PRESENT);

        mScreenReceiver = new ScreenBroadReceiver();
        sound_recorder = new Get_Sound();
        axis_recorder = new Nine_axis_record();
        date_time =new ArrayList();
        id = new ArrayList();
        name = new ArrayList();
        x_axis = new ArrayList();
        y_axis = new ArrayList();
        z_axis = new ArrayList();
        sound_db = new ArrayList();
        up_id = new ArrayList();
        up_name = new ArrayList();
        up_date_time = new ArrayList();
        up_x_axis = new ArrayList();
        up_y_axis = new ArrayList();
        up_z_axis = new ArrayList();
        up_sound_db = new ArrayList();
        up_id_u = new ArrayList();
        up_date_time_u = new ArrayList();
        up_period_u = new ArrayList();
        dbSoundaxis = new DB_soundaxis(usagecount.this);
        dbUsage = new DB_usage(usagecount.this);
        sql = new String();
        sqltmp = new String();
        sql_u = new String();
        sql_u_tmp = new String();
        connecting = new Connect_To_Server();

        Intent intent1 = getIntent();
        time = intent1.getLongExtra("time", 0);
        Log.d("tag", "get"+time);

        getScreen();
        start_time = System.currentTimeMillis();
        stop_record_time = start_time+ 60*1000;//結束時間設為一分鐘後
        Log.d("state", "s"+state);
        if(state == "true"){
            //axis_recorder.start_record(sensorManager, true);//九軸開幾紀錄
            start_listen_nine_axis();
            sound_recorder.startRecord();//分貝開始記錄
            startListenAudio();//存取資料在陣列裡的執行續
            Log.d("紀錄", "開始");
        }
        CheckState_SubmitRecord();
        CheckRecordtime_SubmitRecord();
    }
    public void usagetime(long stopuse){
        UsageStatsManager usm = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(time);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(stopuse);

        final List<UsageStats> stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_BEST, calendar1.getTimeInMillis(),
                calendar2.getTimeInMillis());
        if (stats == null || stats.isEmpty()){

        }else {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
            for (UsageStats usageStats : stats){
                usetime = usageStats.getTotalTimeInForeground();
                totaltime = totaltime + usetime;
                min = (int) ((totaltime)/(1000*60)%60);
                sec = (int) (totaltime/1000)%60;
                hr = (int)((totaltime/(1000*60*60))%24);

            }
        }

    }

    public void getScreen(){
        PowerManager pm = (PowerManager)getBaseContext().getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isInteractive();
        if (isScreenOn == true){
            state = "true";
        }else if (isScreenOn == false){state = "false"; }

    }
    public void start_listen_nine_axis(){
        Thread start = new Thread((new Runnable() {
            @Override
            public void run() {
                axis_recorder.start_record(sensorManager, true);
            }
        }));
        start.start();
    }
    public void CheckRecordtime_SubmitRecord(){
        Thread thread_check_time = new Thread(new Runnable() {
            @Override
            public void run() {
                while(System.currentTimeMillis() <= stop_record_time){
                        if(System.currentTimeMillis() == stop_record_time){
                            //axis_recorder.start_record(sensorManager,false);//九軸停止紀錄(但現在我註解掉了)
                            sound_recorder.stopRecord();//停止錄音
                            for(int i = 0;i<name.size();i++){
                                dbSoundaxis.insert(id.get(i).toString(),(String) name.get(i), (Timestamp) date_time.get(i),Double.parseDouble(x_axis.get(i).toString()),Double.parseDouble(y_axis.get(i).toString()),Double.parseDouble(z_axis.get(i).toString()),Double.parseDouble(sound_db.get(i).toString()));
                                Log.d("資料庫",id.get(i).toString()+"/"+(String) name.get(i)+"/"+(Timestamp) date_time.get(i)+"/"+Double.parseDouble(x_axis.get(i).toString())+"/"+Double.parseDouble(y_axis.get(i).toString())+"/"+Double.parseDouble(z_axis.get(i).toString())+"/"+Double.parseDouble(sound_db.get(i).toString()));
                            }
                            Log.d("紀錄", "結束");
                            Log.d("TAG", "九軸&分貝資料上傳");
                        }
                }
            }
        });
        thread_check_time.start();
        try {
            thread_check_time.join();
            Cursor update_cursor = dbSoundaxis.select_update();
            for(int i = 0; i<update_cursor.getCount();i++){
                update_cursor.moveToPosition(i);
                up_id.add(update_cursor.getString(update_cursor.getColumnIndex("_id")));
                up_name.add(update_cursor.getString(update_cursor.getColumnIndex("name")));
                up_date_time.add((update_cursor.getString(update_cursor.getColumnIndex("date_time"))));
                up_x_axis.add((update_cursor.getString(update_cursor.getColumnIndex("x_axis"))));
                up_y_axis.add((update_cursor.getString(update_cursor.getColumnIndex("y_axis"))));
                up_z_axis.add((update_cursor.getString(update_cursor.getColumnIndex("z_axis"))));
                up_sound_db.add((update_cursor.getString(update_cursor.getColumnIndex("sound_db"))));
            }
            sql = "INSERT INTO `sound_axis_record` (`User_id`, `Name`, `Date_time`, `X_axis`, `Y_axis`, `Z_axis`, `Sound_db`) VALUES";
            for(int i = 0;i<up_name.size();i++){
                String id = up_id.get(i).toString();
                String name = (String) up_name.get(i);
                Timestamp time = Timestamp.valueOf(up_date_time.get(i).toString());
                double x_axis = Double.parseDouble(up_x_axis.get(i).toString());
                double y_axis = Double.parseDouble(up_y_axis.get(i).toString());
                double z_axis = Double.parseDouble(up_z_axis.get(i).toString());
                double sound_db = Double.parseDouble(up_sound_db.get(i).toString());
                if(i == up_name.size()-1){
                    sqltmp = sqltmp+"('"+id+"','"+name+"', '"+time+"', '"+x_axis+"', '"+y_axis+"', '"+z_axis+"', '"+sound_db+"');";
                }else {
                    sqltmp = sqltmp+"('"+id+"','"+name+"', '"+time+"', '"+x_axis+"', '"+y_axis+"', '"+z_axis+"', '"+sound_db+"'),";
                }
            }
            sql = sql+sqltmp;
            Log.d("sql語法",sql);
            connecting.connect("insert_sql",sql);
            dbSoundaxis.update_state_change();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public void CheckState_SubmitRecord(){
        Thread thread_check_state = new Thread(new Runnable() {
            @Override
            public void run() {
                while(state == "true"){
                    getScreen();
                    Log.d("執行續", "檢查螢幕狀態");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(state == "false"){
                    Log.d("state", "enter if");
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTimeInMillis(System.currentTimeMillis());
                    stopuse = calendar2.getTimeInMillis();
                    usagetime(stopuse);
                    dbUsage.insert("ct25283115@gmail.com",(int)totaltime,(int)time);
                    Log.d("紀錄", "結束");
                    Log.d("TAG", "螢幕使用狀態上傳");
                }
            }
        });
        thread_check_state.start();
        try {
            thread_check_state.join();
            Cursor update_cursor = dbUsage.select_update();
            for(int i = 0; i<update_cursor.getCount();i++){
                update_cursor.moveToPosition(i);
                up_id_u.add(update_cursor.getString(update_cursor.getColumnIndex("id")));
                up_date_time_u.add(update_cursor.getString(update_cursor.getColumnIndex("date")));
                up_period_u.add((update_cursor.getString(update_cursor.getColumnIndex("period"))));
            }
            sql_u = "INSERT INTO `screen_record` (`Date`, `User_id`, `Period`) VALUES";
            for(int i = 0;i<up_id_u.size();i++){
                String id = up_id_u.get(i).toString();
                int time = Integer.parseInt(up_date_time_u.get(i).toString());
                int period  = Integer.parseInt(up_period_u.get(i).toString());
                if(i == up_id_u.size()-1){
                    sql_u_tmp = sql_u_tmp+"('"+time+"','"+id+"', '"+period+"');";
                }else {
                    sql_u_tmp = sql_u_tmp+"('"+time+"','"+id+"', '"+period+"'),";
                }
            }
            sql_u = sql_u+sql_u_tmp;
            Log.d("sql語法",sql_u);
            connecting.connect("insert_sql",sql_u);
            dbUsage.update_state_change();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void startListenAudio() {
        final int Base = 1;
        Thread thread_sound = new Thread(new Runnable() {
            @Override
            public void run() {
                while (System.currentTimeMillis() <= stop_record_time) {
                    try {
                        if (sound_recorder.mMediaRecorder != null) {
                            volume = sound_recorder.mMediaRecorder.getMaxAmplitude() / Base;  //獲取聲壓值
                            Today = new Date();
                            timestamp = new Timestamp(Today.getTime());
                            id.add("ct25283115@gmail.com");
                            name.add("Tim");
                            date_time.add(timestamp);
                            x_axis.add(String.format("%.02f", axis_recorder.x));
                            y_axis.add(String.format("%.02f", axis_recorder.y));
                            z_axis.add(String.format("%.02f", axis_recorder.z));
                            if (volume > 1) {
                                volume = 20 * Math.log10(volume);//將聲壓值轉為分貝值
                            }
                            sound_db.add(String.format("%.02f",volume));
                            Log.d("執行續",volume+"分貝");
                            Log.d("執行續",axis_recorder.x+"X軸");
                            Log.d("執行續",axis_recorder.y+"Y軸");
                            Log.d("執行續",axis_recorder.z+"Z軸");
                        }
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread_sound.start();
    }
    private class ScreenBroadReceiver extends BroadcastReceiver {
        private String action = null;
        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)|| Intent.ACTION_SCREEN_OFF.equals(action)){
                state = "true";
            }else if (Intent.ACTION_USER_PRESENT.equals(action)){
                state = "false";
            }
        }
    }
}

