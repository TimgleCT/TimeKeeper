package com.example.user.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static com.example.user.myapplication.mainpage.KEY;

public class login extends AppCompatActivity {
    Connect_To_Server db_select;
    TextView signup;
    EditText user_mail, user_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button login = (Button)findViewById(R.id.login_btn);
        user_mail = (EditText)findViewById(R.id.user_mail);
        user_pwd = (EditText)findViewById(R.id.user_pwd);
        db_select = new Connect_To_Server();

        user_mail.setText(getSharedPreferences(KEY,MODE_PRIVATE).getString("u_id",null));
        user_pwd.setText(getSharedPreferences(KEY,MODE_PRIVATE).getString("u_pwd",null));

        signup = findViewById(R.id.signup_text);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, sign_up.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usermail = "";
                String userpwd = "";
                usermail = user_mail.getText().toString();
                userpwd = user_pwd.getText().toString();
                final String finalUsername = usermail;
                Thread get_data = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        db_select.connect("select_sql","SELECT user_id,u_password,u_name FROM `user` WHERE user_id ='"+ finalUsername +"'");
                        Log.d("連線","安安SELECT user_id,u_password,u_name FROM `user` WHERE user_id ='"+ finalUsername +"'");
                    }
                });
                get_data.start();
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String db_data =  db_select.get_data;
                Log.d("連線","安安"+db_data);
                String[] token = db_data.split("/");
                if(token.length != 3){
                    new AlertDialog.Builder(login.this).setTitle("請再試試看").setMessage("帳號或密碼錯誤!!")
                            .setNegativeButton("OK",null)
                            .show();
                }else{
                    String db_u_id = token[0];
                    String db_u_pwd = token[1];
                    if(db_u_pwd.equals(userpwd)){

                        SharedPreferences pref  = getApplication().getSharedPreferences(KEY, Context.MODE_PRIVATE);
                        pref.edit().clear();
                        pref.edit().putString("u_id",db_u_id).putString("u_pwd",db_u_pwd).commit();

                        Intent intent = new Intent(login.this, mainpage.class);
                        startActivity(intent);
                    }else{
                        new AlertDialog.Builder(login.this).setTitle("請再試試看").setMessage("帳號或密碼錯誤!!")
                                .setNegativeButton("OK",null)
                                .show();
                    }
                }
            }
        });


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(login.this)
                    .setTitle("要離開TimeKeeper了嗎?")
                    .setMessage("只是還未登入而已啦~?")
                    .setPositiveButton("我要離開了~",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                                    startMain.addCategory(Intent.CATEGORY_HOME);
                                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(startMain);
                                    System.exit(0);
                                    //MainActivity.this.finish();

                                }
                            })
                    .setNegativeButton("好啦~我登入一下",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub

                                }
                            }).show();
        }
        return true;
    }


    //點擊空白處隱藏鍵盤
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
