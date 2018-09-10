package com.example.timct.timekeeper0823;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;


public class alarm extends Fragment {
    public static final String INTENT_ALARM_LOG = "intent_alarm_log";
    private Button btnsubmit;
    private View view;
    private TextView tvtime;
    private Button btnadd;
    private CheckBox checkBox;
    private TimePicker timePicker;
    private int hour;
    private int minute;
    AlarmManager am;

    Calendar calendar = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_alarm, container, false);

        if (!isNoSwitch()){
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
        setalarm();

        return view;
    }

    public void setalarm(){
        tvtime = (TextView) view.findViewById(R.id.tvtime);
        btnadd = (Button) view.findViewById(R.id.btnadd);
        checkBox = (CheckBox) view.findViewById(R.id.checkBox);


        am = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.setTimeInMillis(System.currentTimeMillis());
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);

                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener(){


                    public void onTimeSet(TimePicker view, int hourOfDay, int minute1) {
                        tvtime.setText(hourOfDay + ":" + minute1);

                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute1);
                        calendar.set(Calendar.SECOND, 0);

                        Intent intent = new Intent(getActivity(), alarmalert.class);
                        PendingIntent pi = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        if (!checkBox.isChecked()) {
                            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
                            Toast.makeText(getActivity(), "Alarm:"+hour+":"+minute, Toast.LENGTH_LONG).show();
                        } else {
                            long intervalMillis  = 60 * 1000; // 60秒
                            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intervalMillis, pi);
                            Toast.makeText(getActivity(), "Alarm set", Toast.LENGTH_LONG).show();
                        }

                        Toast.makeText(getActivity(), hourOfDay+":"+minute1, Toast.LENGTH_LONG).show();



                    }
                }, hour, minute, false).show();

            }
        });
    }

    public void addalarm(){

        setalarm();

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), alarmalert.class);
                PendingIntent pi = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                if (!checkBox.isChecked()) {
                    am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
                    Toast.makeText(getActivity(), "Alarm:"+hour+":"+minute, Toast.LENGTH_LONG).show();
                } else {
                    long intervalMillis  = 60 * 1000; // 60秒
                    am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intervalMillis, pi);
                    Toast.makeText(getActivity(), "Alarm set", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public interface CallBack{
        public void getResult(long result);
    }
    public void getData(CallBack callBack){
        long time = calendar.getTimeInMillis();
        callBack.getResult(time);
    }

    private boolean isNoSwitch(){
        long ts = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) getActivity().getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, 0, ts);
        if (queryUsageStats == null || queryUsageStats.isEmpty()){
            return false;
        }
        return true;
    }


}