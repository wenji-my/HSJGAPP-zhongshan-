package com.example.hsjgapp.IntentUtil;

import java.net.URLDecoder;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

public class ConnectMethods {
	// 联网部分
	/*
	 * ip:服务器地址 methodName：接口类别 jkxlh：接口序列号 jkid：接口ID xmlDoc：上传的XML文档
	 * ResultGs：返回数据需解析的格式
	 */
	public static String connectWebService(String ip, String methodName, String jkxlh, String jkid, String xmlDoc,
			String ResultGs, int timeout, int cscs) {
		try {
			ConnectWeb wsh = new ConnectWeb(methodName, ip, 30000, 0);
			wsh.addProperty("xtlb", "18");
			wsh.addProperty("jkxlh", jkxlh);
			wsh.addProperty("jkid", jkid);
			wsh.addProperty("xmlDoc", xmlDoc);
			SoapObject result = wsh.connectToWebService();
			String detail = result.getProperty(ResultGs).toString();
			String data1 = URLDecoder.decode(detail, "utf-8");
			Log.i("BBB", "detail:" + data1);
			return data1;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
