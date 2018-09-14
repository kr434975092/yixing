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

import java.util.regex.Pattern;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class register2 extends AppCompatActivity {

    private EditText height = null;
    private EditText weight = null;
    private EditText sex = null;
    private EditText age = null;
    private TextView btn1 = null;
    private TextView btn2 = null;
    String plate = null;
    String driverl = null;

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        height = (EditText)this.findViewById(R.id.height);
        weight = (EditText)this.findViewById(R.id.weight);
        sex = (EditText)this.findViewById(R.id.sex);
        age = (EditText)this.findViewById(R.id.age);
        btn1 = (TextView)this.findViewById(R.id.main_btn_register2);
        btn2 = (TextView)this.findViewById(R.id.log);
        plate = this.getIntent().getExtras().getString("plate");
        driverl = this.getIntent().getExtras().getString("driverl");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                Integer age1 = new Integer(age.getText().toString());
                Double weight1 = new Double(weight.getText().toString());
                Integer height1 = new Integer(height.getText().toString());
                String sex1 = sex.getText().toString();
                if(age1==null||age1.equals("")||weight1==null||weight1.equals("")
                        ||height1==null||height1.equals("")||sex1==null||sex1.equals("")||(!sex1.equals("男")&&!sex1.equals("女"))){
                    Toast.makeText(register2.this,"请完善注册信息!",Toast.LENGTH_LONG).show();
                }else{
                    User user = new User();
                    user.setPlate(plate);
                    user.setDriverl(driverl);
                    user.setAge(age1);
                    user.setHeight(height1);
                    user.setWeight(weight1);
                    user.setSex(sex1);
                    user.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e==null){
                                Toast.makeText(register2.this,"注册成功!返回登陆!",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(register2.this,login.class));
                                register2.this.finish();
                            }else{
                                Toast.makeText(register2.this,"注册失败!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }}catch(Exception ee){
                    Toast.makeText(register2.this,"注意格式!身高、体重、年龄要求均为整数",Toast.LENGTH_LONG).show();
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(register2.this,login.class));
                register2.this.finish();
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
                    register2.this.finish();
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
