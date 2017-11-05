package com.example.hsjgapp;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.hsjgapp.R;
import com.example.hsjgapp.R.id;
import com.example.hsjgapp.R.layout;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	private ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	private Context context;

	public MyAdapter(Context context, ArrayList<HashMap<String, Object>> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
		// TODO Auto-generated method stub
		View view = null;
		if (paramView == null) {
			view = LayoutInflater.from(context).inflate(R.layout.vehicle_information_item, null);
		} else {
			view = paramView;
		}
		TextView jylsh = (TextView) view.findViewById(R.id.jylsh);
		jylsh.setText(list.get(paramInt).get("jylsh").toString());
		TextView hphm = (TextView) view.findViewById(R.id.hphm);
		hphm.setText(list.get(paramInt).get("hphm").toString());
		TextView hpzl = (TextView) view.findViewById(R.id.hpzl);
		hpzl.setText(list.get(paramInt).get("hpzl").toString());
		TextView clsbdh = (TextView) view.findViewById(R.id.clsbdh);
		clsbdh.setText(list.get(paramInt).get("clsbdh").toString());
		TextView text_jylsh = (TextView) view.findViewById(R.id.text_jylsh);
		TextView text_clsbdh = (TextView) view.findViewById(R.id.text_clsbdh);
		if (list.get(paramInt).get("slzt").toString().equals("3") || list.get(paramInt).get("slzt").toString().equals("7")) {
			jylsh.setTextColor(Color.RED);
			hphm.setTextColor(Color.RED);
			hpzl.setTextColor(Color.RED);
			clsbdh.setTextColor(Color.RED);
			text_jylsh.setTextColor(Color.RED);
			text_clsbdh.setTextColor(Color.RED);
		}
		return view;
	}

}
