package com.example.hsjgapp.XmlTool;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.example.hsjgapp.domain.Q13Domain;
import com.example.hsjgapp.domain.Q11Domain;

public class MyQ13Xml extends DefaultHandler {
	private List<Q13Domain> photonamelists;
	private Q13Domain photoname;
	private String temp;

	public List<Q13Domain> getPhotoNames() {
		return this.photonamelists;
	}

	@Override
	public void startDocument() throws SAXException {
		photonamelists = new ArrayList<Q13Domain>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("item")) {
			this.photoname = new Q13Domain();
			photoname.setId(attributes.getValue(0));
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		temp = new String(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (this.photoname != null) {
			if (qName.equalsIgnoreCase("item")) {
				photoname.setName(temp);
				photonamelists.add(photoname);
			}
			temp = "";
		}
	}
}
