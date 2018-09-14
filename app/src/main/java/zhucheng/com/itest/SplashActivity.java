package zhucheng.com.itest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SplashActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private String driverl = null;
    public static LocationManager locationManager;
    public static int locationFlag = 0;
    public static int writeFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            else{
                locationFlag = 1;
                writeFlag = 1;
            }
        else {
                locationFlag = 1;
                writeFlag = 1;
        }
/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            else writeFlag = 1;
        else writeFlag = 1;
*/
        SharedPreferences infoPref = this.getSharedPreferences(Util.FILE_NAME, Context.MODE_PRIVATE);
        driverl = infoPref.getString("driverl","0");
        handler.postDelayed(new Runnable(){
            public void run(){
                Intent intent = null;
                if(driverl.equals("0")){
                    DBManager db = new DBManager(SplashActivity.this);
                    Health health = null;
                    String sql = "insert into health values(?,?,?,?,?,?,?,?,?,?)";
                    try {
                        db.execSQL("create table if not exists health(tmp REAL,hi INTEGER,hbp INTEGER,lbp INTEGER,tc REAL,tg REAL,date TEXT,hour INTEGER,mins REAL,rhour INTEGER)",null);
                        db.delete(null);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar c = Calendar.getInstance();
                        for(int i = 25;i>=1;i--){
                            for(int j = 0;j<=24;j++){
                                RandomUtil.getRandom();
                                c.setTime(new Date());
                                c.add(Calendar.DATE,-1*i);
                                Date d = c.getTime();
                                String temp = format.format(d);
                                double tmp = Double.parseDouble(String.format("%.1f",RandomUtil.std[0]));
                                int hi = Integer.parseInt(String.format("%.0f",RandomUtil.std[1]));
                                int hbp= Integer.parseInt(String.format("%.0f",RandomUtil.std[2]));
                                int lbp= Integer.parseInt(String.format("%.0f",RandomUtil.std[3]));
                                double tc = Double.parseDouble(String.format("%.2f",RandomUtil.std[4]));
                                double tg = Double.parseDouble(String.format("%.2f",RandomUtil.std[5]));
                                db.execSQL(sql,new Object[]{tmp,hi,hbp,lbp,tc,tg,
                                        temp,j,0,j});
                            }}
                    } catch (Exception e) {

                    }finally{
                        db.closeDB();
                    }

                    intent = new Intent(SplashActivity.this,login.class);
                }
                    else {
                    Util.DRIVERL = driverl;
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }
                    startActivity(intent);
                    SplashActivity.this.finish();

            }
        },3000);
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

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

    public void onRequestPermissionsResult(int requestCode,String[]permissions,int[]grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch(requestCode){
            case 1:
                // 如果用户授权
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    locationFlag = 1;
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
                    writeFlag = 1;
                }
                else {
                    locationFlag = 0;
                    writeFlag = 0;
                    Toast.makeText(this, "GPS定位权限和手机读写权限已被用户拒绝,部分功能可能无法正常使用", Toast.LENGTH_LONG).show();
                }
        }
    }

    public static double[] getLal(){
        if(locationFlag==1){
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location!=null)return new double[]{location.getLatitude(),location.getLongitude()};
            else return null;
        }else return null;
    }


}
