package com.boyueapp.privatechat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.boyueapp.privatechat.databinding.ActivityMultiUserChatBinding;
import com.boyueapp.privatechat.ui.home.TcpClient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ScrollView;

import com.boyueapp.privatechat.databinding.ActivityMultiUserChatBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MultiUserChatActivity extends AppCompatActivity {
    String startTitle = null;
    private TcpClient tcpclient = null;
    private Handler rcvHandler = null;
    private ActivityMultiUserChatBinding binding;
    private NotificationManager manager;
    private Notification notification1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent in = getIntent();
        startTitle = (String) in.getStringExtra("users");
        binding = ActivityMultiUserChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tcpclient = MyApplication.mainApp.client;
        binding.textChatName.setText(startTitle.split("@NewTargetUser:")[1]);

        binding.sndMultiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tcpclient == null) return;
                tcpclient.sendMessage(startTitle + "@NewTargetUser:" + binding.sndMultiEdit.getText().toString(),(byte) 0x02);
                binding.sndMultiEdit.setText("");
            }
        });

        this.rcvHandler = new Handler(Looper.getMainLooper()) {           //创建主线程接收子线程消息的接口对象，必须指定是在主线程循环内
            @Override
            public void handleMessage(Message msg) {
                String message = msg.obj.toString();
                if (message.startsWith("@UserLoginInResult")) return;
                Date curDate = new Date(System.currentTimeMillis());
                if (curDate.getTime() - MyApplication.mainApp.lastDate.getTime()  >= 60000){
                    MyApplication.mainApp.lastDate = curDate;
                    SimpleDateFormat formater = new SimpleDateFormat("MM/dd HH:mm");
                    String str = formater.format(curDate);

                    SpannableStringBuilder timeBuilder = new SpannableStringBuilder();
                    timeBuilder.append(str);
                    timeBuilder.setSpan(new AbsoluteSizeSpan(30),0,str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    timeBuilder.setSpan(new ForegroundColorSpan(Color.BLUE),0,str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    //binding.textHome.setGravity(Gravity.CENTER);
                    binding.textMultiHome.append(timeBuilder);
                    binding.textMultiHome.append("\n");
                }

                SpannableStringBuilder newBuilder = new SpannableStringBuilder();
                newBuilder.append(message);
                newBuilder.setSpan(new AbsoluteSizeSpan(65),0,message.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //binding.textHome.setGravity(Gravity.LEFT);
                binding.textMultiHome.append(newBuilder);
                binding.textMultiHome.append("\n");
                binding.multiScrollText.fullScroll(ScrollView.FOCUS_DOWN);

                /*manager = MyApplication.mainApp.publicManager;
                notification1 = new NotificationCompat.Builder(getBaseContext(),"999").setContentTitle("收到私聊消息").setContentText(message)
                        .setSmallIcon(R.drawable.ic_launcher_background).build();
                manager.notify(1,notification1);*/
                MyApplication.mainApp.showNotification(getBaseContext(),message);
            }
        };
        tcpclient.outHandler = rcvHandler;
    }

}