package com.example.hsjgapp;

import java.util.ArrayList;
import java.util.List;
import com.example.hsjgapp.R;
import com.example.hsjgapp.DialogTool.DialogTool;
import com.example.hsjgapp.IntentUtil.ConnectMethods;
import com.example.hsjgapp.ToolUtil.TimeTool;
import com.example.hsjgapp.XmlTool.UnXmlTool;
import com.example.hsjgapp.XmlTool.XMLParsingMethods;
import com.example.hsjgapp.domain.Code;
import com.example.hsjgapp.domain.Q09Domain;
import com.example.hsjgapp.domain.Q11Domain;
import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class RoadTestToMove extends Activity {
	private ProgressDialog builder2 = null;
	private Message message;
	private Q11Domain theinformation;
	private List<Q09Domain> operatorlists;
	private TextView lshphm;
	private Button lsbtnBeginDet, lsjslx, lsbtnSubmit;
	private EditText zdcsd, zdxtsj, xckzzdjl, xcmzzdjl, xckzmfdd, xcmzmfdd, xczdczlz, yjzdcsd, yjkzzdjl, yjkzmfdd, yjmzzdjl,
			yjmzmfdd, yjzdczlz;
	private Spinner zdwdx, lszdpd, yjzdczlfs, yjzdpd;
	private String ip;
	private String jkph;
	private String jkxlh;
	private String jcxdh = "1";
	private String[] zdwdxfdata = { "", "未跑偏", "左跑偏", "右跑偏" };
	private String[] hegetwo = { "", "未检", "合格", "不合格" };
	private String[] yjzdczlfsdata = { "", "手操纵", "脚操纵" };
	private String[] startc55 = StaticValues.startc55;// 18C55开始接口
	private String[] endc58 = StaticValues.endc58;// 18C58结束接口
	private String[] snapj31 = StaticValues.snapj31;// 18J31抓拍接口
	private String[] videoj11j12 = StaticValues.videoj11j12;// 18J11,18J12录像接口
	private String[] submittedc54 = StaticValues.submittedc54;// 18C54路试数据提交接口
	private static final int MSG_ROAD_SEEUESS = 10091;
	private static final int MSG_ROAD_FAIL = 10092;
	private static final int MSG_ROAD_SHOW = 10093;
	private static final int MSG_ROAD_DISMISS = 10094;
	private static final int MSG_ROAD_CHAOSHI = 10095;
	private static final int MSG_ROAD_ZIDINGYI = 10096;
	private static final int MSG_ROAD_LXJS = 10097;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == MSG_ROAD_SEEUESS) {
				lsbtnBeginDet.setEnabled(false);
				lsjslx.setEnabled(true);
			}
			if (msg.what == MSG_ROAD_LXJS) {
				lsbtnSubmit.setEnabled(true);
			}
			if (msg.what == MSG_ROAD_FAIL) {
				DialogTool.AlertDialogShow(RoadTestToMove.this, msg.obj.toString());
			}
			if (msg.what == MSG_ROAD_SHOW) {
				builder2 = new ProgressDialog(RoadTestToMove.this);
				builder2.setMessage("正在加载中，请稍等。。。");
				builder2.setCancelable(false);
				builder2.show();
			}
			if (msg.what == MSG_ROAD_DISMISS) {
				builder2.dismiss();
			}
			if (msg.what == MSG_ROAD_CHAOSHI) {
				DialogTool.AlertDialogShow(RoadTestToMove.this, "请求超时，请检查网络环境!");
			}
			if (msg.what == MSG_ROAD_ZIDINGYI) {
				AlertDialog.Builder builder1 = new AlertDialog.Builder(RoadTestToMove.this);
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
		setContentView(R.layout.activity_road_test_to_move);
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
		lshphm.setText("路试制动(" + theinformation.getHphm() + ")");
		zdwdx.setAdapter(SetSpinner(zdwdxfdata));
		lszdpd.setAdapter(SetSpinner(hegetwo));
		lszdpd.setSelection(1);
		yjzdczlfs.setAdapter(SetSpinner(yjzdczlfsdata));
		yjzdpd.setAdapter(SetSpinner(hegetwo));
		yjzdpd.setSelection(1);
		dialogs();
	}

	// 初始化控件
	private void Initialization() {
		// TODO Auto-generated method stub
		lshphm = (TextView) findViewById(R.id.lshphm);
		lsbtnBeginDet = (Button) findViewById(R.id.lsbtnBeginDet);
		lsjslx = (Button) findViewById(R.id.lsjslx);
		lsbtnSubmit = (Button) findViewById(R.id.lsbtnSubmit);
		zdcsd = (EditText) findViewById(R.id.zdcsd);
		zdxtsj = (EditText) findViewById(R.id.zdxtsj);
		xckzzdjl = (EditText) findViewById(R.id.xckzzdjl);
		xcmzzdjl = (EditText) findViewById(R.id.xcmzzdjl);
		xckzmfdd = (EditText) findViewById(R.id.xckzmfdd);
		xcmzmfdd = (EditText) findViewById(R.id.xcmzmfdd);
		xczdczlz = (EditText) findViewById(R.id.xczdczlz);
		yjzdcsd = (EditText) findViewById(R.id.yjzdcsd);
		yjkzzdjl = (EditText) findViewById(R.id.yjkzzdjl);
		yjkzmfdd = (EditText) findViewById(R.id.yjkzmfdd);
		yjmzzdjl = (EditText) findViewById(R.id.yjmzzdjl);
		yjmzmfdd = (EditText) findViewById(R.id.yjmzmfdd);
		yjzdczlz = (EditText) findViewById(R.id.yjzdczlz);
		zdwdx = (Spinner) findViewById(R.id.zdwdx);
		lszdpd = (Spinner) findViewById(R.id.lszdpd);
		yjzdczlfs = (Spinner) findViewById(R.id.yjzdczlfs);
		yjzdpd = (Spinner) findViewById(R.id.yjzdpd);
	}

	// 建立Spinner的Adapter
	public ArrayAdapter<String> SetSpinner(String[] spidata) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spidata);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}

	// 对话框选通道调枪机
	private void dialogs() {
		AlertDialog.Builder builder3 = new AlertDialog.Builder(RoadTestToMove.this);
		builder3.setTitle("请选择路试制动通道：");
		final String[] dxh = { "1", "2", "3", "4", "5" };
		builder3.setSingleChoiceItems(dxh, 0, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				jcxdh = dxh[which];
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
		case R.id.lsbtnBeginDet:
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						// 18C55开始
						handler.sendEmptyMessage(MSG_ROAD_SHOW);
						List<Code> c55data = getStart18C55data(theinformation);
						handler.sendEmptyMessage(MSG_ROAD_DISMISS);
						if (c55data.get(0).getCode().equals("1")) {
							handler.sendEmptyMessage(MSG_ROAD_SEEUESS);
							setmessegas(MSG_ROAD_FAIL, c55data.get(0).getMessage());
							// 18J31开始时触发拍照
							try {
								handler.sendEmptyMessage(MSG_ROAD_SHOW);
								List<Code> j31data = getSnap18J31data("R1", jcxdh, theinformation, "0341");
								handler.sendEmptyMessage(MSG_ROAD_DISMISS);
								if (j31data.get(0).getCode().equals("1")) {
									setmessegas(MSG_ROAD_FAIL, "路试触发拍照成功，正在拍照！");
								} else {
									setmessegas(MSG_ROAD_FAIL, j31data.get(0).getMessage());
								}
							} catch (Exception e) {
								handler.sendEmptyMessage(MSG_ROAD_DISMISS);
								handler.sendEmptyMessage(MSG_ROAD_CHAOSHI);
								e.printStackTrace();
							}
							// 18J11开始时触发录像开始
							try {
								handler.sendEmptyMessage(MSG_ROAD_SHOW);
								List<Code> j11data = getVideo18J11J12data(theinformation, "R1", jcxdh, "18J11");
								handler.sendEmptyMessage(MSG_ROAD_DISMISS);
								if (j11data.get(0).getCode().equals("1")) {
									setmessegas(MSG_ROAD_FAIL, "路试触发录像成功，正在录像！");
								} else {
									setmessegas(MSG_ROAD_FAIL, j11data.get(0).getMessage());
								}
							} catch (Exception e) {
								handler.sendEmptyMessage(MSG_ROAD_DISMISS);
								handler.sendEmptyMessage(MSG_ROAD_CHAOSHI);
								e.printStackTrace();
							}
						} else {
							handler.sendEmptyMessage(MSG_ROAD_SEEUESS);
							setmessegas(MSG_ROAD_FAIL, c55data.get(0).getMessage());
						}
					} catch (Exception e) {
						handler.sendEmptyMessage(MSG_ROAD_DISMISS);
						handler.sendEmptyMessage(MSG_ROAD_CHAOSHI);
						e.printStackTrace();
					}
				}
			}).start();
			break;
		// 结束录像
		case R.id.lsjslx:
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						// 18J12结束时触发录像结束
						try {
							handler.sendEmptyMessage(MSG_ROAD_LXJS);
							handler.sendEmptyMessage(MSG_ROAD_SHOW);
							List<Code> j12data = getVideo18J11J12data(theinformation, "R1", jcxdh, "18J12");
							handler.sendEmptyMessage(MSG_ROAD_DISMISS);
							if (j12data.get(0).getCode().equals("1")) {
								setmessegas(MSG_ROAD_FAIL, "路试制动结束录像成功！");
							} else {
								setmessegas(MSG_ROAD_FAIL, j12data.get(0).getMessage());
							}
						} catch (Exception e) {
							handler.sendEmptyMessage(MSG_ROAD_DISMISS);
							handler.sendEmptyMessage(MSG_ROAD_CHAOSHI);
							e.printStackTrace();
						}
						// 18J31结束时触发拍照
						handler.sendEmptyMessage(MSG_ROAD_SHOW);
						List<Code> j31data = getSnap18J31data("R1", jcxdh, theinformation, "0343");
						handler.sendEmptyMessage(MSG_ROAD_DISMISS);
						if (j31data.get(0).getCode().equals("1")) {
							setmessegas(MSG_ROAD_FAIL, "路试触发拍照成功，正在拍照！");
						} else {
							setmessegas(MSG_ROAD_FAIL, j31data.get(0).getMessage());
						}
					} catch (Exception e) {
						handler.sendEmptyMessage(MSG_ROAD_DISMISS);
						handler.sendEmptyMessage(MSG_ROAD_CHAOSHI);
						e.printStackTrace();
					}
				}
			}).start();
			break;
		// 提交数据
		case R.id.lsbtnSubmit:
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						if (getkekong(yjzdpd.getSelectedItem().toString(), hegetwo).equals("0")
								&& getkekong(lszdpd.getSelectedItem().toString(), hegetwo).equals("0")) {
							setmessegas(MSG_ROAD_FAIL, "应急路试制动判定或行车路试制动判定以上两个不能同时为空或未检！");
						} else if (getkekong(yjzdpd.getSelectedItem().toString(), hegetwo).equals("")
								&& getkekong(lszdpd.getSelectedItem().toString(), hegetwo).equals("")) {
							setmessegas(MSG_ROAD_FAIL, "应急路试制动判定或行车路试制动判定以上两个不能同时为空或未检！");
						} else if (getkekong(yjzdpd.getSelectedItem().toString(), hegetwo).equals("0")
								&& getkekong(lszdpd.getSelectedItem().toString(), hegetwo).equals("")) {
							setmessegas(MSG_ROAD_FAIL, "应急路试制动判定或行车路试制动判定以上两个不能同时为空或未检！");
						} else if (getkekong(yjzdpd.getSelectedItem().toString(), hegetwo).equals("")
								&& getkekong(lszdpd.getSelectedItem().toString(), hegetwo).equals("0")) {
							setmessegas(MSG_ROAD_FAIL, "应急路试制动判定或行车路试制动判定以上两个不能同时为空或未检！");
						} else if (Judge()) {
							// 18C54提交数据
							handler.sendEmptyMessage(MSG_ROAD_SHOW);
							List<Code> c54data = getSubmitted18C54data();
							handler.sendEmptyMessage(MSG_ROAD_DISMISS);
							if (c54data.get(0).getCode().equals("1")) {
								setmessegas(MSG_ROAD_ZIDINGYI, "数据提交成功！");
							} else {
								setmessegas(MSG_ROAD_ZIDINGYI, c54data.get(0).getMessage());
							}
							// 18C58结束
							handler.sendEmptyMessage(MSG_ROAD_SHOW);
							List<Code> c58data = getEnd18C58data(theinformation);
							handler.sendEmptyMessage(MSG_ROAD_DISMISS);
							if (c58data.get(0).getCode().equals("1")) {
								setmessegas(MSG_ROAD_FAIL, "机动车检验项目结束成功！");
							} else {
								setmessegas(MSG_ROAD_FAIL, c58data.get(0).getMessage());
							}

						}
					} catch (Exception e) {
						handler.sendEmptyMessage(MSG_ROAD_DISMISS);
						handler.sendEmptyMessage(MSG_ROAD_CHAOSHI);
						e.printStackTrace();
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
		datalist2.add("R1");
		datalist2.add(lists.getHpzl());
		datalist2.add(lists.getHphm());
		datalist2.add(lists.getClsbdh());
		datalist2.add(operaor.get(0).getXm());
		datalist2.add(zdcsd.getText().toString().trim());
		datalist2.add(zdxtsj.getText().toString().trim());
		datalist2.add(getzdwdx(zdwdx.getSelectedItem().toString(), zdwdxfdata));
		datalist2.add(xckzzdjl.getText().toString().trim());
		datalist2.add(xcmzzdjl.getText().toString().trim());
		datalist2.add(xckzmfdd.getText().toString().trim());
		datalist2.add(xcmzmfdd.getText().toString().trim());
		datalist2.add(xczdczlz.getText().toString().trim());
		datalist2.add(getkekong(lszdpd.getSelectedItem().toString(), hegetwo));
		datalist2.add(yjzdcsd.getText().toString().trim());
		datalist2.add(yjkzzdjl.getText().toString().trim());
		datalist2.add(yjkzmfdd.getText().toString().trim());
		datalist2.add(yjmzzdjl.getText().toString().trim());
		datalist2.add(yjmzmfdd.getText().toString().trim());
		datalist2.add(getkekong(yjzdczlfs.getSelectedItem().toString(), yjzdczlfsdata));
		datalist2.add(yjzdczlz.getText().toString().trim());
		datalist2.add(getkekong(yjzdpd.getSelectedItem().toString(), hegetwo));
		datalist2.add("");
		datalist2.add("");
		datalist2.add("");
		datalist2.add("");
		datalist2.add("0");
		return datalist2;
	}

	/**
	 * 行车制动稳定性值判断
	 * 
	 * @param zhidatas
	 *            :控件值
	 * @param zhishuzu
	 *            :对应数组
	 * @return
	 */
	public String getzdwdx(String zhidatas, String[] zhishuzu) {
		String zhi = null;
		for (int i = 0; i < zhishuzu.length; i++) {
			if (zhidatas.equals(zhishuzu[i])) {
				zhi = String.valueOf(i);
				if (zhi.equals("0")) {
					zhi = "";
				}
			}
		}
		return zhi;
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

	// 数据小数点判断
	public boolean Judge() {
		String zdxtsjzhi = zdxtsj.getText().toString().trim();
		String xckzmfddzhi = xckzmfdd.getText().toString().trim();
		String xcmzmfddzhi = xcmzmfdd.getText().toString().trim();
		String yjkzmfddzhi = yjkzmfdd.getText().toString().trim();
		String yjmzmfddzhi = yjmzmfdd.getText().toString().trim();
		if (zdxtsjzhi.split("\\.").length == 2 && zdxtsjzhi.split("\\.")[1].length() == 2 || zdxtsjzhi.equals("")) {
			if (xckzmfddzhi.split("\\.").length == 2 && xckzmfddzhi.split("\\.")[1].length() == 1 || xckzmfddzhi.equals("")) {
				if (xcmzmfddzhi.split("\\.").length == 2 && xcmzmfddzhi.split("\\.")[1].length() == 1 || xcmzmfddzhi.equals("")) {
					if (yjkzmfddzhi.split("\\.").length == 2 && yjkzmfddzhi.split("\\.")[1].length() == 1
							|| yjkzmfddzhi.equals("")) {
						if (yjmzmfddzhi.split("\\.").length == 2 && yjmzmfddzhi.split("\\.")[1].length() == 1
								|| yjmzmfddzhi.equals("")) {
							return true;
						} else {
							setmessegas(MSG_ROAD_FAIL, "应急满载MFDD保留小数点1位");
							return false;
						}
					} else {
						setmessegas(MSG_ROAD_FAIL, "应急空载MFDD保留小数点1位");
						return false;
					}
				} else {
					setmessegas(MSG_ROAD_FAIL, "行车满载MFDD保留小数点1位");
					return false;
				}
			} else {
				setmessegas(MSG_ROAD_FAIL, "行车空载MFDD保留小数点1位");
				return false;
			}
		} else {
			setmessegas(MSG_ROAD_FAIL, "行车制动协调时间保留小数点2位");
			return false;
		}
	}

	// 重写返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			AlertDialog.Builder newbuilder = new AlertDialog.Builder(RoadTestToMove.this);
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
