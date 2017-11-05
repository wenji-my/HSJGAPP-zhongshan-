package com.example.hsjgapp.ToolUtil;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by Administrator on 2017/7/31.
 */

public class DialogTool {

    public static void AlertDialogShow(Context context, String data) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("提示框");
        builder1.setMessage(data);
        builder1.setPositiveButton("确定", null);
        builder1.show();
    }
}
