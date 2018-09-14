package zhucheng.com.itest;

import java.util.Random;
import java.util.Vector;

/**
 * Created by 罗爽 on 2018/4/17.
 */

/*
*   public static String format5(double value) {

 return String.format("%.2f", value).toString();
}
*
* */

public class RandomUtil {
    private static Random random = new Random();
    public static double[]std = new double[]{36.1,75,120,75,4.04,1.01};
    public static void getRandom(){
        //对静态的std数组进行维护
        double rate = random.nextDouble();
        double trate = 0;
        double hrate = 0;
        double tc = 0;
        double tg = 0;
        double hbp = 0;
        double lbp = 0;
        if(rate<0.99)trate = random.nextDouble()+36;
        else trate = random.nextDouble()*5+37;
        std[0] = Double.parseDouble(String.format("%.1f",trate));
        rate = random.nextDouble();
        if(rate<0.99)hrate = random.nextInt(40)+60;
        else hrate = 120;   //有待修正
        std[1] = hrate;
        rate = random.nextDouble();
        if(rate<0.99)hbp = random.nextInt(22)+115;
        else hbp = 148;
        std[2] = hbp;
        rate = random.nextDouble();
        if(rate<0.99)lbp = random.nextInt(15)+73;
        else lbp = 100;
        std[3] = lbp;
        rate = random.nextDouble();
        if(rate<0.99)tc = random.nextDouble()*2.2+2.85;
        else tc = 6.5;
        std[4] = Double.parseDouble(String.format("%.2f",tc));
        rate = random.nextDouble();
        if(rate<0.99)tg = random.nextDouble()*1.09+0.6;
        else tg = 3;
        std[5] = Double.parseDouble(String.format("%.2f",tg));
        return;   //体温  心率  高压  低压  总胆固醇  甘油三酯
    }
    public void setStd(double[]std){
        this.std = std;
    }

}
