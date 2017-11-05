package com.example.hsjgapp;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.hsjgapp.R;
import com.example.hsjgapp.DialogTool.DialogTool;
import com.example.hsjgapp.IntentUtil.ConnectMethods;
import com.example.hsjgapp.R.id;
import com.example.hsjgapp.R.layout;
import com.example.hsjgapp.R.menu;
import com.example.hsjgapp.SQL.SQLFuntion;
import com.example.hsjgapp.ToolUtil.PanDuan;
import com.example.hsjgapp.ToolUtil.PhotoTool;
import com.example.hsjgapp.XmlTool.UnXmlTool;
import com.example.hsjgapp.XmlTool.XMLParsingMethods;
import com.example.hsjgapp.domain.Code;
import com.example.hsjgapp.domain.Q09Domain;
import com.example.hsjgapp.domain.Q11Domain;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class VehicleInformationActivity extends Activity {
	private ProgressDialog builder2 = null;
	private ListView vehicle_information_list;
	private Button sx;
	private RelativeLayout textLayout;
	private List<Q11Domain> datas;
	private List<Q11Domain> newdatas;
	private List<Q11Domain> mydata;
	private List<Q09Domain> operatorlists;
	private EditText Edhphm;
	private String ip;
	private String jkph;
	private String jkxlh;
	private String tpscjk;
	private int tpscfs;
	private boolean photozhi = false;
	private String userid;
	Thread uploadThread = null;
	private boolean isedit = false;
	private static final int MSG_DATA_CHANGED = 10021;
	private static final int MSG_TEXT = 10022;
	private static final int MSG_SHOW = 10023;
	private static final int MSG_DISMISS = 10024;
	private static final int MSG_CHAOSHI = 10025;
	private String localImagePath = Environment.getExternalStorageDirectory() + "/" + "MyVehPhoto/";
	private String[] namedata = { "hphm","jyjgbh" };
	private String[] photoscdatac63 = { "jyxm", "jycs", "pssj", "hpzl", "hphm", "zpzl", "zp", "jylsh", "jcxdh", "clsbdh",
			"jyjgbh" };
	private Handler hander = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MSG_DATA_CHANGED) {
				textLayout.setVisibility(View.GONE);
				vehicle_information_list.setAdapter(getAdapter());
				vehicle_information_list.setVisibility(View.VISIBLE);
			}
			if (msg.what == MSG_TEXT) {
				vehicle_information_list.setVisibility(View.GONE);
				textLayout.setVisibility(View.VISIBLE);
			}
			if (msg.what == MSG_SHOW) {
				builder2 = new ProgressDialog(VehicleInformationActivity.this);
				builder2.setMessage("正在加载中，请稍等。。。");
				builder2.setCanceledOnTouchOutside(false);
				builder2.show();
			}
			if (msg.what == MSG_DISMISS) {
				builder2.dismiss();
			}
			if (msg.what == MSG_CHAOSHI) {
				DialogTool.AlertDialogShow(VehicleInformationActivity.this, "请求超时，请检查网络环境!");
			}
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.vehicle_information);
		SharedPreferences preferences = getSharedPreferences("cs_setup", 0);
		ip = preferences.getString("IP", "");
		jkph = preferences.getString("JGPH", "");
		jkxlh = preferences.getString("JKXLH", "");
		tpscfs = preferences.getInt("TPSCFS", 0);
		if (tpscfs == 0) {
			tpscjk = "18Q63";
		} else {
			tpscjk = "18C63";
		}
		SharedPreferences preferencestwo = getSharedPreferences("jbsz", 1);
		photozhi = preferencestwo.getBoolean("PHOTOZ", false);
		Bundle bundle1 = this.getIntent().getExtras();
		operatorlists = (List<Q09Domain>) bundle1.getSerializable("theoperatorlistsObj");
		userid = getIntent().getStringExtra("USERID");
		Toast.makeText(this, operatorlists.get(0).getXm(), 1).show();
		// 上传照片
		uploadThread = new Thread(downLoadBnsInfoRunable);
		uploadThread.start();
		textLayout = (RelativeLayout) findViewById(R.id.textLayout);
		vehicle_information_list = (ListView) findViewById(R.id.vehicle_information_list);
		vehicle_information_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Q11Domain theinformations = datas.get(arg2);
				Intent intent = new Intent();
				intent.setClass(VehicleInformationActivity.this, FunctionActivity.class);
				intent.putExtra("ZDHPHM", theinformations.getHphm());
				intent.putExtra("ZDJYLSH", theinformations.getJylsh());
				intent.putExtra("JYCS", theinformations.getJycs());
				intent.putExtra("USERID", userid);
				Bundle bundle = new Bundle();
				bundle.putSerializable("theoperatorlistsObj", (Serializable) operatorlists);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		sx = (Button) findViewById(R.id.sx);
		sx.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							hander.sendEmptyMessage(MSG_SHOW);
							mydata = Data();
							if (mydata.size() != 0) {
								hander.sendEmptyMessage(MSG_DATA_CHANGED);
								hander.sendEmptyMessage(MSG_DISMISS);
							} else {
								hander.sendEmptyMessage(MSG_TEXT);
								hander.sendEmptyMessage(MSG_DISMISS);
							}
						} catch (Exception e) {
							hander.sendEmptyMessage(MSG_DISMISS);
							hander.sendEmptyMessage(MSG_CHAOSHI);
							e.printStackTrace();
						}
					}
				}).start();
			}
		});

		Edhphm = (EditText) findViewById(R.id.Edhphm);
	}

	// 上传照片线程
	Runnable downLoadBnsInfoRunable = new Runnable() {
		public void run() {
			while (true) {
				try {
					Log.i("AAA", "downLoadBnsInfoRunable---------");
					ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
					list = SQLFuntion.query(VehicleInformationActivity.this, null, null, null);
					for (int j = 0; j < list.size(); j++) {
						try {
							if (list.get(j).get("isfuo").equals("02")) {
								List<String> datalist2 = new ArrayList<String>();
								datalist2.add("");
								datalist2.add(list.get(j).get("jycs"));
								datalist2.add(list.get(j).get("pssj"));
								datalist2.add(list.get(j).get("hpzl"));
								datalist2.add(list.get(j).get("hphm"));
								datalist2.add(list.get(j).get("zpid"));
								Log.i("AAA", list.get(j).get("zpid"));
								datalist2.add("");
								datalist2.add(list.get(j).get("jylsh"));
								datalist2.add(list.get(j).get("jcxdh"));
								datalist2.add(list.get(j).get("clsbdh"));
								datalist2.add(list.get(j).get("jyjgbh"));
								String currentImagePath = localImagePath + list.get(j).get("jylsh") + "/"
										+ list.get(j).get("zpzl") + ".jpg";
								currentImagePath = FindFileFullPath(currentImagePath);
								String xmldata = UnXmlTool.getPhotoXML(photoscdatac63, datalist2,
										PhotoTool.getphotodata(currentImagePath));
								Log.i("BBB", xmldata);
								String information = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh,
										tpscjk, xmldata, "writeObjectOutResult", StaticValues.timeoutThirty,
										StaticValues.numberThree);
								List<Code> codelists = XMLParsingMethods.saxcode(information);
								if (codelists.get(0).getCode().equals("1")) {
									Log.i("BBB", "codelists.get(0).getCode():" + codelists.get(0).getCode());
									Object[] data = { list.get(j).get("hphm"), "03", list.get(j).get("hphm"),
											list.get(j).get("zpzl"), list.get(j).get("jylsh") };
									SQLFuntion.update(VehicleInformationActivity.this, data);
								} else {
									Object[] data = { list.get(j).get("hphm"), "02", list.get(j).get("hphm"),
											list.get(j).get("zpzl"), list.get(j).get("jylsh") };
									SQLFuntion.update(VehicleInformationActivity.this, data);
								}
								Thread.sleep(1000);
							}
						} catch (Exception e) {
							e.printStackTrace();
							Thread.sleep(1000);
						}
					}
					Thread.sleep(5000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	};

	private MyAdapter getAdapter() {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		list = data();
		MyAdapter myAdapter = new MyAdapter(VehicleInformationActivity.this, list);
		return myAdapter;
	}

	public ArrayList<HashMap<String, Object>> data() {
		if (!isedit) {
			datas = mydata;
		} else {
			datas = newdatas;
		}
		ArrayList<HashMap<String, Object>> listdata = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < datas.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("jylsh", datas.get(i).getJylsh());
			map.put("hphm", datas.get(i).getHphm());
			map.put("hpzl", PanDuan.Hpzl(datas.get(i).getHpzl()));
			map.put("clsbdh", datas.get(i).getClsbdh());
			map.put("slzt", datas.get(i).getSlzt());
			listdata.add(map);
		}
		return listdata;

	};

	public List<Q11Domain> Data() {
		try {
			List<Q11Domain> vehicleInformationlists = null;
			List<String> datalist = new ArrayList<String>();
			String ahmcxz = Edhphm.getText().toString().trim();
			datalist.add(ahmcxz);
			datalist.add(jkph);
			String xmldata = UnXmlTool.getQueryXML(namedata, datalist);
			Log.i("BBB", "xmldata:" + xmldata);
			String information = ConnectMethods.connectWebService(ip, StaticValues.queryObject, jkxlh, "18Q11", xmldata,
					"queryObjectOutResult", StaticValues.timeoutFive, StaticValues.numberFour);
			vehicleInformationlists = XMLParsingMethods.saxvehicleinformation(information);
			return vehicleInformationlists;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	String FindFileFullPath(String fileFulePath) {
		long curFileDate = 0;
		String lastFileName = fileFulePath;
		for (int i = 0; i < 20; i++) {
			File file = new File(fileFulePath + i);
			if (file.exists()) {
				long tempTime = file.lastModified();// 返回文件最后修改时间，是以个long型毫秒数
				if (curFileDate < tempTime) {
					lastFileName = file.getPath();
					curFileDate = tempTime;
				}
			}
		}
		return lastFileName;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		textLayout.setVisibility(View.GONE);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					hander.sendEmptyMessage(MSG_SHOW);
					mydata = Data();
					if (mydata.size() != 0) {
						hander.sendEmptyMessage(MSG_DATA_CHANGED);
						hander.sendEmptyMessage(MSG_DISMISS);
					} else {
						hander.sendEmptyMessage(MSG_TEXT);
						hander.sendEmptyMessage(MSG_DISMISS);
					}
				} catch (Exception e) {
					hander.sendEmptyMessage(MSG_DISMISS);
					hander.sendEmptyMessage(MSG_CHAOSHI);
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.parameter_settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.clzpsczt:
			Intent intent11 = new Intent(VehicleInformationActivity.this, SCPhotos.class);
			startActivity(intent11);
			break;
		case R.id.parameter_settingsId:
			Intent intent1 = new Intent(VehicleInformationActivity.this, BasicSetup.class);
			startActivity(intent1);
			break;
		case R.id.new_password_cd:
			Intent intent12 = new Intent(VehicleInformationActivity.this, NewPassWordActivity.class);
			intent12.putExtra("USERID", userid);
			startActivity(intent12);
			break;
		case R.id.exitId:
			AlertDialog.Builder builder = new AlertDialog.Builder(VehicleInformationActivity.this);
			builder.setTitle("是否退出程序？");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					VehicleInformationActivity.this.finish();
				}
			});
			builder.setNegativeButton("取消", null);
			builder.show();
			break;
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			AlertDialog.Builder tcbuilder = new AlertDialog.Builder(VehicleInformationActivity.this);
			tcbuilder.setTitle("提示框：");
			tcbuilder.setMessage("是否退出程序？");
			tcbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					VehicleInformationActivity.this.finish();
				}
			});
			tcbuilder.setNegativeButton("取消", null);
			tcbuilder.show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}
}
