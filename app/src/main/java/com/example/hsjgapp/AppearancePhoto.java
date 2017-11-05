package com.example.hsjgapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.example.hsjgapp.R;
import com.example.hsjgapp.DialogTool.DialogTool;
import com.example.hsjgapp.IntentUtil.ConnectMethods;
import com.example.hsjgapp.SQL.SQLFuntion;
import com.example.hsjgapp.ToolUtil.PanDuan;
import com.example.hsjgapp.XmlTool.UnXmlTool;
import com.example.hsjgapp.XmlTool.XMLParsingMethods;
import com.example.hsjgapp.domain.Q09Domain;
import com.example.hsjgapp.domain.Q13Domain;
import com.example.hsjgapp.domain.Q11Domain;
import com.example.hsjgapp.domain.Q22Domain;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

//图片界面
public class AppearancePhoto extends Activity {
	private ProgressDialog builder2 = null;
	private Message message;
	int holdPhotoDay = 0;
	int singleLineCount = 2;// 列的个数
	int lineCount = 0;// 行的个数
	int width;
	int height;
	private String tpname;
	private String ip;
	private String jkph;
	private String jkxlh;
	private String jcxdh;
	private TextView appearance_license_plate_number;
	private TextView appearance_plate_type;
	private TextView appearance_chassis_number;
	private TextView lpzp;
	private TextView phototextXY;
	private LinearLayout photoXY;
	private Button tj;
	private Spinner phototj;
	private boolean ph_pzqx;
	private LinearLayout appearance_imageview;
	private Q11Domain theinformation;
	private List<Q09Domain> operatorlists;
	private List<Q13Domain> photoName;
	private String[] newphotoid;
	private String[] newphotoname;
	private String lpphotodata = null;
	private String[] ifxs = null;
	private String wgxm;
	String localImagePath = Environment.getExternalStorageDirectory() + "/" + "MyVehPhoto/";
	private String[] fileType = null;
	private String[] filezpid = null;
	private static final int MSG_PHOTO = 10071;
	private static final int MSG_PHOTO_SHOW = 10072;
	private static final int MSG_PHOTO_DISMISS = 10073;
	private static final int MSG_PHOTO_FAIL = 10074;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == MSG_PHOTO) {
				RecyleImg();
				appearance_imageview.removeAllViews();
				AddNewJob(fileType, ifxs);
			}
			if (msg.what == MSG_PHOTO_SHOW) {
				builder2 = new ProgressDialog(AppearancePhoto.this);
				builder2.setMessage("正在加载中，请稍等。。。");
				builder2.setCanceledOnTouchOutside(false);
				builder2.show();
			}
			if (msg.what == MSG_PHOTO_DISMISS) {
				builder2.dismiss();
			}
			if (msg.what == MSG_PHOTO_FAIL) {
				DialogTool.AlertDialogShow(AppearancePhoto.this, msg.obj.toString());
			}
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.appearance_photo);
		ControlsInitialization();
		wgxm = getIntent().getStringExtra("LP");
		Bundle bundle = this.getIntent().getExtras();
		theinformation = (Q11Domain) bundle.getSerializable("informationsObj");
		operatorlists = (List<Q09Domain>) bundle.getSerializable("theoperatorlistsObj");
		photoName = (List<Q13Domain>) bundle.getSerializable("photonamelistObj");

		// 根据网络返回的照片数据进行更新
		if (photoName == null) {
			newphotoid = PanDuan.photoid;
			newphotoname = PanDuan.photoname;
		} else {
			newphotoid = new String[photoName.size()];
			newphotoname = new String[photoName.size()];
			for (int j = 0; j < photoName.size(); j++) {
				newphotoid[j] = photoName.get(j).getId();
				newphotoname[j] = photoName.get(j).getName();
			}
		}
		appearance_license_plate_number.setText(theinformation.getHphm());
		// appearance_plate_type.setText(PanDuan.Hpzl(theinformation.getHpzl()));
		appearance_plate_type.setText(theinformation.getHpzl());
		appearance_chassis_number.setText(theinformation.getClsbdh());
		tpname = theinformation.getJylsh();

		SharedPreferences preferences = getSharedPreferences("cs_setup", 0);
		ip = preferences.getString("IP", "");
		jkph = preferences.getString("JGPH", "");
		jkxlh = preferences.getString("JKXLH", "");
		jcxdh = preferences.getString("JCXDH", "");

		phototj = (Spinner) findViewById(R.id.phototj);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, newphotoname);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		phototj.setAdapter(adapter);

		if (wgxm.equals("BP")) {
			filezpid = theinformation.getBhgcyzp().split(",");
			String zpStr[] = theinformation.getBhgcyzpsm().split(",");
			fileType = new String[zpStr.length];
			for (int k = 0; k < zpStr.length; k++) {
				fileType[k] = zpStr[k];
			}
			phototextXY.setVisibility(View.GONE);
			photoXY.setVisibility(View.VISIBLE);
			lpzp.setVisibility(View.VISIBLE);
			AddNewJob(fileType, getphotozt(null, filezpid));
		}
		if (wgxm.equals("ZC")) {
			ph_pzqx = getIntent().getBooleanExtra("PZQX", false);
			if (!theinformation.getWgjyzp().equals("")) {
				if (ph_pzqx) {
					phototextXY.setVisibility(View.GONE);
					photoXY.setVisibility(View.VISIBLE);
					filezpid = theinformation.getWgjyzp().split(",");
					String zpStr[] = theinformation.getWgjyzpsm().split(",");
					fileType = new String[zpStr.length];
					for (int k = 0; k < zpStr.length; k++) {
						fileType[k] = zpStr[k];
					}
					AddNewJob(fileType, getphotozt(theinformation.getWgjyzp(), filezpid));
				}
			}
		}

		tj = (Button) findViewById(R.id.tj);
		tj.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String phototjdata = phototj.getSelectedItem().toString().trim();
				boolean sfcz = false;
				for (int g = 0; g < fileType.length; g++) {
					if (fileType[g].equals(phototjdata)) {
						sfcz = true;
					}
				}
				if (sfcz) {
					DialogTool.AlertDialogShow(AppearancePhoto.this, "该类型照片已存在，无法添加！");
				} else {
					int cd = fileType.length;
					String[] newfileType = fileType;
					String[] newfilezpid = filezpid;
					fileType = null;
					fileType = new String[cd + 1];
					for (int i = 0; i < newfileType.length; i++) {
						fileType[i] = newfileType[i];
					}
					fileType[cd] = phototjdata;
					filezpid = null;
					filezpid = new String[cd + 1];
					for (int i = 0; i < newfilezpid.length; i++) {
						filezpid[i] = newfilezpid[i];
					}
					filezpid[cd] = getphotoid(phototjdata);
					Log.i("AAA", fileType[cd] + filezpid[cd]);
					if (wgxm.equals("ZC")) {
						ifxs = getphotozt(lpphotodata, filezpid);
					} else {
						ifxs = getphotozt(null, filezpid);
					}
					handler.sendEmptyMessage(MSG_PHOTO);
				}
			}
		});

		Thread threadDelete = new Thread(deletePhotoRunnable);
		threadDelete.start();
	}

	// 图片名字转为图片ID
	public String getphotoid(String zpname) {
		String zpid = null;
		for (int i = 0; i < newphotoname.length; i++) {
			if (newphotoname[i].equals(zpname)) {
				zpid = newphotoid[i];
			}
		}
		return zpid;
	}

	// 图片ID转为图片名字
	public String getphotoname(String zpid) {
		String zpname = null;
		for (int i = 0; i < filezpid.length; i++) {
			if (filezpid[i].equals(zpid)) {
				zpname = fileType[i];
			}
		}
		return zpname;
	}

	public String[] getphotozt(String lpphoto2, String[] filezpid2) {
		String[] ifxstwo = new String[filezpid2.length];
		if (lpphoto2 != null) {
			String[] lpphotolist2 = lpphoto2.split(",");
			for (int i = 0; i < filezpid2.length; i++) {
				boolean zhi = false;
				for (int j = 0; j < lpphotolist2.length; j++) {
					if (lpphotolist2[j].equals(filezpid2[i])) {
						zhi = true;
					}
				}
				if (zhi) {
					ifxstwo[i] = "\n\n已上传";
				} else {
					ifxstwo[i] = "";
				}
			}
		} else {
			for (int k = 0; k < filezpid2.length; k++) {
				ifxstwo[k] = "";
			}
		}
		String[] ifyx = DetermineWhetherEffective(ifxstwo, filezpid2);
		return ifyx;
	};

	// 判断图片是否有效，可以上传
	private String[] DetermineWhetherEffective(String[] ifxsthree, String[] zpidthree) {
		String[] newifyx = new String[ifxsthree.length];
		for (int t = 0; t < ifxsthree.length; t++) {
			ArrayList<HashMap<String, String>> zpsjone = SQLFuntion.query(AppearancePhoto.this, theinformation.getHphm(),
					getphotoname(zpidthree[t]), theinformation.getJylsh());
			if (zpsjone.size() != 0) {
				if (zpsjone.get(0).get("isfuo").equals("00")) {
					newifyx[t] = "\n\n无效图片";
				} else {
					newifyx[t] = ifxsthree[t];
				}
			} else {
				newifyx[t] = ifxsthree[t];
			}

		}
		return newifyx;
	}

	private void ControlsInitialization() {
		appearance_license_plate_number = (TextView) findViewById(R.id.appearance_license_plate_number);
		appearance_plate_type = (TextView) findViewById(R.id.appearance_plate_type);
		appearance_chassis_number = (TextView) findViewById(R.id.appearance_chassis_number);
		phototextXY = (TextView) findViewById(R.id.phototextXY);
		photoXY = (LinearLayout) findViewById(R.id.photoXY);
		lpzp = (TextView) findViewById(R.id.lpzp);
		appearance_imageview = (LinearLayout) findViewById(R.id.appearance_imageview);
		WindowManager manager = getWindowManager();
		width = manager.getDefaultDisplay().getWidth();
		height = manager.getDefaultDisplay().getHeight();
	}

	void AddNewJob(String[] fileType, String[] ifxsdata) {
		if (fileType != null) {
			int total = fileType.length;// 图片名字长度
			for (int j = 1; j <= (total + 1 / 2); j++) {
				lineCount = (j + 1) / 2;
			}
			int currentIndex = 0;
			for (int i = 0; i < lineCount; i++) {
				LinearLayout linearLayout = CreateALinearLayout();
				for (int j = 0; j < singleLineCount; j++) {
					RelativeLayout relativeLayout = CreateARelativeLayout();
					ImageView imageView;
					TextView textView1;
					TextView textView2;
					if (currentIndex < total) {
						imageView = CreateAImageView(fileType[currentIndex]);
						textView1 = CreateATextView(fileType[currentIndex]);
						textView2 = CreateATextViewTwo(ifxsdata[currentIndex]);
					} else {
						imageView = CreateAImageView("");
						textView1 = CreateATextView("");
						textView2 = CreateATextViewTwo("");
					}
					if (imageView.getTag().toString() != "") {
						imageView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent1 = new Intent();
								String localImageFullPath = localImagePath + tpname.replace(" ", "") + "/"
										+ v.getTag().toString() + ".jpg";
								localImageFullPath = FindFileFullPath(localImageFullPath);
								File file = new File(localImageFullPath);
								if (!file.exists())
									return;
								Bundle bundle1 = new Bundle();
								bundle1.putSerializable("informationsObj", theinformation);
								intent1.putExtras(bundle1);
								intent1.putExtra("bnsType", appearance_plate_type.getText().toString().trim());
								intent1.putExtra("ZPID", getzpid(v.getTag().toString()));
								intent1.putExtra("plateID", tpname);
								intent1.putExtra("photoType", v.getTag().toString());
								intent1.putExtra("imageFullPath", localImageFullPath);
								intent1.putExtra("code", v.getTag().toString() + "_" + CommonData.GetImei());
								intent1.setClass(AppearancePhoto.this, ImageShowActivity.class);
								AppearancePhoto.this.startActivity(intent1);

							}
						});
						imageView.setOnLongClickListener(longClickListener);

					}
					relativeLayout.addView(imageView);
					relativeLayout.addView(textView1);
					relativeLayout.addView(textView2);
					linearLayout.addView(relativeLayout);
					currentIndex++;
				}
				appearance_imageview.addView(linearLayout);
			}
			SetImageViewImg();
		}
	}

	public String getzpid(String zhi) {
		String zszpid = null;
		for (int i = 0; i < fileType.length; i++) {
			if (fileType[i].equals(zhi)) {
				zszpid = filezpid[i];
			}
		}
		return zszpid;
	}

	public OnLongClickListener longClickListener = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			if (v.getTag().toString().equals(""))
				return true;
			String localTempImgDir = localImagePath + tpname + "/";
			String fileFullPath = localTempImgDir + v.getTag().toString() + ".jpg";
			fileFullPath = FindFileFullPath(fileFullPath);
			if (CommonData.GetUsingFileName().equals(fileFullPath)) {
				Toast.makeText(AppearancePhoto.this, "文件正常使用中，请稍后再试！", Toast.LENGTH_LONG).show();
				return true;
			}
			Intent intent = new Intent();
			intent.putExtra("imagePath", localTempImgDir);
			Bundle bundle = new Bundle();
			bundle.putSerializable("informationsObj", theinformation);
			intent.putExtras(bundle);
			intent.putExtra("photoTypes", fileType);
			intent.putExtra("photoIDs", filezpid);
			intent.putExtra("plateID", tpname);
			intent.putExtra("bnsType", appearance_plate_type.getText().toString().trim());
			intent.putExtra("photoType", v.getTag().toString());
			intent.putExtra("jyyName", operatorlists.get(0).getXm());
			intent.setClass(AppearancePhoto.this, TakePhotoActivity.class);
			RecyleImg();
			AppearancePhoto.this.startActivity(intent);
			return true;
		}
	};

	private void RecyleImg() {
		for (int i = 0; i < appearance_imageview.getChildCount(); i++) {
			View view = appearance_imageview.getChildAt(i);// LinearLayout

			for (int j = 0; j < ((LinearLayout) view).getChildCount(); j++) {
				View view2 = ((LinearLayout) view).getChildAt(j);// RelativeLayout
				for (int n = 0; n < ((RelativeLayout) view2).getChildCount(); n++) {
					View view3 = ((RelativeLayout) view2).getChildAt(n);
					boolean flag = view3.getClass().getName().contains("ImageView");
					if (flag) {
						// 定位到了ImageView控件
						if (view3.getTag().toString().equals(""))
							return;
						String currentImagePath = localImagePath + tpname + "/" + view3.getTag().toString() + ".jpg";
						currentImagePath = FindFileFullPath(currentImagePath);
						ImageView currentView = (ImageView) view3;
						currentView.setDrawingCacheEnabled(true);
						Bitmap bitmap = currentView.getDrawingCache();
						if (bitmap != null) {
							currentView.setImageBitmap(null);
							bitmap.recycle();
							currentView.setDrawingCacheEnabled(false);
						}
					}
				}
			}
		}
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

	@SuppressWarnings("deprecation")
	private LinearLayout CreateALinearLayout() {
		// TODO Auto-generated method stub
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT, 2));
		return linearLayout;
	}

	@SuppressWarnings("deprecation")
	private RelativeLayout CreateARelativeLayout() {
		RelativeLayout relativeLayout = new RelativeLayout(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 2);
		lp.setMargins(2, 2, 2, 2);
		relativeLayout.setBackgroundColor(Color.BLACK);
		relativeLayout.setLayoutParams(lp);
		relativeLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		return relativeLayout;
	}

	@SuppressWarnings("deprecation")
	private ImageView CreateAImageView(String picName) {
		// TODO Auto-generated method stub
		ImageView imageView = new ImageView(this);
		imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT, 2));
		imageView.setTag(picName);
		if (picName != "")
			imageView.setBackgroundColor(Color.BLACK);
		imageView.getLayoutParams().height = width / 3;
		return imageView;
	}

	private TextView CreateATextView(String text) {
		// TODO Auto-generated method stub
		TextView textView = new TextView(this);
		textView.setLayoutParams(new TextSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1));
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
		textView.setTextSize(20);
		textView.setTextColor(Color.WHITE);
		textView.setText(text);
		return textView;
	}

	private TextView CreateATextViewTwo(String text) {
		// TODO Auto-generated method stub
		TextView textView = new TextView(this);
		textView.setLayoutParams(new TextSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1));
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
		textView.setTextSize(20);
		textView.setTextColor(Color.RED);
		textView.setText(text);
		return textView;
	}

	private void SetImageViewImg() {
		for (int i = 0; i < appearance_imageview.getChildCount(); i++) {
			View view = appearance_imageview.getChildAt(i);// LinearLayout

			for (int j = 0; j < ((LinearLayout) view).getChildCount(); j++) {
				View view2 = ((LinearLayout) view).getChildAt(j);// RelativeLayout

				for (int n = 0; n < ((RelativeLayout) view2).getChildCount(); n++) {
					View view3 = ((RelativeLayout) view2).getChildAt(n);
					boolean flag = view3.getClass().getName().contains("ImageView");
					if (flag) {
						// 定位到了ImageView控件
						if (view3.getTag().toString().equals(""))
							return;
						String currentImagePath = localImagePath + tpname + "/" + view3.getTag().toString() + ".jpg";
						currentImagePath = FindFileFullPath(currentImagePath);
						ImageView currentView = (ImageView) view3;
						currentView.setDrawingCacheEnabled(true);
						Bitmap bitmap = currentView.getDrawingCache();
						if (bitmap != null) {
							currentView.setImageBitmap(null);
							bitmap.recycle();
							currentView.setDrawingCacheEnabled(false);
						}
						// 然后再显示新的图片
						currentView.setImageBitmap(GetImage(currentImagePath));
					}
				}
			}
		}
	}

	public Bitmap GetImage(String currentImagePath) {
		try {
			InputStream input = new FileInputStream(currentImagePath);
			byte[] imgData = new byte[input.available()];
			input.read(imgData);
			input.close();
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// BitmapFactory.decodeFile(currentImagePath, opts);
			BitmapFactory.decodeByteArray(imgData, 0, imgData.length, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, 256 * 256);
			opts.inJustDecodeBounds = false;
			Bitmap bmp = BitmapFactory.decodeByteArray(imgData, 0, imgData.length, opts);
			return bmp;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	byte[] encode(byte[] original, byte[] keyValue) {
		byte[] buffer = original;
		int index = 0;
		int length = keyValue.length;
		for (int i = 0; i < buffer.length; i += 10) {
			original[i] = (byte) (original[i] ^ keyValue[index]);
			index++;
			if (index == length) {
				index = 0;
			}
		}
		return buffer;
	}

	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double h = options.outWidth;
		double w = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
				Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	Runnable deletePhotoRunnable = new Runnable() {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(10000);
					// 执行文件夹的遍历删除
					File file = new File(localImagePath);
					File[] files1 = file.listFiles();
					if (files1 != null)
						for (int i = 0; i < files1.length; i++) {
							File fileTemp = files1[i];
							Date beginTime = new Date(fileTemp.lastModified());// 获取当前文件的最后修改时间
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							if (IsPassDate(formatter.format(beginTime)))
								DeleteAllFile(fileTemp.getPath());
						}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	boolean IsPassDate(String beginTimeStr) {
		SharedPreferences share = getSharedPreferences("nhhscamera", MODE_PRIVATE);
		holdPhotoDay = Integer.parseInt(share.getString("PhotoStayTime", "15"));

		long totalDate = 24 * 60 * 60 * holdPhotoDay;//
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date beginTime = null;
		try {
			beginTime = df.parse(beginTimeStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 初始时间
		Date endTime = new Date(System.currentTimeMillis());// 获取当前时间，结束时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long currentSeconds = getGapCount(beginTime, endTime);

		if (currentSeconds >= totalDate)
			return true;
		return false;
	}

	public static long getGapCount(Date startDate, Date endDate) {
		long result = (endDate.getTime() - startDate.getTime()) / 1000;
		return result;
	}

	void DeleteAllFile(String filePath) {
		File file = new File(filePath);
		if (file.isDirectory()) // 处理目录
		{
			File[] filesTemp = file.listFiles();
			for (int i = 0; i < filesTemp.length; i++)
				DeleteAllFile(filesTemp[i].getPath());
		}
		file.delete();
	}

	public void setmessegas(int msgnumber, String neirong) {
		message = new Message();
		message.what = msgnumber;
		message.obj = neirong;
		handler.sendMessage(message);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onRestart();
		try {
			CommonData.SetUsingFileName("");
			SetImageViewImg();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (wgxm.equals("ZC")) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						handler.sendEmptyMessage(MSG_PHOTO_SHOW);
						List<String> datalists = new ArrayList<String>();
						datalists.add(theinformation.getJylsh());
						datalists.add(theinformation.getClsbdh());
						String xmldata = UnXmlTool.getQueryXML(StaticValues.lpdataq22, datalists);
						String lpphotonames = ConnectMethods.connectWebService(ip, StaticValues.queryObject, jkxlh, "18Q22",
								xmldata, "queryObjectOutResult", StaticValues.timeoutFive, StaticValues.numberFive);
						List<Q22Domain> lpphotodatas = XMLParsingMethods.saxlpphotos(lpphotonames);
						lpphotodata = lpphotodatas.get(0).getYpzp();
						ifxs = getphotozt(lpphotodatas.get(0).getYpzp(), filezpid);
						handler.sendEmptyMessage(MSG_PHOTO_DISMISS);
						handler.sendEmptyMessage(MSG_PHOTO);
					} catch (Exception e) {
						handler.sendEmptyMessage(MSG_PHOTO_DISMISS);
						ifxs = getphotozt(lpphotodata, filezpid);
						handler.sendEmptyMessage(MSG_PHOTO);
						setmessegas(MSG_PHOTO_FAIL, "调18Q22接口失败！");
						e.printStackTrace();
					}

				}
			}).start();
		}
	}

	// 重写返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (wgxm.equals("ZC")) {
				AlertDialog.Builder newbuilder = new AlertDialog.Builder(AppearancePhoto.this);
				newbuilder.setTitle("提示框：");
				newbuilder.setMessage("是否确定退出？");
				newbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						onBackPressed();
					}
				});
				newbuilder.setNegativeButton("取消", null);
				newbuilder.show();
			} else {
				onBackPressed();
			}
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
