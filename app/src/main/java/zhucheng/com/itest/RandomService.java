package zhucheng.com.itest;


import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

/**
 * Created by 罗爽 on 2018/4/20.
 */

public class RandomService extends Service {
    private static boolean isRunning = true;
    private static Thread rThread = null;
    public static int addNum = 0;
    private static DBManager db = null;
    private static double[] avg = new double[6];
    private static double[] davg = new double[12];
    private static TextToSpeech textToSpeech;

    public final class LocalBinder extends Binder {
        public RandomService getService() {
            return RandomService.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    public void onCreate() {
        super.onCreate();
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == textToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.CHINA);
                    if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE && result != TextToSpeech.LANG_AVAILABLE)
                        Toast.makeText(RandomService.this, "TTS暂时不支持这种语音的朗读!", Toast.LENGTH_SHORT).show();
                    if(Util.TR!=-1)RandomService.sound("今日的限行尾号为"+Util.TR);
                    else RandomService.sound("未查询到当日的限行尾号");
                }
            }
        });
        for (int i = 0; i < 6; i++) avg[i] = 0;


        db = new DBManager(RandomService.this);


        davg[0] = 37.0;
        davg[1] = 36.0;
        davg[2] = 100;
        davg[3] = 60;
        davg[4] = 136;
        davg[5] = 115;
        davg[6] = 87;
        davg[7] = 73;
        davg[8] = 5.17;
        davg[9] = 2.85;
        davg[10] = 1.69;
        davg[11] = 0.56;


        this.asyn();
    }

    public void asyn() {

        rThread = new Thread(new Runnable() {
            public void run() {
                while (isRunning) {
                    try {
                        Thread.sleep(30 * 1000);  //每30s更新一次
                    } catch (InterruptedException e) {
                        break;
                    }
                    RandomUtil.getRandom();
                    addNum++;
                    for (int i = 0; i < 6; i++)
                        avg[i] += RandomUtil.std[i];
                    if (addNum == 120) {    //每一小时插入一次数据
                        addNum = 0;
                        final Health health = new Health();
                        health.setTmp(avg[0] / 120);


                        health.setHi(avg[1] / 120);
                        health.setHbp(avg[2] / 120);
                        health.setLbp(avg[3] / 120);


                        health.setTc(avg[4] / 120);
                        health.setTg(avg[5] / 120);
                        for (int i = 0; i < 6; i++)
                            avg[i] = 0;
                        new Thread(new Runnable() {  //在异步线程中完成插入操作

                            @Override
                            public void run() {
                                try {
                                    db.insert(health);
                                    //insertFlag = 0;
                                } catch (Exception e) {
                                    //insertFlag = 1;
                                    Log.d("insertError", "数据插入有误");
                                }
                            }
                        }).start();
                    }
                    Intent intent = new Intent();
                    intent.setAction("zhucheng.com.itest.content");
                    Bundle bundle = new Bundle();
                    bundle.putInt("errorflag", 0);
                    intent.putExtras(bundle);
                    sendBroadcast(intent);
                    new Thread(new Runnable() {
                        public void run() {
                            int flag = 0;
                            StringBuffer sb = new StringBuffer("您当前的");
                            if (RandomUtil.std[0] > davg[0] || RandomUtil.std[0] < davg[1]) {
                                sb.append("体温");
                                try {
                                    Date day = new Date();
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                    Calendar c = Calendar.getInstance();
                                    int hour = c.get(Calendar.HOUR_OF_DAY);
                                    int minute = c.get(Calendar.MINUTE);
                                    db.execSQL("insert into normal values(?,?,?,?,?)", new Object[]{df.format(day), hour, minute, "tmp", RandomUtil.std[0]});
                                } catch (Exception e) {
                                    Log.d("SQLException", "normal插入失败!");
                                    e.printStackTrace();
                                }
                                flag = 4;
                            }
                            if (RandomUtil.std[1] > davg[2] || RandomUtil.std[1] < davg[3]) {
                                sb.append("心率");
                                try {
                                    Date day = new Date();
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                    Calendar c = Calendar.getInstance();
                                    int hour = c.get(Calendar.HOUR_OF_DAY);
                                    int minute = c.get(Calendar.MINUTE);
                                    db.execSQL("insert into normal values(?,?,?,?,?)", new Object[]{df.format(day), hour, minute, "hi", RandomUtil.std[1]});
                                } catch (Exception e) {
                                    Log.d("SQLException", "normal插入失败!");
                                    e.printStackTrace();
                                }
                                flag = 1;
                            }
                            if (RandomUtil.std[2] > davg[4] || RandomUtil.std[2] < davg[5]) {
                                sb.append("血压");
                                try {
                                    Date day = new Date();
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                    Calendar c = Calendar.getInstance();
                                    int hour = c.get(Calendar.HOUR_OF_DAY);
                                    int minute = c.get(Calendar.MINUTE);
                                    db.execSQL("insert into normal values(?,?,?,?,?)", new Object[]{df.format(day), hour, minute, "hbp", RandomUtil.std[2]});
                                } catch (Exception e) {
                                    Log.d("SQLException", "normal插入失败!");
                                    e.printStackTrace();
                                }
                                flag = 2;
                            }
                            if (RandomUtil.std[3] > davg[6] || RandomUtil.std[3] < davg[7]) {
                                sb.append("血压");
                                try {
                                    Date day = new Date();
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                    Calendar c = Calendar.getInstance();
                                    int hour = c.get(Calendar.HOUR_OF_DAY);
                                    int minute = c.get(Calendar.MINUTE);
                                    db.execSQL("insert into normal values(?,?,?,?,?)", new Object[]{df.format(day), hour, minute, "lbp", RandomUtil.std[3]});
                                } catch (Exception e) {
                                    Log.d("SQLException", "normal插入失败!");
                                    e.printStackTrace();
                                }
                                flag = 2;
                            }
                            if (RandomUtil.std[4] > davg[8] || RandomUtil.std[4] < davg[9]) {
                                sb.append("血脂");
                                Log.d("ero", RandomUtil.std[4] + " " + davg[8] + "-" + davg[9]);
                                try {
                                    Date day = new Date();
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                    Calendar c = Calendar.getInstance();
                                    int hour = c.get(Calendar.HOUR_OF_DAY);
                                    int minute = c.get(Calendar.MINUTE);
                                    db.execSQL("insert into normal values(?,?,?,?,?)", new Object[]{df.format(day), hour, minute, "tc", RandomUtil.std[4]});
                                } catch (Exception e) {
                                    Log.d("SQLException", "normal插入失败!");
                                    e.printStackTrace();
                                }
                                flag = 3;
                            }
                            if (RandomUtil.std[5] > davg[10] || RandomUtil.std[5] < davg[11]) {
                                sb.append("血脂");
                                Log.d("ero", RandomUtil.std[5] + " " + davg[10] + "-" + davg[11]);
                                try {
                                    Date day = new Date();
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                    Calendar c = Calendar.getInstance();
                                    int hour = c.get(Calendar.HOUR_OF_DAY);
                                    int minute = c.get(Calendar.MINUTE);
                                    db.execSQL("insert into normal values(?,?,?,?,?)", new Object[]{df.format(day), hour, minute, "tg", RandomUtil.std[5]});
                                } catch (Exception e) {
                                    Log.d("SQLException", "normal插入失败!");
                                    e.printStackTrace();
                                }
                                flag = 3;
                            }
                            if (flag != 0) {
                                sb.append("指数异常，请及时检查!");
                                textToSpeech.speak(sb.toString(), TextToSpeech.QUEUE_FLUSH, null);
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putInt("errorflag", flag);
                                intent.putExtras(bundle);
                                intent.setAction("zhucheng.com.itest.content");
                                sendBroadcast(intent);
                            }
                        }
                    }).start();
                }
                db.closeDB();
                Log.d("flag", "dbclose");
            }
        });
        rThread.start();
    }

    public static void stop() {
        textToSpeech.shutdown();
        isRunning = false;
        rThread.interrupt();
    }

    public static List<Health> query() throws Exception {
        return db.query();
    }

    public static Cursor rawQuery(String sql, String[] Items) throws Exception {
        return db.rawQuery(sql, Items);
    }

    public static List<Health> query(String attr, String request) throws Exception {
        if (attr.equals("tcg")) return db.queryTcg(request);
        else if (attr.equals("bp")) return db.queryBp(request);
        else if (attr.equals("hi")) return db.queryHi(request);
        else return db.queryTm(request);
    }

    public static void sound(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    public static void soundStop() {
        textToSpeech.stop();
    }

    public boolean performClick(){
        final boolean result = true;
        System.out.println("呵呵");
        if(Util.TR!=-1)RandomService.sound("今日的限行尾号为"+Util.TR);
        else RandomService.sound("未查询到当日的限行尾号");
        System.out.println("呵呵");
        return result;
    }


}
