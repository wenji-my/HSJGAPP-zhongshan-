package com.example.hsjgapp.XmlTool;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import android.util.Log;

public class UnXmlTool {

	// 写入类进行UTF-8编码
	public static String getWriteXML(String[] namedata, List<String> datalist) {
		try {
			StringBuilder sbuilder = new StringBuilder();
			// 设置XML的数据头
			sbuilder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			sbuilder.append("<root>");
			sbuilder.append("<vehispara>");
			for (int i = 0; i < namedata.length; i++) {
				sbuilder.append("<" + namedata[i] + ">");
				sbuilder.append(URLEncoder.encode(datalist.get(i), "utf-8"));
				sbuilder.append("</" + namedata[i] + ">");
			}
			sbuilder.append("</vehispara>");
			sbuilder.append("</root>");
			byte[] xml = sbuilder.toString().getBytes();
			String str = new String(xml);
			return str;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 根据查询类进行UTF-8编码
	public static String getQueryXML(String[] namedata, List<String> datalist) {
		try {
			StringBuilder sbuilder = new StringBuilder();
			// 设置XML的数据头
			sbuilder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			sbuilder.append("<root>");
			sbuilder.append("<QueryCondition>");
			for (int i = 0; i < namedata.length; i++) {
				sbuilder.append("<" + namedata[i] + ">");
				sbuilder.append(URLEncoder.encode(datalist.get(i), "utf-8"));
				sbuilder.append("</" + namedata[i] + ">");
			}
			sbuilder.append("</QueryCondition>");
			sbuilder.append("</root>");
			byte[] xml = sbuilder.toString().getBytes();
			String str = new String(xml);
			return str;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 根据查询类不进行UTF-8编码
	public static String getUNQueryXML(String[] namedata, List<String> datalist) {
		StringBuilder sbuilder = new StringBuilder();
		// 设置XML的数据头
		sbuilder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sbuilder.append("<root>");
		sbuilder.append("<QueryCondition>");
		for (int i = 0; i < namedata.length; i++) {
			sbuilder.append("<" + namedata[i] + ">");
			sbuilder.append(datalist.get(i));
			sbuilder.append("</" + namedata[i] + ">");
		}
		sbuilder.append("</QueryCondition>");
		sbuilder.append("</root>");
		byte[] xml = sbuilder.toString().getBytes();
		String str = new String(xml);
		return str;
	}

	// 根据上传照片进行编码
	public static String getPhotoXML(String[] namedata, List<String> datalist, String photodata) {
		try {
			StringBuilder sbuilder = new StringBuilder();
			// 设置XML的数据头
			sbuilder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			sbuilder.append("<root>");
			sbuilder.append("<vehispara>");
			for (int i = 0; i < namedata.length; i++) {
				if (namedata[i].equals("zp")) {
					sbuilder.append("<" + namedata[i] + ">");
					sbuilder.append(URLEncoder.encode(photodata, "utf-8"));
					sbuilder.append("</" + namedata[i] + ">");
				} else {
					sbuilder.append("<" + namedata[i] + ">");
					sbuilder.append(URLEncoder.encode(datalist.get(i), "utf-8"));
					sbuilder.append("</" + namedata[i] + ">");
				}
			}
			sbuilder.append("</vehispara>");
			sbuilder.append("</root>");
			byte[] xml = sbuilder.toString().getBytes();
			String str = new String(xml);
			return str;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getTimeXML(String[] namedata, List<String> datalist) {
		try {
			StringBuilder sbuilder = new StringBuilder();
			// 设置XML的数据头
			sbuilder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			sbuilder.append("<root>");
			sbuilder.append("<vehispara>");
			for (int i = 0; i < namedata.length; i++) {
				if (namedata[i].equals("kssj")) {
					sbuilder.append("<" + namedata[i] + ">");
					sbuilder.append(datalist.get(i));
					sbuilder.append("</" + namedata[i] + ">");
				} else {
					sbuilder.append("<" + namedata[i] + ">");
					sbuilder.append(URLEncoder.encode(datalist.get(i), "utf-8"));
					sbuilder.append("</" + namedata[i] + ">");
				}
			}
			sbuilder.append("</vehispara>");
			sbuilder.append("</root>");
			byte[] xml = sbuilder.toString().getBytes();
			String str = new String(xml);
			return str;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 写入类进行UTF-8编码（18C80上传特别格式数据）
	public static String getWriteXMLSM(String[] namedata, List<String> datalist) {
		try {
			StringBuilder sbuilder = new StringBuilder();
			// 设置XML的数据头
			sbuilder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			sbuilder.append("<root>");
			sbuilder.append("<vehispara>");
			for (int i = 0; i < namedata.length; i++) {
				if (!namedata[i].equals("bhgsm")) {
					sbuilder.append("<" + namedata[i] + ">");
					sbuilder.append(URLEncoder.encode(datalist.get(i), "utf-8"));
					sbuilder.append("</" + namedata[i] + ">");
				} else {
					sbuilder.append("<" + namedata[i] + ">");
					String[] smdatas = datalist.get(i).split(",");
					for (int j = 0; j < (smdatas.length / 2); j++) {
						String bhgnr = URLEncoder.encode(smdatas[j * 2 + 1], "utf-8");
						Log.i("AAA", smdatas[j * 2]);
						Log.i("AAA", smdatas[j * 2 + 1]);
						sbuilder.append("<bhgx item=\'" + smdatas[j * 2] + "\'>" + bhgnr + "</bhgx>");
					}
					sbuilder.append("</" + namedata[i] + ">");
				}
			}
			sbuilder.append("</vehispara>");
			sbuilder.append("</root>");
			byte[] xml = sbuilder.toString().getBytes();
			String str = new String(xml);
			return str;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getItems(String data) {
		String newdata = null;
		newdata = "<item>" + data + "</item>";
		return newdata;
	}
}
