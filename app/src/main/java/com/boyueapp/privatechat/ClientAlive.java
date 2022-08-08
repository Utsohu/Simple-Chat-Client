package com.boyueapp.privatechat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.boyueapp.privatechat.MyApplication;
import com.boyueapp.privatechat.R;
import com.boyueapp.privatechat.databinding.ActivityMainBinding;

public class ClientAlive extends Service {
    private Handler checkPTimer;
    private Runnable checkPTask;
    private NotificationManager newManager;

    @Override
    public void onCreate() {
        super.onCreate();

        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        String id = "1";
        String name = "channel_name_1";
        Notification notification = null;
        newManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            newManager.createNotificationChannel(new NotificationChannel("999","vip", NotificationManager.IMPORTANCE_DEFAULT));
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(id,name,NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(null,null);
            newManager.createNotificationChannel(channel);
            notification = new Notification.Builder(this)
                    .setChannelId(id).setContentTitle(this.getResources().getString(R.string.app_name))
                    .setContentIntent(pendingIntent).setAutoCancel(false).setOngoing(true).setSmallIcon(R.drawable.ic_launcher_foreground)
                    .build();
        }
        else{
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(this.getResources().getString(R.string.app_name))
                    .setContentIntent(pendingIntent).setPriority(Notification.PRIORITY_DEFAULT)
                    .setAutoCancel(false).setOngoing(true).setSmallIcon(R.drawable.ic_launcher_foreground);
            notification = notificationBuilder.build();
        }
        startForeground(1,notification);

//        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.ON_AFTER_RELEASE|PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
//        wakeLock.acquire();

        checkPTimer = new Handler();
        checkPTask = new Runnable() {
            @Override
            public void run() {
                if(checkPTimer == null) return;
                if (!MyApplication.mainApp.client.isConnected()){
                    MyApplication.mainApp.client.startConnect();
                }
                checkPTimer.postDelayed(this, 5000);
            }
        };
        checkPTimer.postDelayed(checkPTask, 5000);
        MyApplication.mainApp.service = this;

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);
    }
    public void Notify(String title ,String content) {

    }

    public void sendNotification(String message, Context context){
        if (newManager == null) return;
        Notification notification1 = new NotificationCompat.Builder(context,"998").setContentTitle("新消息").setContentText(message)
                .setSmallIcon(R.drawable.ic_notification).build();
        newManager.notify(1,notification1);
    }
}