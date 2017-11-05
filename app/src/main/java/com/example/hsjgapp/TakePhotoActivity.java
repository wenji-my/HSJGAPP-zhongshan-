package com.example.hsjgapp;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.example.hsjgapp.R;
import com.example.hsjgapp.DialogTool.DialogTool;
import com.example.hsjgapp.IntentUtil.ConnectMethods;
import com.example.hsjgapp.SQL.SQLFuntion;
import com.example.hsjgapp.ToolUtil.PhotoTool;
import com.example.hsjgapp.ToolUtil.TimeTool;
import com.example.hsjgapp.XmlTool.UnXmlTool;
import com.example.hsjgapp.XmlTool.XMLParsingMethods;
import com.example.hsjgapp.domain.Code;
import com.example.hsjgapp.domain.Q11Domain;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextPaint;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class TakePhotoActivity extends Activity implements Callback, OnClickListener, OnLongClickListener, AutoFocusCallback {
	SurfaceView mySurfaceView;// surfaceView声明
	SurfaceHolder holder;// surfaceHolder声明
	Camera myCamera;// 相机声明
	String[] photoTypes, photoids;
	String currentPhotoType, filePath, plateID, bnsType, jyyXm;
	boolean isLongClicked = false;// 是否点击标识
	ImageView flashImageView, takePhotoImageView, savePhotoImageView, focusImageView, cancelPhotoImageView,
			increase_focal_length, minus_focal_length;
	private SeekBar seekbar_zoom;
	int startIndex = 0;
	TextView currentPhotoTextView, curLineNumTextView;
	int curPhotoLine = 1;
	boolean isOpenFlash = false;
	boolean isxh = true;
	private boolean photozhi = false;
	private Q11Domain theinformation;
	private String jkph;
	private String jcxdh;
	private String ip;
	private String jkxlh;
	private int tpscfs;
	private String tpscjk;
	private static final int MSG_SC_SHOW = 110111;
	private static final int MSG_SC_DISMISS = 110112;
	private static final int MSG_SC_CHAOSHI = 110113;
	private static final int MSG_SC_CUOWU = 110114;
	private static final int MSG_SC_XJKJXY = 110115;
	private static final int MSG_SC_SSCTCK = 110116;
	private static final int MSG_SC_MWLTCK = 110117;
	private String[] photoscdatac63 = { "jyxm", "jycs", "pssj", "hpzl", "hphm", "zpzl", "zp", "jylsh", "jcxdh", "clsbdh",
			"jyjgbh" };
	private ProgressDialog builder2 = null;
	private Message msg;
	private String localImagePath = Environment.getExternalStorageDirectory() + "/" + "MyVehPhoto/";
	private LinearLayout ex_lin;
	private SeekBar focusSb;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == MSG_SC_SHOW) {
				builder2 = new ProgressDialog(TakePhotoActivity.this);
				builder2.setMessage("正在上传中，请稍等。。。");
				builder2.setCancelable(false);
				builder2.show();
			}
			if (msg.what == MSG_SC_DISMISS) {
				builder2.dismiss();
			}
			if (msg.what == MSG_SC_CHAOSHI) {
				DialogTool.AlertDialogShow(TakePhotoActivity.this, "请求超时，请重新上传!");
			}
			if (msg.what == MSG_SC_CUOWU) {
				DialogTool.AlertDialogShow(TakePhotoActivity.this, msg.obj.toString());
			}
			if (msg.what == MSG_SC_XJKJXY) {
				ChangePhotoType();
				myCamera.startPreview();// 开启预览
				takePhotoImageView.setEnabled(true);
				flashImageView.setVisibility(View.VISIBLE);
				takePhotoImageView.setVisibility(View.VISIBLE);
				cancelPhotoImageView.setVisibility(View.INVISIBLE);
				savePhotoImageView.setVisibility(View.INVISIBLE);
				currentPhotoTextView.setEnabled(true);
			}
			if (msg.what == MSG_SC_SSCTCK) {
				AlertDialog.Builder builder1 = new AlertDialog.Builder(TakePhotoActivity.this);
				builder1.setTitle("提示框");
				builder1.setMessage("是否上传照片：" + currentPhotoType);
				builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									handler.sendEmptyMessage(MSG_SC_SHOW);
									if (getphotodatas() == null) {
										handler.sendEmptyMessage(MSG_SC_DISMISS);
										messegas(MSG_SC_CUOWU, "照片信息为空！");
									} else {
										String xmls = get18C63data(getphotodatas());
										String photoinformation = ConnectMethods.connectWebService(ip, StaticValues.writeObject,
												jkxlh, tpscjk, xmls, "writeObjectOutResult", StaticValues.timeoutThirty,
												StaticValues.numberThree);
										List<Code> codelists = XMLParsingMethods.saxcode(photoinformation);
										handler.sendEmptyMessage(MSG_SC_DISMISS);
										if (codelists.get(0).getCode().equals("1")) {
											Object[] data = { theinformation.getHphm(), "03", theinformation.getHphm(),
													currentPhotoType, theinformation.getJylsh() };
											SQLFuntion.update(TakePhotoActivity.this, data);
											if (isxh) {
												handler.sendEmptyMessage(MSG_SC_XJKJXY);
											} else {
												TakePhotoActivity.this.finish();
											}
										} else {
											messegas(MSG_SC_CUOWU, codelists.get(0).getMessage());
										}
									}
								} catch (Exception e) {
									handler.sendEmptyMessage(MSG_SC_DISMISS);
									handler.sendEmptyMessage(MSG_SC_CHAOSHI);
									e.printStackTrace();
								}
							}
						}).start();
					}
				});
				builder1.setNegativeButton("取消", null);
				builder1.show();
			}
			if (msg.what == MSG_SC_MWLTCK) {
				AlertDialog.Builder builder3 = new AlertDialog.Builder(TakePhotoActivity.this);
				builder3.setCancelable(false);
				builder3.setTitle("提示框");
				builder3.setMessage("网络异常,照片：" + currentPhotoType + "无法上传，请检测网络再重新拍摄照片！");
				builder3.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						TakePhotoActivity.this.finish();
					}
				});
				builder3.show();
			}
		};
	};

	// 创建jpeg图片回调数据对象
	PictureCallback jpeg = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			FileOutputStream fos = null;
			File file = new File(filePath);
			// 按照指定的路径创建文件夹
			if (!file.exists())
				file.mkdirs();
			String fileFullPath = filePath + currentPhotoType + "_temp.jpg";
			File file2 = new File(fileFullPath);
			if (file2.exists() && !file2.canWrite()) {
				Toast.makeText(TakePhotoActivity.this, "文件正常使用中，请稍后再试！", Toast.LENGTH_LONG).show();
			}

			try {
				fos = new FileOutputStream(fileFullPath);
				Bitmap photoBitmap = Bytes2Bimap(data);
				photoBitmap = PhotoTool.getPhotoByPixel(photoBitmap, 1280);
				// 加水印
				photoBitmap = createBitmap(photoBitmap, currentPhotoType, jyyXm);
				// 图片压缩
				data = PhotoTool.getCompressPhotoByPixel(photoBitmap);
				fos.write(data, 0, data.length);
				fos.close();
			} catch (Exception e) {
				if (fos != null)
					e.printStackTrace();
			}
			data = null;
			System.gc();
			flashImageView.setVisibility(View.INVISIBLE);
			takePhotoImageView.setVisibility(View.INVISIBLE);
			cancelPhotoImageView.setVisibility(View.VISIBLE);
			savePhotoImageView.setVisibility(View.VISIBLE);
			currentPhotoTextView.setEnabled(false);
			ex_lin.setVisibility(View.GONE);
		}
	};

	MyHandler myHandler;

	class MyHandler extends Handler {
		public MyHandler() {
		}

		// 子类必须重写此方法,接受数据
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				Toast.makeText(getApplicationContext(), msg.obj.toString(), 10).show();
				;
				break;
			}
		}
	}

	void HandlerMessageMethod(Object obj, int what) {
		Message msg = new Message();
		msg.obj = obj;
		msg.what = what;
		TakePhotoActivity.this.myHandler.sendMessage(msg);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.take_photo);
		SharedPreferences shareone = getSharedPreferences("jbsz", 1);
		isxh = shareone.getBoolean("SFXH", true);
		photozhi = shareone.getBoolean("PHOTOZ", false);
		SharedPreferences preferences = getSharedPreferences("cs_setup", 0);
		jkph = preferences.getString("JGPH", "");
		jcxdh = preferences.getString("JCXDH", "1");
		ip = preferences.getString("IP", "");
		jkxlh = preferences.getString("JKXLH", "");
		tpscfs = preferences.getInt("TPSCFS", 0);
		if (tpscfs == 0) {
			tpscjk = "18Q63";
		} else {
			tpscjk = "18C63";
		}
		Intent intent = getIntent();
		filePath = intent.getStringExtra("imagePath");
		photoTypes = intent.getStringArrayExtra("photoTypes");
		photoids = intent.getStringArrayExtra("photoIDs");
		plateID = intent.getStringExtra("plateID");
		bnsType = intent.getStringExtra("bnsType");
		currentPhotoType = intent.getStringExtra("photoType");
		jyyXm = intent.getStringExtra("jyyName");
		Bundle bundle = this.getIntent().getExtras();
		theinformation = (Q11Domain) bundle.getSerializable("informationsObj");
		for (int i = 0; i < photoTypes.length; i++)
			if (photoTypes[i].equals(currentPhotoType))
				startIndex = i;
		myHandler = new MyHandler();
		ex_lin = (LinearLayout) this.findViewById(R.id.ex_lin);//曝光度LinearLayout
		ex_lin.setVisibility(View.VISIBLE);
		takePhotoImageView = (ImageView) this.findViewById(R.id.takePhotoImageView); //
		takePhotoImageView.setOnClickListener(this);
		currentPhotoTextView = (TextView) this.findViewById(R.id.currentPhotoType);// 当前照片
		flashImageView = (ImageView) this.findViewById(R.id.flashImageView);
		focusImageView = (ImageView) this.findViewById(R.id.focusImageView);
		focusImageView.setVisibility(View.INVISIBLE);
		// 当前照片
		flashImageView.setOnClickListener(this);
		currentPhotoTextView.setText(currentPhotoType);
		currentPhotoTextView.setOnClickListener(this);
		currentPhotoTextView.setTextColor(Color.RED);
		currentPhotoTextView.setTextSize(20.0f);

		cancelPhotoImageView = (ImageView) this.findViewById(R.id.cancelPhotoImageView);// 取消
		savePhotoImageView = (ImageView) this.findViewById(R.id.savePhotoImageView); // 保存
		cancelPhotoImageView.setOnClickListener(this);
		savePhotoImageView.setOnClickListener(this);
		seekbar_zoom = (SeekBar) this.findViewById(R.id.seekbar_zoom);// 进度条
		seekbar_zoom.setOnSeekBarChangeListener(seekBarChange);
		focusSb = (SeekBar) findViewById(R.id.seekbar_ex);//曝光度SeekBar
		focusSb.setVisibility(View.VISIBLE);
		focusSb.setOnSeekBarChangeListener(seekBarChange);
		increase_focal_length = (ImageView) this.findViewById(R.id.increase_focal_length);// 放大
		minus_focal_length = (ImageView) this.findViewById(R.id.minus_focal_length);// 缩小
		increase_focal_length.setOnClickListener(this);
		minus_focal_length.setOnClickListener(this);
		Log.i("TakePhotoActivityCreate", "TakePhotoActivityCreate");
		// 获得控件
		mySurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
		mySurfaceView.setOnClickListener(this);
		mySurfaceView.setOnLongClickListener(this);
		// 获得句柄
		holder = mySurfaceView.getHolder();
		// 添加回调
		holder.addCallback(this);
		// 设置类型
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// 设置监听
		mySurfaceView.setOnClickListener(this);
		curLineNumTextView = (TextView) this.findViewById(R.id.curLineNumTextView);//
		SharedPreferences share = getSharedPreferences("nhhscamera", MODE_PRIVATE);
		curPhotoLine = share.getInt("PhotoLine", 1);
		curLineNumTextView.setText("线路" + curPhotoLine);
		curLineNumTextView.setTextColor(Color.WHITE);
	}

	/**
	 * 进度条拖动事件
	 */
	OnSeekBarChangeListener seekBarChange = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//			Camera.Parameters par = myCamera.getParameters();
