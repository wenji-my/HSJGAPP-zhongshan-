package com.example.hsjgapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class BasicSetup extends Activity {
	private RadioGroup WJType, DTType, DPType, DATAType, LSType, XJType, PhotoType, TimeType;
	private RadioButton wjtck, wjbt, dttck, dtbt, dptck, dpbt, datatck, databt, lstck, lsbt, xjs, xjf, Photos, Photof, times,
			timef;
	private Button buttonSettingOK;
	private boolean wjzhi = true;// 外观
	private boolean dtzhi = true;// 动态底盘
	private boolean dpzhi = true;// 底盘
	private boolean sfxh = true;// 拍摄设置
	private boolean wgdataxz = false;// 数据上传方式
	private boolean lsczzhi = true;// 路试操作方式
	private boolean photozhi = false;// 照片上传方式
	private boolean timezhi = false;// 时间调控
	private String info = "jbsz";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.basic_setup);
		// 初始化控件
		chushihua();
		// 控件辅值
		chushifuzhi();
		// 控件发生改变事件
		WJType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				int radioButtonId = arg0.getCheckedRadioButtonId();
				if (radioButtonId == R.id.wjtck) {
					wjzhi = true;
				} else {
					wjzhi = false;
				}
			}
		});
		DTType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				int radioButtonId = arg0.getCheckedRadioButtonId();
				if (radioButtonId == R.id.dttck) {
					dtzhi = true;
				} else {
					dtzhi = false;
				}
			}
		});
		DPType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				int radioButtonId = arg0.getCheckedRadioButtonId();
				if (radioButtonId == R.id.dptck) {
					dpzhi = true;
				} else {
					dpzhi = false;
				}
			}
		});
		DATAType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				int radioButtonId = arg0.getCheckedRadioButtonId();
				if (radioButtonId == R.id.datatck) {
					wgdataxz = true;
				} else {
					wgdataxz = false;
				}
			}
		});
		LSType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				int radioButtonId = arg0.getCheckedRadioButtonId();
				if (radioButtonId == R.id.lstck) {
					lsczzhi = true;
				} else {
					lsczzhi = false;
				}
			}
		});
		XJType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				int radioButtonId = arg0.getCheckedRadioButtonId();
				if (radioButtonId == R.id.xjs) {
					sfxh = true;
				} else {
					sfxh = false;
				}
			}
		});
		PhotoType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				int radioButtonId = arg0.getCheckedRadioButtonId();
				if (radioButtonId == R.id.Photos) {
					photozhi = true;
				} else {
					photozhi = false;
				}
			}
		});
		TimeType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				int radioButtonId = arg0.getCheckedRadioButtonId();
				if (radioButtonId == R.id.Times) {
					timezhi = true;
				} else {
					timezhi = false;
				}
			}
		});
		// 保存
		buttonSettingOK.setOnClickListener(new OnClickListener() {
			@SuppressLint("CommitPrefEdits")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SharedPreferences share = getSharedPreferences(info, 1);
				Editor editor = share.edit();
				editor.putBoolean("WJXZ", wjzhi);
				editor.putBoolean("DTXZ", dtzhi);
				editor.putBoolean("DPXZ", dpzhi);
				editor.putBoolean("DATAXZ", wgdataxz);
				editor.putBoolean("LSXZ", lsczzhi);
				editor.putBoolean("SFXH", sfxh);
				editor.putBoolean("PHOTOZ", photozhi);
				editor.putBoolean("TIMETK", timezhi);
				editor.commit();
				BasicSetup.this.finish();
			}
		});
	}

	private void chushifuzhi() {
		SharedPreferences share = getSharedPreferences(info, 1);
		wjzhi = share.getBoolean("WJXZ", true);
		dtzhi = share.getBoolean("DTXZ", true);
		dpzhi = share.getBoolean("DPXZ", true);
		wgdataxz = share.getBoolean("DATAXZ", false);
		lsczzhi = share.getBoolean("LSXZ", true);
		sfxh = share.getBoolean("SFXH", true);
		photozhi = share.getBoolean("PHOTOZ", false);
		timezhi = share.getBoolean("TIMETK", false);
		RadioButton rb = null;
		if (wjzhi) {
			rb = (RadioButton) findViewById(R.id.wjtck);
		} else {
			rb = (RadioButton) findViewById(R.id.wjbt);
		}
		rb.setChecked(true);
		if (dtzhi) {
			rb = (RadioButton) findViewById(R.id.dttck);
		} else {
			rb = (RadioButton) findViewById(R.id.dtbt);
		}
		rb.setChecked(true);
		if (dpzhi) {
			rb = (RadioButton) findViewById(R.id.dptck);
		} else {
			rb = (RadioButton) findViewById(R.id.dpbt);
		}
		rb.setChecked(true);
		if (wgdataxz) {
			rb = (RadioButton) findViewById(R.id.datatck);
		} else {
			rb = (RadioButton) findViewById(R.id.databt);
		}
		rb.setChecked(true);
		if (lsczzhi) {
			rb = (RadioButton) findViewById(R.id.lstck);
		} else {
			rb = (RadioButton) findViewById(R.id.lsbt);
		}
		rb.setChecked(true);
		if (sfxh) {
			rb = (RadioButton) findViewById(R.id.xjs);
		} else {
			rb = (RadioButton) findViewById(R.id.xjf);
		}
		rb.setChecked(true);
		if (photozhi) {
			rb = (RadioButton) findViewById(R.id.Photos);
		} else {
			rb = (RadioButton) findViewById(R.id.Photof);
		}
		rb.setChecked(true);
		if (timezhi) {
			rb = (RadioButton) findViewById(R.id.Times);
		} else {
			rb = (RadioButton) findViewById(R.id.Timef);
		}
		rb.setChecked(true);
	}

	private void chushihua() {
		WJType = (RadioGroup) findViewById(R.id.WJType);
		DTType = (RadioGroup) findViewById(R.id.DTType);
		DPType = (RadioGroup) findViewById(R.id.DPType);
		DATAType = (RadioGroup) findViewById(R.id.DATAType);
		LSType = (RadioGroup) findViewById(R.id.LSType);
		XJType = (RadioGroup) findViewById(R.id.XJType);
		PhotoType = (RadioGroup) findViewById(R.id.PhotoType);
		TimeType = (RadioGroup) findViewById(R.id.TimeType);
		buttonSettingOK = (Button) findViewById(R.id.buttonSettingOK);
	}

}
