package zhucheng.com.itest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 罗爽 on 2018/4/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "chezai";
    private static final int DATABASE_VERSION = 1;
    public DatabaseHelper(Context context) {

        //调用父类的构造函数,cursorFactory设置为null，使用默认值
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table if not exists health(tmp REAL,hi INTEGER,hbp INTEGER,lbp INTEGER,tc REAL,tg REAL,date TEXT,hour INTEGER,mins REAL,rhour INTEGER)");
        db.execSQL("create table if not exists normal(date TEXT,hour INTEGER,minute INTEGER,stand VARCHAR,value REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //db.execSQL("alter table health add column other string");
        db.execSQL("create table if not exists health(tmp REAL,hi INTEGER,hbp REAL,lbp REAL,tc REAL,tg REAL,date TEXT,hour INTEGER,mins REAL,rhour INTEGER)");
    }
}
