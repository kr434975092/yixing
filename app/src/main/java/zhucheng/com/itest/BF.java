package zhucheng.com.itest;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class BF extends AppCompatActivity{  //到时候要注册时间监听器

    private Receiver mReceiver = null;
    private TextView today = null;
    private TextView week = null;
    private TextView month = null;
    private TextView tc = null;
    private TextView tg = null;
    private TextView tips = null;
    private RelativeLayout relativeLayout = null;
    private LinearLayout linearLayout = null;
    private int id = 100001;

    //视图位置
    LinearLayout linear11 = null;
    LinearLayout linear12 = null;
    LinearLayout linear13 = null;
    LinearLayout linear14 = null;
    LinearLayout linear15 = null;
    //状况分析
    TextView c11 = null;
    TextView c12 = null;
    TextView c13 = null;
    TextView c14 = null;
    TextView c15 = null;
    //视图位置
    LinearLayout linear21 = null;
    LinearLayout linear22 = null;
    LinearLayout linear23 = null;
    LinearLayout linear24 = null;
    LinearLayout linear25 = null;
    //状况分析
    TextView c21 = null;
    TextView c22 = null;
    TextView c23 = null;
    TextView c24 = null;
    TextView c25 = null;

    class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int flag = intent.getExtras().getInt("errorflag");
            if(flag==0){
            tc.setText(RandomUtil.std[4]+"");
            tg.setText(RandomUtil.std[5]+"");}
            else if(flag==1){
                Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.warn1);
                WarnToast.makeImage(BF.this,source, Toast.LENGTH_LONG).show();
            }else if(flag==2){
                Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.warn3);
                WarnToast.makeImage(BF.this,source,Toast.LENGTH_LONG).show();
            }else if(flag==3){
                Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.warn2);
                WarnToast.makeImage(BF.this,source,Toast.LENGTH_LONG).show();
            }else if(flag==4){
                Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.warn4);
                WarnToast.makeImage(BF.this,source,Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bf);
        today = (TextView)this.findViewById(R.id.todaybf);
        week = (TextView)this.findViewById(R.id.weekbf);
        month = (TextView)this.findViewById(R.id.monthbf);
        tips = (TextView)this.findViewById(R.id.tips);
        tc = (TextView)this.findViewById(R.id.tc);
        tg = (TextView)this.findViewById(R.id.tg);
        tc.setText(RandomUtil.std[4]+"");
        tg.setText(RandomUtil.std[5]+"");

        bindService(new Intent(this,RandomService.class),sc,BIND_AUTO_CREATE);  //绑定service
        doRegisterReceiver();

        relativeLayout = (RelativeLayout)this.findViewById(R.id.photoW);
        linearLayout = (LinearLayout)this.findViewById(R.id.linearbf);

        linear11 = (LinearLayout)this.findViewById(R.id.tcv1);
        linear12 = (LinearLayout)this.findViewById(R.id.tcv2);
        linear13 = (LinearLayout)this.findViewById(R.id.tcv3);
        linear14 = (LinearLayout)this.findViewById(R.id.tcv4);
        linear15 = (LinearLayout)this.findViewById(R.id.tcin);
        linear21 = (LinearLayout)this.findViewById(R.id.tgv1);
        linear22 = (LinearLayout)this.findViewById(R.id.tgv2);
        linear23 = (LinearLayout)this.findViewById(R.id.tgv3);
        linear24 = (LinearLayout)this.findViewById(R.id.tgv4);
        linear25 = (LinearLayout)this.findViewById(R.id.tgin);

        c11 = (TextView)this.findViewById(R.id.tcc1);
        c12 = (TextView)this.findViewById(R.id.tcc2);
        c13 = (TextView)this.findViewById(R.id.tcc3);
        c14 = (TextView)this.findViewById(R.id.tcc4);
        c15 = (TextView)this.findViewById(R.id.tcc5);
        c21 = (TextView)this.findViewById(R.id.tgc1);
        c22 = (TextView)this.findViewById(R.id.tgc2);
        c23 = (TextView)this.findViewById(R.id.tgc3);
        c24 = (TextView)this.findViewById(R.id.tgc4);
        c25 = (TextView)this.findViewById(R.id.tgc5);


        new Thread(new Runnable(){
            public void run(){
                try{
                List<Health> list = RandomService.query("tcg","today");
                if(list.size()<3){
                    TextView tmp = new TextView(BF.this);
                    tmp.setId(id);
                    tmp.setText("当前无统计数据");
                    linearLayout.addView(tmp);
                    tips.setVisibility(View.GONE);
                }else{
                    List<BrokenLine>bl = new ArrayList<BrokenLine>();
                    BrokenLineView blv = new BrokenLineView(BF.this);
                    blv.setId(id);
                    double temp1;
                    double temp2;
                    for(Health tmp:list){
                        temp1 = Double.parseDouble(String.format("%.2f",tmp.getTc()));
                        temp2 = Double.parseDouble(String.format("%.2f",tmp.getTg()));
                        BrokenLine blt = new BrokenLine("今天",temp1,temp2);
                        bl.add(blt);
                    }
                    blv.setMdata(bl);
                    linearLayout.addView(blv);
                    tips.setVisibility(View.VISIBLE);
                }}catch(Exception e){
                    TextView tmp = new TextView(BF.this);
                    tmp.setId(id);
                    tmp.setText("当前无统计数据");
                    linearLayout.addView(tmp);
                    tips.setVisibility(View.GONE);
                }
            }
        }).start();
        try{
            final ImageView img11 = this.findViewById(R.id.tcsound1);
            final ImageView img12 = this.findViewById(R.id.tcsound2);
            final ImageView img13 = this.findViewById(R.id.tcsound3);
            final ImageView img14 = this.findViewById(R.id.tcsound4);
            final ImageView img21 = this.findViewById(R.id.tgsound1);
            final ImageView img22 = this.findViewById(R.id.tgsound2);
            final ImageView img23 = this.findViewById(R.id.tgsound3);
            final ImageView img24 = this.findViewById(R.id.tgsound4);

            Util.intallView(0,0,4,linear11,this,img11,c11);
            Util.intallView(0,1,4,linear12,this,img12,c12);
            Util.intallView(1,0,4,linear13,this,img13,c13);
            Util.intallView(1,1,4,linear14,this,img14,c14);
            Util.intallView(0,2,4,linear15,this,null,c15);

            Util.intallView(0,0,5,linear21,this,img21,c21);
            Util.intallView(0,1,5,linear22,this,img22,c22);
            Util.intallView(1,0,5,linear23,this,img23,c23);
            Util.intallView(1,1,5,linear24,this,img24,c24);
            Util.intallView(0,2,5,linear25,this,null,c25);

        }catch(Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
            Log.d("error",e.getMessage());
            e.printStackTrace();
        }finally{

        }


        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View viewTmp = (View)BF.this.findViewById(id);
                linearLayout.removeView(viewTmp);
                try{
                List<Health> list = RandomService.query("tcg","today");
                if(list.size()<3){
                    TextView tmp = new TextView(BF.this);
                    tmp.setId(id);
                    tmp.setText("当前无统计数据");
                    linearLayout.addView(tmp);
                    tips.setVisibility(View.GONE);
                }else{
                    List<BrokenLine>bl = new ArrayList<BrokenLine>();
                    BrokenLineView blv = new BrokenLineView(BF.this);
                    blv.setId(id);
                    double temp1;
                    double temp2;
                    for(Health tmp:list){
                        temp1 = Double.parseDouble(String.format("%.2f",tmp.getTc()));
                        temp2 = Double.parseDouble(String.format("%.2f",tmp.getTg()));
                        BrokenLine blt = new BrokenLine("今天",temp1,temp2);
                        bl.add(blt);
                    }
                    blv.setMdata(bl);
                    linearLayout.addView(blv);
                    tips.setVisibility(View.VISIBLE);
                }}catch(Exception e){
                    TextView tmp = new TextView(BF.this);
                    tmp.setId(id);
                    tmp.setText("当前无统计数据");
                    linearLayout.addView(tmp);
                    tips.setVisibility(View.GONE);
                }
            }
        });
        week.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                View viewTmp = (View)BF.this.findViewById(id);
                linearLayout.removeView(viewTmp);
                try{
                List<Health> list = RandomService.query("tcg","week");
                Log.d("num",""+list.size());
                if(list.size()<3){
                    TextView tmp = new TextView(BF.this);
                    tmp.setId(id);
                    tmp.setText("当前无统计数据");
                    linearLayout.addView(tmp);
                    tips.setVisibility(View.GONE);
                }else{
                    List<BrokenLine>bl = new ArrayList<BrokenLine>();
                    BrokenLineView blv = new BrokenLineView(BF.this);
                    blv.setId(id);
                    double temp1;
                    double temp2;
                    for(Health tmp:list){
                        temp1 = Double.parseDouble(String.format("%.2f",tmp.getTc()));
                        temp2 = Double.parseDouble(String.format("%.2f",tmp.getTg()));
                        BrokenLine blt = new BrokenLine(tmp.getDate(),temp1,temp2);
                        bl.add(blt);
                    }
                    blv.setMdata(bl);
                    linearLayout.addView(blv);
                    tips.setVisibility(View.VISIBLE);
                }}catch(Exception e){
                    TextView tmp = new TextView(BF.this);
                    tmp.setId(id);
                    tmp.setText("当前无统计数据");
                    linearLayout.addView(tmp);
                    tips.setVisibility(View.GONE);
                }
            }
        });
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View viewTmp = (View)BF.this.findViewById(id);
                linearLayout.removeView(viewTmp);
                try{
                List<Health> list = RandomService.query("tcg","month");
                Log.d("num",""+list.size());
                if(list.size()<3){
                    TextView tmp = new TextView(BF.this);
                    tmp.setId(id);
                    tmp.setText("当前无统计数据");
                    linearLayout.addView(tmp);
                    tips.setVisibility(View.GONE);
                }else{
                    List<BrokenLine>bl = new ArrayList<BrokenLine>();
                    BrokenLineView blv = new BrokenLineView(BF.this);
                    blv.setId(id);
                    double temp1;
                    double temp2;
                    for(Health tmp:list){
                        temp1 = Double.parseDouble(String.format("%.2f",tmp.getTc()));
                        temp2 = Double.parseDouble(String.format("%.2f",tmp.getTg()));
                        BrokenLine blt = new BrokenLine(tmp.getDate(),temp1,temp2);
                        bl.add(blt);
                    }
                    blv.setMdata(bl);
                    linearLayout.addView(blv);
                    tips.setVisibility(View.VISIBLE);
                }}catch(Exception e){
                    TextView tmp = new TextView(BF.this);
                    tmp.setId(id);
                    tmp.setText("当前无统计数据");
                    linearLayout.addView(tmp);
                    tips.setVisibility(View.GONE);
                }
            }
        });
    }

    //必备参数
    ServiceConnection sc = new ServiceConnection() {  //参数必备
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    private void doRegisterReceiver(){
        mReceiver = new BF.Receiver();
        IntentFilter filter = new IntentFilter("zhucheng.com.itest.content");
        registerReceiver(mReceiver,filter);
    }

    protected void onDestroy(){   //销毁activity   导航栏还没有做，这个销毁后去那个界面的Intent跳转再议
        super.onDestroy();
        unbindService(sc);   //解除service绑定
        Log.d("warning","exit");
        if(mReceiver!=null){
            unregisterReceiver(mReceiver);  //解除广播接收器绑定
        }
        RandomService.soundStop();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){  //模块测试环节先这么写，到时候有了导航栏再改不迟
        if(keyCode==KeyEvent.KEYCODE_BACK){

            startActivity(new Intent(this,MainActivity.class));
            this.finish();
        }
        return false;
    }


}
