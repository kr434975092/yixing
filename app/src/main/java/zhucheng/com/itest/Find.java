package zhucheng.com.itest;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Find extends AppCompatActivity {

    private Receiver mReceiver = null;
    private RelativeLayout remain1 = null;
    private RelativeLayout remain2 = null;
    private RelativeLayout remain3 = null;
    private RadioGroup rgGroup = null;
    private RadioButton rbHealth = null;
    private RadioButton rbGps = null;
    private RadioButton rbFind = null;
    //private RadioButton rbMe = null;
    private RollPagerView mRollViewPager;
    private String host = "http://v.juhe.cn/toutiao/index";
    private ListView newsList = null;
    private int dpi = 0;
    private List<String>newsUrl = new ArrayList<String>();

    public class Receiver extends BroadcastReceiver {   //广播接收者

        @Override
        public void onReceive(Context context, Intent intent) {  //接收广播
            int flag = intent.getExtras().getInt("errorflag");
            if(flag==0){

            }
            else if(flag==1){
                Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.warn1);
                WarnToast.makeImage(Find.this,source, Toast.LENGTH_LONG).show();
            }else if(flag==2){
                Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.warn3);
                WarnToast.makeImage(Find.this,source,Toast.LENGTH_LONG).show();
            }else if(flag==3){
                Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.warn2);
                WarnToast.makeImage(Find.this,source,Toast.LENGTH_LONG).show();
            }else if(flag==4){
                Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.warn4);
                WarnToast.makeImage(Find.this,source,Toast.LENGTH_LONG).show();
            }
        }
    }

    private void doRegisterReceiver(){  //注册广播接收器
        mReceiver = new Receiver();
        IntentFilter filter = new IntentFilter("zhucheng.com.itest.content");
        registerReceiver(mReceiver,filter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        dpi = metrics.densityDpi;
        bindService(new Intent(this,RandomService.class),sc,BIND_AUTO_CREATE);  //绑定service
        doRegisterReceiver();
        remain1 = (RelativeLayout)this.findViewById(R.id.find1);
        remain1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Find.this,Weather.class));
                //但是界面不要销毁，不然还得再绑定一个service服务等耗时麻烦的操作
            }
        });
        remain2 = (RelativeLayout)this.findViewById(R.id.find2);
        remain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Find.this,car.class));
            }
        });
        remain3 = (RelativeLayout)this.findViewById(R.id.find3);
        remain3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Find.this,road.class));
            }
        });
        mRollViewPager = (RollPagerView) findViewById(R.id.roll_view_pager);

        //设置播放时间间隔
        mRollViewPager.setPlayDelay(3*1000);
        //设置透明度
        mRollViewPager.setAnimationDurtion(500);
        //设置适配器
        mRollViewPager.setAdapter(new TestNormalAdapter());

        //设置指示器（顺序依次）
        //自定义指示器图片
        //设置圆点指示器颜色
        //设置文字指示器
        //隐藏指示器
        //mRollViewPager.setHintView(new IconHintView(this, R.drawable.point_focus, R.drawable.point_normal));
        mRollViewPager.setHintView(new ColorPointHintView(this, Color.YELLOW, Color.WHITE));
        //mRollViewPager.setHintView(new TextHintView(this));
        //mRollViewPager.setHintView(null);

        newsList = (ListView)this.findViewById(R.id.newslist);



        rgGroup = (RadioGroup) findViewById(R.id.rg_tab_bar3);
        rbHealth = (RadioButton) findViewById(R.id.rb_health3);

        rbGps = (RadioButton) findViewById(R.id.rb_gps3);
        rbFind = (RadioButton) findViewById(R.id.rb_find3);
        rbFind.setChecked(true);
        //rbMe = (RadioButton) findViewById(R.id.rb_me3);

        rbGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MapUtil.inInstallByread("com.autonavi.minimap"))MapUtil.setUpAmpByName(Find.this);
                else if(MapUtil.inInstallByread("com.baidu.BaiduMap"))MapUtil.setUpBaiduMapByName(Find.this);
                else Toast.makeText(Find.this,"请先安装高德地图或百度地图!",Toast.LENGTH_LONG).show();
            }
        });

        //定义底部标签图片大小
        rbHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Find.this,MainActivity.class));
                Find.this.finish();
            }
        });

        Map<String,String>query = new HashMap<String,String>();
        query.put("type","jiankang");
        query.put("key","3a6dea4133a3c2b5400763b49412a8e2");
        NewsGet task = new NewsGet();
        task.execute(query);
    }

    protected void onDestroy() {
        super.onDestroy();
        unbindService(sc);   //解除service绑定
        if(mReceiver!=null){
            unregisterReceiver(mReceiver);  //解除广播接收器绑定
        }
    }

    private class TestNormalAdapter extends StaticPagerAdapter {
        private int[] imgs = {
                R.drawable.first,
                R.drawable.second,
                R.drawable.third,
        };


        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }


        public int getCount() {
            return imgs.length;
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

    public boolean onKeyDown(int keyCode, KeyEvent event){  //模块测试环节先这么写，到时候有了导航栏再改不迟
        if(keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(this,MainActivity.class));
            this.finish();
        }
        return false;
    }

    class NewsGet extends AsyncTask<Map<String,String>,Void,List<News>>{

        @Override
        protected List<News> doInBackground(Map<String, String>[] maps) {
            String result = null;
            Map<String,String>querys2 = maps[0];
            result = HttpUtil.doGet(host,null,querys2);
            List<News> list = new ArrayList<News>();
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject child1 = jsonObject.getJSONObject("result");
                JSONArray array = child1.getJSONArray("data");
                int size = array.length();
                for(int i = 0;i<size;++i){
                    News news = new News();
                    String url = array.getJSONObject(i).getString("thumbnail_pic_s");
                    news.logo = HttpUtil.getHttpBitmap(url);
                    news.date = array.getJSONObject(i).getString("date")+" "+array.getJSONObject(i).getString("author_name");
                    news.title = array.getJSONObject(i).getString("title");
                    newsUrl.add(array.getJSONObject(i).getString("url"));
                    list.add(news);
                }
            } catch (JSONException e) {
                Log.d("error","新闻可视化出错！");
            }finally{

            }
            return list;
        }

        protected void onPostExecute(List<News> list){
            NewsListAdapter adapter = new NewsListAdapter(Find.this,list);
            newsList.setAdapter(adapter);
            ViewGroup.LayoutParams params = newsList.getLayoutParams();
            params.height = (list.size()*100+20)*dpi/160;
            newsList.setLayoutParams(params);
            newsList.setOnItemClickListener(newsListListener);
        }


    }
    public AdapterView.OnItemClickListener newsListListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("url",newsUrl.get(position));
                Intent intent = new Intent();
                intent.setClass(Find.this,NewsView.class);
                intent.putExtras(bundle);
                startActivity(intent);
        }
    };




}
