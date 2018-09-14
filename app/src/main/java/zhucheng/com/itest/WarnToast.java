package zhucheng.com.itest;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by 罗爽 on 2018/5/23.
 */

public class WarnToast extends Toast {
    private static View v;
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public WarnToast(Context context) {
        super(context);
    }
    public static WarnToast makeImage(Context context,Bitmap image,int duration){
        WarnToast result = new WarnToast(context);
        LayoutInflater inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflate.inflate(R.layout.warntoast,null);
        ImageView tv = (ImageView)v.findViewById(R.id.tv);
        tv.setImageBitmap(image);
        result.setView(v);
        result.setDuration(duration);
        return result;
    }


}
