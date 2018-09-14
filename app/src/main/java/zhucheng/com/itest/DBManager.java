package zhucheng.com.itest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by 罗爽 on 2018/4/18.
 */

//health(tmp REAL,hi INTEGER,hbp INTEGER,lbp INTEGER,tc REAL,tg REAL,wei INTEGER,date TEXT)

public class DBManager {
    private DatabaseHelper databaseHelper = null;
    private SQLiteDatabase db = null;
    public DBManager(Context context){
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
        databaseHelper.onCreate(db);
    }
    public void insert(Health health)throws Exception{
        String sql = "insert into health values(?,?,?,?,?,?,?,?,?,?)";
        Date day=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        DecimalFormat df1 = new DecimalFormat(".00");
        double mins = Double.parseDouble(df1.format(minute/60.0));
        int rhour = mins>0.5?hour+1:hour;
        db.execSQL("create table if not exists health(tmp REAL,hi INTEGER,hbp INTEGER,lbp INTEGER,tc REAL,tg REAL,date TEXT,hour INTEGER,mins REAL,rhour INTEGER)");
        db.execSQL(sql,new Object[]{health.getTmp(),health.getHi(),health.getHbp(),health.getLbp(),health.getTc(),health.getTg(),df.format(day),hour,mins,rhour});
        Log.d("DataBase","insert a record");
    }
    public void execSQL(String sql,Object[]params)throws Exception{
        if(params!=null)db.execSQL(sql,params);
        else db.execSQL(sql);
    }

    public Cursor rawQuery(String sql,String[]params)throws Exception{
         return db.rawQuery(sql,params);
    }

    public void delete(String str)throws Exception{
        String sql = "delete from health"+(str==null?"":" where date = ?");
        if(str==null)db.execSQL(sql);
        else db.execSQL(sql,new Object[]{str});
    }
    public void deletet()throws Exception{
        db.execSQL("drop table if exists health");
        db.close();
    }

    public List<Health>query()throws Exception{
        ArrayList<Health>array = new ArrayList<Health>();
        db.execSQL("create table if not exists health(tmp REAL,hi INTEGER,hbp INTEGER,lbp INTEGER,tc REAL,tg REAL,date TEXT,hour INTEGER,mins REAL,rhour INTEGER)");
        Cursor c = db.rawQuery("select * from health",null);
        while(c.moveToNext()){
            Health h = new Health();
            h.setTmp(c.getDouble(c.getColumnIndex("tmp")));
            h.setHi(c.getInt(c.getColumnIndex("hi")));
            h.setHbp(c.getInt(c.getColumnIndex("hbp")));
            h.setLbp(c.getInt(c.getColumnIndex("lbp")));
            h.setTc(c.getDouble(c.getColumnIndex("tc")));
            h.setTg(c.getDouble(c.getColumnIndex("tg")));
            h.setDate(c.getString(c.getColumnIndex("date")));
            h.setHour(c.getInt(c.getColumnIndex("hour")));
            h.setMins(c.getDouble(c.getColumnIndex("mins")));
            h.setRhour(c.getInt(c.getColumnIndex("rhour")));
            array.add(h);
        }
        c.close();
        return array;
    }

/*
    public float[] query(String date,String title){
        if(date.equals("today")){
            Cursor c = db.rawQuery("select count(*) from health where data like %   ?%",null);
        }
    }
*/
    public List<Health>queryBp(String tit)throws Exception{
        Vector<Health>vector = new Vector<Health>();
        String sql = null;
        if(tit.equals("today")){
            sql = "select hbp,lbp from health where date = date('now')";
            Cursor cursor = db.rawQuery(sql,null);
            while(cursor.moveToNext()){
                Health health = new Health();
                health.setHbp(cursor.getInt(cursor.getColumnIndex("hbp")));
                health.setLbp(cursor.getInt(cursor.getColumnIndex("lbp")));
                health.setDate("今天");
                vector.add(health);
            }
            cursor.close();
        }
        if(tit.equals("week")){
            sql = "select avg(hbp) t1,avg(lbp) t2,date from health group by date having date between date('now','-7 day') and date('now')";
            Cursor cursor = db.rawQuery(sql,null);
            while(cursor.moveToNext()){
                Health health = new Health();
                health.setHbp(cursor.getInt(cursor.getColumnIndex("t1")));
                health.setLbp(cursor.getInt(cursor.getColumnIndex("t2")));
                String str = cursor.getString(cursor.getColumnIndex("date"));
                String[]tmp=str.split("-");
                health.setDate(tmp[1]+"."+tmp[2]);
                vector.add(health);
            }
            cursor.close();
        }
        if(tit.equals("month")){
            sql = "select avg(hbp) t1,avg(lbp) t2,date from health group by date having date between date('now','-1 month') and date('now')";
            Cursor cursor = db.rawQuery(sql,null);
            while(cursor.moveToNext()){
                Health health = new Health();
                health.setHbp(cursor.getInt(cursor.getColumnIndex("t1")));
                health.setLbp(cursor.getInt(cursor.getColumnIndex("t2")));
                String str = cursor.getString(cursor.getColumnIndex("date"));
                String[]tmp=str.split("-");
                health.setDate(tmp[1]+"."+tmp[2]);
                vector.add(health);
            }
            cursor.close();
        }

        return vector;
    }

