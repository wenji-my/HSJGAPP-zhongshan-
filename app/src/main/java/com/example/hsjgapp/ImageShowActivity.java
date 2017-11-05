package com.example.hsjgapp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.hsjgapp.R;
import com.example.hsjgapp.DialogTool.DialogTool;
import com.example.hsjgapp.IntentUtil.ConnectMethods;
import com.example.hsjgapp.R.id;
import com.example.hsjgapp.R.layout;
import com.example.hsjgapp.SQL.SQLFuntion;
import com.example.hsjgapp.ToolUtil.PhotoTool;
import com.example.hsjgapp.ToolUtil.TimeTool;
import com.example.hsjgapp.XmlTool.UnXmlTool;
import com.example.hsjgapp.XmlTool.XMLParsingMethods;
import com.example.hsjgapp.domain.Code;
import com.example.hsjgapp.domain.Q11Domain;
import com.example.hsjgapp.domain.Q13Domain;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageShowActivity extends Activity implements OnClickListener {

	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	private int mode = NONE;
	private float oldDist;
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private PointF start = new PointF();
	private PointF mid = new PointF();
	Bitmap bmp;
	String bnsType, zpid, localTempImgDir, plateID, photoType, code;
	private Q11Domain theinformation;
	private ProgressDialog builder2 = null;
	private Message message;
	private String jkph;
	private String jcxdh;
	private String ip;
	private String jkxlh;
	private int tpscfs;
	private String tpscjk;
	private String[] photoscdatac63 = { "jyxm", "jycs", "pssj", "hpzl", "hphm", "zpzl", "zp", "jylsh", "jcxdh", "clsbdh",
			"jyjgbh" };
	private String localImagePath = Environment.getExternalStorageDirectory() + "/" + "MyVehPhoto/";
	private static final int MSG_ZS_SHOW = 111111;
	private static final int MSG_ZS_DISMISS = 111112;
	private static final int MSG_ZS_CHAOSHI = 111113;
	private static final int MSG_ZS_CUOWU = 111114;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == MSG_ZS_SHOW) {
				builder2 = new ProgressDialog(ImageShowActivity.this);
				builder2.setMessage("正在上传中，请稍等。。。");
				builder2.setCancelable(false);
				builder2.show();
			}
			if (msg.what == MSG_ZS_DISMISS) {
				builder2.dismiss();
			}
			if (msg.what == MSG_ZS_CHAOSHI) {
				DialogTool.AlertDialogShow(ImageShowActivity.this, "请求超时，请重新上传!");
			}
			if (msg.what == MSG_ZS_CUOWU) {
				DialogTool.AlertDialogShow(ImageShowActivity.this, msg.obj.toString());
			}
		};
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (bmp != null)
			bmp.recycle();
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imageshow);
		try {
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
			Bundle bundle = this.getIntent().getExtras();
			theinformation = (Q11Domain) bundle.getSerializable("informationsObj");
			Intent intent = getIntent();
			bnsType = intent.getStringExtra("bnsType");
			zpid = intent.getStringExtra("ZPID");
			localTempImgDir = intent.getStringExtra("imageFullPath");
			plateID = intent.getStringExtra("plateID");
			photoType = intent.getStringExtra("photoType");
			code = intent.getStringExtra("code");
			bmp = getLoacalBitmap(localTempImgDir);

			ImageView view = (ImageView) findViewById(R.id.imageViewShow);
			view.setImageBitmap(bmp);

			int screenWith, screenHeight;
			WindowManager windowManager = getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			screenWith = display.getWidth();
			screenHeight = display.getHeight();
			float scale = (float) screenWith / (float) bmp.getWidth();
			// Toast.makeText(ImageShowActivity.this,
			// screenWith + "  " +screenHeight+" "+bmp.getWidth()+ " "+
			// bmp.getHeight()+" "+(float) bmp.getHeight()*scale+" "+Math
			// .abs(((float) screenHeight - ((float) bmp.getHeight()*scale)) /
			// 2), Toast.LENGTH_LONG)
			// .show();

			matrix.postScale(scale, scale, 0, Math.abs(((float) screenHeight - (float) bmp.getHeight() * scale) / 2));
			view.setImageMatrix(matrix);
			view.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					ImageView view = (ImageView) v;
					switch (event.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_DOWN:
						savedMatrix.set(matrix);
						start.set(event.getX(), event.getY());
						mode = DRAG;
						break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_POINTER_UP:
						mode = NONE;
						break;
					case MotionEvent.ACTION_POINTER_DOWN:
						oldDist = spacing(event);
						if (oldDist > 10f) {
							savedMatrix.set(matrix);
							midPoint(mid, event);
							mode = ZOOM;
						}
						break;
					case MotionEvent.ACTION_MOVE:
						if (mode == DRAG) {
							matrix.set(savedMatrix);
							matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
						} else if (mode == ZOOM) {
							float newDist = spacing(event);
							if (newDist > 10f) {
								matrix.set(savedMatrix);
								float scale = newDist / oldDist;
								matrix.postScale(scale, scale, mid.x, mid.y);
							}
						}
						break;
					}
					view.setImageMatrix(matrix);
					return true;
				}

				private float spacing(MotionEvent event) {
					float x = event.getX(0) - event.getX(1);
					float y = event.getY(0) - event.getY(1);
					return FloatMath.sqrt(x * x + y * y);
				}

				private void midPoint(PointF point, MotionEvent event) {
					float x = event.getX(0) + event.getX(1);
					float y = event.getY(0) + event.getY(1);
					point.set(x / 2, y / 2);
				}
			});

			ImageView deleteImageView = (ImageView) findViewById(R.id.deleteImageView);
			deleteImageView.setOnClickListener(clickListener);
			ImageView updataImageView = (ImageView) findViewById(R.id.updataImageView);
			updataImageView.setOnClickListener(updataclickListener);
			IsShow(updataImageView);// 判断是否显示提交按钮
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(ImageShowActivity.this, e.toString(), Toast.LENGTH_LONG).show();
		}

	}

	private void IsShow(ImageView updataImageView) {
		ArrayList<HashMap<String, String>> sjdatas = SQLFuntion.query(ImageShowActivity.this, theinformation.getHphm(),
				photoType, theinformation.getJylsh());
		if (sjdatas.get(0).get("isfuo").equals("00")) {
			updataImageView.setVisibility(View.GONE);
		}
	}

	public OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			new AlertDialog.Builder(ImageShowActivity.this).setTitle("删除警告").setMessage("您确认要删除这张图片吗？")
					.setPositiveButton("确认", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							File file = new File(localTempImgDir);
							if (file.exists() && file.canWrite()) {
								file.delete();
							}
							// DBVehPhotoHelper helper = new
							// DBVehPhotoHelper(ImageShowActivity.this);
							// ContentValues values = new
							// ContentValues();
							// values.put("isUploadOk", -1);
							// boolean ret =
							// helper.update(values,plateID, photoType);
							ImageShowActivity.this.finish();
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					}).show();

		}
	};

	public OnClickListener updataclickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			new AlertDialog.Builder(ImageShowActivity.this).setTitle("上传照片").setMessage("您是否确认上传照片 : " + photoType + "?")
					.setPositiveButton("确认", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							new Thread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									try {
										handler.sendEmptyMessage(MSG_ZS_SHOW);
										if (getphotodatas() == null) {
											handler.sendEmptyMessage(MSG_ZS_DISMISS);
											setmessegas(MSG_ZS_CUOWU, "读到的照片信息为空！");
										} else {
											String xmls = get18C63data(getphotodatas());
											String photoinformation = ConnectMethods.connectWebService(ip,
													StaticValues.writeObject, jkxlh, tpscjk, xmls, "writeObjectOutResult",
													StaticValues.timeoutNine, StaticValues.numberThree);
											List<Code> codelists = XMLParsingMethods.saxcode(photoinformation);
											handler.sendEmptyMessage(MSG_ZS_DISMISS);
											if (codelists.get(0).getCode().equals("1")) {
												Object[] data = { theinformation.getHphm(), "03", theinformation.getHphm(),
														photoType, theinformation.getJylsh() };
												SQLFuntion.update(ImageShowActivity.this, data);
												setmessegas(MSG_ZS_CUOWU, "照片上传成功！");
											} else {
												setmessegas(MSG_ZS_CUOWU, codelists.get(0).getMessage());
											}
										}
									} catch (Exception e) {
										handler.sendEmptyMessage(MSG_ZS_DISMISS);
										handler.sendEmptyMessage(MSG_ZS_CHAOSHI);
										e.printStackTrace();
									}
								}
							}).start();
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).show();

		}
	};

	private String get18C63data(String photodata) {
		String xmldata = null;
		List<String> datalist2 = new ArrayList<String>();
		datalist2.add("");
		datalist2.add(theinformation.getJycs() + "");
		datalist2.add(TimeTool.getTiem());
		datalist2.add(bnsType);
		datalist2.add(theinformation.getHphm());
		datalist2.add(zpid);
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
		String currentImagePath = localImagePath + theinformation.getJylsh() + "/" + photoType + ".jpg";
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

	public void setmessegas(int msgnumber, String neirong) {
		message = new Message();
		message.what = msgnumber;
		message.obj = neirong;
		handler.sendMessage(message);
	}

	@Override
	public void onClick(View v) {
		new AlertDialog.Builder(this).setTitle("删除警告").setMessage("您确认要删除这张图片吗？")
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						File file = new File(localTempImgDir);
						if (file.exists() && file.canWrite()) {
							file.delete();
						}
						ImageShowActivity.this.finish();
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).show();

	}

	/**
	 * 加载本地图片 http://bbs.3gstdy.com
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(String url) {
		try {
			Bitmap bmp = BitmapFactory.decodeFile(url);
			return bmp;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
