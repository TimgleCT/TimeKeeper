package com.example.timct.timekeeper0823;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String KEY = "com.example.timct.timekeeper0823.app";
    public static boolean logon = false;
    private android.support.design.widget.TabLayout mTabs;
    private ViewPager mViewPager;
    private FragmentManager mfm;
    private alarm alarm = new alarm();
    private usage usage = new usage();
    public class BuildDev{
        public static final int RECORD_AUDIO = 0;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},BuildDev.RECORD_AUDIO);
        }

        final String user = getSharedPreferences(KEY,MODE_PRIVATE).getString("u_id",null);
        final String pwd = getSharedPreferences(KEY,MODE_PRIVATE).getString("u_pwd",null);
        if(user ==null ||pwd == null){
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);
        }
            mTabs = (android.support.design.widget.TabLayout) findViewById(R.id.tabs);
            mViewPager = (ViewPager) findViewById(R.id.viewpager);

            mTabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));

            mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    switch (position) {
                        case 0:
                            return alarm;
                        case 1:
                            return usage;
                    }
                    return null;
                }

                @Override
                public int getCount() {
                    return 2;
                }

            });

            alarm.getData(new alarm.CallBack() {
                @Override
                public void getResult(long result) {

                }
            });

    }

    public void onTabSelect(TabLayout.Tab tab){
        mViewPager.setCurrentItem(tab.getPosition());
    }

    public void onTabUnselected(TabLayout.Tab tab) {

    }

    public void onTabReselected(TabLayout.Tab tab) {

    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
}
