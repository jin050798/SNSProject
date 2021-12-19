package com.test.sns_project.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import com.test.sns_project.R;

public class levelActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{
    private static final String API_Key = "l7xx5191db20e9d245d58e8dad551c3ae142";

    // T Map View
    double distance = 0;
    private TMapView tMapView = null;
    private Context mContext;
    private boolean m_bTrackingMode = true;
    private TMapGpsManager gps = null;
    TextView text;
    Animation anim;
    TMapPoint tMapPointStart;

    double longitude=0;//경도
    double latitude=0; //위도

    double petlongi =127.34452717420048;
    double petlati = 36.36624725889481;

    Button btn_community;
    Button btn_call;


    @Override
    public void onLocationChange(Location location) {

        longitude = location.getLongitude();
        latitude = location.getLatitude();

        if (m_bTrackingMode) {


            distance = getDistance(latitude,longitude,petlati,petlongi);

            tMapPointStart = new TMapPoint(latitude, longitude);
            tMapView.setLocationPoint(longitude, latitude);
        }
        Log.e("오류","실패");

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.point);
        tMapView.setIcon(bitmap);
        Bitmap bitmap1 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.poi_dot);



        if(distance<100){

            btn_call.setVisibility(View.INVISIBLE);
            btn_community.setVisibility(View.INVISIBLE);
            text.setText("반려동물이 100m 이내에 있습니다.");
            text.setTextColor(Color.GREEN);
            text.startAnimation(anim);
            TMapPoint tMapPointEnd = new TMapPoint(petlati, petlongi);

            TMapData tmapdata = new TMapData();
            tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH,tMapPointStart, tMapPointEnd, new TMapData.FindPathDataListenerCallback() {
                @Override
                public void onFindPathData(TMapPolyLine polyLine) {
                    tMapView.addTMapPath(polyLine);
                    polyLine.setLineColor(Color.GREEN);
                    polyLine.setLineWidth(8);
                }
            });

        }else if(distance>=100&&distance<500){
            text.setText("반려동물이 500m 이내에 있습니다.");

            TMapPoint tMapPointEnd = new TMapPoint(petlati, petlongi);

            btn_call.setVisibility(View.INVISIBLE);
            btn_community.setVisibility(View.INVISIBLE);

            text.setTextColor(Color.YELLOW);
            text.startAnimation(anim);

            TMapData tmapdata = new TMapData();
            tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH,tMapPointStart, tMapPointEnd, new TMapData.FindPathDataListenerCallback() {
                @Override
                public void onFindPathData(TMapPolyLine polyLine) {
                    tMapView.addTMapPath(polyLine);
                    polyLine.setLineColor(Color.YELLOW);
                    polyLine.setLineWidth(8);
                }
            });


        }else{


            btn_call.setVisibility(View.VISIBLE);
            btn_community.setVisibility(View.VISIBLE);
            text.setText("반려동물이 500m 이상으로 떨어졌습니다.");
            text.setTextColor(Color.RED);
            text.startAnimation(anim);
            TMapPoint tMapPointEnd = new TMapPoint(petlati, petlongi);

            TMapData tmapdata = new TMapData();
            tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH,tMapPointStart, tMapPointEnd, new TMapData.FindPathDataListenerCallback() {
                @Override
                public void onFindPathData(TMapPolyLine polyLine) {
                    tMapView.addTMapPath(polyLine);
                }
            });

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        text = findViewById(R.id.blink);
        // T Map View
        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey(API_Key);
        mContext = this;

        //글자 깜빡거리기 위해
        anim = new AlphaAnimation(0.0f,1.0f);
        anim.setDuration(100);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);

        //초기 세팅
        tMapView.setZoomLevel(17);
        tMapView.setIconVisibility(true);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        // T Map View Using Linear Layout
        linearLayoutTmap.addView(tMapView);

        gps = new TMapGpsManager(levelActivity.this);
        gps.setMinTime(1000);
        gps.setMinDistance(5);
        gps.setProvider(TMapGpsManager.GPS_PROVIDER);
        gps.OpenGps();

        btn_community = (Button)findViewById(R.id.btn_community);
        btn_call = (Button)findViewById(R.id.btn_call);

        btn_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);
        tMapView.setCenterPoint(127.34447222396332, 36.36740562078646);


    }
    public void onClickZoomInBtn(View v) {
        mapZoomIn();
    }

    public void onClickZoomOutBtn(View v) {
        mapZoomOut();
    }

    public void mapZoomIn() {
        tMapView.MapZoomIn();
    }

    /**
     * mapZoomOut 지도를 한단계 축소한다.
     */
    public void mapZoomOut() {
        tMapView.MapZoomOut();
    }

    public double getDistance(double latitude, double longitude, double platitude, double plongitude){

        Location PetLocation = new Location("pointA");
        PetLocation.setLatitude(platitude);
        PetLocation.setLongitude(plongitude);
        Location GPSLocation = new Location("pointB");
        GPSLocation.setLatitude(latitude);
        GPSLocation.setLongitude(longitude);

        double distance = GPSLocation.distanceTo(PetLocation);

        return distance;
    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(this,c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
