package com.boyueapp.privatechat;

import android.content.Intent;
import android.os.Bundle;

import com.boyueapp.privatechat.ui.home.TcpClient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import com.boyueapp.privatechat.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityRegisterBinding binding;
    private Handler rcvHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TcpClient tcpClient = MyApplication.mainApp.client;

        binding.registerAcceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = binding.registerUserName.getText().toString();
                String password = binding.registerPassword.getText().toString();
                tcpClient.sendMessage(userName + "@Password" + password, (byte)0x05);
                MyApplication.mainApp.currentUserName = userName;
            }
        });

        this.rcvHandler = new Handler(Looper.getMainLooper()) {           //创建主线程接收子线程消息的接口对象，必须指定是在主线程循环内
            @Override
            public void handleMessage(Message msg) {
                String message = msg.toString();
                if (message.contains("@UserLoginInResult")){
                    String judge = message.split("@UserLoginInResult")[1];
                    if (judge.startsWith("true")) startMainActivity();
                }
            }
        };
        tcpClient.outHandler = rcvHandler;
    }

    private void startMainActivity(){
        Intent in = new Intent();
        in.setClass(this, MainActivity.class);
        startActivity(in);
    }

}