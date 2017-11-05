package com.example.hsjgapp;

import java.util.ArrayList;
import java.util.List;
import com.example.hsjgapp.R;
import com.example.hsjgapp.DialogTool.DialogTool;
import com.example.hsjgapp.IntentUtil.ConnectMethods;
import com.example.hsjgapp.ToolUtil.PanDuan;
import com.example.hsjgapp.XmlTool.UnXmlTool;
import com.example.hsjgapp.XmlTool.XMLParsingMethods;
import com.example.hsjgapp.domain.Code;
import com.example.hsjgapp.domain.Q11Domain;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

//车辆退办
public class BackOffice extends Activity {
	private TextView back_hphm, back_hpzl, back_cjh, back_lsh, back_text;
	private Button tb;
	private String ip;
	private String jkph;
	private String jkxlh;
	private Q11Domain theinformation;
	private String[] tuibanname = { "jylsh", "jyjgbh", "hpzl", "hphm" };
	protected static final int MSG_DATA_SUCCESS = 10101;
	protected static final int MSG_DATA_FAIL = 10102;
	protected static final int MSG_DATA_START = 10103;
	protected static final int MSG_DATA_DISSMSS = 10104;
	private ProgressDialog builder = null;
	private Handler hander = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MSG_DATA_SUCCESS) {
				back_text.setVisibility(View.VISIBLE);
				tb.setEnabled(false);
			}
			if (msg.what == MSG_DATA_FAIL) {
				back_text.setText(msg.obj.toString());
				back_text.setVisibility(View.VISIBLE);
				tb.setEnabled(false);
				DialogTool.AlertDialogShow(BackOffice.this, msg.obj.toString());
			}
			if (msg.what == MSG_DATA_START) {
				builder = new ProgressDialog(BackOffice.this);
				builder.setMessage("正在加载中，请稍等。。。");
				builder.setCancelable(false);
				builder.show();
			}
			if (msg.what == MSG_DATA_DISSMSS) {
				builder.dismiss();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.back_office);
		SharedPreferences preferences = getSharedPreferences("cs_setup", 0);
		ip = preferences.getString("IP", "");
		jkph = preferences.getString("JGPH", "");
		jkxlh = preferences.getString("JKXLH", "");
		Bundle bundle = this.getIntent().getExtras();
		theinformation = (Q11Domain) bundle.getSerializable("informationsObj");
		chushihua();
		back_hphm.setText(theinformation.getHphm());
		back_hpzl.setText(PanDuan.Hpzl(theinformation.getHpzl()));
		back_cjh.setText(theinformation.getClsbdh());
		back_lsh.setText(theinformation.getJylsh());

		tb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							hander.sendEmptyMessage(MSG_DATA_START);
							List<String> datalist = new ArrayList<String>();
							datalist.add(theinformation.getJylsh());
							datalist.add(jkph);
							datalist.add(theinformation.getHpzl());
							datalist.add(theinformation.getHphm());
							String xmldata = UnXmlTool.getWriteXML(tuibanname, datalist);
							String information = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh, "18C72",
									xmldata, "writeObjectOutResult", StaticValues.timeoutThree, StaticValues.numberFive);
							List<Code> codelists = XMLParsingMethods.saxcode(information);
							if (codelists.get(0).getCode().equals("1")) {
								hander.sendEmptyMessage(MSG_DATA_SUCCESS);
								hander.sendEmptyMessage(MSG_DATA_DISSMSS);
							} else {
								hander.sendEmptyMessage(MSG_DATA_DISSMSS);
								Message message = new Message();
								message.what = MSG_DATA_FAIL;
								message.obj = codelists.get(0).getMessage();
								hander.sendMessage(message);
							}
						} catch (Exception e) {
							e.printStackTrace();
							return;
						}
					}
				}).start();
			}
		});
	}

	private void chushihua() {
		// TODO Auto-generated method stub
		back_hphm = (TextView) findViewById(R.id.back_hphm);
		back_hpzl = (TextView) findViewById(R.id.back_hpzl);
		back_cjh = (TextView) findViewById(R.id.back_cjh);
		back_lsh = (TextView) findViewById(R.id.back_lsh);
		back_text = (TextView) findViewById(R.id.back_text);
		tb = (Button) findViewById(R.id.tb);
	}
}
