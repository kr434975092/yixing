package zhucheng.com.itest;

import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by 罗爽 on 2018/6/16.
 */

public class MapUtil {
    //判断是否安装目标应用
    public static boolean inInstallByread(String packageName){
        return new File("/data/data/"+packageName).exists();
    }
    //使用高德地图
    public static void setUpAmpByName(Context context){
        try {
            //Intent intent = Intent.getIntent("androidamap://route?sourceApplication=softname&sname=我的位置&dlat="+LATITUDE_B+"&dlon="+LONGTITUDE_B+"&dname="+"东郡华城广场|A座"+"&dev=0&m=0&t=1");
            Intent intent = Intent.getIntent("androidamap://route?sourceApplication=softname&sname=我的位置&dev=0&m=0&t=1");
            context.startActivity(intent);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    //使用百度地图
    public static void setUpBaiduMapByName(Context context){
        try {
            Intent intent = Intent.getIntent("intent://map/direction?origin=我的位置&mode=driving&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            context.startActivity(intent);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
