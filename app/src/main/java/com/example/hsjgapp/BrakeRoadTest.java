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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class BrakeRoadTest extends Activity {
	private ProgressDialog builder2 = null;
	private Button road_btnBeginDet;
	private Button road_btnSubmit;
	private Button road_btnVedio;
	private Button lslxks, zplxks, cslxks, lslxjs, zplxjs, cslxjs;
	private LinearLayout lxxz;
	private boolean lxpzan = true;
	private LinearLayout brakelayout, R1, R2, R3;
	private EditText zdcsd, zdxtsj, xckzzdjl, xcmzzdjl, xckzmfdd, xcmzmfdd, xczdczlz, yjzdcsd, yjkzzdjl, yjkzmfdd, yjmzzdjl,
			yjmzmfdd, yjzdczlz, csdscz;
	private Spinner zdwdx, lszdpd, yjzdczlfs, yjzdpd, zcpd, csbpd, lszczdpd, lsjg;
	private Q11Domain theinformation;
	private List<Q09Domain> operatorlists;
	private String ip;
	private String jkph;
	private String jkxlh;
	private String jcxdh = "1";
	private String r1xdh = "1";
	private String c2jcxdh = "1";
	private boolean zptdr1 = false;
	private boolean zptdr2 = false;
	private boolean zptdr3 = false;
	private String[] namedata = StaticValues.startc55;
	private String[] brakename = StaticValues.submittedc54;
	private String[] namedata2 = StaticValues.endc58;
	private String[] cfpzname = StaticValues.snapj31;
	private String[] vedioname = StaticValues.videoj11j12;
	protected static final int MSG_BRAKE_FAIL = 10091;
	protected static final int MSG_BRAKE_SEEUESS = 10092;
	private static final int MSG_BRAKE_SHOW = 10094;
	private static final int MSG_BRAKE_DISMISS = 10095;
	private static final int MSG_BRAKE_CHAOSHI = 10096;
	private static final int MSG_BRAKE_ZIDINGYI = 10097;
	private static final int MSG_BRAKE_AULSKS = 100981;
	private static final int MSG_BRAKE_AUZPKS = 100982;
	private static final int MSG_BRAKE_AUCSKS = 100983;
	private static final int MSG_BRAKE_AULSJS = 100984;
	private static final int MSG_BRAKE_AUZPJS = 100985;
	private static final int MSG_BRAKE_AUCSJS = 100986;
	private String[] zdwdxfdata = { "", "未跑偏", "左跑偏", "右跑偏" };
	private String[] hege1 = { "未检", "合格", "不合格" };
	private String[] hege2 = { "", "未检", "合格", "不合格" };
	private String[] yjzdczlfsdata = { "", "手操纵", "脚操纵" };
	private String[] zcpddata = { "", "20%", "15%" };
	private Message msg;
	private Handler hander = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MSG_BRAKE_SEEUESS) {
				road_btnBeginDet.setEnabled(false);
				road_btnSubmit.setEnabled(true);
				road_btnVedio.setEnabled(true);
			}
			if (msg.what == MSG_BRAKE_FAIL) {
				DialogTool.AlertDialogShow(BrakeRoadTest.this, msg.obj.toString());
			}
			if (msg.what == MSG_BRAKE_SHOW) {
				builder2 = new ProgressDialog(BrakeRoadTest.this);
				builder2.setMessage("正在加载中，请稍等。。。");
				builder2.setCancelable(false);
				builder2.show();
			}
			if (msg.what == MSG_BRAKE_DISMISS) {
				builder2.dismiss();
			}
			if (msg.what == MSG_BRAKE_CHAOSHI) {
				DialogTool.AlertDialogShow(BrakeRoadTest.this, "请求超时，请检查网络环境!");
			}
			if (msg.what == MSG_BRAKE_AULSKS) {
				lslxks.setEnabled(false);
			}
			if (msg.what == MSG_BRAKE_AUZPKS) {
				zplxks.setEnabled(false);
			}
			if (msg.what == MSG_BRAKE_AUCSKS) {
				cslxks.setEnabled(false);
			}
			if (msg.what == MSG_BRAKE_AULSJS) {
				lslxjs.setEnabled(false);
			}
			if (msg.what == MSG_BRAKE_AUZPJS) {
				zplxjs.setEnabled(false);
			}
			if (msg.what == MSG_BRAKE_AUCSJS) {
				cslxjs.setEnabled(false);
			}
			if (msg.what == MSG_BRAKE_ZIDINGYI) {
				AlertDialog.Builder builder1 = new AlertDialog.Builder(BrakeRoadTest.this);
				builder1.setTitle("提示框");
				builder1.setMessage(msg.obj.toString());
				builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						BrakeRoadTest.this.finish();
					}
				});
				builder1.show();
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
		setContentView(R.layout.brake_road_test);
		chushihua();
		SharedPreferences preferences = getSharedPreferences("cs_setup", 0);
		ip = preferences.getString("IP", "");
		jkph = preferences.getString("JGPH", "");
		jkxlh = preferences.getString("JKXLH", "");
		Bundle bundle = this.getIntent().getExtras();
		theinformation = (Q11Domain) bundle.getSerializable("informationsObj");
		operatorlists = (List<Q09Domain>) bundle.getSerializable("theoperatorlistsObj");
		zdwdx.setAdapter(SetSpinner(zdwdxfdata));
		lszdpd.setAdapter(SetSpinner(hege2));
		lszdpd.setSelection(1);
		yjzdczlfs.setAdapter(SetSpinner(yjzdczlfsdata));
		yjzdpd.setAdapter(SetSpinner(hege2));
		yjzdpd.setSelection(1);
		zcpd.setAdapter(SetSpinner(zcpddata));
		zcpd.setSelection(1);
		csbpd.setAdapter(SetSpinner(hege2));
		csbpd.setSelection(1);
		lszczdpd.setAdapter(SetSpinner(hege2));
		lszczdpd.setSelection(1);
		lsjg.setAdapter(SetSpinner(hege1));
		Log.i("AAA", theinformation.getJyxm());
		String zpStr[] = theinformation.getJyxm().split(",");
		String Rdata = "";
		for (int k = 0; k < zpStr.length; k++) {
			if (zpStr[k].equals("R1")) {
				zptdr1 = true;
				lslxks.setEnabled(true);
				lslxjs.setEnabled(true);
				Rdata = "R1";
				R1.setVisibility(View.VISIBLE);
			} else if (zpStr[k].equals("R2")) {
				zptdr2 = true;
				zplxks.setEnabled(true);
				zplxjs.setEnabled(true);
				if (Rdata.equals("")) {
					Rdata = "R2";
				} else {
					Rdata = Rdata + ",R2";
				}
				R2.setVisibility(View.VISIBLE);
			} else if (zpStr[k].equals("R3")) {
				zptdr3 = true;
				cslxks.setEnabled(true);
				cslxjs.setEnabled(true);
				if (Rdata.equals("")) {
					Rdata = "R3";
				} else {
					Rdata = Rdata + ",R3";
				}
				R3.setVisibility(View.VISIBLE);
			}
		}
		Log.i("AAA", Rdata);

		if (zptdr2) {
			dialogs();
		}
		if (zptdr1) {
			dialogls();
		}
	}

	public ArrayAdapter<String> SetSpinner(String[] spidata) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spidata);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}

	public void doClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.road_btnBeginDet:
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						hander.sendEmptyMessage(MSG_BRAKE_SHOW);
						List<String> datalist = new ArrayList<String>();
						datalist.add("");
						datalist.add("R");
						datalist.add(theinformation.getJycs() + "");
						datalist.add(TimeTool.getTiem());
						datalist.add(theinformation.getHpzl());
						datalist.add(theinformation.getHphm());
						datalist.add(theinformation.getJylsh());
						datalist.add(jcxdh);
						datalist.add(theinformation.getClsbdh());
						datalist.add(jkph);
						String xmldata = UnXmlTool.getWriteXML(namedata, datalist);
						Log.i("BBB", "xmldata:" + xmldata);
						String startdata = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh, "18C55",
								xmldata, "writeObjectOutResult", StaticValues.timeoutNine, StaticValues.numberOne);
						List<Code> codelists = XMLParsingMethods.saxcode(startdata);
						hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
						if (codelists.get(0).getCode().equals("1")) {
							hander.sendEmptyMessage(MSG_BRAKE_SEEUESS);
							msg = new Message();
							msg.what = MSG_BRAKE_FAIL;
							msg.obj = codelists.get(0).getMessage();
							hander.sendMessage(msg);
						} else {
							msg = new Message();
							msg.what = MSG_BRAKE_FAIL;
							msg.obj = codelists.get(0).getMessage();
							hander.sendMessage(msg);
						}
					} catch (Exception e) {
						hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
						hander.sendEmptyMessage(MSG_BRAKE_SEEUESS);
						e.printStackTrace();
						return;
					}
				}
			}).start();
			break;
		case R.id.road_btnSubmit:
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						if (lsJudge()) {
							hander.sendEmptyMessage(MSG_BRAKE_SHOW);
							String roaddata = UnXmlTool.getWriteXML(brakename, getroaddata());
							Log.i("BBB", "roaddata:" + roaddata);
							String enddata = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh, "18C54",
									roaddata, "writeObjectOutResult", StaticValues.timeoutFive, StaticValues.numberFour);
							List<Code> endlists = XMLParsingMethods.saxcode(enddata);
							hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
							if (endlists.get(0).getCode().equals("1")) {
								msg = new Message();
								msg.what = MSG_BRAKE_ZIDINGYI;
								msg.obj = endlists.get(0).getMessage();
								hander.sendMessage(msg);
							} else {
								msg = new Message();
								msg.what = MSG_BRAKE_ZIDINGYI;
								msg.obj = endlists.get(0).getMessage();
								hander.sendMessage(msg);
							}
							// 调结束18C58
							hander.sendEmptyMessage(MSG_BRAKE_SHOW);
							List<String> datalist = new ArrayList<String>();
							datalist.add(theinformation.getJylsh());
							datalist.add(jkph);
							datalist.add(jcxdh);
							datalist.add(theinformation.getJycs() + "");
							datalist.add("R");
							datalist.add(theinformation.getHpzl());
							datalist.add(theinformation.getHphm());
							datalist.add(theinformation.getClsbdh());
							datalist.add("");
							datalist.add(TimeTool.getTiem());
							String xmldata2 = UnXmlTool.getWriteXML(namedata2, datalist);
							Log.i("BBB", "xmldata:" + xmldata2);
							String jieshudata = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh, "18C58",
									xmldata2, "writeObjectOutResult", StaticValues.timeoutNine, StaticValues.numberOne);
							List<Code> jieshulists = XMLParsingMethods.saxcode(jieshudata);
							hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
							msg = new Message();
							msg.what = MSG_BRAKE_FAIL;
							msg.obj = jieshulists.get(0).getMessage();
							hander.sendMessage(msg);
						}
					} catch (Exception e) {
						hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
						hander.sendEmptyMessage(MSG_BRAKE_CHAOSHI);
						e.printStackTrace();
						return;
					}
				}
			}).start();
			break;
		case R.id.road_btnVedio:
			if (lxpzan) {
				lxxz.setVisibility(View.VISIBLE);
				lxpzan = false;
			} else {
				lxxz.setVisibility(View.GONE);
				lxpzan = true;
			}
			break;
		case R.id.lslxks:
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					// 调R1开始时触发拍照1
					try {
						hander.sendEmptyMessage(MSG_BRAKE_SHOW);
						List<Code> r1ksdatas = tdzphs("R1", r1xdh, theinformation, "0341");
						hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
						if (r1ksdatas.get(0).getCode().equals("1")) {
							messegas(MSG_BRAKE_FAIL, "路试触发拍照成功，正在拍照！");
						} else {
							messegas(MSG_BRAKE_FAIL, r1ksdatas.get(0).getMessage());
						}
					} catch (Exception e) {
						hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
						hander.sendEmptyMessage(MSG_BRAKE_CHAOSHI);
						e.printStackTrace();
					}
					// 调R1开始时触发录像开始
					try {
						hander.sendEmptyMessage(MSG_BRAKE_SHOW);
						List<Code> r1kslx = tdlx(theinformation, "R1", r1xdh, "18J11");
						hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
						if (r1kslx.get(0).getCode().equals("1")) {
							messegas(MSG_BRAKE_FAIL, "路试触发录像成功，正在录像！");
							hander.sendEmptyMessage(MSG_BRAKE_AULSKS);
						} else {
							messegas(MSG_BRAKE_FAIL, r1kslx.get(0).getMessage());
						}
					} catch (Exception e) {
						hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
						hander.sendEmptyMessage(MSG_BRAKE_CHAOSHI);
						e.printStackTrace();
					}
				}
			}).start();
			break;
		case R.id.zplxks:
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					// 调R2触发拍照
					try {
						hander.sendEmptyMessage(MSG_BRAKE_SHOW);
						List<Code> r2datas = tdzphs("R2", c2jcxdh, theinformation, "0345");
						hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
						if (r2datas.get(0).getCode().equals("1")) {
							messegas(MSG_BRAKE_FAIL, "路试驻坡触发拍照成功，正在拍照！");
						} else {
							messegas(MSG_BRAKE_FAIL, r2datas.get(0).getMessage());
						}
					} catch (Exception e) {
						hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
						hander.sendEmptyMessage(MSG_BRAKE_CHAOSHI);
						e.printStackTrace();
					}
					// 调R2开始时触发录像开始
					try {
						hander.sendEmptyMessage(MSG_BRAKE_SHOW);
						List<Code> r2kslx = tdlx(theinformation, "R2", c2jcxdh, "18J11");
						hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
						if (r2kslx.get(0).getCode().equals("1")) {
							messegas(MSG_BRAKE_FAIL, "路试驻坡触发录像成功，正在录像！");
							hander.sendEmptyMessage(MSG_BRAKE_AUZPKS);
						} else {
							messegas(MSG_BRAKE_FAIL, r2kslx.get(0).getMessage());
						}
					} catch (Exception e) {
						hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
						hander.sendEmptyMessage(MSG_BRAKE_CHAOSHI);
						e.printStackTrace();
					}
				}
			}).start();
			break;
		case R.id.cslxks:
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					// 调R3触发拍照
					try {
						hander.sendEmptyMessage(MSG_BRAKE_SHOW);
						List<Code> r3datas = tdzphs("R3", jcxdh, theinformation, "0347");
						hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
						if (r3datas.get(0).getCode().equals("1")) {
							messegas(MSG_BRAKE_FAIL, "路试车速表触发拍照成功，正在拍照！");
						} else {
							messegas(MSG_BRAKE_FAIL, r3datas.get(0).getMessage());
						}
					} catch (Exception e) {
						hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
						hander.sendEmptyMessage(MSG_BRAKE_CHAOSHI);
						e.printStackTrace();
					}
					// 调R3开始时触发录像开始
					try {
						hander.sendEmptyMessage(MSG_BRAKE_SHOW);
						List<Code> r3kslx = tdlx(theinformation, "R3", jcxdh, "18J11");
						hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
						if (r3kslx.get(0).getCode().equals("1")) {
							messegas(MSG_BRAKE_FAIL, "路试车速表触发录像成功，正在录像！");
							hander.sendEmptyMessage(MSG_BRAKE_AUCSKS);
						} else {
							messegas(MSG_BRAKE_FAIL, r3kslx.get(0).getMessage());
						}
					} catch (Exception e) {
						hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
						hander.sendEmptyMessage(MSG_BRAKE_CHAOSHI);
						e.printStackTrace();
					}
				}
			}).start();
			break;
		case R.id.lslxjs:
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					// 调R1结束时触发录像结束
					try {
						hander.sendEmptyMessage(MSG_BRAKE_SHOW);
						List<Code> r1jslx = tdlx(theinformation, "R1", r1xdh, "18J12");
						hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
						if (r1jslx.get(0).getCode().equals("1")) {
							messegas(MSG_BRAKE_FAIL, "路试制动结束录像成功！");
						} else {
							messegas(MSG_BRAKE_FAIL, r1jslx.get(0).getMessage());
						}
					} catch (Exception e) {
						hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
						hander.sendEmptyMessage(MSG_BRAKE_CHAOSHI);
						e.printStackTrace();
					}
					// 调R1结束时触发拍照2
					try {
						hander.sendEmptyMessage(MSG_BRAKE_SHOW);
						List<Code> r1jsdatas = tdzphs("R1", r1xdh, theinformation, "0343");
						hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
						if (r1jsdatas.get(0).getCode().equals("1")) {
							messegas(MSG_BRAKE_FAIL, "路试触发拍照成功，正在拍照！");
							hander.sendEmptyMessage(MSG_BRAKE_AULSJS);
						} else {
							messegas(MSG_BRAKE_FAIL, r1jsdatas.get(0).getMessage());
						}
					} catch (Exception e) {
						hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
						hander.sendEmptyMessage(MSG_BRAKE_CHAOSHI);
						e.printStackTrace();
					}
				}
			}).start();
			break;
		case R.id.zplxjs:
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					// 调R2结束时触发录像结束
					try {
						hander.sendEmptyMessage(MSG_BRAKE_SHOW);
						List<Code> r2jslx = tdlx(theinformation, "R2", c2jcxdh, "18J12");
						hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
						if (r2jslx.get(0).getCode().equals("1")) {
							messegas(MSG_BRAKE_FAIL, "驻坡结束录像成功！");
							hander.sendEmptyMessage(MSG_BRAKE_AUZPJS);
						} else {
							messegas(MSG_BRAKE_FAIL, r2jslx.get(0).getMessage());
						}
					} catch (Exception e) {
						hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
						hander.sendEmptyMessage(MSG_BRAKE_CHAOSHI);
						e.printStackTrace();
					}
				}
			}).start();
			break;
		case R.id.cslxjs:
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					// 调R3结束时触发录像结束
					try {
						hander.sendEmptyMessage(MSG_BRAKE_SHOW);
						List<Code> r3jslx = tdlx(theinformation, "R3", jcxdh, "18J12");
						hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
						if (r3jslx.get(0).getCode().equals("1")) {
							messegas(MSG_BRAKE_FAIL, "车速表结束录像成功！");
							hander.sendEmptyMessage(MSG_BRAKE_AUCSJS);
						} else {
							messegas(MSG_BRAKE_FAIL, r3jslx.get(0).getMessage());
						}
					} catch (Exception e) {
						hander.sendEmptyMessage(MSG_BRAKE_DISMISS);
						hander.sendEmptyMessage(MSG_BRAKE_CHAOSHI);
						e.printStackTrace();
					}
				}
			}).start();
			break;
		}
	}

	// 路试对话框选通道调枪机
	private void dialogls() {
		AlertDialog.Builder builder3 = new AlertDialog.Builder(BrakeRoadTest.this);
		builder3.setTitle("请选择路试制动通道：");
		final String[] dxh = { "1", "2", "3", "4", "5" };
		builder3.setSingleChoiceItems(dxh, 0, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				r1xdh = dxh[which];
			}
		});
		builder3.setPositiveButton("确定", null);
		builder3.setCancelable(false);
		builder3.show();
	}

	private void dialogs() {
		AlertDialog.Builder builder3 = new AlertDialog.Builder(BrakeRoadTest.this);
		builder3.setTitle("请选择路试驻坡通道号：");
		final String[] dxh = { "通道1为20%", "通道2为15%" };
		builder3.setSingleChoiceItems(dxh, 0, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					c2jcxdh = "1";
				} else {
					c2jcxdh = "2";
				}
				int zcpdz = which + 1;
				zcpd.setSelection(zcpdz);
			}
		});
		builder3.setPositiveButton("确定", null);
		builder3.setCancelable(false);
		builder3.show();
	}

	private List<String> getroaddata() {
		// TODO Auto-generated method stub
		List<String> datalist2 = new ArrayList<String>();
		datalist2.add(theinformation.getJylsh());
		datalist2.add(jkph);
		datalist2.add(jcxdh);
		datalist2.add(theinformation.getJycs() + "");
		datalist2.add("R1,R2,R3");
		datalist2.add(theinformation.getHpzl());
		datalist2.add(theinformation.getHphm());
		datalist2.add(theinformation.getClsbdh());
		datalist2.add(operatorlists.get(0).getXm());
		datalist2.add(zdcsd.getText().toString().trim());
		datalist2.add(zdxtsj.getText().toString().trim());
		datalist2.add(getzdwdx(zdwdx.getSelectedItem().toString(), zdwdxfdata));
		datalist2.add(xckzzdjl.getText().toString().trim());
		datalist2.add(xcmzzdjl.getText().toString().trim());
		datalist2.add(xckzmfdd.getText().toString().trim());
		datalist2.add(xcmzmfdd.getText().toString().trim());
		datalist2.add(xczdczlz.getText().toString().trim());
		datalist2.add(getkekong(lszdpd.getSelectedItem().toString(), hege2));
		datalist2.add(yjzdcsd.getText().toString().trim());
		datalist2.add(yjkzzdjl.getText().toString().trim());
		datalist2.add(yjkzmfdd.getText().toString().trim());
		datalist2.add(yjmzzdjl.getText().toString().trim());
		datalist2.add(yjmzmfdd.getText().toString().trim());
		datalist2.add(getkekong(yjzdczlfs.getSelectedItem().toString(), yjzdczlfsdata));
		datalist2.add(yjzdczlz.getText().toString().trim());
		datalist2.add(getkekong(yjzdpd.getSelectedItem().toString(), hege2));
		datalist2.add(getkekong(zcpd.getSelectedItem().toString(), zcpddata));
		datalist2.add(getkekong(lszczdpd.getSelectedItem().toString(), hege2));
		datalist2.add(csdscz.getText().toString().trim());
		datalist2.add(getkekong(csbpd.getSelectedItem().toString(), hege2));
		datalist2.add(getfeikong(lsjg.getSelectedItem().toString(), hege1));
		return datalist2;
	}

	public void messegas(int msgnumber, String neirong) {
		msg = new Message();
		msg.what = MSG_BRAKE_FAIL;
		msg.obj = neirong;
		hander.sendMessage(msg);
	}

	// 通道抓拍
	public List<Code> tdzphs(String gw, String dxh, Q11Domain lists, String zpwgzpbh) {
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
		String cfpzdatas = UnXmlTool.getTimeXML(cfpzname, cfpzdata);
		Log.i("BBB", "cfpzdatas:" + cfpzdatas);
		String startcfpzdata = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh, "18J31", cfpzdatas,
				"writeObjectOutResult", StaticValues.timeoutFifteen, StaticValues.numberTwo);
		List<Code> cfpzlists = XMLParsingMethods.saxcode(startcfpzdata);
		return cfpzlists;
	}

	public List<Code> tdlx(Q11Domain information, String gwz, String tdh, String jkh) {
		List<String> vediolist = new ArrayList<String>();
		vediolist.add(information.getJylsh());
		vediolist.add(information.getHphm());
		vediolist.add(information.getHpzl());
		vediolist.add(information.getClsbdh());
		vediolist.add(gwz);
		vediolist.add(tdh);
		vediolist.add(information.getJycs() + "");
		String vediodata = UnXmlTool.getWriteXML(vedioname, vediolist);
		Log.i("BBB", "vediodata:" + vediodata);
		String vediodatareturn = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh, jkh, vediodata,
				"writeObjectOutResult", StaticValues.timeoutFifteen, StaticValues.numberTwo);
		List<Code> vediolists = XMLParsingMethods.saxcode(vediodatareturn);
		return vediolists;
	}

	// 行车制动稳定性值判断
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

	// 可空合格判断
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

	// 非空合格判断
	public String getfeikong(String zhidatas, String[] zhishuzu) {
		String zhi = null;
		for (int i = 0; i < zhishuzu.length; i++) {
			if (zhidatas.equals(zhishuzu[i])) {
				zhi = String.valueOf(i);
			}
		}
		return zhi;
	}

	private void chushihua() {
		// TODO Auto-generated method stub
		road_btnBeginDet = (Button) findViewById(R.id.road_btnBeginDet);
		road_btnSubmit = (Button) findViewById(R.id.road_btnSubmit);
		road_btnVedio = (Button) findViewById(R.id.road_btnVedio);
		lslxks = (Button) findViewById(R.id.lslxks);
		zplxks = (Button) findViewById(R.id.zplxks);
		cslxks = (Button) findViewById(R.id.cslxks);
		lslxjs = (Button) findViewById(R.id.lslxjs);
		zplxjs = (Button) findViewById(R.id.zplxjs);
		cslxjs = (Button) findViewById(R.id.cslxjs);
		lxxz = (LinearLayout) findViewById(R.id.lxxz);
		brakelayout = (LinearLayout) findViewById(R.id.brakelayout);
		R1 = (LinearLayout) findViewById(R.id.R1);
		R2 = (LinearLayout) findViewById(R.id.R2);
		R3 = (LinearLayout) findViewById(R.id.R3);
		zdcsd = (EditText) findViewById(R.id.zdcsd);
		zdxtsj = (EditText) findViewById(R.id.zdxtsj);
		zdwdx = (Spinner) findViewById(R.id.zdwdx);
		xckzzdjl = (EditText) findViewById(R.id.xckzzdjl);
		xcmzzdjl = (EditText) findViewById(R.id.xcmzzdjl);
		xckzmfdd = (EditText) findViewById(R.id.xckzmfdd);
		xcmzmfdd = (EditText) findViewById(R.id.xcmzmfdd);
		xczdczlz = (EditText) findViewById(R.id.xczdczlz);
		lszdpd = (Spinner) findViewById(R.id.lszdpd);
		yjzdcsd = (EditText) findViewById(R.id.yjzdcsd);
		yjkzzdjl = (EditText) findViewById(R.id.yjkzzdjl);
		yjkzmfdd = (EditText) findViewById(R.id.yjkzmfdd);
		yjmzzdjl = (EditText) findViewById(R.id.yjmzzdjl);
		yjmzmfdd = (EditText) findViewById(R.id.yjmzmfdd);
		yjzdczlfs = (Spinner) findViewById(R.id.yjzdczlfs);
		yjzdczlz = (EditText) findViewById(R.id.yjzdczlz);
		yjzdpd = (Spinner) findViewById(R.id.yjzdpd);
		zcpd = (Spinner) findViewById(R.id.zcpd);
		lszczdpd = (Spinner) findViewById(R.id.lszczdpd);
		csdscz = (EditText) findViewById(R.id.csdscz);
		csbpd = (Spinner) findViewById(R.id.csbpd);
		lsjg = (Spinner) findViewById(R.id.lsjg);
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
							messegas(MSG_BRAKE_FAIL, "应急满载MFDD保留小数点1位");
							return false;
						}
					} else {
						messegas(MSG_BRAKE_FAIL, "应急空载MFDD保留小数点1位");
						return false;
					}
				} else {
					messegas(MSG_BRAKE_FAIL, "行车满载MFDD保留小数点1位");
					return false;
				}
			} else {
				messegas(MSG_BRAKE_FAIL, "行车空载MFDD保留小数点1位");
				return false;
			}
		} else {
			messegas(MSG_BRAKE_FAIL, "行车制动协调时间保留小数点2位");
			return false;
		}
	}

	public boolean lsJudge() {
		if (zptdr1) {
			if (getkekong(yjzdpd.getSelectedItem().toString(), hege2).equals("0")
					&& getkekong(lszdpd.getSelectedItem().toString(), hege2).equals("0")) {
				messegas(MSG_BRAKE_FAIL, "应急路试制动判定或行车路试制动判定以上两个不能同时为空或未检！");
				return false;
			} else if (getkekong(yjzdpd.getSelectedItem().toString(), hege2).equals("")
					&& getkekong(lszdpd.getSelectedItem().toString(), hege2).equals("")) {
				messegas(MSG_BRAKE_FAIL, "应急路试制动判定或行车路试制动判定以上两个不能同时为空或未检！");
				return false;
			} else if (getkekong(yjzdpd.getSelectedItem().toString(), hege2).equals("0")
					&& getkekong(lszdpd.getSelectedItem().toString(), hege2).equals("")) {
				messegas(MSG_BRAKE_FAIL, "应急路试制动判定或行车路试制动判定以上两个不能同时为空或未检！");
				return false;
			} else if (getkekong(yjzdpd.getSelectedItem().toString(), hege2).equals("")
					&& getkekong(lszdpd.getSelectedItem().toString(), hege2).equals("0")) {
				messegas(MSG_BRAKE_FAIL, "应急路试制动判定或行车路试制动判定以上两个不能同时为空或未检！");
				return false;
			} else if (!Judge()) {
				return false;
			}
		}
		if (zptdr2) {
			if (getkekong(lszczdpd.getSelectedItem().toString(), hege2).equals("")
					|| getkekong(lszczdpd.getSelectedItem().toString(), hege2).equals("0")) {
				messegas(MSG_BRAKE_FAIL, "路试驻车制动判定不能为空或未检！");
				return false;
			}
		}
		if (zptdr3) {
			if (getkekong(csbpd.getSelectedItem().toString(), hege2).equals("")
					|| getkekong(csbpd.getSelectedItem().toString(), hege2).equals("0")) {
				messegas(MSG_BRAKE_FAIL, "车速表判定不能为空或未检！");
				return false;
			}
		}
		if (getfeikong(lsjg.getSelectedItem().toString(), hege1).equals("0")) {
			messegas(MSG_BRAKE_FAIL, "路试结果不能为未检！");
			return false;
		}
		return true;
	}

	// 重写返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			AlertDialog.Builder newbuilder = new AlertDialog.Builder(BrakeRoadTest.this);
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
