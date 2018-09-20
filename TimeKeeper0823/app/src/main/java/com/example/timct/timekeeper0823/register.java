package com.example.timct.timekeeper0823;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class register extends Activity {
    Connect_To_Server db_select;
    Connect_To_Server db_insert;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Button login = (Button)findViewById(R.id.login);
        Button submit = (Button)findViewById(R.id.submit);
        db_select = new Connect_To_Server();
        db_insert = new Connect_To_Server();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register.this, login.class);
                startActivity(intent);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText get_u_id = (EditText)findViewById(R.id.u_id);
                EditText get_u_pwd = (EditText)findViewById(R.id.u_pwd);
                EditText get_u_pwd_check = (EditText)findViewById(R.id.u_pwd_check);
                EditText get_u_name = (EditText)findViewById(R.id.u_name);
                final String u_id = get_u_id.getText().toString();
                String u_pwd = get_u_pwd_check.getText().toString();
                String u_name = get_u_name.getText().toString();
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(u_id).matches()){
                    new AlertDialog.Builder(register.this).setTitle("輸入錯誤").setMessage("帳號請填E-mail喔~~")
                            .setNegativeButton("OK",null)
                            .show();
                }else{
                    if(get_u_pwd.getText().toString().equals(get_u_pwd_check.getText().toString())){
                        Thread get_data = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                db_select.connect("select_sql","SELECT user_id,u_password,u_name FROM `user` WHERE user_id ='"+u_id+"'");
                                Log.d("連線","安安SELECT user_id,u_password FROM `user` WHERE user_id ='"+u_id+"'");
                            }
                        });
                        get_data.start();
                        //db_select.connect("select_sql","SELECT user_id,u_password FROM `user` WHERE user_id ='"+u_id+"'");
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        String db_data =  db_select.get_data;
                        Log.d("連線","安安"+db_data);
                        String[] token = db_data.split("/");
                        String db_u_id = token[0];
                        ////再來要做字串分割~取得getdata值
                        if(u_id.equals(db_u_id.toString())){
                            new AlertDialog.Builder(register.this).setTitle("輸入錯誤").setMessage("已有人註冊此帳號~換一個試試吧!")
                                    .setNegativeButton("OK",null)
                                    .show();
                        }else {
                            db_insert.connect("insert_sql","INSERT INTO `user` (`user_id`, `u_name`, `u_password`) VALUES('"+u_id+"', '"+u_name+"', '"+u_pwd+"');");
                            new AlertDialog.Builder(register.this).setTitle("註冊成功").setMessage("快去登入吧!!")
                                    .setNegativeButton("OK",null)
                                    .show();
                        }
                    }else {
                        new AlertDialog.Builder(register.this).setTitle("輸入錯誤").setMessage("剛剛輸入的兩組密碼不相同喔~")
                                .setNegativeButton("OK",null)
                                .show();
                    }
                }
            }
        });
    }
}
