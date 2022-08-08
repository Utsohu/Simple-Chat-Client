package com.boyueapp.privatechat.ui.home;

import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.boyueapp.privatechat.MyApplication;
import com.boyueapp.privatechat.databinding.FragmentHomeBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TcpClient tcpclient = null;
    private Handler rcvHandler = null;
    private NotificationManager manager;
    private Notification notification1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        binding.textHome.setText("");
//创建tcp连接客户端对象
        tcpclient = MyApplication.mainApp.client;


        /*manager = MyApplication.mainApp.publicManager;
        notification1 = new NotificationCompat.Builder(this.getContext(),"999").setContentTitle("切换提醒").setContentText("已切换至群聊")
                .setSmallIcon(R.drawable.ic_launcher_background).build();
        manager.notify(1,notification1);*/

//发送按钮点击函数实现
        binding.sndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tcpclient == null) return;
//客户端发送消息
                tcpclient.sendMessage(binding.sndEdit.getText().toString(),(byte) 0x01);
                binding.sndEdit.setText("");
            }
        });


        this.rcvHandler = new Handler(Looper.getMainLooper()) {           //创建主线程接收子线程消息的接口对象，必须指定是在主线程循环内
            @Override
            public void handleMessage(Message msg) {
                String message = msg.obj.toString();
                if (message.startsWith("@UserLoginInResult")) return;
                Date curDate = new Date(System.currentTimeMillis());
                if (curDate.getTime() - MyApplication.mainApp.lastDate.getTime()  >= 10000){
                    MyApplication.mainApp.lastDate = curDate;
                    SimpleDateFormat formater = new SimpleDateFormat("MM/dd HH:mm");
                    String str = formater.format(curDate);

                    SpannableStringBuilder timeBuilder = new SpannableStringBuilder();
                    timeBuilder.append(str);
                    timeBuilder.setSpan(new AbsoluteSizeSpan(30),0,str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    timeBuilder.setSpan(new ForegroundColorSpan(Color.BLUE),0,str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    //binding.textHome.setGravity(Gravity.CENTER);
                    binding.textHome.append(timeBuilder);
                    binding.textHome.append("\n");
                }

                SpannableStringBuilder newBuilder = new SpannableStringBuilder();
                newBuilder.append(message);
                newBuilder.setSpan(new AbsoluteSizeSpan(65),0,message.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //binding.textHome.setGravity(Gravity.LEFT);
                binding.textHome.append(newBuilder);
                binding.textHome.append("\n");
                binding.scrollText.fullScroll(ScrollView.FOCUS_DOWN);

                /*manager = (NotificationManager) MyApplication.mainApp.getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    manager.createNotificationChannel(new NotificationChannel("998","vip", NotificationManager.IMPORTANCE_DEFAULT));
                }
                notification1 = new NotificationCompat.Builder(getContext(),"998").setContentTitle("新消息").setContentText(message)
                        .setSmallIcon(R.drawable.ic_launcher_background).build();
                manager.notify(1,notification1);*/
                MyApplication.mainApp.showNotification(getContext(),message);
            }
        };
        tcpclient.outHandler = rcvHandler;

        return root;
    }
//客户端消息处理
    public void showMessage(String message){

    }
}