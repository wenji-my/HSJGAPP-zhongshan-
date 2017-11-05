package com.example.hsjgapp.XmlTool;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.example.hsjgapp.domain.Q22Domain;

public class MyQ22Xml extends DefaultHandler {
	private List<Q22Domain> lpphotolists;
	private Q22Domain lpphoto;
	private String temp;

	public List<Q22Domain> getQ22Domains() {
		return this.lpphotolists;
	}

	@Override
	public void startDocument() throws SAXException {
		lpphotolists = new ArrayList<Q22Domain>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("root")) {
			this.lpphoto = new Q22Domain();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		temp = new String(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (this.lpphoto != null) {
			if (qName.equalsIgnoreCase("jylsh")) {
				lpphoto.setJylsh(temp);
			} else if (qName.equalsIgnoreCase("ypzp")) {
				lpphoto.setYpzp(temp);
			} else if (qName.equalsIgnoreCase("root")) {
				lpphotolists.add(lpphoto);
			}
			temp = "";
		}
	}
}
