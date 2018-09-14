package zhucheng.com.itest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by 罗爽 on 2018/5/6.
 */

public class HttpUtil {
    public static String doGet(String u,Map<String,String>querys1,Map<String,String>querys2){
        StringBuffer sb1 = new StringBuffer(u);
        String result = "";
        HttpURLConnection conn = null;
        BufferedReader in = null;
        try {
            //简单写一下即可
            if(querys2!=null){
            StringBuffer sb2 = new StringBuffer();
            for(Map.Entry<String,String>entry:querys2.entrySet()){
                if(sb2.length()>0)sb2.append("&");
                sb2.append(entry.getKey()+"="+entry.getValue());
            }
            sb1.append("?").append(sb2);}
            URL url = new URL(sb1.toString());
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            if(querys1!=null){
                for(Map.Entry<String,String>entry:querys1.entrySet())
                   conn.addRequestProperty(entry.getKey(),entry.getValue());

            }
            conn.setUseCaches(false);  //不使用缓存
            if(conn.getResponseCode()==200){
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while((line=in.readLine())!=null){
                    result+='\n'+line;
                }
            }else{
                Log.d("connect","请求失败"+conn.getResponseCode());
                result = "请求失败";
            }
        } catch (Exception e) {
            Log.d("error",e.getMessage());
            e.printStackTrace();
        }finally{
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(conn!=null){
                conn.disconnect();
            }
        }
        return result;
    }
    public static Bitmap getHttpBitmap(String url){
        URL urll;
        Bitmap bitmap = null;
        try {
            urll = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)urll.openConnection();
            conn.setConnectTimeout(6000);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
