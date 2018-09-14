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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class BP extends AppCompatActivity {

    private Receiver mReceiver = null;
    private TextView today = null;
    private TextView week = null;
    private TextView month  = null;
    private TextView hbp = null;
    private TextView lbp = null;
    private TextView tips = null;
    private RelativeLayout linearLayout = null;
    private int id = 100002;

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
            hbp.setText(String.format("%.0f",RandomUtil.std[2]));
            lbp.setText(String.format("%.0f",RandomUtil.std[3]));}
            else if(flag==1){
                Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.warn1);
                WarnToast.makeImage(BP.this,source, Toast.LENGTH_LONG).show();
            }else if(flag==2){
                Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.warn3);
                WarnToast.makeImage(BP.this,source,Toast.LENGTH_LONG).show();
            }else if(flag==3){
                Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.warn2);
                WarnToast.makeImage(BP.this,source,Toast.LENGTH_LONG).show();
            }else if(flag==4){
                Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.warn4);
                WarnToast.makeImage(BP.this,source,Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bp);


        today = (TextView)this.findViewById(R.id.todaybp);
        week = (TextView)this.findViewById(R.id.weekbp);
        month = (TextView)this.findViewById(R.id.monthbp);
        hbp = (TextView)this.findViewById(R.id.hbp);
        lbp = (TextView)this.findViewById(R.id.lbp);
        hbp.setText(String.format("%.0f",RandomUtil.std[2]));
        lbp.setText(String.format("%.0f",RandomUtil.std[3]));
        tips = (TextView)this.findViewById(R.id.tips);

        bindService(new Intent(this,RandomService.class),sc,BIND_AUTO_CREATE);  //绑定service
        doRegisterReceiver();


        linearLayout = (RelativeLayout)this.findViewById(R.id.linearbp);
        linear11 = (LinearLayout)this.findViewById(R.id.hbpv1);
        linear12 = (LinearLayout)this.findViewById(R.id.hbpv2);
        linear13 = (LinearLayout)this.findViewById(R.id.hbpv3);
        linear14 = (LinearLayout)this.findViewById(R.id.hbpv4);
        linear15 = (LinearLayout)this.findViewById(R.id.hbpin);
        linear21 = (LinearLayout)this.findViewById(R.id.lbpv1);
        linear22 = (LinearLayout)this.findViewById(R.id.lbpv2);
        linear23 = (LinearLayout)this.findViewById(R.id.lbpv3);
        linear24 = (LinearLayout)this.findViewById(R.id.lbpv4);
        linear25 = (LinearLayout)this.findViewById(R.id.lbpin);

        c11 = (TextView)this.findViewById(R.id.hbpc1);
        c12 = (TextView)this.findViewById(R.id.hbpc2);
        c13 = (TextView)this.findViewById(R.id.hbpc3);
        c14 = (TextView)this.findViewById(R.id.hbpc4);
        c15 = (TextView)this.findViewById(R.id.hbpc5);
        c21 = (TextView)this.findViewById(R.id.lbpc1);
        c22 = (TextView)this.findViewById(R.id.lbpc2);
        c23 = (TextView)this.findViewById(R.id.lbpc3);
        c24 = (TextView)this.findViewById(R.id.lbpc4);
        c25 = (TextView)this.findViewById(R.id.lbpc5);


        new Thread(new Runnable(){
            public void run(){
                try {

                    List<Health> list = RandomService.query("bp", "today");
                    if (list.size() < 3) {
                        TextView tmp = new TextView(BP.this);
                        tmp.setId(id);
                        tmp.setText("当前无统计数据");
                        linearLayout.addView(tmp);
                        tips.setVisibility(View.GONE);
                    } else {
                        List<BrokenLine> bl = new ArrayList<BrokenLine>();
                        BrokenLineView blv = new BrokenLineView(BP.this);
                        blv.setId(id);
                        int temp1;
                        int temp2;
                        for (Health tmp : list) {
                            temp1 = Integer.parseInt(String.format("%.0f",tmp.getHbp()));
                            temp2 = Integer.parseInt(String.format("%.0f",tmp.getLbp()));
                            BrokenLine blt = new BrokenLine("今天", temp1, temp2);
                            bl.add(blt);
                        }
                        blv.setMdata(bl);
                        linearLayout.addView(blv);
                        tips.setVisibility(View.VISIBLE);
                    }

                }catch(Exception e){
                    TextView tmp = new TextView(BP.this);
                    tmp.setId(id);
                    tmp.setText("当前无统计数据");
                    linearLayout.addView(tmp);
                    tips.setVisibility(View.VISIBLE);
                }
            }
        }).start();

        try{

            final ImageView img11 = this.findViewById(R.id.hbpsound1);
            final ImageView img12 = this.findViewById(R.id.hbpsound2);
            final ImageView img13 = this.findViewById(R.id.hbpsound3);
            final ImageView img14 = this.findViewById(R.id.hbpsound4);
            final ImageView img21 = this.findViewById(R.id.lbpsound1);
            final ImageView img22 = this.findViewById(R.id.lbpsound2);
            final ImageView img23 = this.findViewById(R.id.lbpsound3);
            final ImageView img24 = this.findViewById(R.id.lbpsound4);

            Util.intallView(0,0,2,linear11,this,img11,c11);
            Util.intallView(0,1,2,linear12,this,img12,c12);
            Util.intallView(1,0,2,linear13,this,img13,c13);
            Util.intallView(1,1,2,linear14,this,img14,c14);
            Util.intallView(0,2,2,linear15,this,null,c15);

            Util.intallView(0,0,3,linear21,this,img21,c21);
            Util.intallView(0,1,3,linear22,this,img22,c22);
            Util.intallView(1,0,3,linear23,this,img23,c23);
            Util.intallView(1,1,3,linear24,this,img24,c24);
            Util.intallView(0,2,3,linear25,this,null,c25);

        }catch(Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
            Log.d("error",e.getMessage());
            e.printStackTrace();
        }finally{

        }




        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View viewTmp = (View)BP.this.findViewById(id);
                linearLayout.removeView(viewTmp);
                try {
//                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//
                    List<Health> list = RandomService.query("bp", "today");
                    if (list.size() < 3) {
                        TextView tmp = new TextView(BP.this);
                        tmp.setId(id);
                        tmp.setText("当前无统计数据");
                        linearLayout.addView(tmp);
                        tips.setVisibility(View.GONE);
                    } else {
                        List<BrokenLine> bl = new ArrayList<BrokenLine>();
                        BrokenLineView blv = new BrokenLineView(BP.this);
                        blv.setId(id);
                        int temp1;
                        int temp2;
                        for (Health tmp : list) {
                            temp1 = Integer.parseInt(String.format("%.0f",tmp.getHbp()));
                            temp2 = Integer.parseInt(String.format("%.0f",tmp.getLbp()));
                            BrokenLine blt = new BrokenLine("今天",temp1,temp2);
                            bl.add(blt);
                        }
                        blv.setMdata(bl);
                        linearLayout.addView(blv);
                        tips.setVisibility(View.VISIBLE);
                    }
                }catch(Exception e){
                    TextView tmp = new TextView(BP.this);
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
                View viewTmp = (View)BP.this.findViewById(id);
                linearLayout.removeView(viewTmp);
                try{
                List<Health> list = RandomService.query("bp","week");
                //Log.d("num",""+list.size());
                if(list.size()<3){
                    TextView tmp = new TextView(BP.this);
                    tmp.setId(id);
                    tmp.setText("当前无统计数据");
                    linearLayout.addView(tmp);
                    tips.setVisibility(View.GONE);
                }else{
                    List<BrokenLine>bl = new ArrayList<BrokenLine>();
                    BrokenLineView blv = new BrokenLineView(BP.this);
                    blv.setId(id);
                    int temp1;
                    int temp2;
                    for(Health tmp:list){
                        temp1 = Integer.parseInt(String.format("%.0f",tmp.getHbp()));
                        temp2 = Integer.parseInt(String.format("%.0f",tmp.getLbp()));
                        BrokenLine blt = new BrokenLine(tmp.getDate(),temp1,temp2);
                        bl.add(blt);
                    }
                    blv.setMdata(bl);
                    linearLayout.addView(blv);
                    tips.setVisibility(View.VISIBLE);
                }}catch(Exception e){
                    TextView tmp = new TextView(BP.this);
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
                View viewTmp = (View)BP.this.findViewById(id);
                linearLayout.removeView(viewTmp);
                try{
                List<Health> list = RandomService.query("bp","month");
                //Log.d("num",""+list.size());
                if(list.size()<3){
                    TextView tmp = new TextView(BP.this);
                    tmp.setId(id);
                    tmp.setText("当前无统计数据");
                    linearLayout.addView(tmp);
                    tips.setVisibility(View.GONE);
                }else{
                    List<BrokenLine>bl = new ArrayList<BrokenLine>();
                    BrokenLineView blv = new BrokenLineView(BP.this);
                    blv.setId(id);
                    int temp1;
                    int temp2;
                    for(Health tmp:list){
                        temp1 = Integer.parseInt(String.format("%.0f",tmp.getHbp()));
                        temp2 = Integer.parseInt(String.format("%.0f",tmp.getLbp()));
                        BrokenLine blt = new BrokenLine(tmp.getDate(),temp1,temp2);
                        bl.add(blt);
                    }
                    blv.setMdata(bl);
                    linearLayout.addView(blv);
                    tips.setVisibility(View.VISIBLE);
                }}catch(Exception e){
                    TextView tmp = new TextView(BP.this);
                    tmp.setId(id);
                    tmp.setText("当前无统计数据");
                    linearLayout.addView(tmp);
                    tips.setVisibility(View.GONE);
                }
            }
        });
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
        mReceiver = new BP.Receiver();
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
