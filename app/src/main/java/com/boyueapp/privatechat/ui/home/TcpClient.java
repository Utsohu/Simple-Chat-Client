package com.boyueapp.privatechat.ui.home;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;

public class TcpClient {
//用户消息接口定义
    public interface OnMessageListener {
        void onMessageReceived(String message);
        void onConnected();
        void onDisConnected(String message);
        void onError(String message);
    }
    private final int msg_connected = 0;            //连接消息
    private final int msg_disconnected = 1;         //断开消息
    private final int msg_err = 2;                  //出错消息
    private final int msg_msg = 3;                  //收到信息消息
    private String mServerIp;               //主机ip
    private int nPort;                      //主机端口
    private OnMessageListener mListener;    //消息接口对象
    private boolean isConnecting = false;

    private boolean mRun = false;           //数据接收死循环条件
    private Socket clientSocket = null;     //客户端Socket对象
    private OutputStream outStream = null;  //tcp客户端输出流
    private ReceiveThread mReceiveThread = null;    //接收线程
    private Handler mHandler = null;        //子线程向主线程发送消息接口
    public Handler outHandler = null;
    public TcpClient(String serverIp, Integer port, OnMessageListener listener) {
        this.mServerIp = serverIp;
        this.nPort = port;
        this.mListener = listener;
        this.mHandler = new Handler(Looper.getMainLooper()) {           //创建主线程接收子线程消息的接口对象，必须指定是在主线程循环内
            @Override
            public void handleMessage(Message msg) {
                //重载消息处理
                if(msg.arg1 == msg_connected)                           //各类消息处理，最后调用用户接口
                    mListener.onConnected();
                else if(msg.arg1 == msg_disconnected) {
                    mListener.onDisConnected(msg.obj.toString());
                }
                else if(msg.arg1 == msg_err) {
                    mListener.onError(msg.obj.toString());
                    stopConnect();
                }
                else if(msg.arg1 == msg_msg) {
                    mListener.onMessageReceived(msg.obj.toString());
                    if (outHandler != null) {
                        Message handlerMsg = new Message();
                        handlerMsg.obj = msg.obj.toString();
                        outHandler.sendMessage(handlerMsg);
                    }
                }
            }
        };
    }
// 断开连接

    public void stopConnect() {
        mRun = false;
        Thread thread = new Thread(){           //启动一个线程进行连接处理，不能在主线程里执行，会报错
            @Override
            public void run() {
                if(clientSocket == null) return;
                try {
                    clientSocket.close();               //关闭Socket
                }catch (Exception e) {
                    e.printStackTrace();
                }
                clientSocket = null;                    //清除Socket对象
            }
        };
        thread.start();             //启动线程
    }
//开始连接，
    public void startConnect() {
        if ((clientSocket != null) || isConnecting) return;
        Thread thread = new Thread(){           //启动一个线程进行连接处理，不能在主线程里执行，会报错
            @Override
            public void run() {
                Message msg = new Message();        //创建给主线程的消息对象
                try {
                    isConnecting = true;
                    clientSocket = new Socket(mServerIp, nPort);        //创建Socket，并连接主机
                    msg.arg1 = msg_connected;
                    mHandler.sendMessage(msg);//发送消息
                    isConnecting = false;

                }catch (Exception e) {                                  //连接异常处理，发送消息给主线程
                    msg.arg1 = msg_disconnected;
                    msg.obj = "连接失败！！！原因：" + TcpClient.getStackTraceInfo(e);
                    mHandler.sendMessage(msg);
                    e.printStackTrace();
                    isConnecting = false;
                    clientSocket = null;

                    return;
                }
                mReceiveThread = new ReceiveThread(clientSocket);       //连接成功后，创建接收线程
                mRun = true;
                mReceiveThread.start();                                 //启动接收线程
            }
        };
        thread.start();             //启动线程
    }
//发送消息
    public void sendMessage(String message, byte type) {
        if(clientSocket == null) return;
        Thread thread = new Thread(){           //启动线程，必须在子线程内发送，在主线程内发送会报错
            @Override
            public void run() {

                byte[] msgBuffer = null;
                Message msg = new Message();
                try {
                    // 字符编码转换
                    msgBuffer = message.getBytes("UTF-8"); //字符串转为字节流
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    msg.arg1 = msg_err;
                    msg.obj = TcpClient.getStackTraceInfo(e);
                    mHandler.sendMessage(msg);
                    return;
                }
                try {
                    // 获得Socket的输出流
                    outStream = clientSocket.getOutputStream();     //得到Socket的发送流
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.arg1 = msg_err;
                    msg.obj = TcpClient.getStackTraceInfo(e);
                    mHandler.sendMessage(msg);
                    return;
                }

                byte[] msgHead = new byte[8];
                msgHead[0] = (byte)(0x1a);
                msgHead[1] = (byte)(0x1b);
                msgHead[2] = (byte)(0x1f);
                msgHead[3] = (byte)(0x1e);
                short len = (short)(msgBuffer.length);
                msgHead[4] = (byte)(len%256);
                msgHead[5] = (byte)(len/256);
                msgHead[6] = (byte)(0x01);
                msgHead[7] = type;

                try {
                    // 发送数据
                    outStream.write(msgHead);
                    outStream.write(msgBuffer);                 //往发送流里发送数据
                } catch (Exception e) {
                    msg.arg1 = msg_err;
                    msg.obj = TcpClient.getStackTraceInfo(e);
                    mHandler.sendMessage(msg);
                }
            }
        };
        thread.start();         //启动线程
    }
//接收线程
    private class ReceiveThread extends Thread {
        private InputStream inStream = null;
        private ArrayList<Byte> readBuf;

