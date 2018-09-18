package com.example.timct.timekeeper0823;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.example.timct.timekeeper0823.MainActivity.KEY;

public class login extends Activity {
    Connect_To_Server db_select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Button login = (Button)findViewById(R.id.login);
        Button register = (Button)findViewById(R.id.register);
        final EditText get_u_id = (EditText)findViewById(R.id.user_id);
        final EditText get_u_pwd = (EditText)findViewById(R.id.u_pwd);
        db_select = new Connect_To_Server();


        get_u_id.setText(getSharedPreferences(KEY,MODE_PRIVATE).getString("u_id",null));
        get_u_pwd.setText(getSharedPreferences(KEY,MODE_PRIVATE).getString("u_pwd",null));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = "";
                String userpwd = "";
                username = get_u_id.getText().toString();
                userpwd = get_u_pwd.getText().toString();
                final String finalUsername = username;
                Thread get_data = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        db_select.connect("select_sql","SELECT user_id,u_password FROM `user` WHERE user_id ='"+ finalUsername +"'");
                        Log.d("連線","安安SELECT user_id,u_password FROM `user` WHERE user_id ='"+ finalUsername +"'");
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
                if(token.length != 2){
                    new AlertDialog.Builder(login.this).setTitle("在試試看一次喔~").setMessage("帳號或密碼錯誤!!")
                            .setNegativeButton("OK",null)
                            .show();
                }else{
                    String db_u_id = token[0];
                    String db_u_pwd = token[1];
                    if(db_u_pwd.equals(userpwd)){

                        SharedPreferences pref  = getApplication().getSharedPreferences(KEY, Context.MODE_PRIVATE);
                        pref.edit().clear();
                        pref.edit().putString("u_id",db_u_id).putString("u_pwd",db_u_pwd).commit();

                        Intent intent = new Intent(login.this, MainActivity.class);
                        startActivity(intent);
                    }else{
                        new AlertDialog.Builder(login.this).setTitle("在試試看一次喔~").setMessage("帳號或密碼錯誤!!")
                                .setNegativeButton("OK",null)
                                .show();
                    }
                }

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, register.class);
                startActivity(intent);
            }
        });
    }
}
