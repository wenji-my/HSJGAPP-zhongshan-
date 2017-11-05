package com.example.hsjgapp.SQL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.hsjgapp.ToolUtil.TimeTool;

import android.content.Context;
import android.util.Log;

public class SQLFuntion {

	static DBHelper dbManager;

	public static void initTable(Context context) {
		dbManager = new DBHelper(context);
		dbManager.getReadableDatabase();
		// dbManager.getWritableDatabase();
	}

	public static void insert(Context context, Object[] data) {
		dbManager = new DBHelper(context);
		dbManager.getWritableDatabase();
		String sql = "insert into vehicle(jycs,pssj,hpzl,hphm,zpzl,zpid,jylsh,jcxdh,clsbdh,jyjgbh,isfuo) values(?,?,?,?,?,?,?,?,?,?,?)";
		Object[] bindArgs = data;
		DBManager manager = new DBManager(context);
		manager.updateBySQL(sql, bindArgs);
	}

	public static void update(Context context, Object[] data) {
		dbManager = new DBHelper(context);
		dbManager.getWritableDatabase();
		String sql = "update  vehicle set hphm = ?,isfuo = ? where hphm = ? and zpzl= ? and jylsh=?";
		DBManager manager = new DBManager(context);
		manager.updateBySQL(sql, data);
	}

	public static void delete(Context context, Object[] data) {
		String sql = "delete  from  vehicle  where jylsh = ? ";
		DBManager manager = new DBManager(context);
		manager.updateBySQL(sql, data);
	}

	public static ArrayList<HashMap<String, String>> query(Context context, String where1, String where2, String where3) {
		dbManager = new DBHelper(context);
		dbManager.getReadableDatabase();
		String sql = "select *   from  vehicle where hphm like ?  and zpzl like? and jylsh like?";
		DBManager manager = new DBManager(context);
		ArrayList<HashMap<String, String>> list = null;
		if (where1 == null) {
			list = manager.queryMultiMaps(sql, new String[] { "%", "%", "%" });
		} else {
			where3 = "%" + where3 + "%";
			list = manager.queryMultiMaps(sql, new String[] { where1, where2, where3 });
		}
		return list;
	}

	public static ArrayList<HashMap<String, String>> dgquery(Context context, String where4) {
		dbManager = new DBHelper(context);
		dbManager.getReadableDatabase();
		String sql = "select *   from  vehicle where hphm like ?  and zpzl like? and jylsh like?and isfuo like?";
		DBManager manager = new DBManager(context);
		ArrayList<HashMap<String, String>> list = null;
		if (where4 == null) {
			list = manager.queryMultiMaps(sql, new String[] { "%", "%", "%", "%" });
		} else {
			where4 = "%" + where4 + "%";
			list = manager.queryMultiMaps(sql, new String[] { "%", "%", where4, "%" });
		}
		return list;
	}
}
