package com.example.user.myapplication;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ai_alarm_group extends Activity {
    ListView ai_gruop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ai_alarm_group);

        ai_gruop = findViewById(R.id.group_list);
        String[] str = {"Tina","Jahone","YYYYYY","TTTTTTT","BBBBBBB"};
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.ai_group_item, R.id.name, str);
        ai_gruop.setAdapter(adapter);

    }
}
