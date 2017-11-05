package com.example.hsjgapp;

import java.util.ArrayList;
import java.util.List;

import com.example.hsjgapp.R;
import com.example.hsjgapp.DialogTool.DialogTool;
import com.example.hsjgapp.IntentUtil.ConnectMethods;
import com.example.hsjgapp.R.id;
import com.example.hsjgapp.R.layout;
import com.example.hsjgapp.ToolUtil.TimeTool;
import com.example.hsjgapp.XmlTool.UnXmlTool;
import com.example.hsjgapp.XmlTool.XMLParsingMethods;
import com.example.hsjgapp.domain.Code;
import com.example.hsjgapp.domain.Q09Domain;
import com.example.hsjgapp.domain.Q11Domain;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class SlopeOfBrake extends Activity {
	private ProgressDialog builder2 = null;
	private Message message;
	private Q11Domain theinformation;
	private List<Q09Domain> operatorlists;
	private TextView zphphm;
	private Button zpbtnBeginDet, zpbtnSubmit;
	private Spinner zcpd, lszczdpd;
	private String ip;
	private String jkph;
	private String jkxlh;
	private String jcxdh = "1";
	private String zpxdh = "1";
	private String[] hegetwo = { "", "未检", "合格", "不合格" };
	private String[] zcpddata = { "", "20%", "15%" };
	private String[] startc55 = { "gwjysbbh", "jyxm", "jycs", "kssj", "hpzl", "hphm", "jylsh", "jcxdh", "clsbdh", "jyjgbh" };// 18C55开始接口
	private String[] endc58 = { "jylsh", "jyjgbh", "jcxdh", "jycs", "jyxm", "hpzl", "hphm", "clsbdh", "gwjysbbh", "jssj" };// 18C58结束接口
	private String[] snapj31 = { "jylsh", "jyjgbh", "jycs", "jcxdh", "hpzl", "hphm", "clsbdh", "gwjysbbh", "jyxm", "kssj", "zpzl" };// 18J31抓拍接口
	private String[] videoj11j12 = { "jylsh", "hphm", "hpzl", "clsbdh", "gwxm", "jcxdh", "jycs" };// 18J11,18J12录像接口
	private String[] submittedc54 = { "jylsh", "jyjgbh", "jcxdh", "jycs", "jyxm", "hpzl", "hphm", "clsbdh", "lsy", "zdcsd",
			"zdxtsj", "zdwdx", "xckzzdjl", "xcmzzdjl", "xckzmfdd", "xcmzmfdd", "xczdczlz", "lszdpd", "yjzdcsd", "yjkzzdjl",
			"yjkzmfdd", "yjmzzdjl", "yjmzmfdd", "yjzdczlfs", "yjzdczlz", "yjzdpd", "zcpd", "lszczdpd", "csdscz", "csbpd", "lsjg" };// 18C54路试数据提交接口
	private static final int MSG_SLOPE_SEEUESS = 10091;
	private static final int MSG_SLOPE_FAIL = 10092;
	private static final int MSG_SLOPE_SHOW = 10093;
	private static final int MSG_SLOPE_DISMISS = 10094;
	private static final int MSG_SLOPE_CHAOSHI = 10095;
	private static final int MSG_SLOPE_ZIDINGYI = 10096;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == MSG_SLOPE_SEEUESS) {
				zpbtnBeginDet.setEnabled(false);
				zpbtnSubmit.setEnabled(true);
			}
			if (msg.what == MSG_SLOPE_FAIL) {
				DialogTool.AlertDialogShow(SlopeOfBrake.this, msg.obj.toString());
			}
			if (msg.what == MSG_SLOPE_SHOW) {
				builder2 = new ProgressDialog(SlopeOfBrake.this);
				builder2.setMessage("正在加载中，请稍等。。。");
				builder2.setCancelable(false);
				builder2.show();
			}
			if (msg.what == MSG_SLOPE_DISMISS) {
				builder2.dismiss();
			}
			if (msg.what == MSG_SLOPE_CHAOSHI) {
				DialogTool.AlertDialogShow(SlopeOfBrake.this, "请求超时，请检查网络环境!");
			}
			if (msg.what == MSG_SLOPE_ZIDINGYI) {
				AlertDialog.Builder builder1 = new AlertDialog.Builder(SlopeOfBrake.this);
				builder1.setTitle("提示框");
				builder1.setMessage(msg.obj.toString());
				builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						onBackPressed();
					}
				});
				builder1.show();
			}
		};
	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_slope_of_brake);
		// 获取缓存信息
		SharedPreferences preferences = getSharedPreferences("cs_setup", 0);
		ip = preferences.getString("IP", "");
		jkph = preferences.getString("JGPH", "");
		jkxlh = preferences.getString("JKXLH", "");
		// 上一个界面传入的数据
		Bundle bundle = this.getIntent().getExtras();
		theinformation = (Q11Domain) bundle.getSerializable("informationsObj");
		operatorlists = (List<Q09Domain>) bundle.getSerializable("theoperatorlistsObj");
		Initialization();
		// Spinner控件辅值
		zphphm.setText("驻坡制动(" + theinformation.getHphm() + ")");
		zcpd.setAdapter(SetSpinner(zcpddata));
		zcpd.setSelection(1);
		lszczdpd.setAdapter(SetSpinner(hegetwo));
		lszczdpd.setSelection(1);
		// 驻坡通道选择
		dialogs();
	}

	// 初始化控件
	private void Initialization() {
		// TODO Auto-generated method stub
		zphphm = (TextView) findViewById(R.id.zphphm);
		zpbtnBeginDet = (Button) findViewById(R.id.zpbtnBeginDet);
		zpbtnSubmit = (Button) findViewById(R.id.zpbtnSubmit);
		zcpd = (Spinner) findViewById(R.id.zcpd);
		lszczdpd = (Spinner) findViewById(R.id.lszczdpd);
	}

	// 建立Spinner的Adapter
	public ArrayAdapter<String> SetSpinner(String[] spidata) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spidata);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}

	// 通道选择
	public void dialogs() {
		AlertDialog.Builder builder3 = new AlertDialog.Builder(SlopeOfBrake.this);
		builder3.setTitle("请选择路试驻坡通道号：");
		final String[] dxh = { "通道1为20%", "通道2为15%" };
		builder3.setSingleChoiceItems(dxh, 0, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					zpxdh = "1";
				} else {
					zpxdh = "2";
				}
				int zcpdz = which + 1;
				zcpd.setSelection(zcpdz);
			}
		});
		builder3.setPositiveButton("确定", null);
		builder3.setCancelable(false);
		builder3.show();
	}

	// 按钮点击事件
	public void doClick(View v) {
		switch (v.getId()) {
		// 检验开始
		case R.id.zpbtnBeginDet:
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						// 开始
						handler.sendEmptyMessage(MSG_SLOPE_SHOW);
						List<Code> c55data = getStart18C55data(theinformation);
						handler.sendEmptyMessage(MSG_SLOPE_DISMISS);
						if (c55data.get(0).getCode().equals("1")) {
							handler.sendEmptyMessage(MSG_SLOPE_SEEUESS);
							setmessegas(MSG_SLOPE_FAIL, c55data.get(0).getMessage());
							// 开始时触发拍照
							try {
								handler.sendEmptyMessage(MSG_SLOPE_SHOW);
								List<Code> j31data = getSnap18J31data("R2", zpxdh, theinformation, "0345");
								handler.sendEmptyMessage(MSG_SLOPE_DISMISS);
								if (j31data.get(0).getCode().equals("1")) {
									setmessegas(MSG_SLOPE_FAIL, "驻坡触发拍照成功，正在拍照！");
								} else {
									setmessegas(MSG_SLOPE_FAIL, j31data.get(0).getMessage());
								}
							} catch (Exception e) {
								handler.sendEmptyMessage(MSG_SLOPE_DISMISS);
								handler.sendEmptyMessage(MSG_SLOPE_CHAOSHI);
								e.printStackTrace();
							}
							// 开始时触发录像开始
							try {
								handler.sendEmptyMessage(MSG_SLOPE_SHOW);
								List<Code> j11data = getVideo18J11J12data(theinformation, "R2", zpxdh, "18J11");
								handler.sendEmptyMessage(MSG_SLOPE_DISMISS);
								if (j11data.get(0).getCode().equals("1")) {
									setmessegas(MSG_SLOPE_FAIL, "驻坡触发录像成功，正在录像！");
								} else {
									setmessegas(MSG_SLOPE_FAIL, j11data.get(0).getMessage());
								}
							} catch (Exception e) {
								handler.sendEmptyMessage(MSG_SLOPE_DISMISS);
								handler.sendEmptyMessage(MSG_SLOPE_CHAOSHI);
								e.printStackTrace();
							}

						} else {
							handler.sendEmptyMessage(MSG_SLOPE_SEEUESS);
							setmessegas(MSG_SLOPE_FAIL, c55data.get(0).getMessage());
						}
					} catch (Exception e) {
						handler.sendEmptyMessage(MSG_SLOPE_DISMISS);
						handler.sendEmptyMessage(MSG_SLOPE_CHAOSHI);
						e.printStackTrace();
					}
				}
			}).start();
			break;
		// 提交数据
		case R.id.zpbtnSubmit:
			new Thread(new Runnable() {
				@Override
				public void run() {
					if (getkekong(lszczdpd.getSelectedItem().toString(), hegetwo).equals("")
							|| getkekong(lszczdpd.getSelectedItem().toString(), hegetwo).equals("0")) {
						setmessegas(MSG_SLOPE_FAIL, "路试驻车制动判定不能为空或未检！");
					} else {
						try {
							// 提交数据
							handler.sendEmptyMessage(MSG_SLOPE_SHOW);
							List<Code> c54data = getSubmitted18C54data();
							handler.sendEmptyMessage(MSG_SLOPE_DISMISS);
							if (c54data.get(0).getCode().equals("1")) {
								setmessegas(MSG_SLOPE_ZIDINGYI, "数据提交成功！");
							} else {
								setmessegas(MSG_SLOPE_ZIDINGYI, c54data.get(0).getMessage());
							}
							// 结束
							handler.sendEmptyMessage(MSG_SLOPE_SHOW);
							List<Code> c58data = getEnd18C58data(theinformation);
							handler.sendEmptyMessage(MSG_SLOPE_DISMISS);
							if (c58data.get(0).getCode().equals("1")) {
								setmessegas(MSG_SLOPE_FAIL, "机动车检验项目结束成功！");
							} else {
								setmessegas(MSG_SLOPE_FAIL, c58data.get(0).getMessage());
							}
							// 结束时触发录像结束
							handler.sendEmptyMessage(MSG_SLOPE_SHOW);
							List<Code> j12data = getVideo18J11J12data(theinformation, "R2", zpxdh, "18J12");
							handler.sendEmptyMessage(MSG_SLOPE_DISMISS);
							if (j12data.get(0).getCode().equals("1")) {
								setmessegas(MSG_SLOPE_FAIL, "驻坡结束录像成功！");
							} else {
								setmessegas(MSG_SLOPE_FAIL, j12data.get(0).getMessage());
							}
						} catch (Exception e) {
							handler.sendEmptyMessage(MSG_SLOPE_DISMISS);
							handler.sendEmptyMessage(MSG_SLOPE_CHAOSHI);
							e.printStackTrace();
						}
					}
				}
			}).start();
			break;
		}
	}

	// 发送Handler
	public void setmessegas(int msgnumber, String neirong) {
		message = new Message();
		message.what = msgnumber;
		message.obj = neirong;
		handler.sendMessage(message);
	}

	/**
	 * 调开始接口18C55
	 * 
	 * @param lists
	 *            :18Q11车辆基本信息
	 * @return
	 */
	public List<Code> getStart18C55data(Q11Domain lists) {
		List<String> datalist = new ArrayList<String>();
		datalist.add("");
		datalist.add("R");
		datalist.add(lists.getJycs() + "");
		datalist.add(TimeTool.getTiem());
		datalist.add(lists.getHpzl());
		datalist.add(lists.getHphm());
		datalist.add(lists.getJylsh());
		datalist.add(jcxdh);
		datalist.add(lists.getClsbdh());
		datalist.add(jkph);
		String xmldata = UnXmlTool.getWriteXML(startc55, datalist);
		Log.i("BBB", "18C55xmldata:" + xmldata);
		String startc55data = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh, "18C55", xmldata,
				"writeObjectOutResult", StaticValues.timeoutNine, StaticValues.numberOne);
		List<Code> c55lists = XMLParsingMethods.saxcode(startc55data);
		return c55lists;
	}

	/**
	 * 调结束接口18C58
	 * 
	 * @param lists
	 *            :18Q11车辆基本信息
	 * @return
	 */
	public List<Code> getEnd18C58data(Q11Domain lists) {
		List<String> datalist = new ArrayList<String>();
		datalist.add(lists.getJylsh());
		datalist.add(jkph);
		datalist.add(jcxdh);
		datalist.add(lists.getJycs() + "");
		datalist.add("R");
		datalist.add(lists.getHpzl());
		datalist.add(lists.getHphm());
		datalist.add(lists.getClsbdh());
		datalist.add("");
		datalist.add(TimeTool.getTiem());
		String xmldata2 = UnXmlTool.getWriteXML(endc58, datalist);
		Log.i("BBB", "18C58xmldata:" + xmldata2);
		String endc58data = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh, "18C58", xmldata2,
				"writeObjectOutResult", StaticValues.timeoutNine, StaticValues.numberOne);
		List<Code> endc58lists = XMLParsingMethods.saxcode(endc58data);
		return endc58lists;
	}

	/**
	 * 调抓拍接口18J31
	 * 
	 * @param gw
	 *            :抓拍工位
	 * @param dxh
	 *            :检测线代号
	 * @param lists
	 *            :18Q11车辆基本信息
	 * @param zpwgzpbh
	 *            :照片代号
	 * @return
	 */
	public List<Code> getSnap18J31data(String gw, String dxh, Q11Domain lists, String zpwgzpbh) {
		List<String> cfpzdata = new ArrayList<String>();
		cfpzdata.add(lists.getJylsh());
		cfpzdata.add(jkph);
		cfpzdata.add(lists.getJycs() + "");
		cfpzdata.add(dxh);
		cfpzdata.add(lists.getHpzl());
		cfpzdata.add(lists.getHphm());
		cfpzdata.add(lists.getClsbdh());
		cfpzdata.add("");
		cfpzdata.add(gw);
		cfpzdata.add(TimeTool.getTiem());
		cfpzdata.add(zpwgzpbh);
		String cfpzdatas = UnXmlTool.getTimeXML(snapj31, cfpzdata);
		Log.i("BBB", "18J31xmldata:" + cfpzdatas);
		String snapj31data = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh, "18J31", cfpzdatas,
				"writeObjectOutResult", StaticValues.timeoutFifteen, StaticValues.numberTwo);
		List<Code> snapj31lists = XMLParsingMethods.saxcode(snapj31data);
		return snapj31lists;
	}

	/**
	 * 调录像接口18J11,18J12
	 * 
	 * @param lists
	 *            :18Q11车辆基本信息
	 * @param gw
	 *            :录像工位
	 * @param tdh
	 *            :检测线代号
	 * @param jkh
	 *            :接口ID
	 * @return
	 */
	public List<Code> getVideo18J11J12data(Q11Domain lists, String gw, String tdh, String jkh) {
		List<String> vediolist = new ArrayList<String>();
		vediolist.add(lists.getJylsh());
		vediolist.add(lists.getHphm());
		vediolist.add(lists.getHpzl());
		vediolist.add(lists.getClsbdh());
		vediolist.add(gw);
		vediolist.add(tdh);
		vediolist.add(lists.getJycs() + "");
		String vediodata = UnXmlTool.getWriteXML(videoj11j12, vediolist);
		Log.i("BBB", "18j11j12xmldata:" + vediodata);
		String videoj11j12data = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh, jkh, vediodata,
				"writeObjectOutResult", StaticValues.timeoutFifteen, StaticValues.numberTwo);
		List<Code> videoj11j12lists = XMLParsingMethods.saxcode(videoj11j12data);
		return videoj11j12lists;
	}

	// 调路试制动数据提交接口18C54
	public List<Code> getSubmitted18C54data() {
		String roaddata = UnXmlTool.getWriteXML(submittedc54, getroaddata(theinformation, operatorlists, jcxdh));
		Log.i("BBB", "18C54xmldata:" + roaddata);
		String submittedc54data = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh, "18C54", roaddata,
				"writeObjectOutResult", StaticValues.timeoutFive, StaticValues.numberFour);
		List<Code> submittedc54lists = XMLParsingMethods.saxcode(submittedc54data);
		return submittedc54lists;
	}

	/**
	 * 18C54数据整合
	 * 
	 * @param lists
	 *            :18Q11车辆基本信息
	 * @param operaor
	 *            ：18Q09用户信息
	 * @param xdh
	 *            :检测线代号
	 * @return
	 */
	public List<String> getroaddata(Q11Domain lists, List<Q09Domain> operaor, String xdh) {
		// TODO Auto-generated method stub
		List<String> datalist2 = new ArrayList<String>();
		datalist2.add(lists.getJylsh());
		datalist2.add(jkph);
		datalist2.add(xdh);
		datalist2.add(lists.getJycs() + "");
		datalist2.add("R2");
		datalist2.add(lists.getHpzl());
		datalist2.add(lists.getHphm());
		datalist2.add(lists.getClsbdh());
		datalist2.add(operaor.get(0).getXm());
		datalist2.add("");
		datalist2.add("");
		datalist2.add("");
		datalist2.add("");
		datalist2.add("");
		datalist2.add("");
		datalist2.add("");
		datalist2.add("");
		datalist2.add("");
		datalist2.add("");
		datalist2.add("");
		datalist2.add("");
		datalist2.add("");
		datalist2.add("");
		datalist2.add("");
		datalist2.add("");
		datalist2.add("");
		datalist2.add(getkekong(zcpd.getSelectedItem().toString(), zcpddata));
		datalist2.add(getkekong(lszczdpd.getSelectedItem().toString(), hegetwo));
		datalist2.add("");
		datalist2.add("");
		datalist2.add("0");
		return datalist2;
	}

	/**
	 * 可空合格判断
	 * 
	 * @param zhidatas
	 *            :控件值
	 * @param zhishuzu
	 *            :对应数组
	 * @return
	 */
	public String getkekong(String zhidatas, String[] zhishuzu) {
		String zhi = null;
		for (int i = 0; i < zhishuzu.length; i++) {
			if (zhidatas.equals(zhishuzu[i])) {
				if (i == 0) {
					zhi = "";
				} else {
					int k = i - 1;
					zhi = String.valueOf(k);
				}
			}
		}
		return zhi;
	}

	// 重写返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			AlertDialog.Builder newbuilder = new AlertDialog.Builder(SlopeOfBrake.this);
			newbuilder.setTitle("提示框：");
			newbuilder.setMessage("是否确定退出？");
			newbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					onBackPressed();
				}
			});
			newbuilder.setNegativeButton("取消", null);
			newbuilder.show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
