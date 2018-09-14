package zhucheng.com.itest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

public class car extends AppCompatActivity {

    private TextView cartemp = null;
    private TextView speed = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        cartemp = (TextView)this.findViewById(R.id.cartmp);
        speed = (TextView)this.findViewById(R.id.speed);
        cartemp.setText(Util.CARTMP+"℃");
        speed.setText(Util.SPEED+"km/h");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){  //模块测试环节先这么写，到时候有了导航栏再改不迟
        if(keyCode==KeyEvent.KEYCODE_BACK)this.finish();
        return false;
    }

}
