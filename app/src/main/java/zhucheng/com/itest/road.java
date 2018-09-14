package zhucheng.com.itest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class road extends AppCompatActivity {

    private TextView textView = null;
    private TextView tmp = null;
    private ListView listView =null;
    private TextView sMoney = null;
    private TextView sMark = null;
    private TextView sum = null;
    private int marksum = 0;
    private int smoney = 0;
    private int msgcode = -10000;
    private int dpi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        dpi = metrics.densityDpi;
        textView = (TextView)this.findViewById(R.id.xianxing);
        tmp = (TextView)this.findViewById(R.id.tmp12);
        sMoney = (TextView)this.findViewById(R.id.textView9);
        sMark = (TextView)this.findViewById(R.id.textView14);
        sum = (TextView)this.findViewById(R.id.textView12);
        listView = (ListView)this.findViewById(R.id.brList);
        if(Util.TR!=-1)textView.setText("今日限行尾号为:"+Util.TR);
        else textView.setText("未获取到今日限行尾号");
        Map<String,String>query = new HashMap<String,String>();
        query.put("key",ApiKey.BRKEY);
        query.put("city",Util.CITY3);
        query.put("hphm",Util.PLATE);
        query.put("hpzl","02");
        WGet task = new road.WGet();
        task.execute(query);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){  //模块测试环节先这么写，到时候有了导航栏再改不迟
        if(keyCode==KeyEvent.KEYCODE_BACK){
            this.finish();
        }
        return false;
    }

    class WGet extends AsyncTask<Map<String,String>,Void,List<BR>> {

        @Override
        protected List<BR> doInBackground(Map<String, String>[] maps) {
            List<BR>list = new ArrayList<>();

            String result = null;
            Map<String,String>query1 = maps[0];
            result = HttpUtil.doGet(ApiKey.BRHOST,null,query1);
            try {
                JSONObject jsonObject1 = new JSONObject(result);
                JSONObject jsonObject = jsonObject1.getJSONObject("result");
                JSONArray array = jsonObject.getJSONArray("lists");
                for(int i = 0;i<array.length();i++){
                    BR br = new BR();
                    br.setTime(array.getJSONObject(i).getString("date"));
                    br.setAccdient(array.getJSONObject(i).getString("act"));
                    br.setPlace(array.getJSONObject(i).getString("wzcity")+array.getJSONObject(i).getString("area"));
                    String mark = array.getJSONObject(i).getString("fen");
                    String money = array.getJSONObject(i).getString("moeny");
                    br.setMark(mark+"分");
                    br.setMoney(money+"元");
                    marksum += Integer.parseInt(mark);
                    smoney += Integer.parseInt(money);
                    list.add(br);
                }
            } catch (Exception e) {
                System.out.print(e.getMessage());

            }
            return list;
        }
        protected void onPostExecute(List<BR>list){
           int len = list.size();
           if(len!=0){
               BreakRuleListAdapter adapter = new BreakRuleListAdapter(road.this,list);
               listView.setAdapter(adapter);
               tmp.setVisibility(View.GONE);
               sum.setText(len+"条");
               sMoney.setText(smoney+"元");
               sMark.setText(marksum+"分");
           }else{
               listView.setVisibility(View.GONE);
               sum.setText(len+"条");
               sMoney.setText(smoney+"元");
               sMark.setText(marksum+"分");
           }
        }

    }
}
