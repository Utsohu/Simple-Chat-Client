package com.boyueapp.privatechat.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.boyueapp.privatechat.ClientAdapter;
import com.boyueapp.privatechat.MultiUserChatActivity;
import com.boyueapp.privatechat.MyApplication;
import com.boyueapp.privatechat.R;
import com.boyueapp.privatechat.databinding.FragmentDashboardBinding;
import com.boyueapp.privatechat.model.ClientInfo;
import com.boyueapp.privatechat.ui.home.TcpClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private TcpClient tcpclient = null;
    private List<ClientInfo> listRadioShow = new ArrayList<ClientInfo>();
    private ClientAdapter smp = null;
    private Handler rcvHandler = null;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        smp = new ClientAdapter(this, R.layout.list_item_client, listRadioShow);
        binding.MultiChatUsers.setAdapter(smp);

//创建tcp连接客户端对象
        tcpclient = MyApplication.mainApp.client;
        tcpclient.sendMessage("request",(byte) 0x04);

        /*binding.MultiUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tcpclient == null) return;
//客户端发送消息
                String message = "";
                for (ClientInfo targetInfo : listRadioShow){
                    if (targetInfo.beChosen) message += "@NewTargetUser:" + targetInfo.ip;
                }
                if (message.equals("")) return;
                startMultiActivity(message);
                //tcpclient.sendMessage(message + "@NewTargetUser:" + binding.DashEdit.getText().toString(),(byte)0x02);
                //binding.DashEdit.setText("");
            }
        });*/

        this.rcvHandler = new Handler(Looper.getMainLooper()) {           //创建主线程接收子线程消息的接口对象，必须指定是在主线程循环内
            @Override
            public void handleMessage(Message msg) {
                String message = msg.obj.toString();//重载消息处理
                if (message.startsWith("@MultiChatGroupInfo")){
                    if (message.split("@MultiChatGroupInfo").length <= 1) return;
                    message = message.split("@MultiChatGroupInfo")[1];
                    String[] names = message.split("@UserName:");
                    names = Arrays.copyOfRange(names,1,names.length);
                    for (String name : names){
                        ClientInfo newInfo = new ClientInfo(name,false);
                        listRadioShow.add(newInfo);
                    }
                    smp.notifyDataSetChanged();
                }
                else if (message.startsWith("@MultiChatGroupAva")){
                    message = message.split("@MultiChatGroupAva")[1];
                    String[] names = message.split("@UserName:");
                    names = Arrays.copyOfRange(names,1,names.length);
                    for (String name : names){
                        for (ClientInfo targetInfo : listRadioShow){
                            if (targetInfo.ip.equals(name)){
                                targetInfo.changeChosen(true);
                            }
                        }
                    }
                    smp.notifyDataSetChanged();
                }
            }
        };
        tcpclient.outHandler = rcvHandler;
        return root;
    }
    //客户端消息处理
    private TcpClient.OnMessageListener listener = new TcpClient.OnMessageListener() {
        @Override
        public void onMessageReceived(String message) {

        }

        @Override
        public void onConnected() {
        }

        @Override
        public void onDisConnected(String message) {
            tcpclient.startConnect();
        }

        @Override
        public void onError(String message) {
            tcpclient.stopConnect();
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void startMultiActivity(String message){
        Intent in = new Intent();
        in.setClass(this.getContext(), MultiUserChatActivity.class);
        in.putExtra("users", message);
        startActivity(in);
    }
}