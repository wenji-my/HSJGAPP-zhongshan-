package com.example.hsjgapp.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "mydbs.db";
	private static final int VERSION = 2;

	public DBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 建表语句执行
		String sql = "create table vehicle(pid integer primary key autoincrement,jycs varchar(64),pssj varchar(64),hpzl varchar(64),hphm varchar(64),zpzl varchar(64),zpid varchar(64),jylsh varchar(64),jcxdh varchar(64),clsbdh varchar(64),jyjgbh varchar(64),isfuo varchar(60))";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}
}
