package zhucheng.com.itest;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class htmp extends AppCompatActivity {

    private Receiver mReceiver = null;
    private TextView today = null;
    private TextView week = null;
    private TextView month  = null;
    private LinearLayout linearLayout = null;
    private int id = 100003;

    LinearLayout linear1 = null;
    LinearLayout linear2 = null;
    LinearLayout linear3 = null;
    LinearLayout linear4 = null;
    LinearLayout linear5 = null;

    TextView c1 = null;
    TextView c2 = null;
    TextView c3 = null;
    TextView c4 = null;
    TextView c5 = null;


    class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int flag = intent.getExtras().getInt("errorflag");
            if(flag==0){

            }
            else if(flag==1){
                Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.warn1);
                WarnToast.makeImage(htmp.this,source, Toast.LENGTH_LONG).show();
            }else if(flag==2){
                Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.warn3);
                WarnToast.makeImage(htmp.this,source,Toast.LENGTH_LONG).show();
            }else if(flag==3){
                Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.warn2);
                WarnToast.makeImage(htmp.this,source,Toast.LENGTH_LONG).show();
            }else if(flag==4){
                Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.warn4);
                WarnToast.makeImage(htmp.this,source,Toast.LENGTH_LONG).show();
            }
        }
    }

    ServiceConnection sc = new ServiceConnection() {  //参数必备
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    private void doRegisterReceiver(){
        mReceiver = new htmp.Receiver();
        IntentFilter filter = new IntentFilter("zhucheng.com.itest.content");
        registerReceiver(mReceiver,filter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_htmp);

        bindService(new Intent(this,RandomService.class),sc,BIND_AUTO_CREATE);  //绑定service
        doRegisterReceiver();

        linearLayout = (LinearLayout)this.findViewById(R.id.lineartmp);
        today = (TextView)this.findViewById(R.id.todaytmp);
        week = (TextView)this.findViewById(R.id.weektmp);
        month = (TextView)this.findViewById(R.id.monthtmp);

        linear1 = (LinearLayout)this.findViewById(R.id.tmpv1);
        linear2 = (LinearLayout)this.findViewById(R.id.tmpv2);
        linear3 = (LinearLayout)this.findViewById(R.id.tmpv3);
        linear4 = (LinearLayout)this.findViewById(R.id.tmpv4);
        linear5 = (LinearLayout)this.findViewById(R.id.norc);

        c1 = (TextView)this.findViewById(R.id.ctmp1);
        c2 = (TextView)this.findViewById(R.id.ctmp2);
        c3 = (TextView)this.findViewById(R.id.ctmp3);
        c4 = (TextView)this.findViewById(R.id.ctmp4);
        c5 = (TextView)this.findViewById(R.id.ctmp5);

        new Thread(new Runnable(){
            public void run(){
                try{
                    List<Health> list = RandomService.query("tmp","today");
                    if(list.size()<3){
                        TextView tmp = new TextView(htmp.this);
                        tmp.setId(id);
                        tmp.setText("当前无统计数据");
                        linearLayout.addView(tmp);
                    }else{
                        List<BrokenLine>bl = new ArrayList<BrokenLine>();
                        BrokenLineView blv = new BrokenLineView(htmp.this);
                        blv.setId(id);
                        double temp1;

                        for(Health tmp:list){
                            temp1 = Double.parseDouble(String.format("%.0f",tmp.getTmp()));
                            BrokenLine blt = new BrokenLine("今天",temp1,0);
                            bl.add(blt);
                        }
                        blv.setMdata(bl);
                        linearLayout.addView(blv);
                    }}catch(Exception e){
                    TextView tmp = new TextView(htmp.this);
                    tmp.setId(id);
                    tmp.setText("当前无统计数据");
                    linearLayout.addView(tmp);
                }
            }
        }).start();
        try{

            final ImageView img1 = this.findViewById(R.id.tmpsound1);
            final ImageView img2 = this.findViewById(R.id.tmpsound2);
            final ImageView img3 = this.findViewById(R.id.tmpsound3);
            final ImageView img4 = this.findViewById(R.id.tmpsound4);

            Util.intallView(0,0,0,linear1,this,img1,c1);
            Util.intallView(0,1,0,linear2,this,img2,c2);
            Util.intallView(1,0,0,linear3,this,img3,c3);
            Util.intallView(1,1,0,linear4,this,img4,c4);
            Util.intallView(0,2,0,linear5,this,null,c5);

        }catch(Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
            Log.d("error",e.getMessage());
            e.printStackTrace();
        }finally{
            //if(c!=null)c.close();
        }
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = (View)htmp.this.findViewById(id);
                linearLayout.removeView(view);
                try{
                    List<Health> list = RandomService.query("tmp","today");
                    if(list.size()<3){
                        TextView tmp = new TextView(htmp.this);
                        tmp.setId(id);
                        tmp.setText("当前无统计数据");
                        linearLayout.addView(tmp);
                    }else{
                        List<BrokenLine>bl = new ArrayList<BrokenLine>();
                        BrokenLineView blv = new BrokenLineView(htmp.this);
                        blv.setId(id);
                        double temp1;

                        for(Health tmp:list){
                            temp1 = Double.parseDouble(String.format("%.1f",tmp.getTmp()));
                            BrokenLine blt = new BrokenLine("今天",temp1,0);
                            bl.add(blt);
                        }
                        blv.setMdata(bl);
                        linearLayout.addView(blv);
                    }}catch(Exception e){
                    TextView tmp = new TextView(htmp.this);
                    tmp.setId(id);
                    tmp.setText("当前无统计数据");
                    linearLayout.addView(tmp);
                }
            }
        });
        week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = (View)htmp.this.findViewById(id);
                linearLayout.removeView(view);
                try{
                    List<Health> list = RandomService.query("tmp","week");
                    if(list.size()<3){
                        TextView tmp = new TextView(htmp.this);
                        tmp.setId(id);
                        tmp.setText("当前无统计数据");
                        linearLayout.addView(tmp);
                    }else{
                        List<BrokenLine>bl = new ArrayList<BrokenLine>();
                        BrokenLineView blv = new BrokenLineView(htmp.this);
                        blv.setId(id);
                        double temp1;

                        for(Health tmp:list){
                            temp1 = Double.parseDouble(String.format("%.1f",tmp.getTmp()));
                            BrokenLine blt = new BrokenLine(tmp.getDate(),temp1,0);
                            bl.add(blt);
                        }
                        blv.setMdata(bl);
                        linearLayout.addView(blv);
                    }}catch(Exception e){
                    TextView tmp = new TextView(htmp.this);
                    tmp.setId(id);
                    tmp.setText("当前无统计数据");
                    linearLayout.addView(tmp);
                }
            }
        });

        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = (View)htmp.this.findViewById(id);
                linearLayout.removeView(view);
                try{
                    List<Health> list = RandomService.query("tmp","month");
                    if(list.size()<3){
                        TextView tmp = new TextView(htmp.this);
                        tmp.setId(id);
                        tmp.setText("当前无统计数据");
                        linearLayout.addView(tmp);
                    }else{
                        List<BrokenLine>bl = new ArrayList<BrokenLine>();
                        BrokenLineView blv = new BrokenLineView(htmp.this);
                        blv.setId(id);
                        double temp1;

                        for(Health tmp:list){
                            temp1 = Double.parseDouble(String.format("%.1f",tmp.getTmp()));
                            BrokenLine blt = new BrokenLine(tmp.getDate(),temp1,0);
                            bl.add(blt);
                        }
                        blv.setMdata(bl);
                        linearLayout.addView(blv);
                    }}catch(Exception e){
                    TextView tmp = new TextView(htmp.this);
                    tmp.setId(id);
                    tmp.setText("当前无统计数据");
                    linearLayout.addView(tmp);
                }
            }
        });

    }

    public boolean onKeyDown(int keyCode, KeyEvent event){  //模块测试环节先这么写，到时候有了导航栏再改不迟
        if(keyCode==KeyEvent.KEYCODE_BACK){

            startActivity(new Intent(this,MainActivity.class));
            this.finish();
        }
        return false;
    }


    public void onDestroy(){
        super.onDestroy();
        unbindService(sc);   //解除service绑定
        Log.d("warning","exit");
        if(mReceiver!=null){
            unregisterReceiver(mReceiver);  //解除广播接收器绑定
        }
        RandomService.soundStop();
    }

}
