package com.example.hsjgapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.hsjgapp.DialogTool.DialogTool;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class OnLineSchedulingActivity extends Activity {

    private String xm;
    private String lsh;
    private String jcxdh;
    private static final int MSG_WAIGUAN = 10038;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_WAIGUAN) {
                DialogTool.AlertDialogShow(OnLineSchedulingActivity.this, (String) msg.obj);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.online_schedul);

        Intent intent = getIntent();
        xm = intent.getStringExtra("XM");
        lsh = intent.getStringExtra("LSH");
        jcxdh = intent.getStringExtra("JCXDH");

    }

    public void doClick(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String str = "<jylsh>" + lsh + "</jylsh><jycs>1</jycs><jcxdh>" + jcxdh + "</jcxdh><ycy>" + xm + "</ycy>";
                    String ip = null;
                    if (jcxdh.equals("1")) {
                        ip = "192.26.1.106";
                    }
                    if (jcxdh.equals("3")) {
                        ip = "192.26.1.107";
                    }
                    Socket client = new Socket(ip, 6666);
                    OutputStream os = client.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "gbk"));
                    bw.write(str);
                    bw.flush();
                    client.shutdownOutput();
                    InputStream is = client.getInputStream();
                    String content;
                    byte[] buff;
                    buff = new byte[is.available()];
                    if (buff.length != 0) {
                        // 读取数据
                        is.read(buff);
                        content = new String(buff, "gbk");
                        Message msg = Message.obtain();
                        msg.what=MSG_WAIGUAN;
                        msg.obj=content;
                        handler.sendMessage(msg);
//						Log.i("TAG", "服务器的数据content：" + content);
                    }
                    is.close();
                    bw.close();
                    os.close();
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


}
