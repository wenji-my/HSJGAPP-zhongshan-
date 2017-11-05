package com.example.hsjgapp;

import java.util.HashMap;
import java.util.List;

import com.example.hsjgapp.R;
import com.example.hsjgapp.R.id;
import com.example.hsjgapp.R.layout;
import com.example.hsjgapp.ToolUtil.PanDuan;
import com.example.hsjgapp.domain.Q32Domain;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MaulItemAdapterSpinner extends BaseExpandableListAdapter {

	class ExpandableListHolder {
		TextView tvName;
		TextView tvPhone;
		Spinner chkSpinner;
		Button editbhexx;
		LinearLayout linearLayout;
	}

	private Context context;
	private LayoutInflater mChildInflater; // 用于加载listitem的布局xml
	private LayoutInflater mGroupInflater; // 用于加载group的布局xml
	private List<MaulItemGroup> groups; // 所有group
	private String[][] checklist = null;
	static HashMap<String, String> map = null;
	static HashMap<String, String> bhgxxmap = null;
	static HashMap<String, String> clbhexmap = null;
	private Handler handler;
	private String[] sfhe = { "未检", "合格", "不合格" };
	private String number5;
	private String string;
	private ArrayAdapter<String> adapter;
	private List<Q32Domain> buhegexingxx;
	private List<String> vehiclebuheges;

	public MaulItemAdapterSpinner(Context c, List<MaulItemGroup> groups, String number5, String string, Handler hander,
			List<Q32Domain> buhegexingxx, List<String> vehiclebuheges) {
		context = c;
		mChildInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mGroupInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.buhegexingxx = buhegexingxx;
		this.vehiclebuheges = vehiclebuheges;
		this.groups = groups;
		this.handler = hander;
		this.number5 = number5;
		this.string = string;
		// 计算groups的最长长度
		int gcd = 0;
		for (int e = 0; e < groups.size(); e++) {
			if (gcd < groups.get(e).children.size()) {
				gcd = groups.get(e).children.size();
			}
		}
		Log.i("AAA", "gcd:" + gcd);
		checklist = new String[groups.size()][gcd];
		map = new HashMap<String, String>();
		bhgxxmap = new HashMap<String, String>();
		for (int i = 0; i < groups.size(); i++) {
			for (int j = 0; j < gcd; j++) {
				if (string.equals("F1")) {
					if (!(i == (groups.size() - 1))) {
						checklist[i][j] = "1";
					} else {
						checklist[i][j] = "0";
					}
				} else {
					checklist[i][j] = "1";
				}
			}
		}
		// 不合格项内容归类
		if (vehiclebuheges != null && vehiclebuheges.size() != 0) {
			for (int k = 0; k < vehiclebuheges.size(); k++) {
				String szdatas = vehiclebuheges.get(k).split("\\$")[0];
				if (string.equals("F1")) {
					bhgxxmap.put(PanDuan.twoWgjcxm(szdatas), vehiclebuheges.get(k));
					map.put(PanDuan.twoWgjcxm(szdatas), "2");
				} else if (string.equals("DC")) {
					bhgxxmap.put(PanDuan.Dtdpxm(szdatas), vehiclebuheges.get(k));
					map.put(PanDuan.Dtdpxm(szdatas), "2");
				} else {
					bhgxxmap.put(PanDuan.Dpjyxm(szdatas), vehiclebuheges.get(k));
					map.put(PanDuan.Dpjyxm(szdatas), "2");
				}
			}
		}
		// 返回的不合格数据进行分类
		clbhexmap = new HashMap<String, String>();
		for (int t = 0; t < buhegexingxx.size(); t++) {
			String bhgxs = buhegexingxx.get(t).getItem().split("\\$")[0];
			if (clbhexmap.get(bhgxs) == null) {
				clbhexmap.put(bhgxs, buhegexingxx.get(t).getItem());
			} else {
				String shuju = clbhexmap.get(bhgxs) + "#" + buhegexingxx.get(t).getItem();
				clbhexmap.put(bhgxs, shuju);
			}
			Log.i("AAA", "data" + bhgxs + ":" + clbhexmap.get(bhgxs));
		}

		adapter = new ArrayAdapter<String>(context, R.layout.spinner_adapter, sfhe);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	private String[] setxlk(String datas) {
		String[] list2 = clbhexmap.get(datas).split("#");
		String[] datalist = new String[list2.length];
		for (int y = 0; y < list2.length; y++) {
			datalist[y] = list2[y].split("\\$")[2];
			Log.i("AAA", "datalist[y]:" + datalist[y]);
		}
		return datalist;
	}

	@Override
	public Object getChild(int arg0, int arg1) {// 根据组索引和item索引，取得listitem
		return groups.get(arg0).children.get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {// 返回item索引
		return arg1;
	}

	@Override
	public int getChildrenCount(int groupPosition) {// 根据组索引返回分组的子item数
		return groups.get(groupPosition).children.size();
	}

	@Override
	public Object getGroup(int groupPosition) {// 根据组索引返回组
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {// 返回分组数
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {// 返回分组索引
		return groupPosition;
	}

	@Override
	public View getGroupView(int position, boolean isExpanded, View view, ViewGroup parent) {
		ExpandableListHolder holder = null;
		if (view == null) {
			view = mGroupInflater.inflate(R.layout.maul_list_item, null);
			// 下面主要是取得组的各子视图，设置子视图的属性。用tag来保存各子视图的引用
			holder = new ExpandableListHolder();
			holder.linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
			holder.linearLayout.setBackgroundColor(Color.YELLOW);
			holder.linearLayout.setPadding(0, 10, 0, 10);
			holder.tvName = (TextView) view.findViewById(R.id.textViewTittle);
			holder.tvName.setTextSize((float) 22);
			view.setTag(holder);
		} else { // 若view不为空，直接从view的tag属性中获得各子视图的引用
			holder = (ExpandableListHolder) view.getTag();
		}
		// 对应于组索引的组数据（模型）
		MaulItemGroup info = this.groups.get(position);
		if (info != null) {
			holder.tvName.setText(info.title);
		}
		return view;
	}

	// 行渲染方法
	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		ExpandableListHolder holder2 = null; // 清空临时变量
		if (convertView == null) {
			convertView = mChildInflater.inflate(R.layout.maul_list_childitem_spinnerr, null);
			holder2 = new ExpandableListHolder();
			holder2.tvName = (TextView) convertView.findViewById(R.id.textViewTittle2);
			holder2.chkSpinner = (Spinner) convertView.findViewById(R.id.ishgxz);
			holder2.editbhexx = (Button) convertView.findViewById(R.id.bhgxx);
			convertView.setTag(holder2);
		} else { // 若行已初始化，直接从tag属性获得子视图的引用
			holder2 = (ExpandableListHolder) convertView.getTag();
		}
		// 获得行数据（模型）
		MaulListItem info = this.groups.get(groupPosition).children.get(childPosition);

		if (info != null) {
			if (map.get(info.name) != null && map.get(info.name).equals("2")) {
				checklist[groupPosition][childPosition] = "2";
			}
			holder2.tvName.setText(info.name);
			holder2.chkSpinner.setAdapter(adapter);
			holder2.chkSpinner.setSelection(Integer.valueOf(checklist[groupPosition][childPosition]).intValue());
			if (checklist[groupPosition][childPosition].equals("2")) {
				String bghsmnr = bhgxxmap.get(info.name);
				if (bghsmnr != null) {
					holder2.editbhexx.setText(bghsmnr.split("\\$")[2]);
				}
				holder2.editbhexx.setVisibility(View.VISIBLE);
			} else {
				holder2.editbhexx.setVisibility(View.GONE);
			}
			final String name = holder2.tvName.getText().toString().trim();
			holder2.chkSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					// TODO Auto-generated method stub
					boolean isyy = true;
					if (map.get(name) == null) {
						map.put(name, checklist[groupPosition][childPosition]);
					}
					if (!map.get(name).equals((arg2 + ""))) {
						isyy = false;
					}
					checklist[groupPosition][childPosition] = (arg2 + "");
					if (arg2 == 0) {
						map.put(name, "0");
					} else if (arg2 == 1) {
						map.put(name, "1");
					} else {
						map.put(name, "2");
					}
					if (!isyy) {
						if (bhgxxmap.get(name) == null) {
							bhgxxmap.put(name, clbhexmap.get(PanDuan.wzZWsz(name)).split("#")[0]);
						}
						handler.sendEmptyMessage(10058);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
				}
			});
			holder2.editbhexx.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle(name + ":");
					builder.setItems(setxlk(PanDuan.wzZWsz(name)), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							bhgxxmap.put(name, clbhexmap.get(PanDuan.wzZWsz(name)).split("#")[which]);
							handler.sendEmptyMessage(10058);
						}
					});
					builder.show();
				}
			});

		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {// 行是否具有唯一id
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {// 行是否可选
		return true;
	}
}
