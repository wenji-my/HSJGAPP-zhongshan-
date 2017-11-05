package com.example.hsjgapp.domain;

import java.io.Serializable;

public class Q09Domain implements Serializable {
	private int code;
	private String message;
	private String yhdh;
	private String xm;
	private String pdaqx;
	private String sfzmhm;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getYhdh() {
		return yhdh;
	}

	public void setYhdh(String yhdh) {
		this.yhdh = yhdh;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getPdaqx() {
		return pdaqx;
	}

	public void setPdaqx(String pdaqx) {
		this.pdaqx = pdaqx;
	}

	public String getSfzmhm() {
		return sfzmhm;
	}

	public void setSfzmhm(String sfzmhm) {
		this.sfzmhm = sfzmhm;
	}

}
