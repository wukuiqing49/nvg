package com.wkq.nvg;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviCommonParams;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRoutePlanManager;
import com.baidu.navisdk.adapter.IBaiduNaviManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String APP_FOLDER_NAME = "BNSDKSimpleDemo";

    private static final String[] authBaseArr = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int authBaseRequestCode = 1;

    private Button mNaviBtn = null;
    private Button mExternalBtn = null;
    private Button mDrivingBtn = null;
    private Button mGotoSettingsBtn = null;

    private String mSDCardPath = null;


    private Button mWgsNaviBtn = null;
    private Button mGcjNaviBtn = null;
    private Button mBdmcNaviBtn = null;
    private Button mSzNaviBtn = null;
    private Button mBjNaviBtn = null;
    private Button mCustomNaviBtn = null;
    private Button mDb06ll = null;

    private double mCurrentLat;
    private double mCurrentLng;
    private LocationManager mLocationManager;

    private LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            mCurrentLat = location.getLatitude();
            mCurrentLng = location.getLongitude();
            Toast.makeText(MainActivity.this, mCurrentLat
                    + "--" + mCurrentLng, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDirs() ;
        initData();
        mWgsNaviBtn = findViewById(R.id.wgsNaviBtn);
        mGcjNaviBtn = findViewById(R.id.gcjNaviBtn);
        mBdmcNaviBtn = findViewById(R.id.bdmcNaviBtn);
        mDb06ll = findViewById(R.id.mDb06llNaviBtn);
        mSzNaviBtn = findViewById(R.id.szNaviBtn);
        mBjNaviBtn = findViewById(R.id.bjNaviBtn);
        mCustomNaviBtn = findViewById(R.id.customNaviBtn);

        initListener();
        initLocation();
    }

    private void initData() {
        BaiduNaviManagerFactory.getBaiduNaviManager().init(this,
                mSDCardPath, APP_FOLDER_NAME, new IBaiduNaviManager.INaviInitListener() {

                    @Override
                    public void onAuthResult(int status, String msg) {
                        String result;
                        if (0 == status) {
                            result = "key校验成功!";
                        } else {
                            result = "key校验失败, " + msg;
                        }
                        Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void initStart() {
                        Toast.makeText(MainActivity.this.getApplicationContext(),
                                "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void initSuccess() {
                        Toast.makeText(MainActivity.this.getApplicationContext(),
                                "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                        // 初始化tts
                        initTTS();
                    }

                    @Override
                    public void initFailed(int errCode) {
                        Toast.makeText(MainActivity.this.getApplicationContext(),
                                "百度导航引擎初始化失败 " + errCode, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void initTTS() {
        // 使用内置TTS
        BaiduNaviManagerFactory.getTTSManager().initTTS(getApplicationContext(),
                getSdcardDir(), APP_FOLDER_NAME, "17780786");
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private void initLocation() {
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (mLocationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission
                    .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                    .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
                return;
            }
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000, 1000, mLocationListener);
        }
    }

    private void initListener() {
//        if (mWgsNaviBtn != null) {
//            mWgsNaviBtn.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
//                    if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {
//                        calRoutePlanNode(BNRoutePlanNode.CoordinateType.WGS84);
//                    }
//                }
//
//            });
//        }
//        if (mGcjNaviBtn != null) {
//            mGcjNaviBtn.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
//                    if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {
//                        calRoutePlanNode(BNRoutePlanNode.CoordinateType.GCJ02);
//                    }
//                }
//
//            });
//        }
//        if (mBdmcNaviBtn != null) {
//            mBdmcNaviBtn.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
//                    if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {
//                        calRoutePlanNode(BNRoutePlanNode.CoordinateType.BD09_MC);
//                    }
//                }
//            });
//        }
//
//        if (mDb06ll != null) {
//            mDb06ll.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View arg0) {
//                    if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {
//                        calRoutePlanNode(BNRoutePlanNode.CoordinateType.BD09LL);
//                    }
//                }
//            });
//        }

        if (mSzNaviBtn != null) {
            mSzNaviBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {
                        if (mCurrentLat == 0 && mCurrentLng == 0) {
                            return;
                        }
                        BNRoutePlanNode sNode = new BNRoutePlanNode.Builder()
                                .latitude(mCurrentLat)
                                .longitude(mCurrentLng)
                                .coordinateType(BNRoutePlanNode.CoordinateType.WGS84)
                                .build();
                        BNRoutePlanNode eNode = new BNRoutePlanNode.Builder()
                                .latitude(22.613435)
                                .longitude(114.025550)
                                .name("深圳北站")
                                .description("深圳北站")
                                .coordinateType(BNRoutePlanNode.CoordinateType.WGS84)
                                .build();

                        routePlanToNavi(sNode, eNode);
                    }
                }
            });
        }
//
        if (mBjNaviBtn != null) {
            mBjNaviBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {
                        if (mCurrentLat == 0 && mCurrentLng == 0) {
                            return;
                        }
                        BNRoutePlanNode sNode = new BNRoutePlanNode.Builder()
                                .latitude(mCurrentLat)
                                .longitude(mCurrentLng)
                                .coordinateType(BNRoutePlanNode.CoordinateType.WGS84)
                                .build();
                        BNRoutePlanNode eNode = new BNRoutePlanNode.Builder()
                                .latitude(39.908749)
                                .longitude(116.397491)
                                .name("北京天安门")
                                .description("北京天安门")
                                .coordinateType(BNRoutePlanNode.CoordinateType.WGS84)
                                .build();

                        routePlanToNavi(sNode, eNode);
                    }
                }
            });
        }


    }


    private void routePlanToNavi(BNRoutePlanNode sNode, BNRoutePlanNode eNode) {
        List<BNRoutePlanNode> list = new ArrayList<>();
        list.add(sNode);
        list.add(eNode);

        BaiduNaviManagerFactory.getRoutePlanManager().routeplanToNavi(
                list,
                IBNRoutePlanManager.RoutePlanPreference.ROUTE_PLAN_PREFERENCE_DEFAULT,
                null,
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_START:
                                Toast.makeText(MainActivity.this.getApplicationContext(),
                                        "算路开始", Toast.LENGTH_SHORT).show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_SUCCESS:
                                Toast.makeText(MainActivity.this.getApplicationContext(),
                                        "算路成功", Toast.LENGTH_SHORT).show();
                                // 躲避限行消息
                                Bundle infoBundle = (Bundle) msg.obj;
                                if (infoBundle != null) {
                                    String info = infoBundle.getString(
                                            BNaviCommonParams.BNRouteInfoKey.TRAFFIC_LIMIT_INFO
                                    );
                                    Log.d("OnSdkDemo", "info = " + info);
                                }
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_FAILED:
                                Toast.makeText(MainActivity.this.getApplicationContext(),
                                        "算路失败", Toast.LENGTH_SHORT).show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_TO_NAVI:
                                Toast.makeText(MainActivity.this.getApplicationContext(),
                                        "算路成功准备进入导航", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(MainActivity.this,
                                        DemoGuideActivity.class);

                                startActivity(intent);
                                break;
                            default:
                                // nothing
                                Toast.makeText(MainActivity.this.getApplicationContext(),
                                        "算路成功准备进入导航", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    private boolean checkValid(String startPoint, String endPoint) {
        if (TextUtils.isEmpty(startPoint) || TextUtils.isEmpty(endPoint)) {
            return false;
        }

        if (!startPoint.contains(",") || !endPoint.contains(",")) {
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }
}
