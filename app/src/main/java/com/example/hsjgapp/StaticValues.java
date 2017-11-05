package com.example.hsjgapp;

import android.os.Environment;

public class StaticValues {
	// 联网参数配置
	public static String queryObject = "queryObjectOut";
	public static String writeObject = "writeObjectOut";
	public static String queryResult = "queryObjectOutResult";
	public static String writeResult = "writeObjectOutResult";
	public static String nameSpace = "http://gdhs.com/";
	// 超时时间
	public static int timeoutThree = 3000;
	public static int timeoutFive = 5000;
	public static int timeoutNine = 9000;
	public static int timeoutFifteen = 15000;
	public static int timeoutThirty = 30000;
	// 接口单次调的次数
	public static int numberOne = 1;
	public static int numberTwo = 2;
	public static int numberThree = 3;
	public static int numberFour = 4;
	public static int numberFive = 5;
	// 照片保存路径
	public static String localImagePath = Environment.getExternalStorageDirectory() + "/" + "MyVehPhoto/";

	// 提交数组
	public static String[] wgjsxmdata = { "jylsh", "jyjgbh", "jcxdh", "jycs", "jyxm", "hpzl", "hphm", "clsbdh", "rhplx", "rppxh",
			"rvin", "rfdjh", "rcsys", "rwkcc", "rzj", "rzbzl", "rhdzrs", "rhdzll", "rlbgd", "rhzgbthps", "rkcyjck", "rkccktd",
			"rhx", "rcswg", "rwgbs", "rwbzm", "rlt", "rhpaz", "rjzgj", "rqcaqd", "rsjp", "rmhq", "rxsjly", "rcsfgbs", "rclwbzb",
			"rchfh", "ryjc", "rjjx", "rxsgn", "rfbs", "rfzzd", "rpszdq", "rjjqd", "rfdjcmh", "rsddd", "rfzdtb", "rxcbz",
			"rwxhwbz", "rlwcx", "ztcjrfzzz", "qzdz", "ygddtz", "zzly", "zxzxjxs", "lcbds", "cwkc", "cwkk", "cwkg", "zbzl", "syr",
			"sjhm", "lxdz", "yzbm", "wgbhgyy", "jyyjy", "wgjcjyy", "wgjcjyysfzh" };// 18C80-外观提交数据
	public static String[] dtdpxmdata = { "jylsh", "jyjgbh", "jcxdh", "jycs", "jyxm", "hpzl", "hphm", "clsbdh", "rzxx", "rcdx",
			"rzdx", "rybzsq", "dtdpbhgyy", "jyyjy", "dpdtjyy", "dpdtjyysfzh" };// 18C80-动态底盘提交数据
	public static String[] dpjyxmdata = { "jylsh", "jyjgbh", "jcxdh", "jycs", "jyxm", "hpzl", "hphm", "clsbdh", "rzxxbj",
			"rcdxbj", "rxsxbj", "rzdxbj", "rqtbj", "dpbhgyy", "jyyjy", "dpjcjyy", "dpjyysfzh" };// 18C80-底盘提交数据
	public static String[] namedata = { "gwjysbbh", "jyxm", "jycs", "kssj", "hpzl", "hphm", "jylsh", "jcxdh", "clsbdh", "jyjgbh" };// 18C55-开始检测提交
	public static String[] namedata2 = { "jylsh", "jyjgbh", "jcxdh", "jycs", "jyxm", "hpzl", "hphm", "clsbdh", "gwjysbbh", "jssj" };// 18C58-结束检测提交
	public static String[] cfpzname = { "jylsh", "jyjgbh", "jycs", "jcxdh", "hpzl", "hphm", "clsbdh", "gwjysbbh", "jyxm", "kssj",
			"zpzl" };// 18J31-触发拍照
	public static String[] vedioname = { "jylsh", "hphm", "hpzl", "clsbdh", "gwxm", "jcxdh", "jycs" };// 18J11，18J12-触发视频摄像机开始，结束
	public static String[] lpdataq22 = { "jylsh", "clsbdh" };// 18Q22-漏拍照片查询
	public static String[] startc55 = { "gwjysbbh", "jyxm", "jycs", "kssj", "hpzl", "hphm", "jylsh", "jcxdh", "clsbdh", "jyjgbh" };// 18C55-开始接口
	public static String[] endc58 = { "jylsh", "jyjgbh", "jcxdh", "jycs", "jyxm", "hpzl", "hphm", "clsbdh", "gwjysbbh", "jssj" };// 18C58-结束接口
	public static String[] snapj31 = { "jylsh", "jyjgbh", "jycs", "jcxdh", "hpzl", "hphm", "clsbdh", "gwjysbbh", "jyxm", "kssj",
			"zpzl" };// 18J31-抓拍接口
	public static String[] videoj11j12 = { "jylsh", "hphm", "hpzl", "clsbdh", "gwxm", "jcxdh" };// 18J11,18J12-录像接口
	public static String[] submittedc54 = { "jylsh", "jyjgbh", "jcxdh", "jycs", "jyxm", "hpzl", "hphm", "clsbdh", "lsy", "zdcsd",
			"zdxtsj", "zdwdx", "xckzzdjl", "xcmzzdjl", "xckzmfdd", "xcmzmfdd", "xczdczlz", "lszdpd", "yjzdcsd", "yjkzzdjl",
			"yjkzmfdd", "yjmzzdjl", "yjmzmfdd", "yjzdczlfs", "yjzdczlz", "yjzdpd", "zcpd", "lszczdpd", "csdscz", "csbpd", "lsjg" };// 18C54-路试数据提交接口

}
