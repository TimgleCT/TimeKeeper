package com.example.timct.timekeeper0823;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.util.Calendar;

public class alarmalert extends AppCompatActivity {

    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        //setContentView(R.layout.activity_alarmalert);
        mp = new MediaPlayer();

        mp = MediaPlayer.create(this, R.raw.test);
        mp.setLooping(true);
        mp.start();
        alarmDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp!=null){
            if (mp.isPlaying()){
                mp.stop();
            }
            mp.release();
        }
    }

    public void alarmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("WAKE UP NOW!!");
        builder.setPositiveButton("LATER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                alarm();
                finish();
            }
        });
        builder.setNegativeButton("CLOSED", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Calendar cd = Calendar.getInstance();
                cd.setTimeInMillis(System.currentTimeMillis());
                long time = cd.getTimeInMillis();
                Intent intent1 = new Intent(alarmalert.this, usagecount.class);
                Log.d("alert", "time"+time);
                intent1.putExtra("time", time);
                startActivity(intent1);
                mp.stop();
                finish();
            }
        });
        builder.show();
    }

    private void alarm(){
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        long triggertime = System.currentTimeMillis()+5000;
        Intent intent = new Intent(this, alarmalert.class);
        PendingIntent op = PendingIntent.getActivity(this,0, intent , PendingIntent.FLAG_UPDATE_CURRENT);

        am.set(AlarmManager.RTC, triggertime,op);

    }

}
