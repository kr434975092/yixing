package zhucheng.com.itest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

public class login extends AppCompatActivity {

    private TextView btn1 = null;
    private TextView btn2 = null;
    private EditText driverl = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(this,ApiKey.BMOBKEY);
        btn1 = (TextView)this.findViewById(R.id.main_btn_login);
        btn2 = (TextView)this.findViewById(R.id.reg);
        driverl = (EditText)this.findViewById(R.id.driverl1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String d = driverl.getText().toString();

                if(d==null||d.equals("")){
                    Toast.makeText(login.this,"请填入内容!内容不能为空!",Toast.LENGTH_SHORT).show();
                }else{
                    String sql = "select * from User where driverl = '"+d+"'";
                    new BmobQuery<User>().doSQLQuery(sql, new SQLQueryListener<User>() {
                        @Override
                        public void done(BmobQueryResult<User> bmobQueryResult, BmobException e) {
                            Log.d("num",bmobQueryResult.getCount()+" ");
                            if(e==null){
                                List<User> list = (List<User>) bmobQueryResult.getResults();
                                if(list!=null&&list.size()==1){
                                    SharedPreferences infoPref = login.this.getSharedPreferences(Util.FILE_NAME, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = infoPref.edit();
                                    editor.putString("driverl",driverl.getText().toString());
                                    editor.commit();
                                    Util.DRIVERL = driverl.getText().toString();
                                    startActivity(new Intent(login.this,MainActivity.class));
                                    login.this.finish();
                                }else{
                                    Toast.makeText(login.this,"没有找到该车主!",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(login.this,"没有找到该车主!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this,register.class));
                login.this.finish();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            AlertDialog.Builder isExit = new AlertDialog.Builder(this);
            isExit.setIcon(R.drawable.ic_launcher_background);
            isExit.setTitle("易行提示");
            isExit.setMessage("确定要退出吗?");
            isExit.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    login.this.finish();
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

}
