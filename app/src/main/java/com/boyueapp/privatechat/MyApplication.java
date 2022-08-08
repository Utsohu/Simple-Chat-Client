package com.boyueapp.privatechat;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

import com.boyueapp.privatechat.ui.home.TcpClient;

import java.util.Date;

public class MyApplication extends Application {
    public TcpClient client;
    public static MyApplication mainApp;
    public String currentUserName = "";
    public String currentTime = "";
    public Date lastDate;
    public String serverIpAddress = "192.168.2.139";
    public ClientAlive service = null;
    public NotificationManager publicManager = null;

    @Override
    public void onCreate()
    {
        super.onCreate();
        lastDate = new Date(1);
        client = new TcpClient(serverIpAddress, 2016, listener);
        client.startConnect();
        mainApp = this;
    }

    public boolean isConnnected(){return client.isConnected();}

    public void startConnect(){
        client.startConnect();
    }

    public void sendMessage(String message){
        client.sendMessage(message,(byte)0x03);
    }

    public void showNotification(Context context, String message){
        service.sendNotification(message,context);
    }

    public TcpClient.OnMessageListener listener = new TcpClient.OnMessageListener() {
        @Override
        public void onMessageReceived(String message) {

        }

        @Override
        public void onConnected() {
        }

        @Override
        public void onDisConnected(String message) {
            client.startConnect();
        }

        @Override
        public void onError(String message) {

        }
    };
}
