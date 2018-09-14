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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class Heart extends AppCompatActivity {

    private Receiver mReceiver = null;
    private TextView hi = null;
    private TextView hic = null;

    //视图位置
    LinearLayout linear1 = null;
    LinearLayout linear2 = null;
    LinearLayout linear3 = null;
    LinearLayout linear4 = null;
    LinearLayout linear5 = null;
    //状况分析
    TextView c1 = null;
    TextView c2 = null;
    TextView c3 = null;
    TextView c4 = null;
    TextView c5 = null;
    //叙述
    String[]content1 = new String[5];
    String[]content2 = new String[5];

    class Receiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int flag = intent.getExtras().getInt("errorflag");
            if(flag==0){
            hi.setText(String.format("%.0f",RandomUtil.std[1]));
            if(RandomUtil.std[1]>=50&&RandomUtil.std[1]<=100)hic.setText("心率正常");
            else hic.setText("心率异常");}
            else if(flag==1){
                Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.warn1);
                WarnToast.makeImage(Heart.this,source, Toast.LENGTH_LONG).show();
            }else if(flag==2){
                Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.warn3);
                WarnToast.makeImage(Heart.this,source,Toast.LENGTH_LONG).show();
            }else if(flag==3){
                Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.warn2);
                WarnToast.makeImage(Heart.this,source,Toast.LENGTH_LONG).show();
            }else if(flag==4){
                Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.warn4);
                WarnToast.makeImage(Heart.this,source,Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart);
        hi = (TextView)this.findViewById(R.id.hi);
        hic = (TextView)this.findViewById(R.id.hic);
        hi.setText(String.format("%.0f",RandomUtil.std[1]));


        bindService(new Intent(this,RandomService.class),sc,BIND_AUTO_CREATE);  //绑定service
        doRegisterReceiver();

        linear1 = (LinearLayout)this.findViewById(R.id.hiv1);
        linear2 = (LinearLayout)this.findViewById(R.id.hiv2);
        linear3 = (LinearLayout)this.findViewById(R.id.hiv3);
        linear4 = (LinearLayout)this.findViewById(R.id.hiv4);
        linear5 = (LinearLayout)this.findViewById(R.id.hiin);

        c1 = (TextView)this.findViewById(R.id.hic1);
        c2 = (TextView)this.findViewById(R.id.hic2);
        c3 = (TextView)this.findViewById(R.id.hic3);
        c4 = (TextView)this.findViewById(R.id.hic4);
        c5 = (TextView)this.findViewById(R.id.hic5);

        try{

            final ImageView img1 = this.findViewById(R.id.hisound1);
            final ImageView img2 = this.findViewById(R.id.hisound2);
            final ImageView img3 = this.findViewById(R.id.hisound3);
            final ImageView img4 = this.findViewById(R.id.hisound4);

            Util.intallView(0,0,1,linear1,this,img1,c1);
            Util.intallView(0,1,1,linear2,this,img2,c2);
            Util.intallView(1,0,1,linear3,this,img3,c3);
            Util.intallView(1,1,1,linear4,this,img4,c4);
            Util.intallView(0,2,1,linear5,this,null,c5);

        }catch(Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
            Log.d("error",e.getMessage());
            e.printStackTrace();
        }finally{

        }



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
        mReceiver = new Heart.Receiver();
        IntentFilter filter = new IntentFilter("zhucheng.com.itest.content");
        registerReceiver(mReceiver,filter);
    }

    protected void onDestroy(){   //销毁activity   导航栏还没有做,这个销毁后去那个界面的Intent跳转再议
        super.onDestroy();
        unbindService(sc);   //解除service绑定
        Log.d("warning","exit");
        if(mReceiver!=null){
            unregisterReceiver(mReceiver);  //解除广播接收器绑定
        }
        RandomService.soundStop();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){  //模块测试环节先这么写,到时候有了导航栏再改不迟
        if(keyCode==KeyEvent.KEYCODE_BACK){
           
            startActivity(new Intent(this,MainActivity.class));
            this.finish();
        }
        return false;
    }

}
