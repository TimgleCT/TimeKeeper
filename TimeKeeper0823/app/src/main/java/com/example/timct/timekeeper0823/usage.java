package com.example.timct.timekeeper0823;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static android.R.layout.simple_expandable_list_item_1;


public class usage extends Fragment {
    private View view;
    private TextView tt;
    ListView db_usage_list;
    ListView db_soundaxis_list;
    ArrayList dblist_usage ;
    ArrayList dblist_soundaxis ;
    UsageStats usageStats;
    DB_usage DB_usage;
    DB_soundaxis DB_soundaxis;

    long time, stopuse, usetime, totaltime;
    int min, hr, sec;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_usage, container, false);
        tt = (TextView) view.findViewById(R.id.tt);
        db_usage_list = (ListView)view.findViewById(R.id.db_usage_listview);
        db_soundaxis_list = (ListView)view.findViewById(R.id.db_soundaxis_listview);
        dblist_usage = new ArrayList();
        dblist_soundaxis = new ArrayList();
        DB_usage = new DB_usage(view.getContext());
        DB_soundaxis = new DB_soundaxis(view.getContext());
        //dblist.clear();
        Cursor mCursor_usage = DB_usage.select();
        for(int i = 0; i<mCursor_usage.getCount();i++){
            mCursor_usage.moveToPosition(i);
            String Date = mCursor_usage.getString(mCursor_usage.getColumnIndex("date"));
            String Period = mCursor_usage.getString(mCursor_usage.getColumnIndex("period"));
            String state = mCursor_usage.getString(mCursor_usage.getColumnIndex("state"));
            dblist_usage.add("Date："+Date+"  Period："+Period+"State："+state);
        }
        ArrayAdapter adapter_db_usage = new ArrayAdapter(view.getContext(),simple_expandable_list_item_1,dblist_usage);
        db_usage_list.setAdapter(adapter_db_usage);
        mCursor_usage.close();

        Cursor mCursor_soundaxis = DB_soundaxis.select();
        for(int i = 0; i<mCursor_soundaxis.getCount();i++){
            mCursor_soundaxis.moveToPosition(i);
            String Name = mCursor_soundaxis.getString(mCursor_soundaxis.getColumnIndex("name"));
            String datetime = mCursor_soundaxis.getString(mCursor_soundaxis.getColumnIndex("date_time"));
            String x_axis = mCursor_soundaxis.getString(mCursor_soundaxis.getColumnIndex("x_axis"));
            String y_axis = mCursor_soundaxis.getString(mCursor_soundaxis.getColumnIndex("y_axis"));
            String z_axis = mCursor_soundaxis.getString(mCursor_soundaxis.getColumnIndex("z_axis"));
            String sound_db = mCursor_soundaxis.getString(mCursor_soundaxis.getColumnIndex("sound_db"));
            String state = mCursor_soundaxis.getString(mCursor_soundaxis.getColumnIndex("state"));
            //Log.d("測試","X軸："+x_axis);
            dblist_soundaxis.add(Name+"/"+datetime+"/"+x_axis+"/"+y_axis+"/"+z_axis+"/"+sound_db+"/"+state);
        }
        ArrayAdapter adapter_db_soundaxis = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1,dblist_soundaxis);
        db_soundaxis_list.setAdapter(adapter_db_soundaxis);
        mCursor_soundaxis.close();
        return view;
    }

    public void usagecount(long stopuse){
        UsageStatsManager usm = (UsageStatsManager) getActivity().getSystemService(Context.USAGE_STATS_SERVICE);
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

                tt.setText(hr+":"+min+":"+sec);
            }
        }


    }



}
