package com.example.hsjgapp;

import java.util.List;

public class MaulItemGroup {
	public String title;
	public List<MaulListItem> children;

	public MaulItemGroup(String title, List<MaulListItem> children) {
		this.title = title;
		this.children = children;
	}
}