//			par.setZoom(arg1);
//			myCamera.setParameters(par);
			if (seekBar.getId()==R.id.seekbar_zoom) {
				Camera.Parameters par = myCamera.getParameters();
				par.setZoom(progress);
				myCamera.setParameters(par);
			}
			if (seekBar.getId()==R.id.seekbar_ex){
				Camera.Parameters parameters = myCamera.getParameters();
				int i = progress - 5;
				Log.i("GGG",i+"");
				if (i<0){
					parameters.setExposureCompensation(i>-4?Math.abs(progress-5):4);
				}else {
					parameters.setExposureCompensation(i<4?(progress-5)*(-1):-4);
				}
				myCamera.setParameters(parameters);
			}
		}
	};

	private byte[] BitmapToBytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
		return baos.toByteArray();
	}

	public Bitmap BytesToBimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * 按正方形裁切图片
	 */
	public static Bitmap ImageCrop(Bitmap bitmap) {
		int w = bitmap.getWidth(); // 得到图片的宽，高
		int h = bitmap.getHeight();
		int ww = w;
		int wh = h / 3;
		int retX = 0;
		int retY = h / 3;
		return Bitmap.createBitmap(bitmap, retX, retY, ww, wh, null, false);
	}

	@Override
	public void onAutoFocus(boolean success, Camera camera) {
		try {
			if (success) {
				if (isLongClicked)
					myCamera.takePicture(null, null, jpeg);
			} else
				takePhotoImageView.setEnabled(true);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(TakePhotoActivity.this, "autoFocus:" + e.getMessage(), Toast.LENGTH_LONG).show();
		}
		isLongClicked = false;
	}

	@Override
	public boolean onLongClick(View v) {
		try {
			isLongClicked = true;
			myCamera.autoFocus(this);// 自动对焦

		} catch (Exception e) {
		}
		return true;
	}

	private String getzpid(String zpname) {
		String zpid = null;
		for (int i = 0; i < photoTypes.length; i++) {
			if (photoTypes[i].equals(zpname)) {
				zpid = photoids[i];
			}
		}
		return zpid;
	}

	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.takePhotoImageView: // 拍照
				takePhotoImageView.setEnabled(false);
				MediaPlayer m = MediaPlayer.create(TakePhotoActivity.this, R.raw.pazhao);
				m.setLooping(false);
				m.start();
				myCamera.takePicture(null, null, jpeg);
				break;
			case R.id.cancelPhotoImageView:// 取消
				String fileFullPath = filePath + currentPhotoType + "_temp.jpg" + curPhotoLine;
				File file = new File(fileFullPath);
				if (file.exists() && file.canWrite()) {
					file.delete();
				}
				myCamera.startPreview();// 开启预览
				takePhotoImageView.setEnabled(true);
				flashImageView.setVisibility(View.VISIBLE);
				takePhotoImageView.setVisibility(View.VISIBLE);
				cancelPhotoImageView.setVisibility(View.INVISIBLE);
				savePhotoImageView.setVisibility(View.INVISIBLE);
				currentPhotoTextView.setEnabled(true);
				break;
			case R.id.savePhotoImageView:// 保存
				if (!savephoto())
					return;

				// 添加车辆照片信息入数据库
				String currentImagePathtwo = filePath + "/" + currentPhotoType + ".jpg";
				File f = null;
				try {
					f = new File(currentImagePathtwo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (f.exists()) {
					ArrayList<HashMap<String, String>> zpsj = new ArrayList<HashMap<String, String>>();
					zpsj = SQLFuntion.query(TakePhotoActivity.this, theinformation.getHphm(), currentPhotoType,
							theinformation.getJylsh());
					if (zpsj.size() == 0) {
						Object[] tjdata = { theinformation.getJycs(), TimeTool.getTiem(), bnsType, theinformation.getHphm(),
								currentPhotoType, getzpid(currentPhotoType), theinformation.getJylsh(), jcxdh,
								theinformation.getClsbdh(), jkph, "01" };
						SQLFuntion.insert(TakePhotoActivity.this, tjdata);
					} else {
						Object[] data = { theinformation.getHphm(), "01", theinformation.getHphm(), currentPhotoType,
								theinformation.getJylsh() };
						SQLFuntion.update(TakePhotoActivity.this, data);
					}
				}

				// 判断该照片是否在联网情况下拍的
				new Thread(new Runnable() {
					@Override
					public void run() {
						boolean isintent = ConnectedJudge();
						if (isintent) {
							ArrayList<HashMap<String, String>> zpsjfour = SQLFuntion.query(TakePhotoActivity.this,
									theinformation.getHphm(), currentPhotoType, theinformation.getJylsh());
							if (!zpsjfour.get(0).get("isfuo").equals("00")) {
								if (photozhi) {
									handler.sendEmptyMessage(MSG_SC_SSCTCK);
								} else {
									String currentImagePaththree = filePath + "/" + currentPhotoType + ".jpg";
									File fthree = null;
									try {
										fthree = new File(currentImagePaththree);
									} catch (Exception e) {
										e.printStackTrace();
									}
									if (fthree.exists()) {
										Object[] data = { theinformation.getHphm(), "02", theinformation.getHphm(),
												currentPhotoType, theinformation.getJylsh() };
										SQLFuntion.update(TakePhotoActivity.this, data);
									}
									if (isxh) {
										handler.sendEmptyMessage(MSG_SC_XJKJXY);
									} else {
										TakePhotoActivity.this.finish();
									}
								}
							} else {
								messegas(MSG_SC_CUOWU, "照片异常，请重新拍照上传！");
							}
						} else {
							Object[] data = { theinformation.getHphm(), "00", theinformation.getHphm(), currentPhotoType,
									theinformation.getJylsh() };
							SQLFuntion.update(TakePhotoActivity.this, data);
							handler.sendEmptyMessage(MSG_SC_MWLTCK);
						}
					}
				}).start();
				break;
			case R.id.currentPhotoType:
				if (isxh) {
					ChangePhotoType();
				} else {
					TakePhotoActivity.this.finish();
				}
				break;
			case R.id.surfaceView1:
				try {
					if (cancelPhotoImageView.getVisibility() == View.INVISIBLE) {
						isLongClicked = false;
						myCamera.autoFocus(this);// 自动对焦
					}
				} catch (Exception e) {

				}
				break;
			case R.id.flashImageView:
				try {
					Camera.Parameters params = myCamera.getParameters();
					if (isOpenFlash) {
						params.setFlashMode(Parameters.FLASH_MODE_OFF);
						flashImageView.setImageResource(R.drawable.flash_off);
						isOpenFlash = false;
					} else {
						params.setFlashMode(Parameters.FLASH_MODE_TORCH);// FLASH_MODE_TORCH
						flashImageView.setImageResource(R.drawable.flash_on);
						isOpenFlash = true;
					}
					myCamera.setParameters(params);
				} catch (Exception e) {

				}
				break;
			case R.id.increase_focal_length:
				if (isSupportZoom()) {
					setZoom(true);
				}
				break;
			case R.id.minus_focal_length:
				if (isSupportZoom()) {
					setZoom(false);
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
		}
	}

	public boolean isSupportZoom() {
		return myCamera.getParameters().isSmoothZoomSupported();
	}

	public void setZoom(boolean isAdd) {
		try {
			Camera.Parameters mParameters = myCamera.getParameters();
			int Max = mParameters.getMaxZoom();
			Log.i("AAA", "Max:" + Max);
			int zoomValue = mParameters.getZoom();
			Log.i("AAA", "getZoom:" + zoomValue);
			if (isAdd) {
				// zoomValue = zoomValue + 1;
				zoomValue = zoomValue < Max ? zoomValue + 1 : zoomValue;
				Log.i("AAA", "ADD:" + zoomValue);
			} else {
				// zoomValue = zoomValue - 1;
				zoomValue = zoomValue > 0 ? zoomValue - 1 : zoomValue;
				Log.i("AAA", "MIN:" + zoomValue);
			}
			mParameters.setZoom(zoomValue);
			myCamera.setParameters(mParameters);
			seekbar_zoom.setProgress(zoomValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 判断该照片是否在联网情况下拍的
	private Boolean ConnectedJudge() {
		Log.i("SSS", "isConnected+++++++++");
		if (!isConnected()) {
			Log.i("SSS", "isConnected-----------");
			return false;
		} else {
			// 调用18C50接口
			try {
				handler.sendEmptyMessage(MSG_SC_SHOW);
				String timedata = "<?xml version=\"1.0\" encoding=\"GBK\"?><root><QueryCondition><babh>" + jkph
						+ "</babh></QueryCondition></root>";
				String timec50 = ConnectMethods.connectWebService(ip, StaticValues.queryObject, jkxlh, "18C50", timedata,
						"queryObjectOutResult", StaticValues.timeoutThree, StaticValues.numberFive);
				handler.sendEmptyMessage(MSG_SC_DISMISS);
				if (!timec50.equals("")) {
					return true;
				}
			} catch (Exception e) {
				handler.sendEmptyMessage(MSG_SC_DISMISS);
				return false;
			}
		}
		return false;
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

	private String get18C63data(String photodata) {
		String xmldata = null;
		List<String> datalist2 = new ArrayList<String>();
		datalist2.add("");
		datalist2.add(theinformation.getJycs() + "");
		datalist2.add(TimeTool.getTiem());
		datalist2.add(bnsType);
		datalist2.add(theinformation.getHphm());
		datalist2.add(getzpid(currentPhotoType));
		datalist2.add("");
		datalist2.add(theinformation.getJylsh());
		datalist2.add(jcxdh);
		datalist2.add(theinformation.getClsbdh());
		datalist2.add(jkph);
		xmldata = UnXmlTool.getPhotoXML(photoscdatac63, datalist2, photodata);
		Log.i("AAA", "get18C63data:" + xmldata);
		return xmldata;
	}

	private String getphotodatas() {
		String photodata = null;
		String currentImagePath = localImagePath + theinformation.getJylsh() + "/" + currentPhotoType + ".jpg";
		Log.i("AAA", currentImagePath);
		currentImagePath = FindFileFullPath(currentImagePath);
		Log.i("AAA", currentImagePath);
		photodata = PhotoTool.getphotodata(currentImagePath);
		return photodata;
	}

	String FindFileFullPath(String fileFulePath) {
		long curFileDate = 0;
		String lastFileName = fileFulePath;
		for (int i = 0; i < 20; i++) {
			File file = new File(fileFulePath + i);
			if (file.exists()) {
				long tempTime = file.lastModified();// 返回文件最后修改时间，是以个long型毫秒数
				if (curFileDate < tempTime) {
					lastFileName = file.getPath();
					curFileDate = tempTime;
				}
			}
		}
		return lastFileName;
	}

	public void messegas(int msgnumber, String neirong) {
		msg = new Message();
		msg.what = msgnumber;
		msg.obj = neirong;
		handler.sendMessage(msg);
	}

	void ChangePhotoType() {
		int currentIndex = 0;
		for (int i = 0; i < photoTypes.length; i++) {
			String photoTypeStr = photoTypes[i].replace(" ", "");
			String currentPhotoTypeStr = currentPhotoType.replace(" ", "");
			if (photoTypeStr.equals(currentPhotoTypeStr)) {
				currentIndex = i;
				break;
			}
		}
		String temp = "";
		String fileFullPath = "";
		while (true) {
			temp = photoTypes[currentIndex + 2 > photoTypes.length ? 0 : currentIndex + 1];
			for (int i = 0; i < photoTypes.length; i++)
				if (photoTypes[i].equals(temp) && i == startIndex)
					this.finish();
			fileFullPath = filePath + temp + ".jpg" + curPhotoLine;
			if (CommonData.GetUsingFileName().equals(fileFullPath)) {
				currentIndex = (currentIndex + 2) > photoTypes.length ? 0 : currentIndex + 1;
				Toast.makeText(TakePhotoActivity.this, temp + "正在使用中！", Toast.LENGTH_LONG).show();
			} else
				break;
		}

		currentPhotoType = temp;
		currentPhotoTextView.setText(currentPhotoType);
		fileFullPath = filePath + currentPhotoType + ".jpg" + curPhotoLine;
		CommonData.SetUsingFileName(fileFullPath);
		focusImageView.setVisibility(View.INVISIBLE);

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		try {
			Camera.Parameters params = myCamera.getParameters();
			// myCamera.setDisplayOrientation(90);
			params.setPictureFormat(PixelFormat.JPEG);
			Log.i("adf", "asf");
			WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			int widthR = display.getWidth();
			int heightR = display.getHeight();
			int newHeightR = (int) ((float) heightR / (float) 1200 * (float) 1600);

			List<Size> sizes = params.getSupportedPreviewSizes();

			Size optimalPreviewSize = getOptimalPreviewSize(sizes, widthR, heightR);
			params.setPreviewSize(optimalPreviewSize.width, optimalPreviewSize.height);
			params.setFlashMode(Parameters.FLASH_MODE_OFF);
			List<Size> psizes = params.getSupportedPictureSizes();

			Size optimalPictureSize = GetOptimalPictureSize(psizes);
			Log.i("AAA", "optimalPictureSize:" + optimalPictureSize.height + "    " + optimalPictureSize.width + "");
			params.setPictureSize(optimalPictureSize.width, optimalPictureSize.height);
			params.setJpegQuality(85);
			myCamera.setParameters(params);
			myCamera.startPreview();
		} catch (Exception e) {
			Toast.makeText(TakePhotoActivity.this, e.toString(), Toast.LENGTH_LONG).show();
		}
	}

	String GetFlashMode(List<String> lst) {
		String supFlash = " ";
		for (int i = 0; i < lst.size(); i++) {
			supFlash += lst.get(i) + "   ";
		}
		return supFlash;
	}

	/**
	 * 追加文件：使用FileOutputStream，在构造FileOutputStream时，把第二个参数设为true
	 * 
	 * @param fileName
	 * @param content
	 */
	public void writeSDFile(String str, String fileName) {
		try {
			FileWriter fw = new FileWriter(filePath + "//" + fileName, true);
			File f = new File(filePath + "//" + fileName);
			fw.write(str);
			FileOutputStream os = new FileOutputStream(f);
			DataOutputStream out = new DataOutputStream(os);
			out.writeShort(2);
			out.writeUTF("");
			System.out.println(out);
			fw.flush();
			fw.close();
			System.out.println(fw);
		} catch (Exception e) {
		}
	}

	private Size GetOptimalPictureSize(List<Size> sizes) {
		List<Size> tempSizes = new ArrayList<Size>();
		for (int i = 0; i < sizes.size(); i++) {
			if (1200 < sizes.get(i).width)
				tempSizes.add(sizes.get(i));
		}
		
		Size curSize = tempSizes.get(0);
		for (int i = 0; i < tempSizes.size(); i++) {
			if ((float) ((float) curSize.width / (float) curSize.height) <= (float) ((float) tempSizes.get(i).width / (float) tempSizes
					.get(i).height)) {
				curSize = tempSizes.get(i);
				Log.i("AAA", "curSize:" + curSize.width + "," + curSize.height);
			}
		}
		return curSize;
	}

	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.05;
		double targetRatio = (double) w / h;
		if (sizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = h;

		// Try to find an size match aspect ratio and size
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspect ratio, ignore the requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// 开启相机
		if (myCamera == null) {
			myCamera = Camera.open();
			seekbar_zoom.setMax(myCamera.getParameters().getMaxZoom());
			try {
				myCamera.setPreviewDisplay(holder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// 关闭预览并释放资源
		myCamera.stopPreview();
		myCamera.release();
		myCamera = null;

	}

	public boolean savephoto() {

		try {
			String newFillFullPath = filePath + currentPhotoType + ".jpg";
			DeleteOldFile(newFillFullPath);
			String tempFileFullPath = filePath + currentPhotoType + "_temp.jpg";
			File file = new File(tempFileFullPath);
			if (file.exists() && file.canWrite()) {
				file.renameTo(new File(newFillFullPath));
			}
			// ChangePhotoType();
			return true;
		} catch (Exception e) {
			Toast.makeText(TakePhotoActivity.this, e.toString(), Toast.LENGTH_LONG).show();
			return false;
		}
	}

	void DeleteOldFile(String fileFulePath) {
		for (int i = 0; i < 20; i++) {
			File file = new File(fileFulePath + i);
			if (file.exists()) {
				file.delete();
			}
		}
	}

	// 由Byte转为Bimap
	public Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	// 由Bimap转为Byte
	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}

	private Bitmap createBitmap(Bitmap photo, String strname, String newjyyxm) {
		String str = TimeTool.getTiem();
		String sjimei = "imei:" + ((TelephonyManager) TakePhotoActivity.this.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		String sjjyyxm = "检验员姓名：" + newjyyxm;
		int width = photo.getWidth(), hight = photo.getHeight();
		Bitmap icon = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888); // 建立一个空的BItMap
		Canvas canvas = new Canvas(icon);// 初始化画布绘制的图像到icon上

		Paint photoPaint = new Paint(); // 建立画笔
		photoPaint.setDither(true); // 获取跟清晰的图像采样
		photoPaint.setFilterBitmap(true);// 过滤一些

		Rect src = new Rect(0, 0, photo.getWidth(), photo.getHeight());// 创建一个指定的新矩形的坐标
		Rect dst = new Rect(0, 0, width, hight);// 创建一个指定的新矩形的坐标
		canvas.drawBitmap(photo, src, dst, photoPaint);// 将photo 缩放或则扩大到
														// dst使用的填充区photoPaint

		Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);// 设置画笔
		//textPaint.setTextSize(45.0f);// 字体大小
		textPaint.setTextSize(30.0f);// 字体大小
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);// 采用默认的宽度
		textPaint.setColor(Color.RED);// 采用的颜色

		//textPaint.setAlpha(60);
		//canvas.drawText(str, canvas.getWidth() - gettextwidth(str, 45.0f) - 50, 60, textPaint);// 绘制上去字，开始未知x,y采用那只笔绘制
		//canvas.drawText(strname, canvas.getWidth() - gettextwidth(strname, 45.0f) - 50, 130, textPaint);
		//canvas.drawText(sjimei, canvas.getWidth() - gettextwidth(sjimei, 45.0f) - 50, 200, textPaint);
		//canvas.drawText(sjjyyxm, canvas.getWidth() - gettextwidth(sjjyyxm, 45.0f) - 50, 270, textPaint);

		 canvas.drawText(str, 50, 30, textPaint);// 绘制上去字，开始未知x,y采用那只笔绘制
		 canvas.drawText(strname, 50, 70, textPaint);
		 canvas.drawText(sjimei, 50, 110, textPaint);
		 //canvas.drawText(str, 50, 60, textPaint);// 绘制上去字，开始未知x,y采用那只笔绘制
		 //canvas.drawText(strname, 50, 130, textPaint);
		 //canvas.drawText(sjimei, 50, 200, textPaint);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return icon;
	}

	// 获取字符串的宽度
	public float gettextwidth(String text, float Size) {
		TextPaint FontPaint = new TextPaint();
		FontPaint.setTextSize(Size);
		float textwidth = FontPaint.measureText(text);
		return textwidth;
	}

	// 按返回键时，显示弹出框
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			String fileFullPath = filePath + currentPhotoType + "_temp.jpg" + curPhotoLine;
			File file = new File(fileFullPath);
			if (cancelPhotoImageView.getVisibility() == View.VISIBLE && file.exists() && file.canWrite()) {
				file.delete();
			}
			finish();
			return true;
		}
		return false;
	}

	// Activity创建或者从后台重新回到前台时被调用
	@Override
	protected void onStart() {
		super.onStart();
		Log.i("ActivityLife", "onStart called.");
	}

	// Activity从后台重新回到前台时被调用
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i("ActivityLife", "onRestart called.");
	}

	// Activity创建或者从被覆盖、后台重新回到前台时被调用
	@Override
	protected void onResume() {
		super.onResume();
		Log.i("ActivityLife", "onResume called.");
		/**
		 * 设置为横屏
		 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}

	// Activity窗口获得或失去焦点时被调用,在onResume之后或onPause之后
	/*
	 * @Override public void onWindowFocusChanged(boolean hasFocus) {
	 * super.onWindowFocusChanged(hasFocus); Log.i(TAG,
	 * "onWindowFocusChanged called."); }
	 */

	// Activity被覆盖到下面或者锁屏时被调用
	@Override
	protected void onPause() {
		super.onPause();
		this.finish();
		Log.i("ActivityLife", "onPause called.");
		// 有可能在执行完onPause或onStop后,系统资源紧张将Activity杀死,所以有必要在此保存持久数据

	}

	// 退出当前Activity或者跳转到新Activity时被调用
	@Override
	protected void onStop() {
		super.onStop();
		Log.i("ActivityLife", "onStop called.");
	}

	// 退出当前Activity时被调用,调用之后Activity就结束了
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("ActivityLife", "onDestory called.");
	}
	// 1、不设置Activity的android:configChanges时，切屏会重新调用各个生命周期，切横屏时会执行一次，切竖屏时会执行两次
	// 2、设置Activity的android:configChanges="orientation"时，切屏还是会重新调用各个生命周期，切横、竖屏时只会执行一次
	// 3、设置Activity的android:configChanges="orientation|keyboardHidden"时，切屏不会重新调用各个生命周期，只会执行onConfigurationChanged方法

}
