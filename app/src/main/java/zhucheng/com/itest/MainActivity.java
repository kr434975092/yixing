package zhucheng.com.itest;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{

    private Receiver mReceiver = null;
    private TextView trateView = null;
    private TextView tratec = null;
    private TextView hiView = null;
    private TextView bpView = null;
    private TextView tcgView = null;
    private TextView date1 = null;
    private TextView date2 = null;
    private TextView date3 = null;
    private Calendar calendar = null;
    private RelativeLayout remain0 = null;
    private RelativeLayout remain1 = null;
    private RelativeLayout remain2 = null;
    private RelativeLayout remain3 = null;
    private RadioGroup rgGroup = null;
    private RadioButton rbHealth = null;
    private RadioButton rbGps = null;
    private RadioButton rbFind = null;
    //private RadioButton rbMe = null;
    private Button button = null;
    private static boolean isFirst = true;

    public class Receiver extends BroadcastReceiver{   //广播接收者

        @Override
        public void onReceive(Context context, Intent intent) {  //接收广播
            int flag = intent.getExtras().getInt("errorflag");
            if(flag==0){
            trateView.setText(RandomUtil.std[0]+"");  //修改UI视图上的数据
            if(RandomUtil.std[0]>=36&&RandomUtil.std[0]<=37.3)tratec.setText("体温正常");
            if(RandomUtil.std[0]>37.3&&RandomUtil.std[0]<=38.4)tratec.setText("体温低热");
            if(RandomUtil.std[0]>38.4)tratec.setText("体温发热");//这里设置下颜色变化
            hiView.setText(String.format("%.0f",RandomUtil.std[1]));
            bpView.setText(String.format("%.0f",RandomUtil.std[2])+"/"+String.format("%.0f",RandomUtil.std[3]));
            tcgView.setText(RandomUtil.std[4]+"/"+RandomUtil.std[5]);
            calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH)+1;
            int day = calendar.get(Calendar.DATE);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            String str = month+"月"+day+"日 "+hour+(minute>9?":":":0")+minute;
            date1.setText(str);
            date2.setText(str);
            date3.setText(str);}
            else if(flag==1){
                Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.warn1);
                WarnToast.makeImage(MainActivity.this,source, Toast.LENGTH_LONG).show();
            }else if(flag==2){
                Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.warn3);
                WarnToast.makeImage(MainActivity.this,source,Toast.LENGTH_LONG).show();
            }else if(flag==3){
                Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.warn2);
                WarnToast.makeImage(MainActivity.this,source,Toast.LENGTH_LONG).show();
            }else if(flag==4){
                Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.warn4);
                WarnToast.makeImage(MainActivity.this,source,Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        trateView = (TextView)this.findViewById(R.id.trate1);
        trateView.setText(RandomUtil.std[0]+"");
        tratec = (TextView)this.findViewById(R.id.tratec);
        hiView = (TextView)this.findViewById(R.id.hi1);
        remain0 = (RelativeLayout)this.findViewById(R.id.remain0);
        remain0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,htmp.class));
                MainActivity.this.finish();
            }
        });
        remain1 = (RelativeLayout)this.findViewById(R.id.remain1);
        remain1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Heart.class));
                MainActivity.this.finish();
            }
        });
        hiView.setText(String.format("%.0f",RandomUtil.std[1]));
        bpView = (TextView)this.findViewById(R.id.bp);
        remain2 = (RelativeLayout)this.findViewById(R.id.remain2);
        remain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,BP.class));
                MainActivity.this.finish();
            }
        });
        bpView.setText(String.format("%.0f",RandomUtil.std[2])+"/"+String.format("%.0f",RandomUtil.std[3]));
        tcgView = (TextView)this.findViewById(R.id.tcg);
        remain3 = (RelativeLayout)this.findViewById(R.id.remain3);
        remain3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,BF.class));
                MainActivity.this.finish();
            }
        });
        tcgView.setText(RandomUtil.std[4]+"/"+RandomUtil.std[5]);
        date1 = (TextView)this.findViewById(R.id.sdate1);
        date2 = (TextView)this.findViewById(R.id.sdate2);
        date3 = (TextView)this.findViewById(R.id.sdate3);
        calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String str = month+"月"+day+"日 "+hour+(minute>9?":":":0")+minute;
        date1.setText(str);
        date2.setText(str);
        date3.setText(str);
        bindService(new Intent(this,RandomService.class),sc,BIND_AUTO_CREATE);  //绑定service
        doRegisterReceiver();
        rgGroup = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rbHealth = (RadioButton) findViewById(R.id.rb_health);
        rbHealth.setChecked(true);
        rbGps = (RadioButton) findViewById(R.id.rb_gps);
        rbFind = (RadioButton) findViewById(R.id.rb_find);


        //定义底部标签图片大小
        rbGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MapUtil.inInstallByread("com.autonavi.minimap"))MapUtil.setUpAmpByName(MainActivity.this);
                else if(MapUtil.inInstallByread("com.baidu.BaiduMap"))MapUtil.setUpBaiduMapByName(MainActivity.this);
                else Toast.makeText(MainActivity.this,"请先安装高德地图或百度地图!",Toast.LENGTH_LONG).show();
            }
        });
        rbFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Find.class));
                MainActivity.this.finish();
            }
        });

        button = (Button)this.findViewById(R.id.report);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = MainActivity.this.getLayoutInflater().inflate(R.layout.report,null);
                LinearLayout linear = (LinearLayout) view.findViewById(R.id.hehe111);
                TextView textView11 = (TextView)view.findViewById(R.id.tmpcc);
                TextView textView12 = (TextView)view.findViewById(R.id.tmpex);
                TextView textView21 = (TextView)view.findViewById(R.id.hic);
                TextView textView22 = (TextView)view.findViewById(R.id.hiex);
                TextView textView31 = (TextView)view.findViewById(R.id.hbpc);
                TextView textView32 = (TextView)view.findViewById(R.id.lbpc);
                TextView textView33 = (TextView)view.findViewById(R.id.bpex);
                TextView textView41 = (TextView)view.findViewById(R.id.tcc);
                TextView textView42 = (TextView)view.findViewById(R.id.tgc);
                TextView textView43 = (TextView)view.findViewById(R.id.tcgex);

                String[]h1 = Util.getReport(0,1,MainActivity.this);
                String[]h2 = Util.getReport(1,1,MainActivity.this);
                String[]h3 = Util.getReport(2,1,MainActivity.this);
                String[]h4 = Util.getReport(3,1,MainActivity.this);
                String[]h5 = Util.getReport(4,1,MainActivity.this);
                String[]h6 = Util.getReport(5,1,MainActivity.this);

                textView11.setText(h1[0]);
                textView12.setText(h1[1]);
                textView21.setText(h2[0]);
                textView22.setText(h2[1]);
                textView31.setText(h3[0]);
                textView32.setText(h4[0]);
                textView33.setText(h3[1]+h4[1]);
                textView41.setText(h5[0]);
                textView42.setText(h6[0]);
                textView43.setText(h5[1]+h6[1]);

                ViewGroup.LayoutParams up = linear.getLayoutParams();
                ViewUtil.layoutView(linear,1000,2000);
                Bitmap df = ViewUtil.getCacheBitmapFromView(linear);
                calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH)+1;
                int day = calendar.get(Calendar.DATE);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);
                if(SplashActivity.writeFlag==0)Toast.makeText(MainActivity.this,"请申请媒体文件读写权限!",Toast.LENGTH_LONG).show();
                else{
                    boolean isSuccess = ViewUtil.saveBitmapToPhone2(MainActivity.this,df,"体检报告"+year+month+day+hour+minute+second);
                    if(isSuccess)Toast.makeText(MainActivity.this,"保存到手机 图片 目录下!",Toast.LENGTH_LONG).show();
                    else Toast.makeText(MainActivity.this,"保存失败!",Toast.LENGTH_LONG).show();
                }
            }
        });
        tr();

    }

    ServiceConnection sc = new ServiceConnection() {  //参数必备
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private void doRegisterReceiver(){  //注册广播接收器
        mReceiver = new Receiver();
        IntentFilter filter = new IntentFilter("zhucheng.com.itest.content");
        registerReceiver(mReceiver,filter);
    }

    protected void onDestroy(){   //销毁activity
        super.onDestroy();
        unbindService(sc);   //解除service绑定
        Log.d("warning","exit");
        if(mReceiver!=null){
            unregisterReceiver(mReceiver);  //解除广播接收器绑定
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            AlertDialog.Builder isExit = new AlertDialog.Builder(this);
            isExit.setIcon(R.drawable.logo2);
            isExit.setTitle("易行提示");
            isExit.setMessage("确定要退出吗?");
            isExit.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MainActivity.this.finish();
                    try {
                        Thread.sleep(1000);  //为Service中开启的同步线程提供结束的时间
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
            });
            isExit.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            isExit.create().show();
        }
        return false;
    }

    public void tr(){
        if(isFirst){
            Map<String,String> query1 = new HashMap<String,String>();
            Map<String,String>query2 = new HashMap<String,String>();
            query2.put("city",Util.CITY2);
            query2.put("key",ApiKey.TRKEY);
            WGet task = new WGet();
            task.execute(query1,query2);
            isFirst = false;
        }
    }

    class WGet extends AsyncTask<Map<String,String>,Void,String> {

        @Override
        protected String doInBackground(Map<String, String>[] maps) {
            String result = null;
            Map<String,String>query1 = maps[0];
            Map<String,String>query2 = maps[1];
            result = HttpUtil.doGet(ApiKey.TRHOST,query1,query2);
            return result;
        }
        protected void onPostExecute(String result){
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject child1 = jsonObject.getJSONObject("result");
                int isxianxing = child1.getInt("isxianxing");
                Util.TR = isxianxing;
                Toast.makeText(MainActivity.this,"今日的限行尾号为"+isxianxing,Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                System.out.print(e.getMessage());
                Toast.makeText(MainActivity.this,"未能获取到限行信息,请检查您的网络!",Toast.LENGTH_LONG).show();
            }
        }

    }




}
