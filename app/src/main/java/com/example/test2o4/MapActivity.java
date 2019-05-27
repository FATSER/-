package com.example.test2o4;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class MapActivity extends AppCompatActivity {
    private LocationClient client = null;
    private TextView tvLocationResult;
    private String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
    private final int PERMS_REQUEST_CODE = 200;
    private MapView mapView;
    private BaiduMap baiduMap;//定义地图对象的操作方法与接口
    private boolean isFirstLoc = true;//是否首次定位
    private LocationClient locationClient = null;//定位控制类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.initialize( MapActivity.this );
        setContentView(R.layout.mapactivity);

        mapView= (MapView) findViewById(R.id.mapview);
        baiduMap = mapView.getMap();

        baiduMap.setMyLocationEnabled(true);//开启定位图层

        //用户自定义定位图标
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.mipmap.icon_geo);

        //参数1: 有三个值 LocationMode.COMPASS 罗盘态，显示定位方向圈，保持定位图标在地图中心
        //              LocationMode.FOLLOWING 跟随态，保持定位图标在地图中心
        //              LocationMode.NORMAL  普通态： 更新定位数据时不对地图做任何操作
        //参数2:是否允许显示方向信息
        //参数3:用户自定义定位图标
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,true,mCurrentMarker);
        baiduMap.setMyLocationConfiguration(config);//设置定位图层显示方式

        startRequestLocation();//开启定位

        tvLocationResult= (TextView) findViewById(R.id.tv_location_result);

        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1&&
                PackageManager.PERMISSION_GRANTED!=checkSelfPermission(perms[0])) {
            requestPermissions(perms,PERMS_REQUEST_CODE);
        }else{
            startRequestLocation();
        }
    }
    private void startRequestLocation(){
        LocationClientOption mOption=new LocationClientOption();
        mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        mOption.setScanSpan(3000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
        mOption.setNeedDeviceDirect(true);//可选，设置是否需要设备方向结果
        mOption.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        //mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集

        mOption.setIsNeedAltitude(true);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用

        client = new LocationClient(this);
        client.setLocOption(mOption);//设置定位参数
        client.registerLocationListener(locationListener);//注册监听

        client.start();
    }
    private BDLocationListener locationListener=new BDLocationListener(){
        @Override
        public void onReceiveLocation(BDLocation location){
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                Log.i("ansen","getLongitude:"+location.getLongitude());
                sb.append("当前定位时间 : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\n定位类型 : ");// 定位类型
                if(location.getLocType()==61){
                    sb.append("GPS定位成功");
                }else if (location.getLocType()==161){
                    sb.append("网络定位成功");
                }else if (location.getLocType()==63){
                    sb.append("网络异常");
                }else if (location.getLocType()==62) {
                    sb.append("无法获取有效定位，请到室外GPS信号较强的地方或打开运营商网络、WIFI设置");
                }else if (location.getLocType()==67){
                    sb.append("离线定位失败");
                }else sb.append(location.getLocType());
                //sb.append("\n定位类型说明 : ");// *****对应的定位类型说明*****
                // sb.append(location.getLocTypeDescription());
                sb.append("\n纬度 : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("   经度 : ");// 经度
                sb.append(location.getLongitude());
                sb.append("   精度半径 : ");// 半径
                sb.append(location.getRadius()+"米");
                //sb.append("\n国家码 : ");// 国家码
                //sb.append(location.getCountryCode());
                //sb.append("   城市编码 : ");// 城市编码
                //sb.append(location.getCityCode());
                //sb.append("\n国家名称 : ");// 国家名称
                //sb.append(location.getCountry());
                //sb.append("\n城市 : ");// 城市
                //sb.append(location.getCity());
                //sb.append("\n区 : ");// 区
                //sb.append(location.getDistrict());
                //sb.append("\n街道 : ");// 街道
                //sb.append(location.getStreet());
                sb.append("\n罗盘方位: ");
                sb.append(location.getDirection());// 方向
                sb.append("\n地址信息 : ");// 地址信息
                sb.append(location.getAddrStr());
                //sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                //sb.append(location.getUserIndoorState());
                sb.append("\n位置描述: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\n附近信息: ");// POI信息
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";\n");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\n速度 : ");
                    sb.append(location.getSpeed()+"km/h");// 速度 单位：km/h
                    sb.append("\n海拔 : ");
                    sb.append(location.getAltitude()+"米");// 海拔高度 单位：米
                    sb.append("\n卫星数目 : ");
                    sb.append(location.getSatelliteNumber()+"颗");// 卫星数目
                    sb.append("\nGPS质量 : ");
                    //sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    if (location.getGpsAccuracyStatus()==3){
                        sb.append("弱 ");
                    }else if (location.getGpsAccuracyStatus()==2){
                        sb.append("中 ");
                    }
                    else if (location.getGpsAccuracyStatus()==1){
                        sb.append("强 ");
                    }
                    sb.append("\n描述 : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\n海拔 : ");
                        sb.append(location.getAltitude()+"米");// 单位：米
                    }
                    sb.append("\n运营商信息 : ");// 运营商信息
                    //sb.append(location.getOperators());
                    if(location.getOperators()==1){
                        sb.append("中国移动");
                    }
                    if(location.getOperators()==2){
                        sb.append("中国联通");
                    }
                    if(location.getOperators()==3){
                        sb.append("中国电信");
                    }
                    sb.append("\n描述 : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\n描述 : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\n描述 : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\n描述 : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\n描述 : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                tvLocationResult.setText(sb.toString());
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())//设置定位数据的精度信息
                        .latitude(location.getLatitude())//纬度
                        .longitude(location.getLongitude())//经度
                        .build();

                //设置定位数据, 只有先允许定位图层后设置数据才会生效
                baiduMap.setMyLocationData(locData);

                if (isFirstLoc) {//第一次定位
                    isFirstLoc = false;
                    LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(18.0f);//设置地图缩放级别
                    //以动画方式更新地图状态，动画耗时 300 ms
                    baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
            }
        }
    };
    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){
        switch(permsRequestCode){
            case PERMS_REQUEST_CODE:
                boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(storageAccepted){
                    startRequestLocation();
                }
                break;

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mapView.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        client.stop();//停止定位
        baiduMap.setMyLocationEnabled(false);//关闭定位图层
        //在activity执行onDestroy时执行mapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
        mapView=null;
    }
}
