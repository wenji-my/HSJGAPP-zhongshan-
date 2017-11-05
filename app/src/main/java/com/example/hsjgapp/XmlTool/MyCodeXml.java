package com.example.hsjgapp.XmlTool;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.example.hsjgapp.domain.Code;
import com.example.hsjgapp.domain.C50Domain;

public class MyCodeXml extends DefaultHandler {
	private List<Code> codelists;
	private Code code;
	private String temp;

	public List<Code> getCodes() {
		return this.codelists;
	}

	@Override
	public void startDocument() throws SAXException {
		codelists = new ArrayList<Code>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("root")) {
			this.code = new Code();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		temp = new String(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (this.code != null) {
			if (qName.equalsIgnoreCase("code")) {
				code.setCode(temp);
			} else if (qName.equalsIgnoreCase("message")) {
				code.setMessage(temp);
			} else if (qName.equalsIgnoreCase("root")) {
				codelists.add(code);
			}
			temp = "";
		}
	}
}
