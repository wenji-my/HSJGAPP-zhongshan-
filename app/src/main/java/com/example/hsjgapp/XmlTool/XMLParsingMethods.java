package com.example.hsjgapp.XmlTool;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

import com.example.hsjgapp.domain.Q22Domain;
import com.example.hsjgapp.domain.Q32Domain;
import com.example.hsjgapp.domain.Code;
import com.example.hsjgapp.domain.Q09Domain;
import com.example.hsjgapp.domain.Q13Domain;
import com.example.hsjgapp.domain.C50Domain;
import com.example.hsjgapp.domain.Q33Domain;
import com.example.hsjgapp.domain.Q11Domain;

public class XMLParsingMethods {

	// 18C50时间同步返回数据解析
	public static List<C50Domain> saxtime(String str) {
		List<C50Domain> timelists = new ArrayList<C50Domain>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			MyC50Xml handler = new MyC50Xml();
			parser.parse(new ByteArrayInputStream(str.getBytes()), handler);
			timelists = handler.getTimes();
			return timelists;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 18Q09用户登录返回数据解析
	public static List<Q09Domain> saxuserlogin(String str) {
		List<Q09Domain> operatorlists = new ArrayList<Q09Domain>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			MyQ09Xml handler = new MyQ09Xml();
			parser.parse(new ByteArrayInputStream(str.getBytes()), handler);
			operatorlists = handler.getOperators();
			return operatorlists;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 18Q11返回数据解析
	public static List<Q11Domain> saxvehicleinformation(String str) {
		List<Q11Domain> vehicleInformationlists = new ArrayList<Q11Domain>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			MyQ11Xml handler = new MyQ11Xml();
			parser.parse(new ByteArrayInputStream(str.getBytes()), handler);
			vehicleInformationlists = handler.getVehicleInformations();
			return vehicleInformationlists;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 18C63返回数据解析
	public static List<Code> saxcode(String str) {
		List<Code> codelists = new ArrayList<Code>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			MyCodeXml handler = new MyCodeXml();
			parser.parse(new ByteArrayInputStream(str.getBytes()), handler);
			codelists = handler.getCodes();
			return codelists;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 18Q13返回数据解析
	public static List<Q13Domain> saxphotoname(String str) {
		List<Q13Domain> photonamelists = new ArrayList<Q13Domain>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			MyQ13Xml handler = new MyQ13Xml();
			parser.parse(new ByteArrayInputStream(str.getBytes()), handler);
			photonamelists = handler.getPhotoNames();
			return photonamelists;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 18Q22-漏拍照片查询返回数据解析
	public static List<Q22Domain> saxlpphotos(String str) {
		List<Q22Domain> lpphotolists = new ArrayList<Q22Domain>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			MyQ22Xml handler = new MyQ22Xml();
			parser.parse(new ByteArrayInputStream(str.getBytes()), handler);
			lpphotolists = handler.getQ22Domains();
			return lpphotolists;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 18Q32返回数据解析
	public static List<Q32Domain> saxbuhegexingxxs(String str) {
		List<Q32Domain> buhegexingxxlists = new ArrayList<Q32Domain>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			MyQ32Xml handler = new MyQ32Xml();
			parser.parse(new ByteArrayInputStream(str.getBytes()), handler);
			buhegexingxxlists = handler.getBuHeGeXingXXs();
			return buhegexingxxlists;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 18Q33返回数据解析
	public static List<Q33Domain> saxvehicleBuHeGes(String str) {
		List<Q33Domain> vehicleBuHeGelists = new ArrayList<Q33Domain>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			MyQ33Xml handler = new MyQ33Xml();
			parser.parse(new ByteArrayInputStream(str.getBytes()), handler);
			vehicleBuHeGelists = handler.getVehicleBuHeGes();
			return vehicleBuHeGelists;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
