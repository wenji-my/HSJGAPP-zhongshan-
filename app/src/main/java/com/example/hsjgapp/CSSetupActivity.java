package com.example.hsjgapp;

import com.example.hsjgapp.R;
import com.example.hsjgapp.R.id;
import com.example.hsjgapp.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class CSSetupActivity extends Activity {
	private EditText ip;
	private EditText jgph;
	private Button cshxt;
	private Spinner jcxdh, tpscfs;
	private String[] jcxdhdatas = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15" };
	private String[] zpscfsdatas = { "存本地", "实时传" };
	private String info = "cs_setup";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.cs_setup);
		ip = (EditText) findViewById(R.id.ip);
		jgph = (EditText) findViewById(R.id.jgph);
		jcxdh = (Spinner) findViewById(R.id.jcxdh);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, jcxdhdatas);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		jcxdh.setAdapter(adapter);
		tpscfs = (Spinner) findViewById(R.id.tpscfs);
		ArrayAdapter<String> adapterthree = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, zpscfsdatas);
		adapterthree.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		tpscfs.setAdapter(adapterthree);

		SharedPreferences preferences = getSharedPreferences(info, 0);
		ip.setText(preferences.getString("IP", ""));
		jgph.setText(preferences.getString("JGPH", ""));
		int j = 0;
		if (preferences.getString("JCXDH", "").equals("")) {
			j = 0;
		} else {
			j = (Integer.valueOf(preferences.getString("JCXDH", "")).intValue()) - 1;
		}
		jcxdh.setSelection(j);
		tpscfs.setSelection(preferences.getInt("TPSCFS", 0));
		cshxt = (Button) findViewById(R.id.cshxt);
		cshxt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Save();
				Intent intent = new Intent(CSSetupActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	public void Save() {
		String ipdata = ip.getText().toString().trim();
		String jgphdata = jgph.getText().toString().trim();
		String jcxdhdata = jcxdh.getSelectedItem().toString().trim();
		String tpscfsdata = tpscfs.getSelectedItem().toString().trim();
		int tpsc = 0;
		if (!tpscfsdata.equals("存本地")) {
			tpsc = 1;
		}
		SharedPreferences preferences = getSharedPreferences(info, 0);
		Editor editor = preferences.edit();
		editor.putString("IP", ipdata);
		editor.putString("JGPH", jgphdata);
		editor.putString("JKXLH", "A");
		editor.putString("JCXDH", jcxdhdata);
		editor.putInt("TPSCFS", tpsc);
		editor.commit();
	}
}
