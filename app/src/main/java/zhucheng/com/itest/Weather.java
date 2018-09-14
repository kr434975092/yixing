package zhucheng.com.itest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static zhucheng.com.itest.SplashActivity.locationManager;

public class Weather extends AppCompatActivity {

    private TextView wtemp = null;
    private TextView cop = null;
    private TextView wind = null;
    private TextView aqi = null;
    private TextView todayc = null;
    private ImageView timg = null;
    private TextView todayt = null;
    private TextView todaycp = null;
    private TextView huazh = null;
    private TextView cloth = null;
    private TextView flu = null;
    private TextView spo = null;
    private TextView feel = null;
    private TextView trip = null;
    private TextView wcar = null;
    private TextView bc = null;
    private LinearLayout linearLayout = null;

    private int dpi = 0;

    public static int getImg(String img) {
        int i = Integer.parseInt(img);
        switch (i) {
            case 0:
                return R.drawable.w0;
            case 1:
                return R.drawable.w1;
            case 10:
                return R.drawable.w10;
            case 11:
                return R.drawable.w11;
            case 12:
                return R.drawable.w12;
            case 13:
                return R.drawable.w13;
            case 14:
                return R.drawable.w14;
            case 15:
                return R.drawable.w15;
            case 16:
                return R.drawable.w16;
            case 17:
                return R.drawable.w17;
            case 18:
                return R.drawable.w18;
            case 19:
                return R.drawable.w19;
            case 2:
                return R.drawable.w2;
            case 20:
                return R.drawable.w20;
            case 21:
                return R.drawable.w21;
            case 22:
                return R.drawable.w22;
            case 23:
                return R.drawable.w23;
            case 24:
                return R.drawable.w24;
            case 25:
                return R.drawable.w25;
            case 26:
                return R.drawable.w26;
            case 27:
                return R.drawable.w27;
            case 28:
                return R.drawable.w28;
            case 29:
                return R.drawable.w29;
            case 3:
                return R.drawable.w3;
            case 30:
                return R.drawable.w30;
            case 301:
                return R.drawable.w301;
            case 302:
                return R.drawable.w302;
            case 31:
                return R.drawable.w31;
            case 32:
                return R.drawable.w32;
            case 39:
                return R.drawable.w39;
            case 4:
                return R.drawable.w4;
            case 49:
                return R.drawable.w49;
            case 5:
                return R.drawable.w5;
            case 53:
                return R.drawable.w53;
            case 54:
                return R.drawable.w54;
            case 55:
                return R.drawable.w55;
            case 56:
                return R.drawable.w56;
            case 57:
                return R.drawable.w57;
            case 58:
                return R.drawable.w58;
            case 6:
                return R.drawable.w6;
            case 7:
                return R.drawable.w7;
            case 8:
                return R.drawable.w8;
            case 9:
                return R.drawable.w9;
            case 99:
                return R.drawable.w99;
            default:
                return R.drawable.w0;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        dpi = metrics.densityDpi;

        wtemp = (TextView) this.findViewById(R.id.wtemp);
        cop = (TextView) this.findViewById(R.id.cop);
        wind = (TextView) this.findViewById(R.id.wind);
        aqi = (TextView) this.findViewById(R.id.aqi);
        todayc = (TextView) this.findViewById(R.id.todayc);
        timg = (ImageView) this.findViewById(R.id.timg);
        todayt = (TextView) this.findViewById(R.id.todayt);
        todaycp = (TextView) this.findViewById(R.id.todaycp);
        huazh = (TextView) this.findViewById(R.id.huazh);
        cloth = (TextView) this.findViewById(R.id.cloth);
        flu = (TextView) this.findViewById(R.id.flu);
        spo = (TextView) this.findViewById(R.id.spo);
        feel = (TextView) this.findViewById(R.id.feel);
        trip = (TextView) this.findViewById(R.id.trip);
        wcar = (TextView) this.findViewById(R.id.wcar);
        bc = (TextView) this.findViewById(R.id.bc);
        linearLayout = (LinearLayout) this.findViewById(R.id.weekw);
        Map<String, String> query1 = new HashMap<String, String>();
        query1.put("Authorization", "APPCODE " + ApiKey.WEATHER_APICODE);
        Map<String, String> query2 = new HashMap<String, String>();
        double[] location = SplashActivity.getLal();
        if(location==null||(location[0]<0||location[1]<0))query2.put("city",Util.CITY1);
        else{
            System.out.println("当前位置的经纬度为:"+location);
            query2.put("location",location[0]+","+location[1]);
        }
        WGet task = new WGet();
        task.execute(query1,query2);
    }

    class WGet extends AsyncTask<Map<String,String>,Void,String>{

        @Override
        protected String doInBackground(Map<String, String>[] maps) {
            String result = null;
            Map<String,String>query1 = maps[0];
            Map<String,String>query2 = maps[1];
            result = HttpUtil.doGet(ApiKey.WEATHER_HOST,query1,query2);
            return result;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject ch = jsonObject.getJSONObject("result");
                String temp = ch.getString("temp");
                String weather = ch.getString("weather");
                String hlt = ch.getString("temphigh")+"°/"+ch.getString("templow")+"°";
                String windpower = ch.getString("windpower");
                String wind1 = ch.getString("winddirect");
                String aqi1 = ch.getJSONObject("aqi").getString("aqi");
                String quality = ch.getJSONObject("aqi").getString("quality");
                JSONArray array = ch.getJSONArray("daily");
                String img = array.getJSONObject(0).getJSONObject("day").getString("img");
                JSONArray array1 = ch.getJSONArray("index");
                String huazh1 = array1.getJSONObject(0).getString("ivalue");
                String spo1 = array1.getJSONObject(1).getString("ivalue");
                String bc1 = array1.getJSONObject(2).getString("ivalue");
                String flu1 = array1.getJSONObject(3).getString("ivalue");
                String wcar1 = array1.getJSONObject(4).getString("ivalue");
                String cloth1 = array1.getJSONObject(6).getString("ivalue");
                wtemp.setText(temp+"°");
                cop.setText(hlt+weather);
                wind.setText(windpower+"/"+wind1);
                aqi.setText(aqi1+"/"+quality);
                todayc.setText(weather);
                todayt.setText(hlt);
                todaycp.setText(quality);
                huazh.setText(huazh1);
                spo.setText(spo1);
                bc.setText(bc1);
                flu.setText(flu1);
                wcar.setText(wcar1);
                cloth.setText(cloth1);
                timg.setImageResource(Weather.getImg(img));
                int arrlen = array.length();
                for(int i = 1;i<arrlen;++i){
                    JSONObject json = array.getJSONObject(i);
                    LinearLayout lin = new LinearLayout(Weather.this);
                    lin.setOrientation(LinearLayout.VERTICAL);
                    lin.setLayoutParams(new LinearLayoutCompat.LayoutParams(100*dpi/160,150*dpi/160));
                    lin.setGravity(Gravity.CENTER);
                    String weekday = json.getString("week");
                    JSONObject a = json.getJSONObject("day");
                    JSONObject b = json.getJSONObject("night");
                    String hl = a.getString("temphigh")+"°/"+b.getString("templow")+"°";
                    String img1 = a.getString("img");
                    String weather1 = a.getString("weather");
                    String wind2 = a.getString("winddirect");
                    TextView wek = new TextView(Weather.this);
                    wek.setText(weekday);
                    wek.setWidth(40*dpi/160);
                    wek.setHeight(30*dpi/160);
                    wek.setGravity(Gravity.CENTER);
                    lin.addView(wek);
                    TextView wea = new TextView(Weather.this);
                    wea.setText(weather1);
                    wea.setWidth(40*dpi/160);
                    wea.setHeight(30*dpi/160);
                    wea.setGravity(Gravity.CENTER);
                    lin.addView(wea);
                    ImageView imga = new ImageView(Weather.this);
                    imga.setImageResource(Weather.getImg(img1));
                    imga.setLayoutParams(new LinearLayoutCompat.LayoutParams(30*dpi/160,30*dpi/160));
                    lin.addView(imga);
                    TextView hlc = new TextView(Weather.this);
                    hlc.setText(hl);
                    hlc.setWidth(70*dpi/160);
                    hlc.setHeight(30*dpi/160);
                    hlc.setGravity(Gravity.CENTER);
                    lin.addView(hlc);
                    TextView qu = new TextView(Weather.this);
                    qu.setText(wind2);
                    qu.setTextColor(Color.parseColor("#f39a00"));
                    qu.setWidth(40*dpi/160);
                    qu.setHeight(30*dpi/160);
                    qu.setGravity(Gravity.CENTER);
                    lin.addView(qu);
                    linearLayout.addView(lin);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Weather.this,"天气信息读取有误,请重试!",Toast.LENGTH_LONG).show();
            }

        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){  //模块测试环节先这么写，到时候有了导航栏再改不迟
        if(keyCode==KeyEvent.KEYCODE_BACK){
            this.finish();
        }
        return false;
    }

}
