package com.example.hsjgapp.DialogTool;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

public class DialogTool {

	public static void AlertDialogShow(Context context, String data) {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
		builder1.setTitle("提示框");
		builder1.setMessage(data);
		builder1.setPositiveButton("确定", null);
		builder1.show();
	}

	/*
	 * public static void ProgressBar(Context context, boolean is) {
	 * ProgressDialog builder2 = new ProgressDialog(context);
	 * builder2.setTitle("提示框"); builder2.setMessage("正在登陆中，请稍等。。。");
	 * builder2.setCancelable(false); if (is) { builder2.show(); } else {
	 * builder2.dismiss(); } }
	 */
}
