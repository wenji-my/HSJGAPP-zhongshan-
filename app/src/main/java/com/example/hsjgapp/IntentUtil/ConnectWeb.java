package com.example.hsjgapp.IntentUtil;

import android.util.Log;

import com.example.hsjgapp.StaticValues;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

public class ConnectWeb {
	private static final String nameSpace = StaticValues.nameSpace;
	private String ip = null;
	private int timeout = 0;
	private int numbercs = 0;
	SoapObject rpc = null;
	String methodName = null;

	public ConnectWeb(String methodName, String ip, int timeout, int numbercs) {
		this.methodName = methodName;
		this.timeout = timeout;
		this.numbercs = numbercs;
		this.ip = " http://" + ip + ":8070/JGService.asmx";
		//this.ip = "http://192.168.1.8:8070/JGService.asmx";
		rpc = new SoapObject(nameSpace, this.methodName);

	}

	public void addProperty(String name, String value) {
		rpc.addProperty(name, value);
	}

	public SoapObject connectToWebService() throws Exception, XmlPullParserException {
		//int count = 0;
		//while (true) {
			try {
				Log.i("AAA", "ip:" + ip);
				HttpTransportSE ht = new HttpTransportSE(ip, timeout);
				ht.debug = true;
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.bodyOut = rpc;
				envelope.dotNet = true;
				envelope.setOutputSoapObject(rpc);
				ht.call(nameSpace + this.methodName, envelope);
				SoapObject result = (SoapObject) envelope.bodyIn;
				Log.i("BBB", "result:" + result.toString());
				return result;
			} catch (Exception e) {
				//if (count > numbercs)
					//throw e;
				//count++;
				e.printStackTrace();
				return null;
			}
		//}
	}
}
