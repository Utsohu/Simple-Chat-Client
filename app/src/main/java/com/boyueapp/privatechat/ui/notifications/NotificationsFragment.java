package com.boyueapp.privatechat.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.boyueapp.privatechat.MyApplication;
import com.boyueapp.privatechat.databinding.FragmentHomeBinding;
import com.boyueapp.privatechat.databinding.FragmentNotificationsBinding;
import com.boyueapp.privatechat.ui.home.HomeViewModel;
import com.boyueapp.privatechat.ui.home.TcpClient;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private TcpClient tcpclient = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.editNewServerIp.setHint(MyApplication.mainApp.serverIpAddress);
        binding.newServerIpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                MyApplication.mainApp.serverIpAddress = binding.editNewServerIp.getText().toString();
                MyApplication.mainApp.client.stopConnect();
                MyApplication.mainApp.client = new TcpClient(MyApplication.mainApp.serverIpAddress, 2016, MyApplication.mainApp.listener);
                MyApplication.mainApp.client.startConnect();;
            }
        });
        return root;
    }
}