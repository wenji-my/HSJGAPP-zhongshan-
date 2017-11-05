package com.example.hsjgapp.XmlTool;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.example.hsjgapp.domain.Q11Domain;

public class MyQ11Xml extends DefaultHandler {
	private List<Q11Domain> vehicleInformationlists;
	private Q11Domain vehicleInformation;
	private String temp;

	public List<Q11Domain> getVehicleInformations() {
		return this.vehicleInformationlists;
	}

	@Override
	public void startDocument() throws SAXException {
		vehicleInformationlists = new ArrayList<Q11Domain>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("vehispara")) {
			this.vehicleInformation = new Q11Domain();
			vehicleInformation.setId(new Integer(attributes.getValue(0)));
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		temp = new String(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (this.vehicleInformation != null) {
			if (qName.equalsIgnoreCase("jylsh")) {
				vehicleInformation.setJylsh(temp);
			} else if (qName.equalsIgnoreCase("clsbdh")) {
				vehicleInformation.setClsbdh(temp);
			} else if (qName.equalsIgnoreCase("hphm")) {
				vehicleInformation.setHphm(temp);
			} else if (qName.equalsIgnoreCase("hpzl")) {
				vehicleInformation.setHpzl(temp);
			} else if (qName.equalsIgnoreCase("jycs")) {
				vehicleInformation.setJycs(Integer.parseInt(temp));
			} else if (qName.equalsIgnoreCase("cllx")) {
				vehicleInformation.setCllx(temp);
			} else if (qName.equalsIgnoreCase("jylb")) {
				vehicleInformation.setJylb(temp);
			} else if (qName.equalsIgnoreCase("syxz")) {
				vehicleInformation.setSyxz(temp);
			} else if (qName.equalsIgnoreCase("slzt")) {
				vehicleInformation.setSlzt(temp);
			} else if (qName.equalsIgnoreCase("jyxm")) {
				vehicleInformation.setJyxm(temp);
			} else if (qName.equalsIgnoreCase("wgjcxm")) {
				vehicleInformation.setWgjcxm(temp);
			} else if (qName.equalsIgnoreCase("dpjyxm")) {
				vehicleInformation.setDpjyxm(temp);
			} else if (qName.equalsIgnoreCase("dtdpjyxm")) {
				vehicleInformation.setDtdpjyxm(temp);
			} else if (qName.equalsIgnoreCase("wgjyzp")) {
				vehicleInformation.setWgjyzp(temp);
			} else if (qName.equalsIgnoreCase("wgjyzpsm")) {
				vehicleInformation.setWgjyzpsm(temp);
			} else if (qName.equalsIgnoreCase("bhgcyzp")) {
				vehicleInformation.setBhgcyzp(temp);
			} else if (qName.equalsIgnoreCase("bhgcyzpsm")) {
				vehicleInformation.setBhgcyzpsm(temp);
			} else if (qName.equalsIgnoreCase("vehispara")) {
				vehicleInformationlists.add(vehicleInformation);
			}
			temp = "";
		}
	}
}
