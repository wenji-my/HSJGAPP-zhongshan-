package com.example.hsjgapp.XmlTool;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.example.hsjgapp.domain.Q09Domain;

public class MyQ09Xml extends DefaultHandler {
	private List<Q09Domain> operatorlists;
	private Q09Domain operator;
	private String temp = "";

	public List<Q09Domain> getOperators() {
		return this.operatorlists;
	}

	@Override
	public void startDocument() throws SAXException {
		operatorlists = new ArrayList<Q09Domain>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("root")) {
			this.operator = new Q09Domain();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		temp = new String(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (this.operator != null) {
			if (qName.equalsIgnoreCase("code")) {
				operator.setCode(Integer.parseInt(temp));
			} else if (qName.equalsIgnoreCase("message")) {
				operator.setMessage(temp);
			} else if (qName.equalsIgnoreCase("yhdh")) {
				operator.setYhdh(temp);
			} else if (qName.equalsIgnoreCase("xm")) {
				operator.setXm(temp);
			} else if (qName.equalsIgnoreCase("pdaqx")) {
				operator.setPdaqx(temp);
			} else if (qName.equalsIgnoreCase("sfzmhm")) {
				operator.setSfzmhm(temp);
			} else if (qName.equalsIgnoreCase("root")) {
				operatorlists.add(operator);
			}
			temp = "";
		}
	}
}
