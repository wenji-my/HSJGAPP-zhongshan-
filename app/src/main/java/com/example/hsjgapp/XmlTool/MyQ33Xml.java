package com.example.hsjgapp.XmlTool;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.example.hsjgapp.domain.Q33Domain;

public class MyQ33Xml extends DefaultHandler {
	private List<Q33Domain> vehicleBuHeGelists;
	private Q33Domain vehicleBuHeGe;
	private String temp;
	private List<String> itemlists;

	public List<Q33Domain> getVehicleBuHeGes() {
		return this.vehicleBuHeGelists;
	}

	@Override
	public void startDocument() throws SAXException {
		vehicleBuHeGelists = new ArrayList<Q33Domain>();
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("body")) {
			this.vehicleBuHeGe = new Q33Domain();
			itemlists = new ArrayList<String>();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		temp = new String(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (this.vehicleBuHeGe != null) {
			if (qName.equalsIgnoreCase("jyjgbh")) {
				vehicleBuHeGe.setJyjgbh(temp);
			} else if (qName.equalsIgnoreCase("jylsh")) {
				vehicleBuHeGe.setJylsh(temp);
			} else if (qName.equalsIgnoreCase("jycs")) {
				vehicleBuHeGe.setJycs(Integer.parseInt(temp));
			} else if (qName.equalsIgnoreCase("hphm")) {
				vehicleBuHeGe.setHphm(temp);
			} else if (qName.equalsIgnoreCase("hpzl")) {
				vehicleBuHeGe.setHpzl(temp);
			} else if (qName.equalsIgnoreCase("clsbdh")) {
				vehicleBuHeGe.setClsbdh(temp);
			} else if (qName.equalsIgnoreCase("item")) {
				itemlists.add(temp);
			} else if (qName.equalsIgnoreCase("wgbhgyy")) {
				vehicleBuHeGe.setWgbhgyy(itemlists);
			} else if (qName.equalsIgnoreCase("dtbhgyy")) {
				vehicleBuHeGe.setDtbhgyy(itemlists);
			} else if (qName.equalsIgnoreCase("dpbhgyy")) {
				vehicleBuHeGe.setDpbhgyy(itemlists);
			} else if (qName.equalsIgnoreCase("jyyjy")) {
				vehicleBuHeGe.setJyyjy(temp);
			} else if (qName.equalsIgnoreCase("body")) {
				vehicleBuHeGelists.add(vehicleBuHeGe);
			}
			temp = "";
		}
	}
}