    private String str = null;
        ReceiveThread(Socket s) {
            readBuf = new ArrayList<Byte>();
            try {
                this.inStream = s.getInputStream(); // 获得Socket输入流
            } catch (IOException e) {
                Message msg = new Message();
                msg.arg1 = msg_err;
                msg.obj = TcpClient.getStackTraceInfo(e);
                mHandler.sendMessage(msg);
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            while (mRun) {         //线程死循环中，直到wRun置为false
                byte[] buf = new byte[1024];
                int nsz =0;
                Message msg = new Message();
                String err = "无信息接收";
                try {
                    nsz = inStream.read(buf);    // 读取输入数据（阻塞）
                } catch (IOException e) {       //进行读取时出错处理
                    err = TcpClient.getStackTraceInfo(e);
                    e.printStackTrace();
                }
                if(nsz <= 0) {
                    msg.arg1 = msg_err;
                    msg.obj = err;
                    mHandler.sendMessage(msg);
                    mRun = false;
                    return;
                }
                if (nsz < 8) continue;
                for (byte targetByte : buf){
                    readBuf.add(targetByte);
                }
                checkReceive();
            }
        }
        private void checkReceive(){
            int sz = readBuf.size();
            if(sz < 4) return;
            if (readBuf.get(0) != 0x1a || readBuf.get(1) != 0x1b || readBuf.get(2) != 0x1f || readBuf.get(3) != 0x1e){
                readBuf.clear();
                return;
            }
            int msgLength = readBuf.get(4)& 0xFF + (int)readBuf.get(5)*256;
            if (sz < msgLength + 8) return;
            byte msgType = readBuf.get(6);
            byte contentType = readBuf.get(7);

            ArrayList<Byte> data = new ArrayList<>(readBuf.subList(8, msgLength+8));
            readBuf = new ArrayList<>(readBuf.subList(msgLength+8,readBuf.size()));
            if (msgType == 0x01){
                Message msg = new Message();
                try {
                    byte[] bdata = new byte[msgLength];
                    for(int i=0;i<msgLength;i++)
                        bdata[i] = data.get(i);
                    this.str = new String(bdata, "UTF-8").trim();

                } catch (UnsupportedEncodingException e) {
                    msg.arg1 = msg_err;
                    msg.obj = TcpClient.getStackTraceInfo(e);
                    mHandler.sendMessage(msg);
                    e.printStackTrace();
                }
                msg.arg1 = msg_msg;
                msg.obj = this.str;
                mHandler.sendMessage(msg);
            }
            checkReceive();
        }
    }
//把异常转成字符串
    public static String getStackTraceInfo(Exception e) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);//将出错的栈信息输出到printWriter中
            pw.flush();
            sw.flush();

            return sw.toString();
        } catch (Exception ex) {

            return "发生错误";
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }
    }

    public boolean isConnected(){
        if(clientSocket == null)return false;
        return true;
    }
}