    public List<Health>queryTcg(String tit)throws Exception{
        Vector<Health>vector = new Vector<Health>();
        String sql = null;
        if(tit.equals("today")){
            sql = "select tc,tg from health where date = date('now')";
            Cursor cursor = db.rawQuery(sql,null);
            while(cursor.moveToNext()){
                Health health = new Health();
                health.setTc(cursor.getDouble(cursor.getColumnIndex("tc")));
                health.setTg(cursor.getDouble(cursor.getColumnIndex("tg")));
                health.setDate("今天");
                vector.add(health);
            }
            cursor.close();
        }
        if(tit.equals("week")){
            sql = "select avg(tc) t1,avg(tg) t2,date from health group by date having date between date('now','-7 day') and date('now')";
            Cursor cursor = db.rawQuery(sql,null);
            while(cursor.moveToNext()){
                Health health = new Health();
                health.setTc(cursor.getDouble(cursor.getColumnIndex("t1")));
                health.setTg(cursor.getDouble(cursor.getColumnIndex("t2")));
                String str = cursor.getString(cursor.getColumnIndex("date"));
                String[]tmp=str.split("-");
                health.setDate(tmp[1]+"."+tmp[2]);
                vector.add(health);
            }
            cursor.close();
        }
        if(tit.equals("month")){
            sql = "select avg(tc) t1,avg(tg) t2,date from health group by date having date between date('now','-1 month') and date('now')";
            Cursor cursor = db.rawQuery(sql,null);
            while(cursor.moveToNext()){
                Health health = new Health();
                health.setTc(cursor.getDouble(cursor.getColumnIndex("t1")));
                health.setTg(cursor.getDouble(cursor.getColumnIndex("t2")));
                String str = cursor.getString(cursor.getColumnIndex("date"));
                String[]tmp=str.split("-");
                health.setDate(tmp[1]+"."+tmp[2]);
                vector.add(health);
            }
            cursor.close();
        }

        return vector;
    }

    public List<Health>queryHi(String tit)throws Exception{
        Vector<Health>vector = new Vector<Health>();
        String sql = null;
        if(tit.equals("today")){
            sql = "select hi from health where date = date('now')";
            Cursor cursor = db.rawQuery(sql,null);
            while(cursor.moveToNext()){
                Health health = new Health();
                health.setHi(cursor.getInt(cursor.getColumnIndex("hi")));
                health.setDate("今天");
                vector.add(health);
            }
            cursor.close();
        }
        if(tit.equals("week")){
            sql = "select avg(hi) t1,date from health group by date having date between date('now','-7 day') and date('now')";
            Cursor cursor = db.rawQuery(sql,null);
            while(cursor.moveToNext()){
                Health health = new Health();
                health.setHi(cursor.getInt(cursor.getColumnIndex("t1")));
                String str = cursor.getString(cursor.getColumnIndex("date"));
                String[]tmp=str.split("-");
                health.setDate(tmp[1]+"."+tmp[2]);
                vector.add(health);
            }
            cursor.close();
        }
        if(tit.equals("month")){
            sql = "select avg(hi) t1,date from health group by date having date between date('now','-1 month') and date('now')";
            Cursor cursor = db.rawQuery(sql,null);
            while(cursor.moveToNext()){
                Health health = new Health();
                health.setHi(cursor.getInt(cursor.getColumnIndex("t1")));
                String str = cursor.getString(cursor.getColumnIndex("date"));
                String[]tmp=str.split("-");
                health.setDate(tmp[1]+"."+tmp[2]);
                vector.add(health);
            }
            cursor.close();
        }

        return vector;
    }

    public List<Health>queryTm(String tit)throws Exception{
        Vector<Health>vector = new Vector<Health>();
        String sql = null;
        if(tit.equals("today")){
            sql = "select tmp from health where date = date('now')";
            Cursor cursor = db.rawQuery(sql,null);
            while(cursor.moveToNext()){
                Health health = new Health();
                health.setTmp(cursor.getDouble(cursor.getColumnIndex("tmp")));
                health.setDate("今天");
                vector.add(health);
            }
            cursor.close();
        }
        if(tit.equals("week")){
            sql = "select avg(tmp) t1,date from health group by date having date between date('now','-7 day') and date('now')";
            Cursor cursor = db.rawQuery(sql,null);
            while(cursor.moveToNext()){
                Health health = new Health();
                health.setTmp(cursor.getDouble(cursor.getColumnIndex("t1")));
                String str = cursor.getString(cursor.getColumnIndex("date"));
                String[]tmp=str.split("-");
                health.setDate(tmp[1]+"."+tmp[2]);
                vector.add(health);
            }
            cursor.close();
        }
        if(tit.equals("month")){
            sql = "select avg(tmp) t1,date from health group by date having date between date('now','-1 month') and date('now')";
            Cursor cursor = db.rawQuery(sql,null);
            while(cursor.moveToNext()){
                Health health = new Health();
                health.setTmp(cursor.getDouble(cursor.getColumnIndex("t1")));
                String str = cursor.getString(cursor.getColumnIndex("date"));
                String[]tmp=str.split("-");
                health.setDate(tmp[1]+"."+tmp[2]);
                vector.add(health);
            }
            cursor.close();
        }

        return vector;
    }

    public void closeDB(){
        db.close();
    }
}
