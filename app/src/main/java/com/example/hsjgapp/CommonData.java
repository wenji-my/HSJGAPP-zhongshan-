package com.example.hsjgapp;

public class CommonData {
	static String usingFileName = "";
	static String imei = "";
	static String[] plateProxs = new String[] { "京", "津", "沪", "渝", "冀", "豫", "云", "辽", "黑", "湘", "皖", "鲁", "新", "苏", "浙", "赣",
			"鄂", "桂", "甘", "晋", "蒙", "陕", "吉", "闽", "贵", "粤", "青", "藏", "川", "宁", "琼" };

	public static String GetUsingFileName() {
		return usingFileName;
	}

	public static void SetUsingFileName(String value) {
		usingFileName = value;
	}

	public static String GetImei() {
		return imei;
	}

	public static void SetImei(String value) {
		imei = value;
	}

	public static String[] GetPlateProxs() {
		return plateProxs;
	}
}
