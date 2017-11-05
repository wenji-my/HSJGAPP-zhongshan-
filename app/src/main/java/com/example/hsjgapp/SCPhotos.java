package com.example.hsjgapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import com.example.hsjgapp.R;
import com.example.hsjgapp.SQL.SQLFuntion;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SCPhotos extends Activity {
	private ProgressDialog builder2 = null;
	private EditText edlsh;
	private Button scsx;
	private ListView vehiclelise;
	private String where;
	private RadioGroup SFCType;
	private boolean sczhi = false;
	private static final int MSG_DATA_LIST = 10001;
	private static final int MSG_DATA_SHOW = 10002;
	private static final int MSG_DATA_DISMISS = 10003;

	private Handler hander = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MSG_DATA_LIST) {
				vehiclelise.setAdapter(getAdapter(where));
			}
			if (msg.what == MSG_DATA_SHOW) {
				builder2 = new ProgressDialog(SCPhotos.this);
				builder2.setMessage("正在加载中，请稍等。。。");
				builder2.setCanceledOnTouchOutside(false);
				builder2.show();
			}
			if (msg.what == MSG_DATA_DISMISS) {
				builder2.dismiss();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_informations);
		init();
		vehiclelise.setAdapter(getAdapter(null));
		
		SFCType = (RadioGroup) findViewById(R.id.SFCType);
		SFCType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				int radioButtonId = arg0.getCheckedRadioButtonId();
				if (radioButtonId == R.id.yc) {
					sczhi = true;
				} else {
					sczhi = false;
				}
			}
		});

		scsx.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				hander.sendEmptyMessage(MSG_DATA_SHOW);
				where = edlsh.getText().toString().trim();
				if (where.equals("")) {
					where = null;
				}
				hander.sendEmptyMessage(MSG_DATA_LIST);
				hander.sendEmptyMessage(MSG_DATA_DISMISS);
			}
		});
	}

	private void init() {
		edlsh = (EditText) findViewById(R.id.edlsh);
		scsx = (Button) findViewById(R.id.scsx);
		vehiclelise = (ListView) findViewById(R.id.vehiclelise);
	}

	private SimpleAdapter getAdapter(String wheres) {
		String zt = "02";
		if (sczhi) {
			zt = "03";
		} else {
			zt = "02";
		}
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		list = SQLFuntion.dgquery(SCPhotos.this, wheres);
		Log.i("AAA", "SQLFuntion.size:" + list.size());
		// 数据归类
		ArrayList<HashMap<String, String>> newlist = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).get("isfuo").equals(zt)) {
				if (newlist.size() == 0) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("jylsh", list.get(i).get("jylsh"));
					map.put("hphm", list.get(i).get("hphm"));
					map.put("zpzl", list.get(i).get("zpzl"));
					map.put("pssj", list.get(i).get("pssj"));
					map.put("isfuo", list.get(i).get("isfuo"));
					newlist.add(map);
				} else {
					boolean ifxd = false;
					for (int j = 0; j < newlist.size(); j++) {
						if (newlist.get(j).get("jylsh").equals(list.get(i).get("jylsh"))) {
							String zl = newlist.get(j).get("zpzl");
							newlist.get(j).put("zpzl", zl + "\n" + list.get(i).get("zpzl"));
							ifxd = true;
						}
					}
					if (!ifxd) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("jylsh", list.get(i).get("jylsh"));
						map.put("hphm", list.get(i).get("hphm"));
						map.put("zpzl", list.get(i).get("zpzl"));
						map.put("pssj", list.get(i).get("pssj"));
						map.put("isfuo", list.get(i).get("isfuo"));
						newlist.add(map);
					}
				}
			}
		}
		// 倒序设置
		if (sczhi) {
			Collections.reverse(newlist);
		}
		SimpleAdapter mSimpleAdapter = null;
		if (sczhi) {
			mSimpleAdapter = new SimpleAdapter(SCPhotos.this, newlist, R.layout.activity_hm, new String[] { "jylsh", "hphm",
					"pssj", "zpzl" }, new int[] { R.id.cllsh, R.id.cphaoma, R.id.cjsj, R.id.zpzl });
		} else {
			mSimpleAdapter = new SimpleAdapter(SCPhotos.this, newlist, R.layout.activity_hm_two, new String[] { "jylsh", "hphm",
					"pssj", "zpzl" }, new int[] { R.id.cllsh, R.id.cphaoma, R.id.cjsj, R.id.zpzl });
		}
		return mSimpleAdapter;
	}
}
