package com.example.timct.timekeeper0823;
//使用前請先確定lib內有三個jar檔
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
//javax.mail.Authenticator
public class Forgot_password extends Activity{
    Button submit,back;
    EditText get_account,get_name;
    Connect_To_Server db_select;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        submit = (Button)findViewById(R.id.submit);
        back = (Button)findViewById(R.id.back);
        get_account = (EditText)findViewById(R.id.account);
        get_name = (EditText)findViewById(R.id.name);
        db_select = new Connect_To_Server();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Forgot_password.this, login.class);
                startActivity(intent);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String account = get_account.getText().toString();
                String name = get_name.getText().toString();
                Thread check_account = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        db_select.connect("select_sql","SELECT user_id,u_password,u_name FROM `user` WHERE user_id = '"+ account +"'");
                    }
                });
                check_account.start();
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String db_data =  db_select.get_data;
                String[] token = db_data.split("/");
                if(token.length != 3){
                    new AlertDialog.Builder(Forgot_password.this).setTitle("在試試看一次喔~").setMessage("信箱或姓名錯誤!!")
                            .setNegativeButton("OK",null)
                            .show();
                }else {
                    String db_u_id = token[0];
                    String db_u_pwd = token[1];
                    String db_u_name = token[2];
                    if(db_u_name.equals(name)){
                        main(db_u_id,db_u_name,db_u_pwd);
                    }else{
                        new AlertDialog.Builder(Forgot_password.this).setTitle("在試試看一次喔~").setMessage("信箱或姓名錯誤!!")
                                .setNegativeButton("OK",null)
                                .show();
                    }
                }
            }
        });
    }
    public void main(final String address, final String name, final String pwd) {
        Thread send_mail = new Thread(new Runnable() {
            @Override
            public void run() {
                String host = "smtp.gmail.com";
                int port = 587;
                final String username = "timekeepernukim@gmail.com";
                final String password = "g6ru0 ej03ru8";//your password

                Properties props = new Properties();
                props.put("mail.smtp.host", host);
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.port", port);
                Session session = Session.getInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress("timekeepernukim@gmail.com"));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(address));
                    message.setSubject("TimeKeeper 會員密碼");
                    message.setText("Dear "+name+", \n\n 您好，您的密碼為："+pwd);

                    Transport transport = session.getTransport("smtp");
                    transport.connect(host, port, username, password);

                    Transport.send(message);
                    //Log.d("寄信","結束");
                    System.out.println("寄送email結束.");
                } catch (MessagingException e) {
                    //e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        });
        send_mail.start();
        new AlertDialog.Builder(Forgot_password.this).setTitle("去信箱看看吧~").setMessage("確認信已送出~!!")
                .setNegativeButton("OK",null)
                .show();
    }
}
