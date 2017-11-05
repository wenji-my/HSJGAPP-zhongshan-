package com.example.hsjgapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hsjgapp.DialogTool.DialogTool;
import com.example.hsjgapp.IntentUtil.ConnectMethods;
import com.example.hsjgapp.SpecialEffects.LateraSpreadsMenu.ActionBarDrawerToggle;
import com.example.hsjgapp.SpecialEffects.LateraSpreadsMenu.DrawerArrowDrawable;
import com.example.hsjgapp.ToolUtil.PanDuan;
import com.example.hsjgapp.XmlTool.UnXmlTool;
import com.example.hsjgapp.XmlTool.XMLParsingMethods;
import com.example.hsjgapp.domain.Q09Domain;
import com.example.hsjgapp.domain.Q11Domain;
import com.example.hsjgapp.domain.Q13Domain;
import com.example.hsjgapp.domain.Q32Domain;
import com.example.hsjgapp.domain.Q33Domain;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class FunctionActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private ProgressDialog builder2 = null;
    private TextView function_license_plate_number;
    private TextView function_plate_type;
    private TextView function_jylsh;
    private Button inspection_test;
    private Button photo_collection;
    private Button dynamic_chassis;
    private Button chassis_inspection;
    private Button road_test_to_move;
    private Button slope_of_brake;
    private Button online_scheduling;
    private Button back_office;
    private LinearLayout lsfrist, lstwo;
    private Q11Domain theinformation;
    private List<Q09Domain> operatorlists;
    private String ip;
    private String jkph;
    private String jkxlh;
    private String zdhphm;
    private String zdjylsh;
    private String userid;
    private String jcxdh = "1";
    private int jycs;
    private boolean wgqx = false, dtqx = false, dpqx = false, pzqx = false,
            lsqx = false, bpqx = false;
    private boolean wgdataxz = false;
    private boolean lsczz = true;
    private String[] zdhphmdata = {"hphm"};
    private String[] q32datas = {"jyjgbh"};
    private String[] q33datas = {"jylsh", "jycs", "jyxm"};
    private static final int MSG_FUNCTION_SHOW = 10031;
    private static final int MSG_FUNCTION_SHOW_TWO = 10037;
    private static final int MSG_FUNCTION_DISMISS = 10032;
    private static final int MSG_FUNCTION_CHAOSHI = 10033;
    private static final int MSG_FUNCTION_FUZHI = 10034;
    private static final int MSG_FUNCTION_TOASTS = 10035;
    private static final int MSG_FUNCTION_TOASTF = 10036;
    private static final int MSG_WAIGUAN = 0x001;
    private static final int MSG_SHOW = 0x002;
    private static final int MSG_DISMISS = 0x003;
    private Socket client;

    @SuppressLint("HandlerLeak")
    private Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_SHOW) {
                builder2 = new ProgressDialog(FunctionActivity.this);
                builder2.setMessage("正在登陆中，请稍等。。。");
                builder2.setCancelable(false);
                builder2.show();
            }
            if (msg.what == MSG_DISMISS) {
                builder2.dismiss();
            }
            if (msg.what == MSG_WAIGUAN) {

                String content = (String) msg.obj;
                if (!TextUtils.isEmpty(content)){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(FunctionActivity.this);
                    builder1.setTitle("提示框");
                    builder1.setMessage(content);
                    builder1.setPositiveButton("确定", null);
                    builder1.show();
                }
            }

            if (msg.what == MSG_FUNCTION_SHOW_TWO) {
                builder2 = new ProgressDialog(FunctionActivity.this);
                builder2.setMessage("正在加载中，请稍等。。。");
                builder2.setCanceledOnTouchOutside(false);
                builder2.show();
            }
            if (msg.what == MSG_FUNCTION_SHOW) {
                builder2 = new ProgressDialog(FunctionActivity.this);
                builder2.setMessage("正在加载中，请稍等。。。");
                builder2.setCancelable(false);
                builder2.show();
            }
            if (msg.what == MSG_FUNCTION_DISMISS) {
                builder2.dismiss();
            }
            if (msg.what == MSG_FUNCTION_CHAOSHI) {
                DialogTool.AlertDialogShow(FunctionActivity.this,
                        "请求超时，请检查网络环境!");
            }
            if (msg.what == MSG_FUNCTION_TOASTS) {
                setToast("已更新数据");
            }
            if (msg.what == MSG_FUNCTION_TOASTF) {
                setToast("更新数据失败！");
            }
            if (msg.what == MSG_FUNCTION_FUZHI) {
                inspection_test.setEnabled(false);
                dynamic_chassis.setEnabled(false);
                chassis_inspection.setEnabled(false);
                road_test_to_move.setEnabled(false);
                slope_of_brake.setEnabled(false);
                photo_collection.setEnabled(false);
                back_office.setEnabled(false);
                function_license_plate_number.setText(theinformation.getHphm());
                function_plate_type.setText(PanDuan.Hpzl(theinformation
                        .getHpzl()));
                function_jylsh.setText(theinformation.getJylsh());
                if (theinformation.getSlzt().equals("11")
                        || theinformation.getSlzt().equals("12")) {
                    String zpStr[] = theinformation.getJyxm().split(",");
                    for (int j = 0; j < zpStr.length; j++) {
                        if (zpStr[j].equals("F1")) {
                            if (!theinformation.getWgjcxm().equals("")) {
                                if (wgqx) {
                                    inspection_test.setEnabled(true);
                                }
                            }
                        } else if (zpStr[j].equals("DC")) {
                            if (!theinformation.getDtdpjyxm().equals("")) {
                                if (dtqx) {
                                    dynamic_chassis.setEnabled(true);
                                }
                            }
                        } else if (zpStr[j].equals("C1")) {
                            if (!theinformation.getDpjyxm().equals("")) {
                                if (dpqx) {
                                    chassis_inspection.setEnabled(true);
                                }
                            }
                        } else if (zpStr[j].equals("R1")
                                || zpStr[j].equals("R2")
                                || zpStr[j].equals("R3")) {
                            if (lsqx) {
                                if (zpStr[j].equals("R1")) {
                                    road_test_to_move.setEnabled(true);
                                }
                                if (zpStr[j].equals("R2")) {
                                    slope_of_brake.setEnabled(true);
                                }
                                back_office.setEnabled(true);
                            }
                        }
                    }
                    if (!theinformation.getWgjyzp().equals("")) {
                        if (pzqx) {
                            inspection_test.setEnabled(true);
                        }
                    }
                } else {
                    if (!theinformation.getBhgcyzp().equals(""))
                        if (bpqx) {
                            photo_collection.setEnabled(true);
                        }
                }
            }
        }
    };


    @SuppressLint("NewApi")
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.function);
        ChuShiHua();
        SharedPreferences preferences = getSharedPreferences("cs_setup", 0);
        ip = preferences.getString("IP", "");
        jkph = preferences.getString("JGPH", "");
        jkxlh = preferences.getString("JKXLH", "");
        SharedPreferences share = getSharedPreferences("jbsz", 1);
        wgdataxz = share.getBoolean("DATAXZ", false);
        lsczz = share.getBoolean("LSXZ", true);
        zdhphm = getIntent().getStringExtra("ZDHPHM");
        zdjylsh = getIntent().getStringExtra("ZDJYLSH");
        jycs = getIntent().getIntExtra("JYCS", 1);
        userid = getIntent().getStringExtra("USERID");
        Bundle bundle = this.getIntent().getExtras();
        operatorlists = (List<Q09Domain>) bundle
                .getSerializable("theoperatorlistsObj");
        String[] qxdatas = operatorlists.get(0).getPdaqx().split(",");
        for (int k = 0; k < qxdatas.length; k++) {
            if (qxdatas[k].equals("3")) {
                wgqx = true;
            } else if (qxdatas[k].equals("5")) {
                dtqx = true;
                online_scheduling.setEnabled(true);
            } else if (qxdatas[k].equals("6")) {
                dpqx = true;
            } else if (qxdatas[k].equals("4")) {
                pzqx = true;
            } else if (qxdatas[k].equals("9")) {
                lsqx = true;
            } else if (qxdatas[k].equals("8")) {
                bpqx = true;
            }
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navdrawer);

        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                drawerArrow, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        String[] values = new String[]{"车辆照片上传状态", "基本参数设置", "密码更改"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        mDrawerList.setAdapter(adapter);
        mDrawerList
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        switch (position) {
                            case 0:
                                Intent intent11 = new Intent(FunctionActivity.this,
                                        SCPhotos.class);
                                startActivity(intent11);
                                break;
                            case 1:
                                Intent intent12 = new Intent(FunctionActivity.this,
                                        BasicSetup.class);
                                startActivity(intent12);
                                break;
                            case 2:
                                Intent intent13 = new Intent(FunctionActivity.this,
                                        NewPassWordActivity.class);
                                intent13.putExtra("USERID", userid);
                                startActivity(intent13);
                                break;
                        }
                    }
                });

    }

    private void ChuShiHua() {
        function_license_plate_number = (TextView) findViewById(R.id.function_license_plate_number);
        function_plate_type = (TextView) findViewById(R.id.function_plate_type);
        function_jylsh = (TextView) findViewById(R.id.function_jylsh);
        inspection_test = (Button) findViewById(R.id.inspection_test);
        photo_collection = (Button) findViewById(R.id.photo_collection);
        dynamic_chassis = (Button) findViewById(R.id.dynamic_chassis);
        chassis_inspection = (Button) findViewById(R.id.chassis_inspection);
        road_test_to_move = (Button) findViewById(R.id.road_test_to_move);
        slope_of_brake = (Button) findViewById(R.id.slope_of_brake);
        back_office = (Button) findViewById(R.id.back_office);
        online_scheduling = (Button) findViewById(R.id.online_scheduling);
        lsfrist = (LinearLayout) findViewById(R.id.lsfrist);
        lstwo = (LinearLayout) findViewById(R.id.lstwo);
    }

    public void doClick(View v) {
        switch (v.getId()) {
            // 外检
            case R.id.inspection_test:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            hander.sendEmptyMessage(MSG_FUNCTION_SHOW);
                            Intent intent2 = new Intent();
                            Bundle bundle2 = new Bundle();
                            intent2.setClass(FunctionActivity.this,
                                    TabHostOne.class);
                            List<String> newclbhexx = null;
                            String newjyyjy = null;
                            // 外检数据请求
                            List<Q32Domain> newbhgxxxs = getq32bhgxdata();
                            Q33Domain Vehiclebuheges = get33sft(theinformation,
                                    "F1");
                            if (Vehiclebuheges != null) {
                                newclbhexx = Vehiclebuheges.getWgbhgyy();
                                newjyyjy = Vehiclebuheges.getJyyjy();
                            }
                            hander.sendEmptyMessage(MSG_FUNCTION_DISMISS);
                            // 外检照片总类请求
                            List<Q13Domain> photonamelist = null;
                            try {
                                hander.sendEmptyMessage(MSG_FUNCTION_SHOW);
                                photonamelist = GetPhotoDatas();
                                hander.sendEmptyMessage(MSG_FUNCTION_DISMISS);
                                hander.sendEmptyMessage(MSG_FUNCTION_TOASTS);
                            } catch (Exception e) {
                                hander.sendEmptyMessage(MSG_FUNCTION_DISMISS);
                                hander.sendEmptyMessage(MSG_FUNCTION_TOASTF);
                                e.printStackTrace();
                            }
                            intent2.putExtra("JY", newjyyjy);
                            intent2.putExtra("WJQX", wgqx);
                            intent2.putExtra("PZQX", pzqx);
                            bundle2.putSerializable("photonamelistObj",
                                    (Serializable) photonamelist);
                            bundle2.putSerializable("thenewbhgxxxsObj",
                                    (Serializable) newbhgxxxs);
                            bundle2.putSerializable("thenewVehicleBuHeGesObj",
                                    (Serializable) newclbhexx);
                            bundle2.putSerializable("informationsObj",
                                    theinformation);
                            bundle2.putSerializable("theoperatorlistsObj",
                                    (Serializable) operatorlists);
                            intent2.putExtras(bundle2);
                            startActivity(intent2);
                        } catch (Exception e) {
                            hander.sendEmptyMessage(MSG_FUNCTION_DISMISS);
                            hander.sendEmptyMessage(MSG_FUNCTION_CHAOSHI);
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            // 照片补拍
            case R.id.photo_collection:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent7 = new Intent(FunctionActivity.this,
                                AppearancePhoto.class);
                        intent7.putExtra("LP", "BP");
                        Bundle bundle7 = new Bundle();
                        List<Q13Domain> photonamelist = null;
                        try {
                            hander.sendEmptyMessage(MSG_FUNCTION_SHOW);
                            photonamelist = GetPhotoDatas();
                            hander.sendEmptyMessage(MSG_FUNCTION_DISMISS);
                            hander.sendEmptyMessage(MSG_FUNCTION_TOASTS);
                        } catch (Exception e) {
                            hander.sendEmptyMessage(MSG_FUNCTION_DISMISS);
                            hander.sendEmptyMessage(MSG_FUNCTION_TOASTF);
                            e.printStackTrace();
                        }
                        bundle7.putSerializable("photonamelistObj",
                                (Serializable) photonamelist);
                        bundle7.putSerializable("informationsObj", theinformation);
                        bundle7.putSerializable("theoperatorlistsObj",
                                (Serializable) operatorlists);
                        intent7.putExtras(bundle7);
                        startActivity(intent7);
                    }
                }).start();
                break;
            // 动态底盘
            case R.id.dynamic_chassis:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            hander.sendEmptyMessage(MSG_FUNCTION_SHOW);
                            Intent intent3 = new Intent();
                            Bundle bundle3 = new Bundle();
                            if (!wgdataxz) {
                                intent3.setClass(FunctionActivity.this,
                                        MaulDetActivitySpinner.class);
                                List<String> newclbhexx = null;
                                String newjyyjy = null;
                                List<Q32Domain> newbhgxxxs = getq32bhgxdata();
                                Q33Domain Vehiclebuheges = get33sft(theinformation,
                                        "DC");
                                if (Vehiclebuheges != null) {
                                    newclbhexx = Vehiclebuheges.getDtbhgyy();
                                    newjyyjy = Vehiclebuheges.getJyyjy();
                                }
                                intent3.putExtra("JY", newjyyjy);
                                bundle3.putSerializable("thenewbhgxxxsObj",
                                        (Serializable) newbhgxxxs);
                                bundle3.putSerializable("thenewVehicleBuHeGesObj",
                                        (Serializable) newclbhexx);
                            } else {
                                intent3.setClass(FunctionActivity.this,
                                        MaulDetActivityEditText.class);
                            }
                            intent3.putExtra("XM", "DC");
                            bundle3.putSerializable("informationsObj",
                                    theinformation);
                            bundle3.putSerializable("theoperatorlistsObj",
                                    (Serializable) operatorlists);
                            intent3.putExtras(bundle3);
                            hander.sendEmptyMessage(MSG_FUNCTION_DISMISS);
                            startActivity(intent3);
                        } catch (Exception e) {
                            hander.sendEmptyMessage(MSG_FUNCTION_DISMISS);
                            hander.sendEmptyMessage(MSG_FUNCTION_CHAOSHI);
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            // 底盘
            case R.id.chassis_inspection:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            hander.sendEmptyMessage(MSG_FUNCTION_SHOW);
                            Intent intent4 = new Intent();
                            Bundle bundle4 = new Bundle();
                            if (!wgdataxz) {
                                intent4.setClass(FunctionActivity.this,
                                        MaulDetActivitySpinner.class);
                                List<String> newclbhexx = null;
                                String newjyyjy = null;
                                List<Q32Domain> newbhgxxxs = getq32bhgxdata();
                                Q33Domain Vehiclebuheges = get33sft(theinformation,
                                        "C1");
                                if (Vehiclebuheges != null) {
                                    newclbhexx = Vehiclebuheges.getDpbhgyy();
                                    newjyyjy = Vehiclebuheges.getJyyjy();
                                }
                                intent4.putExtra("JY", newjyyjy);
                                bundle4.putSerializable("thenewbhgxxxsObj",
                                        (Serializable) newbhgxxxs);
                                bundle4.putSerializable("thenewVehicleBuHeGesObj",
                                        (Serializable) newclbhexx);
                            } else {
                                intent4.setClass(FunctionActivity.this,
                                        MaulDetActivityEditText.class);
                            }
                            intent4.putExtra("XM", "C1");
                            bundle4.putSerializable("informationsObj",
                                    theinformation);
                            bundle4.putSerializable("theoperatorlistsObj",
                                    (Serializable) operatorlists);
                            intent4.putExtras(bundle4);
                            hander.sendEmptyMessage(MSG_FUNCTION_DISMISS);
                            startActivity(intent4);
                        } catch (Exception e) {
                            hander.sendEmptyMessage(MSG_FUNCTION_DISMISS);
                            hander.sendEmptyMessage(MSG_FUNCTION_CHAOSHI);
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            // 路试制动
            case R.id.road_test_to_move:
                Intent intent6 = new Intent(FunctionActivity.this,
                        RoadTestToMove.class);
                Bundle bundle6 = new Bundle();
                bundle6.putSerializable("informationsObj", theinformation);
                bundle6.putSerializable("theoperatorlistsObj",
                        (Serializable) operatorlists);
                intent6.putExtras(bundle6);
                startActivity(intent6);
                break;
            // 驻坡制动
            case R.id.slope_of_brake:
                Intent intent9 = new Intent(FunctionActivity.this,
                        SlopeOfBrake.class);
                Bundle bundle9 = new Bundle();
                bundle9.putSerializable("informationsObj", theinformation);
                bundle9.putSerializable("theoperatorlistsObj",
                        (Serializable) operatorlists);
                intent9.putExtras(bundle9);
                startActivity(intent9);
                break;
            // 退办
            case R.id.back_office:
                Intent intent8 = new Intent(FunctionActivity.this,
                        BrakeRoadTest.class);
                Bundle bundle8 = new Bundle();
                bundle8.putSerializable("informationsObj", theinformation);
                bundle8.putSerializable("theoperatorlistsObj",
                        (Serializable) operatorlists);
                intent8.putExtras(bundle8);
                startActivity(intent8);
                break;
            // 菜单键
            case R.id.menu_display:
                mDrawerLayout.openDrawer(mDrawerList);
                break;
            // 上线调度
            case R.id.online_scheduling:
                dialogs();
                break;
        }
    }

    private void dialogs() {
        AlertDialog.Builder builder3 = new AlertDialog.Builder(FunctionActivity.this);
        builder3.setTitle("请选择通道号：");
        final String[] dxh = {"1", "2", "3", "4", "5"};
        builder3.setSingleChoiceItems(dxh, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                jcxdh = dxh[which];
            }
        });
        builder3.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            hander.sendEmptyMessage(MSG_SHOW);
                            String str = "<jylsh>" + zdjylsh + "</jylsh><jycs>" + jycs + "</jycs><jcxdh>" + jcxdh + "</jcxdh><ycy>" + operatorlists.get(0).getXm() + "</ycy>";
                            String ip = null;
                            if (jcxdh.equals("1")) {
                                ip = "192.26.1.106";
                            }
                            if (jcxdh.equals("2")) {
                                ip = "192.26.1.108";
                            }
                            if (jcxdh.equals("3")) {
                                ip = "192.26.1.107";
                            }
                            client = new Socket(ip, 6666);
