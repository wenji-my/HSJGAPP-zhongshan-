package com.example.hsjgapp;

import java.io.Serializable;
import java.util.List;

import com.example.hsjgapp.R;
import com.example.hsjgapp.R.layout;
import com.example.hsjgapp.domain.Q32Domain;
import com.example.hsjgapp.domain.Q09Domain;
import com.example.hsjgapp.domain.Q13Domain;
import com.example.hsjgapp.domain.Q11Domain;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.GestureDetector;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class TabHostOne extends TabActivity {
	TabHost host;
	int i = 0;
	private boolean wgdataxz = false;// 外检检验方式
	private boolean tab_wgqx, tab_pzqx;
	private String tab_jyyjy;// 检验员建议
	private List<Q32Domain> tab_bhgxxxs;// 外检不合格信息全部信息
	private List<String> tab_clbhexx;// 车辆不合格信息
	private Q11Domain tab_theinformation;// 车辆检验基本信息
	private List<Q09Domain> tab_operatorlists;// 登录用户信息
	private List<Q13Domain> tab_photonamelist;// 实时照片总类信息

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.tabhost_one);
		SharedPreferences share = getSharedPreferences("jbsz", 1);
		wgdataxz = share.getBoolean("DATAXZ", false);
		// 获取传入的数据
		tab_jyyjy = getIntent().getStringExtra("JY");
		tab_wgqx = getIntent().getBooleanExtra("WJQX", false);
		tab_pzqx = getIntent().getBooleanExtra("PZQX", false);
		Bundle bundle = TabHostOne.this.getIntent().getExtras();
		tab_bhgxxxs = (List<Q32Domain>) bundle.getSerializable("thenewbhgxxxsObj");
		tab_clbhexx = (List<String>) bundle.getSerializable("thenewVehicleBuHeGesObj");
		tab_theinformation = (Q11Domain) bundle.getSerializable("informationsObj");
		tab_operatorlists = (List<Q09Domain>) bundle.getSerializable("theoperatorlistsObj");
		tab_photonamelist = (List<Q13Domain>) bundle.getSerializable("photonamelistObj");
		host = getTabHost();
		Intent intent1 = new Intent();
		Bundle bundle1 = new Bundle();
		if (!wgdataxz) {
			intent1.setClass(TabHostOne.this, MaulDetActivitySpinner.class);
			intent1.putExtra("JY", tab_jyyjy);
			bundle1.putSerializable("thenewbhgxxxsObj", (Serializable) tab_bhgxxxs);
			bundle1.putSerializable("thenewVehicleBuHeGesObj", (Serializable) tab_clbhexx);
		} else {
			intent1.setClass(TabHostOne.this, MaulDetActivityEditText.class);
		}
		intent1.putExtra("XM", "F1");
		intent1.putExtra("WJQX", tab_wgqx);
		bundle1.putSerializable("informationsObj", tab_theinformation);
		bundle1.putSerializable("theoperatorlistsObj", (Serializable) tab_operatorlists);
		intent1.putExtras(bundle1);
		TabSpec tab1 = host.newTabSpec("tab1").setIndicator("外检检验").setContent(intent1);
		host.addTab(tab1);

		Intent intent2 = new Intent(TabHostOne.this, AppearancePhoto.class);
		Bundle bundle2 = new Bundle();
		intent2.putExtra("LP", "ZC");
		intent2.putExtra("PZQX", tab_pzqx);
		bundle2.putSerializable("informationsObj", tab_theinformation);
		bundle2.putSerializable("theoperatorlistsObj", (Serializable) tab_operatorlists);
		bundle2.putSerializable("photonamelistObj", (Serializable) tab_photonamelist);
		intent2.putExtras(bundle2);
		TabSpec tab2 = host.newTabSpec("tab2").setIndicator("照片拍照").setContent(intent2);
		host.addTab(tab2);
	}

	// 滑屏翻页函数
	private GestureDetector detector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
		@Override
		public boolean onFling(android.view.MotionEvent e1, android.view.MotionEvent e2, float velocityX, float velocityY) {
			if ((e2.getRawX() - e1.getRawX()) > 30) {
				showNext();
				return true;
			}
			if ((e1.getRawX() - e2.getRawX()) > 30) {
				showPre();
				return true;
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}
	});

	public boolean onTouchEvent(android.view.MotionEvent event) {
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	// 下一页
	protected void showNext() {
		host.setCurrentTab(i = i == 1 ? i = 0 : ++i);
	}

	// 上一页
	protected void showPre() {
		host.setCurrentTab(i = i == 0 ? i = 1 : --i);
	}

}
