package com.boyueapp.privatechat.model;

public class ClientInfo {
    public String ip;
    public boolean beChosen = false;

    public ClientInfo(String newIp, boolean newCondition) {
        ip = newIp;
        beChosen = newCondition;
    }

    public void changeChosen(boolean state){
        beChosen = state;
    }
}