//                            socketClient = new MySocketClient(FunctionActivity.this, client,hander);
//                            socketClient.receiveInfo();
//                            socketClient.sendInfo(str);

                            hander.sendEmptyMessage(MSG_DISMISS);

                            OutputStream os = client.getOutputStream();
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "gbk"));
                            bw.write(str);
                            bw.flush();
                            client.shutdownOutput();
                            InputStream is = null;
                            is = client.getInputStream();
                            byte[] buff;
                            buff = new byte[is.available()];
                            final String content;
                            if (buff.length != 0) {
                                // 读取数据
                                is.read(buff);
                                content = new String(buff, "gbk");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(FunctionActivity.this);
                                        builder1.setTitle("提示框");
                                        builder1.setMessage(content);
                                        builder1.setPositiveButton("确定", null);
                                        builder1.show();
                                    }
                                });
//                                if (content.contains("外观查验")) {
//                                    isStart = true;
//                                }
//						           Log.i("TAG", "服务器的数据content：" + content);
                            }

                            is.close();
                            bw.close();
                            os.close();
                            client.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        builder3.setCancelable(false);
        builder3.show();
    }


    // 吐司显示
    public void setToast(String faildata) {
        Toast.makeText(FunctionActivity.this, faildata, Toast.LENGTH_LONG)
                .show();
        // toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
    }

    private Q33Domain get33sft(Q11Domain informations, String crxmz) {
        Q33Domain newVehicleBuHeGes = null;
        if (informations.getJycs() > 1) {
            newVehicleBuHeGes = getq33bhgxxx(informations.getJylsh(),
                    informations.getJycs(), crxmz);
        }
        return newVehicleBuHeGes;
    }

    // 调18Q13接口获取照片信息
    private List<Q13Domain> GetPhotoDatas() {
        String setphotodata = "<?xml version=\"1.0\" encoding=\"GBK\"?><root><QueryCondition><jczbh>"
                + jkph + "</jczbh></QueryCondition></root>";
        String getphotodatas = ConnectMethods.connectWebService(ip,
                StaticValues.queryObject, jkxlh, "18Q13", setphotodata,
                "queryObjectOutResult", StaticValues.timeoutThree,
                StaticValues.numberFive);
        List<Q13Domain> photonamelis = XMLParsingMethods
                .saxphotoname(getphotodatas);
        Log.i("AAA", "photonamelis:" + photonamelis.get(0).getId() + ":"
                + photonamelis.get(0).getName() + ":" + photonamelis.size());
        return photonamelis;
    }

    // 调18Q32接口
    private List<Q32Domain> getq32bhgxdata() {
        List<String> datalists = new ArrayList<String>();
        datalists.add(jkph);
        String xmldata = UnXmlTool.getQueryXML(q32datas, datalists);
        Log.i("BBB", "getq32bhgxdata:" + xmldata);
        String bhgxdatas = ConnectMethods.connectWebService(ip,
                StaticValues.queryObject, jkxlh, "18Q32", xmldata,
                "queryObjectOutResult", StaticValues.timeoutThree,
                StaticValues.numberFive);
        List<Q32Domain> buhegexingxxlists = XMLParsingMethods
                .saxbuhegexingxxs(bhgxdatas);
        return buhegexingxxlists;
    }

    // 调18Q33接口
    private Q33Domain getq33bhgxxx(String newjylsh, int newjycs, String crxmz) {
        List<String> datalists = new ArrayList<String>();
        datalists.add(newjylsh);
        datalists.add(newjycs + "");
        datalists.add(crxmz);
        String xmldata = UnXmlTool.getQueryXML(q33datas, datalists);
        Log.i("BBB", "getq33bhgxxx:" + xmldata);
        String bhgxxxdatas = ConnectMethods.connectWebService(ip,
                StaticValues.queryObject, jkxlh, "18Q33", xmldata,
                "queryObjectOutResult", StaticValues.timeoutThree,
                StaticValues.numberFive);
        List<Q33Domain> vehicleBuHeGelists = XMLParsingMethods
                .saxvehicleBuHeGes(bhgxxxdatas);
        Q33Domain bhginfors = vehicleBuHeGelists.get(0);
        return bhginfors;
    }

    @Override
    protected void onResume() {
//        while (isStart) {
//            DialogTool.AlertDialogShow(this,content);
//            isStart=false;
//        }
        SharedPreferences share = getSharedPreferences("jbsz", 1);
        wgdataxz = share.getBoolean("DATAXZ", false);
        lsczz = share.getBoolean("LSXZ", true);
        if (lsczz) {
            lstwo.setVisibility(View.VISIBLE);
            lsfrist.setVisibility(View.GONE);
        } else {
            lsfrist.setVisibility(View.VISIBLE);
            lstwo.setVisibility(View.GONE);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    hander.sendEmptyMessage(MSG_FUNCTION_SHOW_TWO);
                    List<String> zddatalist = new ArrayList<String>();
                    zddatalist.add(zdhphm);
                    String zdxmldata = UnXmlTool.getUNQueryXML(zdhphmdata,
                            zddatalist);
                    String zdinformation = ConnectMethods.connectWebService(ip,
                            StaticValues.queryObject, jkxlh, "18Q11",
                            zdxmldata, "queryObjectOutResult",
                            StaticValues.timeoutThree, StaticValues.numberFive);
                    List<Q11Domain> zdhphmInformationlists = XMLParsingMethods
                            .saxvehicleinformation(zdinformation);
                    theinformation = null;
                    for (int i = 0; i < zdhphmInformationlists.size(); i++) {
                        if (zdhphmInformationlists.get(i).getJylsh()
                                .equals(zdjylsh)) {
                            theinformation = zdhphmInformationlists.get(i);
                        }
                    }
                    hander.sendEmptyMessage(MSG_FUNCTION_FUZHI);
                    hander.sendEmptyMessage(MSG_FUNCTION_DISMISS);
                } catch (Exception e) {
                    hander.sendEmptyMessage(MSG_FUNCTION_DISMISS);
                    hander.sendEmptyMessage(MSG_FUNCTION_CHAOSHI);
                    e.printStackTrace();
                }
            }
        }).start();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
