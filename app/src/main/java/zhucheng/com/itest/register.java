package zhucheng.com.itest;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class register extends AppCompatActivity {

    private EditText plate = null;
    private EditText driverl = null;
    private TextView btn1 = null;
    private TextView btn2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn1 = (TextView)this.findViewById(R.id.main_btn_register1);
        btn2 = (TextView)this.findViewById(R.id.log);
        plate = (EditText)this.findViewById(R.id.carmark);
        driverl = (EditText)this.findViewById(R.id.drivermark);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p = plate.getText().toString();
                String d = driverl.getText().toString();
                if(p==null||d==null||p.equals("")||d.equals("")){
                    Toast.makeText(register.this,"请填入内容!内容不能为空!",Toast.LENGTH_SHORT).show();
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putString("plate",p);
                    bundle.putString("driverl",d);
                    Intent ahead = new Intent();
                    ahead.putExtras(bundle);
                    ahead.setClass(register.this,register2.class);
                    startActivity(ahead);
                    register.this.finish();
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(register.this,login.class));
                register.this.finish();
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
                    // List<Health>list = RandomService.query();
                    //   Log.d("listItemNum",""+list.size());
                    //RandomService.stop();

                    //RandomService.closeDB();
                    //Log.d("currentThread",Thread.currentThread().getName());
                    //Log.d("end","addNum:"+RandomService.addNum);
                    register.this.finish();
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
                    //  List<Health>list = RandomService.query();
                    //  Log.d("ItemNum",list.size()+"");
                }
            });
            isExit.create().show();
        }
        return false;
    }
}
