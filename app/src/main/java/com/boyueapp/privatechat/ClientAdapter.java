package com.boyueapp.privatechat;

//import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.platform.app.InstrumentationRegistry;

import com.boyueapp.privatechat.model.ClientInfo;
import com.boyueapp.privatechat.ui.dashboard.DashboardFragment;

//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;

import java.util.List;

public class ClientAdapter extends ArrayAdapter<ClientInfo> {
    private int resourceId;
    private DashboardFragment mContext;
    public int clientsNum;

    public ClientAdapter(DashboardFragment context, int textViewResourceId, List<ClientInfo> objects) {
        super(context.getContext(),textViewResourceId,objects);
        mContext = context;
        resourceId = textViewResourceId;
        clientsNum = objects.size();
    }
    public View getView(final int position, View convertView, ViewGroup parent) {
         ClientInfo news = getItem(position);
        View view;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        }
        else view = convertView;
        TextView nameText = view.findViewById(R.id.list_item_client_name);
        nameText.setText(news.ip);
        Switch ClientChoice = view.findViewById(R.id.list_item_client_switch);
        ClientChoice.setTextOff("离线");
        ClientChoice.setTextOn("在线");
        ClientChoice.setEnabled(false);
        ClientChoice.setChecked(news.beChosen);
        ClientChoice.setTag(news);
        ClientChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientInfo news = (ClientInfo) v.getTag();
                news.beChosen = ClientChoice.isChecked();
                notifyDataSetChanged();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startMultiActivity("@NewTargetUser:" + news.ip);
            }
        });;
        return view;
    }


    /**
     * Instrumented test, which will execute on an Android device.
     *
     * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
     *//*
    @RunWith(AndroidJUnit4.class)
    public static class ExampleInstrumentedTest {
        @Test
        public void useAppContext() {
            // Context of the app under test.
            Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Assert.assertEquals("com.example.tcpclient", appContext.getPackageName());
        }
    }
    */
}

