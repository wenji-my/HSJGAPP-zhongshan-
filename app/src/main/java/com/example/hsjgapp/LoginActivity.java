package com.example.hsjgapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.example.hsjgapp.R;
import com.example.hsjgapp.DialogTool.DialogTool;
import com.example.hsjgapp.FTPUtil.FTP;
import com.example.hsjgapp.IntentUtil.ConnectMethods;
import com.example.hsjgapp.R.id;
import com.example.hsjgapp.R.layout;
import com.example.hsjgapp.SQL.SQLFuntion;
import com.example.hsjgapp.ToolUtil.TimeTool;
import com.example.hsjgapp.XmlTool.UnXmlTool;
import com.example.hsjgapp.XmlTool.XMLParsingMethods;
import com.example.hsjgapp.domain.C50Domain;
import com.example.hsjgapp.domain.Code;
import com.example.hsjgapp.domain.Q09Domain;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private Message message;
	private TextView text_version;
	private Button login_login, login_exit, login_forget;
	private EditText login_name, login_passwork;
	private String namedata[] = { "password", "username", "jyjgbh", "pdaid", "version" };
	private String info = "cs_setup";
	private List<Q09Domain> operatorlists;
	private static final int MSG_SHOW = 10011;
	private static final int MSG_DISMISS = 10012;
	private static final int MSG_CHAOSHI = 10013;
	private static final int MSG_CUOWU = 10014;
	public static final int FTP_XZ_TXT = 10015;
	public static final int FTP_AZ_APK = 10016;
	public static final int FTP_GX_UI = 10017;
	private String ip;
	private String jkph;
	private String jkxlh;
	private ProgressDialog builder2 = null;
	private String localImagePath = Environment.getExternalStorageDirectory() + "/" + "MyVehPhoto/";
	private String TextFtpAddress = "//version.txt";
	private String TextSDAddress = Environment.getExternalStorageDirectory() + "/HSJGAPP/APKVersion/";
	private String TextName = "version.txt";
	private String APKFtpAddress = "//HSJGAPP.apk";
	private String APKSDAddress = Environment.getExternalStorageDirectory() + "/HSJGAPP/APKVersion/";
	private String APKName = "HSJGAPP.apk";

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == MSG_SHOW) {
				builder2 = new ProgressDialog(LoginActivity.this);
				builder2.setMessage("正在登陆中，请稍等。。。");
				builder2.setCancelable(false);
				builder2.show();
			}
			if (msg.what == MSG_DISMISS) {
				builder2.dismiss();
			}
			if (msg.what == MSG_CHAOSHI) {
				DialogTool.AlertDialogShow(LoginActivity.this, "请求超时，请重新登录!");
			}

			if (msg.what == MSG_CUOWU) {
				DialogTool.AlertDialogShow(LoginActivity.this, msg.obj.toString());
			}
			if (msg.what == FTP_GX_UI) {
				Toast.makeText(LoginActivity.this, msg.obj.toString(), 1).show();
			}
			if (msg.what == FTP_XZ_TXT) {
				String newversiondata = getVersionData(new File(TextSDAddress + TextName));
				if (!newversiondata.equals(getAPPVersionCodeFromAPP(LoginActivity.this) + "")) {
					Toast.makeText(LoginActivity.this, "版本不一致，需更新", 1).show();
					DownloadFile(APKFtpAddress, APKSDAddress, APKName, handler);
				} else {
					Toast.makeText(LoginActivity.this, "版本一致，无需更新", 1).show();
				}
			}
			if (msg.what == FTP_AZ_APK) {
				AlertDialog.Builder builderthree = new AlertDialog.Builder(LoginActivity.this);
				builderthree.setTitle("温馨提示");
				builderthree.setMessage("有新版本推出，请及时更新！");
				builderthree.setCancelable(false);
				builderthree.setPositiveButton("更新", new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						installApp();
					}
				});
				builderthree.show();
			}
		};
	};

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.login);
		// 获取SharedPreferences里的数据
		SharedPreferences preferences = getSharedPreferences(info, 0);
		ip = preferences.getString("IP", "");
		jkph = preferences.getString("JGPH", "");
		jkxlh = preferences.getString("JKXLH", "");
		// 用户名初始化
		login_name = (EditText) findViewById(R.id.login_name);
		login_name.setText(nameFill());
		// 数据库
		SQLFuntion.initTable(LoginActivity.this);
		login_passwork = (EditText) findViewById(R.id.login_passwork);
		login_passwork.setText("888888");
		// 自动删除手机数据库一个月前的数据
		ArrayList<HashMap<String, String>> sqldatas = new ArrayList<HashMap<String, String>>();
		sqldatas = SQLFuntion.query(LoginActivity.this, null, null, null);
		for (int k = 0; k < sqldatas.size(); k++) {
			try {
				// 计算数据库时间的时间差
				String sqltime1 = getTiem(0);
				String sqltime2 = sqldatas.get(k).get("pssj");
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d1 = df.parse(sqltime1);
				Date d2 = df.parse(sqltime2);
				long sqlsjc = (d1.getTime() - d2.getTime()) / 1000;
				if (sqlsjc >= 691200) {
					Object[] data = { sqldatas.get(k).get("jylsh") };
					SQLFuntion.delete(LoginActivity.this, data);
					Log.i("AAA", sqldatas.get(k).get("jylsh") + "的流水号数据已删除");
				}
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		}

		// 删除照片
		deleteFile(new File(localImagePath));

		// 版本判断是否下载新APK
//		UpdateAPK();

		text_version = (TextView) findViewById(R.id.text_version);
		text_version.setText("版本号：16.06.30.10(v" + getAPPVersionCodeFromAPP(LoginActivity.this) + ")");

		login_login = (Button) findViewById(R.id.login_login);
		login_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!isConnected()) {
					String prompt = "WIFI未连接，请重新设置WIFI。。";
					DialogTool.AlertDialogShow(LoginActivity.this, prompt);
				} else {
					final String name = login_name.getText().toString().trim();
					final String passwork = login_passwork.getText().toString().trim();
					if (name.equals("")) {
						Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_LONG).show();
					} else if (passwork.equals("")) {
						Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
					} else if (ip.equals("") || ip == null) {
						Toast.makeText(LoginActivity.this, "IP地址不能为空", Toast.LENGTH_LONG).show();
					} else if (jkph.equals("") || jkph == null) {
						Toast.makeText(LoginActivity.this, "检验机构编号不能为空", Toast.LENGTH_LONG).show();
					} else {
						new Thread(new Runnable() {
							@SuppressLint("SimpleDateFormat")
							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									handler.sendEmptyMessage(MSG_SHOW);
									long diff = 0;
									String time1 = "";
									// 调用18C50接口
									String data = "<?xml version=\"1.0\" encoding=\"GBK\"?><root><QueryCondition><babh>" + jkph
											+ "</babh></QueryCondition></root>";
									String timedata = ConnectMethods.connectWebService(ip, StaticValues.queryObject, jkxlh,
											"18C50", data, "queryObjectOutResult", StaticValues.timeoutThree,
											StaticValues.numberFive);
									List<C50Domain> timelists = XMLParsingMethods.saxtime(timedata);
									time1 = timelists.get(0).getSj().substring(0, 19);
									Log.i("BBB", time1);
									String time2 = getTiem(0);
									try {
										// 计算时间差
										SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										Date d1 = df.parse(time1);
										Date d2 = df.parse(time2);
										diff = (d1.getTime() - d2.getTime()) / 1000;
									} catch (ParseException e) {
										e.printStackTrace();
									}
									// 18Q09用户登录数据封装
									String password = passwork;
									String username = name;
									String jyjgbh = jkph;
									String imei = ((TelephonyManager) LoginActivity.this.getSystemService(TELEPHONY_SERVICE))
											.getDeviceId();
									// String imei = "9190289381948";
									String version = getAPPVersionCodeFromAPP(LoginActivity.this);
									List<String> datalist = new ArrayList<String>();
									datalist.add(password);
									datalist.add(username);
									datalist.add(jyjgbh);
									datalist.add(imei);
									datalist.add(version);
									Log.i("AAA", "diff:" + diff);
									if (-30 <= diff && diff <= 30) {
										String xmldata = UnXmlTool.getQueryXML(namedata, datalist);
										// 调用18Q09接口
										String userdata = ConnectMethods.connectWebService(ip, StaticValues.queryObject, jkxlh,
												"18Q09", xmldata, "queryObjectOutResult", StaticValues.timeoutThree,
												StaticValues.numberFive);
										List<Code> codelists = XMLParsingMethods.saxcode(userdata);
										String newcode;
										String newmessage;
										if (codelists.get(0).getCode().equals("1")) {
											operatorlists = XMLParsingMethods.saxuserlogin(userdata);
											newcode = operatorlists.get(0).getCode() + "";
											newmessage = operatorlists.get(0).getMessage();
										} else {
											newcode = codelists.get(0).getCode();
											newmessage = codelists.get(0).getMessage();
										}
										if (newcode.equals("1")) {
											SaveName();
											SaveOperationLog();
											handler.sendEmptyMessage(MSG_DISMISS);
											Intent intent = new Intent(LoginActivity.this, VehicleInformationActivity.class);
											intent.putExtra("USERID", login_name.getText().toString().trim());
											Bundle bundle = new Bundle();
											bundle.putSerializable("theoperatorlistsObj", (Serializable) operatorlists);
											intent.putExtras(bundle);
											startActivity(intent);
											finish();
										} else {
											handler.sendEmptyMessage(MSG_DISMISS);
											setmessegas(MSG_CUOWU, newmessage);
										}
									} else {
										handler.sendEmptyMessage(MSG_DISMISS);
										setmessegas(MSG_CUOWU, "服务器时间为：" + time1 + "，请重新设置系统时间，相差不要超过30秒！");
									}
								} catch (Exception e) {
									handler.sendEmptyMessage(MSG_DISMISS);
									setmessegas(MSG_CUOWU, "网络连接异常，请检查网络环境!");
									e.printStackTrace();
									return;
								}
							}
						}).start();
					}
				}
			}
		});

		login_exit = (Button) findViewById(R.id.login_exit);
		login_exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});

		login_forget = (Button) findViewById(R.id.login_forget);
		login_forget.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent1 = new Intent(LoginActivity.this, CSSetupActivity.class);
				startActivity(intent1);
				finish();
			}
		});

	}

	// 获取当前手机版本号
	public String getAPPVersionCodeFromAPP(Context ctx) {
		int currentVersionCode = 0;
		String appVersionName = "";
		PackageManager manager = ctx.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
			appVersionName = info.versionName; // 版本名
			currentVersionCode = info.versionCode; // 版本号
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return appVersionName;
	}

	/**
	 * 删除照片
	 * 
	 * @param oldPath
	 *            :照片保存地址
	 */
	public void deleteFile(final File oldPath) {
		new Thread(new Runnable() {
			@SuppressLint("SimpleDateFormat")
			@Override
			public void run() {
				try {
					// TODO Auto-generated method stub
					File[] oldfiles = oldPath.listFiles();
					if (oldfiles != null) {
						for (int i = 0; i < oldfiles.length; i++) {
							File fileTemp = oldfiles[i];
							long timeone = fileTemp.lastModified();
							SimpleDateFormat dftwo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							long timetwo = dftwo.parse(TimeTool.getfiveTiem() + " 00:00:00").getTime();
							if ((timetwo - timeone) / 1000 > 604800) {
								newdeleteFile(fileTemp);
								Log.i("AAA", (timeone - timetwo) + "");
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	// 删除文件夹所有内容
	public static void newdeleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int j = 0; j < files.length; j++) { // 遍历目录下所有的文件
					LoginActivity.newdeleteFile(files[j]); // 把每个文件 用这个方法进行迭代
				}
				file.delete();
			}

		}
	}

	// 版本判断是否下载新APK
	private void UpdateAPK() {
		// 先下载版本文件判断是否更新
		File fileone = new File(TextSDAddress + TextName);
		fileone.delete();
		File filetwo = new File(TextSDAddress + APKName);
		filetwo.delete();
		DownloadFile(TextFtpAddress, TextSDAddress, TextName, handler);
	}

	// 从FTP下载文件
	private void DownloadFile(final String FtpAdress, final String SDAdress, final String FileName, final Handler newhandler) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 单文件下载
				try {
					new FTP().downloadSingleFile(FtpAdress, SDAdress, FileName, newhandler, ip);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	// 获取版本信息
	private String getVersionData(File versionfile) {
		String versiondata = null;
		try {
			StringBuffer sb = new StringBuffer();
			BufferedReader br = new BufferedReader(new FileReader(versionfile));
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			versiondata = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return versiondata;
	}

	// 保存到SD卡上
	public void SaveName() {
		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File nameFile1 = CreateFolder("/HSJGAPP/LoginName");
				if (!nameFile1.exists()) {
					nameFile1.mkdirs();
				}
				File nameFile2 = CreateFolder("/HSJGAPP/LoginName/name.txt");
				if (!nameFile2.exists()) {
					nameFile2.createNewFile();
				}
				FileOutputStream out = new FileOutputStream(nameFile2, false);
				out.write(login_name.getText().toString().trim().getBytes("UTF-8"));
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File CreateFolder(String filename) {
		try {
			File sdCard = Environment.getExternalStorageDirectory();
			File nameFile = new File(sdCard.getCanonicalPath() + filename);
			return nameFile;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 用户名获取
	public String nameFill() {
		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File sdCard = Environment.getExternalStorageDirectory();
				File nameFile = new File(sdCard.getCanonicalPath() + "/HSJGAPP/LoginName/name.txt");
				byte[] buffer = new byte[1024];
				FileInputStream fis = new FileInputStream(nameFile);
				int len = 0;
				StringBuffer sb = new StringBuffer("");
				while ((len = fis.read(buffer)) > 0) {
					sb.append(new String(buffer, 0, len));
				}
				fis.close();
				return sb.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 获取手机是否联网
	public boolean isConnected() {
		try {
			ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			boolean isconnect = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
			return isconnect;
		} catch (Exception e) {
			return false;
		}
	}

	// 获取手机当前时间点
	public String getTiem(int timed) {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		String time = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
		if (timed == 1) {
			time = year + "年" + month + "月" + day + "日" + hour + "时" + minute + "分" + second + "秒";
		} else if (timed == 2) {
			time = year + "年" + month + "月" + day + "日";
		}
		return time;
	}

	// 编写登录操作日记
	public void SaveOperationLog() {
		String time = getTiem(2);
		String file = "/HSJGAPP/JGLog/" + time + ".txt";
		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File nameFile1 = CreateFolder("/HSJGAPP/JGLog");
				if (!nameFile1.exists()) {
					nameFile1.mkdirs();
				}
				File nameFile2 = CreateFolder(file);
				if (!nameFile2.exists()) {
					nameFile2.createNewFile();
				}
				String time1 = getTiem(1);
				RandomAccessFile raf = new RandomAccessFile(nameFile2, "rw");
				raf.seek(nameFile2.length());
				String content = time1 + "," + login_name.getText().toString().trim() + "进入监管系统APP" + "\n";
				raf.write(content.getBytes("UTF-8"));
				raf.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setmessegas(int msgnumber, String neirong) {
		message = new Message();
		message.what = msgnumber;
		message.obj = neirong;
		handler.sendMessage(message);
	}

	// 安装新版本应用
	private void installApp() {
		try {
			File appFile = new File(APKSDAddress + APKName);
			if (!appFile.exists()) {
				return;
			}
			// 跳转到新版本应用安装页面
			Intent apkintent = new Intent(Intent.ACTION_VIEW);
			apkintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			apkintent.setDataAndType(Uri.parse("file://" + appFile.toString()), "application/vnd.android.package-archive");
			startActivity(apkintent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
