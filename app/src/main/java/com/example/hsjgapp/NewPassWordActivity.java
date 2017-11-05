package com.example.hsjgapp;

import java.util.ArrayList;
import java.util.List;

import com.example.hsjgapp.R;
import com.example.hsjgapp.DialogTool.DialogTool;
import com.example.hsjgapp.IntentUtil.ConnectMethods;
import com.example.hsjgapp.R.id;
import com.example.hsjgapp.R.layout;
import com.example.hsjgapp.XmlTool.UnXmlTool;
import com.example.hsjgapp.XmlTool.XMLParsingMethods;
import com.example.hsjgapp.domain.Code;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class NewPassWordActivity extends Activity {
	private EditText jmm, xmm, qrmm;
	private String jmmdata, xmmdata, qrmmdata;
	private String[] j03names = { "userid", "oldpwd", "newpwd" };
	private Button mmgx;
	private String namedata = "";
	private String ip;
	private String jkph;
	private String jkxlh;
	private ProgressDialog builder2 = null;
	private Message msg;
	private static final int MSG_NP_SHOW = 11001;
	private static final int MSG_NP_DISMISS = 11002;
	private static final int MSG_NP_CHAOSHI = 11003;
	private static final int MSG_NP_CUOWU = 11004;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == MSG_NP_SHOW) {
				builder2 = new ProgressDialog(NewPassWordActivity.this);
				builder2.setMessage("正在登陆中，请稍等。。。");
				builder2.setCancelable(false);
				builder2.show();
			}
			if (msg.what == MSG_NP_DISMISS) {
				builder2.dismiss();
			}
			if (msg.what == MSG_NP_CHAOSHI) {
				DialogTool.AlertDialogShow(NewPassWordActivity.this, "请求超时，请检查网络环境!!");
			}
			if (msg.what == MSG_NP_CUOWU) {
				DialogTool.AlertDialogShow(NewPassWordActivity.this, msg.obj.toString());
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.new_password);
		SharedPreferences preferences = getSharedPreferences("cs_setup", 0);
		ip = preferences.getString("IP", "");
		jkph = preferences.getString("JGPH", "");
		jkxlh = preferences.getString("JKXLH", "");
		jmm = (EditText) findViewById(R.id.jmm);
		xmm = (EditText) findViewById(R.id.xmm);
		qrmm = (EditText) findViewById(R.id.qrmm);
		mmgx = (Button) findViewById(R.id.mmgx);
		namedata = getIntent().getStringExtra("USERID");
		mmgx.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				jmmdata = jmm.getText().toString().trim();
				xmmdata = xmm.getText().toString().trim();
				qrmmdata = qrmm.getText().toString().trim();
				if (!xmmdata.equals(qrmmdata)) {
					DialogTool.AlertDialogShow(NewPassWordActivity.this, "密码不一致，请重新确认密码！");
				} else if (jmmdata.equals("") || xmmdata.equals("") || qrmmdata.equals("")) {
					DialogTool.AlertDialogShow(NewPassWordActivity.this, "请输入密码，密码不能为空！");
				} else {
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								handler.sendEmptyMessage(MSG_NP_SHOW);
								List<String> datalist = new ArrayList<String>();
								datalist.add(namedata);
								datalist.add(jmmdata);
								datalist.add(xmmdata);
								String xmldata = UnXmlTool.getWriteXML(j03names, datalist);
								Log.i("AAA", "18J03" + xmldata);
								String mmdatas = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh, "18J03",
										xmldata, "writeObjectOutResult", StaticValues.timeoutThree, StaticValues.numberFive);
								List<Code> codelists = XMLParsingMethods.saxcode(mmdatas);
								handler.sendEmptyMessage(MSG_NP_DISMISS);
								if (codelists.get(0).getCode().equals("1")) {
									messegas(MSG_NP_CUOWU, "密码更改成功！");
								} else {
									messegas(MSG_NP_CUOWU, codelists.get(0).getMessage());
								}
								handler.sendEmptyMessage(MSG_NP_DISMISS);
							} catch (Exception e) {
								handler.sendEmptyMessage(MSG_NP_DISMISS);
								handler.sendEmptyMessage(MSG_NP_CHAOSHI);
								e.printStackTrace();
							}
						}
					}).start();
				}
			}
		});
	}

	public void messegas(int msgnumber, String neirong) {
		msg = new Message();
		msg.what = msgnumber;
		msg.obj = neirong;
		handler.sendMessage(msg);
	}

}
