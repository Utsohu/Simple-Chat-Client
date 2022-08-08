package com.boyueapp.privatechat;

import android.content.Intent;
import android.os.Bundle;

import com.boyueapp.privatechat.ui.home.TcpClient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.boyueapp.privatechat.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityLoginBinding binding;
    private Handler rcvHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TcpClient tcpClient = MyApplication.mainApp.client;

        binding.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = binding.editUserName.getText().toString();
                String password = binding.editUserPassword.getText().toString();
                MyApplication currentApp = MyApplication.mainApp;
                currentApp.sendMessage("@newUserLoginIn" + "@Username" + userName + "@UserPass" + password);
                currentApp.currentUserName = userName;

            }
        });
        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegisterActivity();
            }
        });
        this.rcvHandler = new Handler(Looper.getMainLooper()) {           //创建主线程接收子线程消息的接口对象，必须指定是在主线程循环内
            @Override
            public void handleMessage(Message msg) {
                String message = msg.toString();
                if (message.contains("@UserLoginInResult")){
                    String judge = message.split("@UserLoginInResult")[1];
                    if (judge.startsWith("true")) startMainActivity();
                    else{
                        binding.loginErrorMsgView.setText("登入失败，请检查账号与密码设置");
                    }
                }
            }
        };

        tcpClient.outHandler = rcvHandler;
    }

    public void startMainActivity(){
        Intent in = new Intent();
        in.setClass(this, MainActivity.class);
        startActivity(in);
    }

    public void startRegisterActivity(){
        Intent in = new Intent();
        in.setClass(this, RegisterActivity.class);
        startActivity(in);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_login);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}