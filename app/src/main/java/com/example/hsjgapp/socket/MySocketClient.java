package com.example.hsjgapp.socket;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by Administrator on 2017/7/31.
 */

public class MySocketClient {

    private Socket clientSocket;
    private boolean isStart = true;
    private Handler handler;
    private Context context;

    /**
     * 初始化Socket
     * @param socket 传入的Socket
     * @param handler 主线程handler
     */
    public MySocketClient(Context context, Socket socket, Handler handler) {
        this.clientSocket = socket;
        this.handler = handler;
        this.context=context;
    }

    public void receiveInfo() {
        String content;
        InputStream is;
        while (isStart) {
            try {
                is = clientSocket.getInputStream();
                byte[] buff;
                buff = new byte[is.available()];
                if (buff.length != 0) {
                    // 读取数据
                    is.read(buff);
                    content = new String(buff,"gbk");
                    Log.d("TAG4", "服务器的数据content：" + content);
                    Message msg = Message.obtain();
                    msg.what = 0x00001;
                    msg.obj = content;
                    handler.sendMessage(msg);
                }

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        if (!clientSocket.isClosed()) {
            try {
                clientSocket.close();
                Log.i("TAG4", "Socket被关闭了");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendInfo(final String content) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OutputStream os = clientSocket.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "gbk"));
                    bw.write(content);
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setStart(boolean isStart) {
        this.isStart = isStart;
    }
}
