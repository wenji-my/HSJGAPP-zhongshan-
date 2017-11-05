package com.example.hsjgapp;

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
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.hsjgapp.DialogTool.DialogTool;
import com.example.hsjgapp.IntentUtil.ConnectMethods;
import com.example.hsjgapp.ToolUtil.PanDuan;
import com.example.hsjgapp.ToolUtil.TimeTool;
import com.example.hsjgapp.XmlTool.UnXmlTool;
import com.example.hsjgapp.XmlTool.XMLParsingMethods;
import com.example.hsjgapp.domain.Code;
import com.example.hsjgapp.domain.Q09Domain;
import com.example.hsjgapp.domain.Q11Domain;
import com.example.hsjgapp.domain.Q22Domain;
import com.example.hsjgapp.domain.Q32Domain;

import java.util.ArrayList;
import java.util.List;

public class MaulDetActivitySpinner extends Activity implements

ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener {
	private ProgressDialog builder2 = null;
	private String[] title1;
	private String[] title11 = { "车辆唯一性检查", "车辆特征参数检查", "车辆外观检查", "安全装置检查", "其它外观项目" };
	private String[] title2 = { "底盘动态检验" };
	private String[] title3 = { "车辆底盘部件检查" };
	private String[] title = null;
	private String[][] data = null;
	private List<MaulItemGroup> groups;
	private MaulItemAdapterSpinner exlist_adapter = null;
	private ExpandableListView exlist;
	private TextView maultextXY;
	private LinearLayout maulXY;
	private boolean ma_wgqx;
	private Button btnBeginDet;
	private Button btnSubmit;
	private Spinner qzdz, ygddtz, zzly, zxzxjxs;
	private ScrollView qtszscr;
	private LinearLayout qtszll, jyyjyll;
	private EditText cwkced, cwkked, cwkged, zbzled, syred, sjhmed, lxdzed, yzbmed, jyyjyed, lcbds;
	private String qzdzdata = "", ygddtzdata = "", zzlydata = "", zxzxjxsdata = "", lcbdsdata = "", cwkcdata = "", cwkkdata = "",
			cwkgdata = "", zbzldata = "", syrdata = "", sjhmdata = "", lxdzdata = "", yzbmdata = "", jyyjydata = "";
	private Q11Domain theinformation;
	private List<Q32Domain> buhegexingxx;
	private List<String> vehiclebuheges;
	private List<Q09Domain> operatorlists;
	private String[] qzdzdatas = { "", "01-四灯远近光", "02-四灯远光", "03-二灯远近光", "04-二灯近光", "05-一灯远光" };
	private String[] ygddtzdatas = { "", "0-否", "1-能" };
	private String[] zzlydatas = { "", "0-气压制动", "1-液压制动", "2-气推油制动" };
	private String[] zxzxjxsdatas = { "", "0-独立悬架", "1-非独立悬架" };
	protected static final int MSG_START_FAIL = 10051;
	protected static final int MSG_START_SEEUESS = 10052;
	private static final int MSG_START_SHOW = 10054;
	private static final int MSG_START_DISMISS = 10055;
	private static final int MSG_START_CHAOSHI = 10056;
	private static final int MSG_START_ZIDINGYI = 10057;
	private static final int MSG_START_LBGX = 10058;
	private static final int MSG_START_SETTEXT = 10059;
	private String ip;
	private String jkph;
	private String jkxlh;
	private String jcxdh;// 调枪机
	private String csszjcxdh;// 调数据等
	private String zpwgzpbh;
	private String number5 = "";
	private String xmname;
	private boolean wjzhi;
	private boolean dtzhi;
	private boolean dpzhi;
	private boolean photozhi;
	private boolean timezhi;
	private String jy;
	private Message msg;
	private Handler hander = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MSG_START_SEEUESS) {
				btnBeginDet.setEnabled(false);
				if (timezhi) {
					AnNiuTime();
				} else {
					btnSubmit.setEnabled(true);
				}
			}
			if (msg.what == MSG_START_FAIL) {
				DialogTool.AlertDialogShow(MaulDetActivitySpinner.this, msg.obj.toString());
			}
			if (msg.what == MSG_START_SHOW) {
				builder2 = new ProgressDialog(MaulDetActivitySpinner.this);
				builder2.setMessage("正在加载中，请稍等。。。");
				builder2.setCancelable(false);
				builder2.show();
			}
			if (msg.what == MSG_START_DISMISS) {
				builder2.dismiss();
			}
			if (msg.what == MSG_START_CHAOSHI) {
				DialogTool.AlertDialogShow(MaulDetActivitySpinner.this, "请求超时，请检查网络环境!");
			}
			if (msg.what == MSG_START_LBGX) {
				exlist_adapter.notifyDataSetChanged();
			}
			if (msg.what == MSG_START_SETTEXT) {
				if (msg.obj.toString().equals("提交数据")) {
					btnSubmit.setText(msg.obj.toString());
					btnSubmit.setEnabled(true);
				} else {
					btnSubmit.setText(msg.obj.toString());
				}
			}
			if (msg.what == MSG_START_ZIDINGYI) {
				AlertDialog.Builder builder1 = new AlertDialog.Builder(MaulDetActivitySpinner.this);
				builder1.setTitle("提示框");
				builder1.setMessage(msg.obj.toString());
				builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (xmname.equals("F1")) {
							maulXY.setVisibility(View.GONE);
							maultextXY.setVisibility(View.VISIBLE);
						} else {
							MaulDetActivitySpinner.this.finish();
						}
					}
				});
				builder1.show();
			}
		}
	};

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.maul_list);
		maChuShiHua();
		SharedPreferences preferences = getSharedPreferences("cs_setup", 0);
		ip = preferences.getString("IP", "");
		jkph = preferences.getString("JGPH", "");
		jkxlh = preferences.getString("JKXLH", "");
		csszjcxdh = preferences.getString("JCXDH", "1");
		jcxdh = "1";
		SharedPreferences share = getSharedPreferences("jbsz", 1);
		wjzhi = share.getBoolean("WJXZ", true);
		dtzhi = share.getBoolean("DTXZ", true);
		dpzhi = share.getBoolean("DPXZ", true);
		photozhi = share.getBoolean("PHOTOZ", true);
		timezhi = share.getBoolean("TIMETK", false);
		Bundle bundle = MaulDetActivitySpinner.this.getIntent().getExtras();
		theinformation = (Q11Domain) bundle.getSerializable("informationsObj");
		buhegexingxx = (List<Q32Domain>) bundle.getSerializable("thenewbhgxxxsObj");
		vehiclebuheges = (List<String>) bundle.getSerializable("thenewVehicleBuHeGesObj");
		operatorlists = (List<Q09Domain>) bundle.getSerializable("theoperatorlistsObj");
		xmname = getIntent().getStringExtra("XM");
		if (xmname.equals("F1")) {
			ma_wgqx = getIntent().getBooleanExtra("WJQX", false);
			maulXY.setVisibility(View.GONE);
			maultextXY.setVisibility(View.VISIBLE);
			if (theinformation.getJyxm().indexOf("F1") == 0) {
				if (!theinformation.getWgjcxm().equals("")) {
					if (ma_wgqx) {
						maultextXY.setVisibility(View.GONE);
						maulXY.setVisibility(View.VISIBLE);
					}
				}
			}
		}
		boolean xmpd = true;
		if (xmname.equals("F1")) {
			if (theinformation.getWgjcxm().equals("")) {
				xmpd = false;
			}
		}
		if (xmpd) {
			setdatas();
			jy = getIntent().getStringExtra("JY");
			if (jy != null) {
				jyyjyed.setText(jy);
			}
			exlist = (ExpandableListView) findViewById(R.id.detItemist);
			init();
			exlist_adapter = new MaulItemAdapterSpinner(this, groups, number5, xmname, hander, buhegexingxx, vehiclebuheges);
			exlist.setOnChildClickListener(this);
			exlist.setAdapter(exlist_adapter);
			for (int i = 0; i < exlist_adapter.getGroupCount(); i++) {
				exlist.expandGroup(i);
			}
		}
	}

	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.btnBeginDet:
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						hander.sendEmptyMessage(MSG_START_SHOW);
						List<String> datalist = new ArrayList<String>();
						datalist.add("");
						datalist.add(xmname);
						datalist.add(theinformation.getJycs() + "");
						datalist.add(TimeTool.getTiem());
						datalist.add(theinformation.getHpzl());
						datalist.add(theinformation.getHphm());
						datalist.add(theinformation.getJylsh());
						datalist.add(csszjcxdh);
						datalist.add(theinformation.getClsbdh());
						datalist.add(jkph);
						String xmldata = UnXmlTool.getWriteXML(StaticValues.namedata, datalist);
						Log.i("BBB", "xmldata:" + xmldata);
						String startdata = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh, "18C55",
								xmldata, "writeObjectOutResult", StaticValues.timeoutNine, StaticValues.numberOne);
						List<Code> codelists = XMLParsingMethods.saxcode(startdata);
						if (codelists.get(0).getCode().equals("1")) {
							hander.sendEmptyMessage(MSG_START_SEEUESS);
							hander.sendEmptyMessage(MSG_START_DISMISS);
							messegas(MSG_START_FAIL, "机动车检验项目开始成功，可以开始采集数据！");
							try {
								// 调触发拍照接口18J31
								if (!xmname.equals("F1")) {
									hander.sendEmptyMessage(MSG_START_SHOW);
									List<String> cfpzdata = new ArrayList<String>();
									cfpzdata.add(theinformation.getJylsh());
									cfpzdata.add(jkph);
									cfpzdata.add(theinformation.getJycs() + "");
									cfpzdata.add(jcxdh);
									cfpzdata.add(theinformation.getHpzl());
									cfpzdata.add(theinformation.getHphm());
									cfpzdata.add(theinformation.getClsbdh());
									cfpzdata.add("");
									cfpzdata.add(xmname);
									cfpzdata.add(TimeTool.getTiem());
									cfpzdata.add(zpwgzpbh);
									String cfpzdatas = UnXmlTool.getTimeXML(StaticValues.cfpzname, cfpzdata);
									Log.i("BBB", "cfpzdatas:" + cfpzdatas);
									String startcfpzdata = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh,
											"18J31", cfpzdatas, "writeObjectOutResult", StaticValues.timeoutFifteen,
											StaticValues.numberTwo);
									List<Code> cfpzlists = XMLParsingMethods.saxcode(startcfpzdata);
									if (cfpzlists.get(0).getCode().equals("1")) {
										hander.sendEmptyMessage(MSG_START_DISMISS);
										messegas(MSG_START_FAIL, "触发拍照成功，正在拍照！");
									} else {
										hander.sendEmptyMessage(MSG_START_DISMISS);
										messegas(MSG_START_FAIL, cfpzlists.get(0).getMessage());
									}
								}
							} catch (Exception e) {
								hander.sendEmptyMessage(MSG_START_DISMISS);
								hander.sendEmptyMessage(MSG_START_CHAOSHI);
								e.printStackTrace();
								return;
							}

							// 调触发视频摄像机18J11
							hander.sendEmptyMessage(MSG_START_SHOW);
							List<String> vediolist = new ArrayList<String>();
							vediolist.add(theinformation.getJylsh());
							vediolist.add(theinformation.getHphm());
							vediolist.add(theinformation.getHpzl());
							vediolist.add(theinformation.getClsbdh());
							vediolist.add(xmname);
							vediolist.add(jcxdh);
							vediolist.add(theinformation.getJycs() + "");
							String vediodataks = UnXmlTool.getWriteXML(StaticValues.vedioname, vediolist);
							Log.i("BBB", "vediodataks:" + vediodataks);
							String vediodatareturn = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh,
									"18J11", vediodataks, "writeObjectOutResult", StaticValues.timeoutFifteen,
									StaticValues.numberTwo);
							List<Code> vediokslists = XMLParsingMethods.saxcode(vediodatareturn);
							if (vediokslists.get(0).getCode().equals("1")) {
								hander.sendEmptyMessage(MSG_START_DISMISS);
								messegas(MSG_START_FAIL, "触发视频摄像机进行视频录像成功！");
							} else {
								hander.sendEmptyMessage(MSG_START_DISMISS);
								messegas(MSG_START_FAIL, vediokslists.get(0).getMessage());
							}

						} else {
							hander.sendEmptyMessage(MSG_START_DISMISS);
							hander.sendEmptyMessage(MSG_START_SEEUESS);
							messegas(MSG_START_FAIL, codelists.get(0).getMessage());
						}
					} catch (Exception e) {
						hander.sendEmptyMessage(MSG_START_DISMISS);
						hander.sendEmptyMessage(MSG_START_CHAOSHI);
						e.printStackTrace();
						return;
					}
				}
			}).start();
			break;
		case R.id.btnSubmit:
			new Thread(new Runnable() {
				@Override
				public void run() {
					/*
					 * boolean sfsc = false; if (xmname.equals("F1")) { try {
					 * hander.sendEmptyMessage(MSG_START_SHOW); String lpphotots
					 * = getLpPhotodata(theinformation).get(0).getWpzp(); if
					 * (lpphotots.equals("")) { sfsc = true; }
					 * hander.sendEmptyMessage(MSG_START_DISMISS); } catch
					 * (Exception e) {
					 * hander.sendEmptyMessage(MSG_START_DISMISS);
					 * hander.sendEmptyMessage(MSG_START_CHAOSHI);
					 * e.printStackTrace(); } } else { sfsc = true; }
					 * Log.i("SSS", sfsc + "");
					 */
					if (true) {
						try {
							hander.sendEmptyMessage(MSG_START_SHOW);
							geteditdatas();
							String xmldata = null;
							if (xmname.equals("F1")) {
								xmldata = UnXmlTool.getWriteXML(StaticValues.wgjsxmdata, getF1());
							} else if (xmname.equals("DC")) {
								xmldata = UnXmlTool.getWriteXML(StaticValues.dtdpxmdata, getDC());
							} else if (xmname.equals("C1")) {
								xmldata = UnXmlTool.getWriteXML(StaticValues.dpjyxmdata, getC1());
							}
							Log.i("BBB", "18C80xmldata:" + xmldata);

							String startdata = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh, "18C80",
									xmldata, "writeObjectOutResult", StaticValues.timeoutFive, StaticValues.numberFour);
							List<Code> tijiaolists = XMLParsingMethods.saxcode(startdata);
							if (tijiaolists.get(0).getCode().equals("1")) {
								hander.sendEmptyMessage(MSG_START_DISMISS);
								messegas(MSG_START_ZIDINGYI, "数据提交成功！");
								// 调结束18C58
								hander.sendEmptyMessage(MSG_START_SHOW);
								List<String> datalist = new ArrayList<String>();
								datalist.add(theinformation.getJylsh());
								datalist.add(jkph);
								datalist.add(csszjcxdh);
								datalist.add(theinformation.getJycs() + "");
								datalist.add(xmname);
								datalist.add(theinformation.getHpzl());
								datalist.add(theinformation.getHphm());
								datalist.add(theinformation.getClsbdh());
								datalist.add("");
								datalist.add(TimeTool.getTiem());
								String xmldata2 = UnXmlTool.getWriteXML(StaticValues.namedata2, datalist);
								Log.i("BBB", "18C58xmldata2:" + xmldata2);
								String jieshudata = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh,
										"18C58", xmldata2, "writeObjectOutResult", StaticValues.timeoutNine,
										StaticValues.numberOne);
								List<Code> jieshulists = XMLParsingMethods.saxcode(jieshudata);
								if (jieshulists.get(0).getCode().equals("1")) {
									hander.sendEmptyMessage(MSG_START_DISMISS);
									messegas(MSG_START_FAIL, "机动车检验项目结束成功！");
								} else {
									hander.sendEmptyMessage(MSG_START_DISMISS);
									messegas(MSG_START_FAIL, jieshulists.get(0).getMessage());
								}

								// 动态底盘结束时调抓拍接口
								if (xmname.equals("DC")) {
									try {
										// 调触发拍照接口18J31
										hander.sendEmptyMessage(MSG_START_SHOW);
										List<String> jscfpzdata = new ArrayList<String>();
										jscfpzdata.add(theinformation.getJylsh());
										jscfpzdata.add(jkph);
										jscfpzdata.add(theinformation.getJycs() + "");
										jscfpzdata.add(jcxdh);
										jscfpzdata.add(theinformation.getHpzl());
										jscfpzdata.add(theinformation.getHphm());
										jscfpzdata.add(theinformation.getClsbdh());
										jscfpzdata.add("");
										jscfpzdata.add(xmname);
										jscfpzdata.add(TimeTool.getTiem());
										jscfpzdata.add("0342");
										String jscfpzdatas = UnXmlTool.getTimeXML(StaticValues.cfpzname, jscfpzdata);
										Log.i("BBB", "jscfpzdatas:" + jscfpzdatas);
										String startjscfpzdata = ConnectMethods.connectWebService(ip, StaticValues.writeObject,
												jkxlh, "18J31", jscfpzdatas, "writeObjectOutResult", StaticValues.timeoutFifteen,
												StaticValues.numberTwo);
										List<Code> jscfpzlists = XMLParsingMethods.saxcode(startjscfpzdata);
										if (jscfpzlists.get(0).getCode().equals("1")) {
											hander.sendEmptyMessage(MSG_START_DISMISS);
											messegas(MSG_START_FAIL, "动态底盘结束触发拍照成功，正在拍照！");
										} else {
											hander.sendEmptyMessage(MSG_START_DISMISS);
											messegas(MSG_START_FAIL, jscfpzlists.get(0).getMessage());
										}
									} catch (Exception e) {
										hander.sendEmptyMessage(MSG_START_DISMISS);
										hander.sendEmptyMessage(MSG_START_CHAOSHI);
										e.printStackTrace();
										return;
									}
								}
								// 调触发视频摄像机停止视频结束18J12
								hander.sendEmptyMessage(MSG_START_SHOW);
								List<String> vediolist = new ArrayList<String>();
								vediolist.add(theinformation.getJylsh());
								vediolist.add(theinformation.getHphm());
								vediolist.add(theinformation.getHpzl());
								vediolist.add(theinformation.getClsbdh());
								vediolist.add(xmname);
								vediolist.add(jcxdh);
								vediolist.add(theinformation.getJycs() + "");
								String vediodatajs = UnXmlTool.getWriteXML(StaticValues.vedioname, vediolist);
								Log.i("BBB", "vediodatajs:" + vediodatajs);
								String vediodatareturn = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh,
										"18J12", vediodatajs, "writeObjectOutResult", StaticValues.timeoutFifteen,
										StaticValues.numberTwo);
								List<Code> vediojslists = XMLParsingMethods.saxcode(vediodatareturn);
								if (vediojslists.get(0).getCode().equals("1")) {
									hander.sendEmptyMessage(MSG_START_DISMISS);
									messegas(MSG_START_FAIL, "触发视频摄像机停止视频录像并保存录像信息成功！");
								} else {
									hander.sendEmptyMessage(MSG_START_DISMISS);
									messegas(MSG_START_FAIL, vediojslists.get(0).getMessage());
								}
							} else {
								hander.sendEmptyMessage(MSG_START_DISMISS);
								messegas(MSG_START_FAIL, tijiaolists.get(0).getMessage());
							}

						} catch (Exception e) {
							hander.sendEmptyMessage(MSG_START_DISMISS);
							hander.sendEmptyMessage(MSG_START_CHAOSHI);
							e.printStackTrace();
							return;
						}
					} else {
						messegas(MSG_START_FAIL, "外观照片需拍全才能上传");
					}
				}
			}).start();
			break;
		case R.id.kxcs:
			qtszscr.setVisibility(View.GONE);
			exlist.setVisibility(View.VISIBLE);
			break;
		case R.id.qtcs:
			exlist.setVisibility(View.GONE);
			qtszscr.setVisibility(View.VISIBLE);
			if (xmname.equals("F1")) {
				qtszll.setVisibility(View.VISIBLE);
			}
			jyyjyll.setVisibility(View.VISIBLE);
			break;
		}
	}

	// 调18Q22接口获取漏拍照片查询数据
	private List<Q22Domain> getLpPhotodata(Q11Domain information) {
		List<Q22Domain> lpphotodatas = null;
		List<String> datalists = new ArrayList<String>();
		datalists.add(information.getJylsh());
		datalists.add(information.getClsbdh());
		String xmldata = UnXmlTool.getQueryXML(StaticValues.lpdataq22, datalists);
		Log.i("BBB", "Q22Domain:" + xmldata);
		String lpphotonames = ConnectMethods.connectWebService(ip, StaticValues.queryObject, jkxlh, "18Q22", xmldata,
				"queryObjectOutResult", StaticValues.timeoutThree, StaticValues.numberFive);
		lpphotodatas = XMLParsingMethods.saxlpphotos(lpphotonames);
		return lpphotodatas;
	}

	private void maChuShiHua() {
		maulXY = (LinearLayout) findViewById(R.id.maulXY);
		maultextXY = (TextView) findViewById(R.id.maultextXY);
		btnBeginDet = (Button) findViewById(R.id.btnBeginDet);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		qtszll = (LinearLayout) findViewById(R.id.qtszll);
		jyyjyll = (LinearLayout) findViewById(R.id.jyyjyll);
		qtszscr = (ScrollView) findViewById(R.id.qtszscr);
		cwkced = (EditText) findViewById(R.id.cwkced);
		cwkked = (EditText) findViewById(R.id.cwkked);
		cwkged = (EditText) findViewById(R.id.cwkged);
		zbzled = (EditText) findViewById(R.id.zbzled);
		syred = (EditText) findViewById(R.id.syred);
		sjhmed = (EditText) findViewById(R.id.sjhmed);
		lxdzed = (EditText) findViewById(R.id.lxdzed);
		yzbmed = (EditText) findViewById(R.id.yzbmed);
		jyyjyed = (EditText) findViewById(R.id.jyyjyed);
		qzdz = (Spinner) findViewById(R.id.qzdz);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, qzdzdatas);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		qzdz.setAdapter(adapter);
		ygddtz = (Spinner) findViewById(R.id.ygddtz);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ygddtzdatas);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ygddtz.setAdapter(adapter2);
		zzly = (Spinner) findViewById(R.id.zzly);
		ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, zzlydatas);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		zzly.setAdapter(adapter3);
		lcbds = (EditText) findViewById(R.id.lcbds);
		zxzxjxs = (Spinner) findViewById(R.id.zxzxjxs);
		ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, zxzxjxsdatas);
		adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		zxzxjxs.setAdapter(adapter4);
	}

	private void geteditdatas() {
		qzdzdata = qzdz.getSelectedItem().toString().trim();
		ygddtzdata = ygddtz.getSelectedItem().toString().trim();
		zzlydata = zzly.getSelectedItem().toString().trim();
		zxzxjxsdata = zxzxjxs.getSelectedItem().toString().trim();
		lcbdsdata = lcbds.getText().toString().trim();
		cwkcdata = cwkced.getText().toString().trim();
		cwkkdata = cwkked.getText().toString().trim();
		cwkgdata = cwkged.getText().toString().trim();
		zbzldata = zbzled.getText().toString().trim();
		syrdata = syred.getText().toString().trim();
		sjhmdata = sjhmed.getText().toString().trim();
		lxdzdata = lxdzed.getText().toString().trim();
		yzbmdata = yzbmed.getText().toString().trim();
		jyyjydata = jyyjyed.getText().toString().trim();
	}

	private void dialogs() {
		AlertDialog.Builder builder3 = new AlertDialog.Builder(MaulDetActivitySpinner.this);
		builder3.setTitle("请选择通道号：");
		final String[] dxh = { "1", "2", "3", "4", "5"};
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

	private void init() {
		groups = new ArrayList<MaulItemGroup>();
		for (int i = 0; i < title.length; i++) {
			List<MaulListItem> group1_children = new ArrayList<MaulListItem>();
			for (int j = 0; j < data[i].length; j++) {
				MaulListItem item = new MaulListItem(data[i][j], false);
				group1_children.add(item);
			}
			MaulItemGroup phonegroup1 = new MaulItemGroup(title[i], group1_children);
			groups.add(phonegroup1);
		}
	}

	private void AnNiuTime() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					int time = ManyTime(theinformation.getCllx(), theinformation.getSyxz(), xmname);
					for (int i = 0; i <= time; time--) {
						String datas = "还剩" + time + "秒";
						messegas(MSG_START_SETTEXT, datas);
						if (time == 0) {
							messegas(MSG_START_SETTEXT, "提交数据");
						}
						Thread.sleep(1000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private int ManyTime(String clls, String syxz, String xm) {
		int time = 0;
		if (xm.equals("F1")) {
			if (clls.indexOf("K3") == 0 || clls.indexOf("K4") == 0) {
				if (syxz.equals("A")) {
					time = 120;
				} else {
					time = 240;
				}
			} else if (clls.indexOf("M") == 0 || clls.indexOf("N") == 0) {
				time = 90;
			} else {
				time = 240;
			}
		}
		if (xm.equals("DC")) {
			if (clls.indexOf("M") == 0 || clls.indexOf("N") == 0) {
				time = 30;
			} else {
				time = 60;
			}
		}
		if (xm.equals("C1")) {
			if (clls.indexOf("K3") == 0 || clls.indexOf("K4") == 0) {
				if (syxz.equals("A")) {
					time = 40;
				} else {
					time = 100;
				}
			} else if (clls.indexOf("M") == 0 || clls.indexOf("N") == 0) {
				time = 90;
			} else {
				time = 100;
			}
		}
		return time;
	}

	public void messegas(int msgnumber, String neirong) {
		msg = new Message();
		msg.what = msgnumber;
		msg.obj = neirong;
		hander.sendMessage(msg);
	}

	public List<String> getF1() {
		String bhesmdatas = "";
		List<String> datalist2 = new ArrayList<String>();
		datalist2.add(theinformation.getJylsh());
		datalist2.add(jkph);
		datalist2.add(csszjcxdh);
		datalist2.add(theinformation.getJycs() + "");
		datalist2.add("F1");
		datalist2.add(theinformation.getHpzl());
		datalist2.add(theinformation.getHphm());
		datalist2.add(theinformation.getClsbdh());
		for (int i = 0; i < PanDuan.wgjsxmdata.length; i++) {
			if (MaulItemAdapterSpinner.map.get(PanDuan.wgjsxmname[i]) != null
					&& MaulItemAdapterSpinner.map.get(PanDuan.wgjsxmname[i]).equals("2")) {
				if (MaulItemAdapterSpinner.bhgxxmap.get(PanDuan.wgjsxmname[i]) != null) {
					bhesmdatas = bhesmdatas + UnXmlTool.getItems(MaulItemAdapterSpinner.bhgxxmap.get(PanDuan.wgjsxmname[i]));
				}
			}
			String zpStr[] = theinformation.getWgjcxm().split(",");
			boolean is = false;
			for (int j = 0; j < zpStr.length; j++) {
				if (PanDuan.wgjsxmdata[i].equals(PanDuan.Wgjcxmsz(zpStr[j]))) {
					is = true;
				}
			}
			if (is) {
				if (MaulItemAdapterSpinner.map.get(PanDuan.wgjcxmpd(PanDuan.wgjsxmdata[i])) != null) {
					datalist2.add(MaulItemAdapterSpinner.map.get(PanDuan.wgjcxmpd(PanDuan.wgjsxmdata[i])));
				} else {
					datalist2.add("1");
				}
				is = false;
			} else {
				if (MaulItemAdapterSpinner.map.get(PanDuan.wgjcxmpd(PanDuan.wgjsxmdata[i])) != null) {
					datalist2.add(MaulItemAdapterSpinner.map.get(PanDuan.wgjcxmpd(PanDuan.wgjsxmdata[i])));
				} else {
					datalist2.add("0");
				}
			}
		}
		Log.i("AAA", "bhesmdatas=" + bhesmdatas);
		datalist2.add(qzdzdata);
		datalist2.add(ygddtzdata);
		datalist2.add(zzlydata);
		datalist2.add(zxzxjxsdata);
		datalist2.add(lcbdsdata);
		datalist2.add(cwkcdata);
		datalist2.add(cwkkdata);
		datalist2.add(cwkgdata);
		datalist2.add(zbzldata);
		datalist2.add(syrdata);
		datalist2.add(sjhmdata);
		datalist2.add(lxdzdata);
		datalist2.add(yzbmdata);
		datalist2.add(bhesmdatas);
		datalist2.add(jyyjydata);
		datalist2.add(operatorlists.get(0).getXm());
		datalist2.add(operatorlists.get(0).getSfzmhm());
		return datalist2;
	}

	public List<String> getDC() {
		String bhesmdatas = "";
		List<String> datalist2 = new ArrayList<String>();
		datalist2.add(theinformation.getJylsh());
		datalist2.add(jkph);
		datalist2.add(csszjcxdh);
		datalist2.add(theinformation.getJycs() + "");
		datalist2.add("DC");
		datalist2.add(theinformation.getHpzl());
		datalist2.add(theinformation.getHphm());
		datalist2.add(theinformation.getClsbdh());
		for (int i = 0; i < PanDuan.dtdpxmdata.length; i++) {
			if (MaulItemAdapterSpinner.map.get(PanDuan.dtdpxmname[i]) != null
					&& MaulItemAdapterSpinner.map.get(PanDuan.dtdpxmname[i]).equals("2")) {
				if (MaulItemAdapterSpinner.bhgxxmap.get(PanDuan.dtdpxmname[i]) != null) {
					bhesmdatas = bhesmdatas + UnXmlTool.getItems(MaulItemAdapterSpinner.bhgxxmap.get(PanDuan.dtdpxmname[i]));
				}
			}
			String zpStr[] = theinformation.getDtdpjyxm().split(",");
			boolean is = false;
			for (int j = 0; j < zpStr.length; j++) {
				if (PanDuan.dtdpxmdata[i].equals(PanDuan.Dtdpxmsz(zpStr[j]))) {
					is = true;
				}
			}
			if (is) {
				if (MaulItemAdapterSpinner.map.get(PanDuan.Dtdpxmpd(PanDuan.dtdpxmdata[i])) != null) {
					datalist2.add(MaulItemAdapterSpinner.map.get(PanDuan.Dtdpxmpd(PanDuan.dtdpxmdata[i])));
				} else {
					datalist2.add("1");
				}
				is = false;
			} else {
				datalist2.add("0");
			}
		}
		Log.i("AAA", "bhesmdatas=" + bhesmdatas);
		datalist2.add(bhesmdatas);
		datalist2.add(jyyjydata);
		datalist2.add(operatorlists.get(0).getXm());
		datalist2.add(operatorlists.get(0).getSfzmhm());
		return datalist2;
	}

	public List<String> getC1() {
		String bhesmdatas = "";
		List<String> datalist2 = new ArrayList<String>();
		datalist2.add(theinformation.getJylsh());
		datalist2.add(jkph);
		datalist2.add(csszjcxdh);
		datalist2.add(theinformation.getJycs() + "");
		datalist2.add("C1");
		datalist2.add(theinformation.getHpzl());
		datalist2.add(theinformation.getHphm());
		datalist2.add(theinformation.getClsbdh());
		for (int i = 0; i < PanDuan.dpjyxmdata.length; i++) {
			if (MaulItemAdapterSpinner.map.get(PanDuan.dpjyxmname[i]) != null
					&& MaulItemAdapterSpinner.map.get(PanDuan.dpjyxmname[i]).equals("2")) {
				if (MaulItemAdapterSpinner.bhgxxmap.get(PanDuan.dpjyxmname[i]) != null) {
					bhesmdatas = bhesmdatas + UnXmlTool.getItems(MaulItemAdapterSpinner.bhgxxmap.get(PanDuan.dpjyxmname[i]));
				}
			}
			String zpStr[] = theinformation.getDpjyxm().split(",");
			boolean is = false;
			for (int j = 0; j < zpStr.length; j++) {
				if (PanDuan.dpjyxmdata[i].equals(PanDuan.Dpjyxmsz(zpStr[j]))) {
					is = true;
				}
			}
			if (is) {
				if (MaulItemAdapterSpinner.map.get(PanDuan.Dpjyxmpd(PanDuan.dpjyxmdata[i])) != null) {
					datalist2.add(MaulItemAdapterSpinner.map.get(PanDuan.Dpjyxmpd(PanDuan.dpjyxmdata[i])));
				} else {
					datalist2.add("1");
				}
				is = false;
			} else {
				datalist2.add("0");
			}
		}
		Log.i("AAA", "bhesmdatas=" + bhesmdatas);
		datalist2.add(bhesmdatas);
		datalist2.add(jyyjydata);
		datalist2.add(operatorlists.get(0).getXm());
		datalist2.add(operatorlists.get(0).getSfzmhm());
		return datalist2;
	}

	// 数据源判断
	public void setdatas() {
		if (xmname.equals("F1")) {
			if (wjzhi) {
				dialogs();
			}
			// csszjcxdh ="1";
			title = title1;
			String titledata = "";
			String zpStr[] = theinformation.getWgjcxm().split(",");
			String number1 = "";
			String number2 = "";
			String number3 = "";
			String number4 = "";
			for (int k = 0; k < zpStr.length; k++) {
				if (PanDuan.Wgjcxm(zpStr[k]).split(",")[1].equals("1")) {
					if (!number1.equals("")) {
						number1 = number1 + "," + PanDuan.Wgjcxm(zpStr[k]).split(",")[0];
					} else {
						number1 = PanDuan.Wgjcxm(zpStr[k]).split(",")[0];
					}
				} else if (PanDuan.Wgjcxm(zpStr[k]).split(",")[1].equals("2")) {
					if (!number2.equals("")) {
						number2 = number2 + "," + PanDuan.Wgjcxm(zpStr[k]).split(",")[0];
					} else {
						number2 = PanDuan.Wgjcxm(zpStr[k]).split(",")[0];
					}
				} else if (PanDuan.Wgjcxm(zpStr[k]).split(",")[1].equals("3")) {
					if (!number3.equals("")) {
						number3 = number3 + "," + PanDuan.Wgjcxm(zpStr[k]).split(",")[0];
					} else {
						number3 = PanDuan.Wgjcxm(zpStr[k]).split(",")[0];
					}
				} else if (PanDuan.Wgjcxm(zpStr[k]).split(",")[1].equals("4")) {
					if (!number4.equals("")) {
						number4 = number4 + "," + PanDuan.Wgjcxm(zpStr[k]).split(",")[0];
					} else {
						number4 = PanDuan.Wgjcxm(zpStr[k]).split(",")[0];
					}
				}
			}
			// 对返回的数据中没有的外观项目进行归纳
			for (int j = 0; j < PanDuan.wgjsxmnumber2.length; j++) {
				boolean iscz = false;
				for (int g = 0; g < zpStr.length; g++) {
					if (zpStr[g].equals(PanDuan.wgjsxmnumber2[j])) {
						iscz = true;
					}
				}
				if (!iscz) {
					if (!number5.equals("")) {
						number5 = number5 + "," + PanDuan.Wgjcxm(PanDuan.wgjsxmnumber2[j]).split(",")[0];
					} else {
						number5 = PanDuan.Wgjcxm(PanDuan.wgjsxmnumber2[j]).split(",")[0];
					}
				}
			}
			if (!number1.equals("")) {
				titledata = title11[0];
			}
			if (!number2.equals("")) {
				if (!titledata.equals("")) {
					titledata = titledata + "," + title11[1];
				} else {
					titledata = title11[1];
				}
			}
			if (!number3.equals("")) {
				if (!titledata.equals("")) {
					titledata = titledata + "," + title11[2];
				} else {
					titledata = title11[2];
				}
			}
			if (!number4.equals("")) {
				if (!titledata.equals("")) {
					titledata = titledata + "," + title11[3];
				} else {
					titledata = title11[3];
				}
			}
			if (!number5.equals("")) {
				if (!titledata.equals("")) {
					titledata = titledata + "," + title11[4];
				} else {
					titledata = title11[4];
				}
			}
			title = titledata.split(",");
			data = new String[title.length][];
			int t = 0;
			if (!number1.equals("")) {
				if (number1.split(",").length >= 2) {
					data[t] = new String[number1.split(",").length];
					for (int y = 0; y < number1.split(",").length; y++) {
						data[t][y] = number1.split(",")[y];
					}
				} else {
					data[t] = new String[1];
					data[t][0] = number1;
				}
				t++;
			}
			if (!number2.equals("")) {
				if (number2.split(",").length >= 2) {
					data[t] = new String[number2.split(",").length];
					for (int y = 0; y < number2.split(",").length; y++) {
						data[t][y] = number2.split(",")[y];
					}
				} else {
					data[t] = new String[1];
					data[t][0] = number2;
				}
				t++;
			}
			if (!number3.equals("")) {
				if (number3.split(",").length >= 2) {
					data[t] = new String[number3.split(",").length];
					for (int y = 0; y < number3.split(",").length; y++) {
						data[t][y] = number3.split(",")[y];
					}
				} else {
					data[t] = new String[1];
					data[t][0] = number3;
				}
				t++;
			}
			if (!number4.equals("")) {
				if (number4.split(",").length >= 2) {
					data[t] = new String[number4.split(",").length];
					for (int y = 0; y < number4.split(",").length; y++) {
						data[t][y] = number4.split(",")[y];
					}
				} else {
					data[t] = new String[1];
					data[t][0] = number4;
				}
				t++;
			}
			if (!number5.equals("")) {
				if (number5.split(",").length >= 2) {
					data[t] = new String[number5.split(",").length];
					for (int y = 0; y < number5.split(",").length; y++) {
						data[t][y] = number5.split(",")[y];
					}
				} else {
					data[t] = new String[1];
					data[t][0] = number5;
				}
				t++;
			}
		} else if (xmname.equals("DC")) {
			if (dtzhi) {
				dialogs();
			}
			zpwgzpbh = "0344";
			// csszjcxdh = "1";
			title = title2;
			String zpStr[] = theinformation.getDtdpjyxm().split(",");
			data = new String[1][zpStr.length];
			for (int j = 0; j < zpStr.length; j++) {
				data[0][j] = PanDuan.Dtdpxm(zpStr[j]);
			}
		} else if (xmname.equals("C1")) {
			jcxdh = csszjcxdh;
			if (dpzhi) {
				dialogs();
			}
			zpwgzpbh = "0323";
			title = title3;
			String zpStr[] = theinformation.getDpjyxm().split(",");
			data = new String[1][zpStr.length];
			for (int j = 0; j < zpStr.length; j++) {
				data[0][j] = PanDuan.Dpjyxm(zpStr[j]);
			}
		}
	}

	// 当分组行背点击时，让分组呈现“选中／取消选中”状态。
	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		return false;
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
		return false;
	}

	// 重写返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			AlertDialog.Builder newbuilder = new AlertDialog.Builder(MaulDetActivitySpinner.this);
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
