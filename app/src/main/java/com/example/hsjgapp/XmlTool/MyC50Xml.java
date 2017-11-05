package com.example.hsjgapp.XmlTool;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.example.hsjgapp.domain.C50Domain;

public class MyC50Xml extends DefaultHandler {
	private List<C50Domain> timelists;
	private C50Domain time;
	private String temp;

	public List<C50Domain> getTimes() {
		return this.timelists;
	}

	@Override
	public void startDocument() throws SAXException {
		timelists = new ArrayList<C50Domain>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("root")) {
			this.time = new C50Domain();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		temp = new String(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (this.time != null) {
			if (qName.equalsIgnoreCase("message")) {
				time.setMessage(temp);
			} else if (qName.equalsIgnoreCase("sj")) {
				time.setSj(temp);
			} else if (qName.equalsIgnoreCase("root")) {
				timelists.add(time);
			}
			temp = "";
		}
	}
}
