package com.example.hsjgapp.XmlTool;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.example.hsjgapp.domain.Q32Domain;

public class MyQ32Xml extends DefaultHandler {
	private List<Q32Domain> buhegexingxxlists;
	private Q32Domain buhegexingxx;
	private String temp;

	public List<Q32Domain> getBuHeGeXingXXs() {
		return this.buhegexingxxlists;
	}

	@Override
	public void startDocument() throws SAXException {
		buhegexingxxlists = new ArrayList<Q32Domain>();
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("item")) {
			this.buhegexingxx = new Q32Domain();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		temp = new String(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (this.buhegexingxx != null) {
			if (qName.equalsIgnoreCase("item")) {
				buhegexingxx.setItem(temp);
				buhegexingxxlists.add(buhegexingxx);
			}
			temp = "";
		}
	}
}
