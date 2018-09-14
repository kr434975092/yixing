package zhucheng.com.itest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by 罗爽 on 2018/6/14.
 */

public class ViewUtil {
    public static Bitmap shotScrollView(ScrollView scrollView){
        int h = 0;
        Bitmap bitmap = null;
        for(int i= 0;i<scrollView.getChildCount();i++){
            h+=scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
        }
        System.out.println(h+" "+scrollView.getWidth());
        bitmap = Bitmap.createBitmap(scrollView.getWidth(),h,Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    public static Bitmap getCacheBitmapFromView(View view) {
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;

        }
        return bitmap;
    }
    public static void layoutView(View v,int width,int height){
        v.layout(0,0,width,height);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width,View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height,View.MeasureSpec.EXACTLY);
        v.measure(measuredWidth,measuredHeight);
        v.layout(0,0,v.getMeasuredWidth(),v.getMeasuredHeight());
    }
    public static boolean saveBitmapToPhone2(Context context,Bitmap bitmap,String name){
        boolean result = false;
        String path = context.getExternalCacheDir()+"/健康报告/";
        System.out.println(path);
        File sd = new File(path);
        if(!sd.exists()){
            sd.mkdir();
        }
        File file = new File(path+name+".jpg");
        FileOutputStream fileOutputStream = null;
        if(!file.exists()){

            try {

            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                file.createNewFile();
                fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();

                MediaStore.Images.Media.insertImage(context.getContentResolver(),file.getAbsolutePath(),name+".jpg",null);
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                context.sendBroadcast(intent);
                result = true;
            }else result = false;
        } catch (Exception e) {
                e.printStackTrace();
                result = false;
        }
        }
        return result;
    }
    public static boolean saveBitmapToPhone1(Context context,Bitmap bitmap,String name){
        boolean result = false;
        String path = context.getFilesDir().getPath().toString()+"/健康报告/";
        File phone = new File(path);
        if(!phone.exists()){
            phone.mkdir();
        }
        File file = new File(path+name+".jpg");
        FileOutputStream fileOutputStream = null;
        File[] files;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            files = context.getExternalFilesDirs(Environment.MEDIA_MOUNTED);
            for(File temp:files){
                Log.e("main",temp.toString());
            }
        }
        if(!file.exists()){
            try {
                    file.createNewFile();
                    fileOutputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();

                    /*
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(file);
                    intent.setData(uri);
                    context.sendBroadcast(intent);
                    */

                    MediaStore.Images.Media.insertImage(context.getContentResolver(),file.getAbsolutePath(),name+".jpg",null);
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(new File(file.getPath()))));
                    result = true;
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }
        }
        return result;
    }

}
