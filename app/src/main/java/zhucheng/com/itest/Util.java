package zhucheng.com.itest;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;

/**
 * Created by 罗爽 on 2018/5/23.
 */

public class Util {

    public static String FILE_NAME = "info";
    public static String CITY1 = "北京";
    public static String CITY2 = "beijing";
    public static String CITY3 = "BJ";
    public static String DRIVERL = "";
    public static String PLATE = "";
    public static int TR = -1;
    public static double SPEED = 30;
    public static double CARTMP = 25;

    //index               tmp:0  hi:1  hbp:2  lbp:3  tc:4  tg:5
    //指标简写
    public static String[]target = {"tmp","hi","hbp","lbp","tc","tg"};
    //指标名称
    public static String[]chs = {"体温","心率","高压","低压","总胆固醇","甘油三酯"};
    //指标单位
    public static String[]unit = {"℃","次/分钟","mmHg","mmHg","mmol/L","mmol/L"};
    //基本标准
    public static double[]std = {36.1,65,120,75,4.04,1.01};
    //求值标准
    public static String[][]fstd = {{"%.1f","%.2f"},{"%.0f","%.2f"},{"%.0f","%.2f"},{"%.0f","%.2f"},{"%.2f","%.3f"},{"%.2f","%.3f"}};
    //相邻斜率差
    public static double[]ds = {0.5,5,5,5,0.5,0.2};
    //斜率均值的正常范围
    public static double[][]mv = {{0.5,1},{5,10},{10,30},{5,10},{0.3,1},{0.2,0.3}};
    //斜率方差的临界值
    public static double[]sv = {0.5,30,30,10,0.5,0.1};
    //斜率的峰值
    public static double[]smax = {1,10,25,10,1,0.3};
    //正常取值
    public static double[][]range = {{36,37},{60,100},{115,136},{73,87},{2.85,5.17},{0.56,1.69}};
    //面积差（按时段分析专用）
    public static double[][]maxrange= {{35,43},{50,130},{115,136},{73,87},{2.3,6.5},{0.4,2}};
    public static double[]darea = {40,120,350,200,35,4};
    //折叠后的概述
    public static String[][]outline = new String[6][2];
    //具体的分析
    public static String[][]canalysis = new String[6][2];
    //症状
    public static String[]symptom = {"发生了发烧退烧等体温剧烈变化的过程","发生了情绪变化或疾病突发或剧烈运动","发生了头晕、头疼、身体乏力等症状","发生了头晕、头疼、身体乏力等症状","可能食用了热量较高的食物","可能饮食不健康"};
    //异常分析中的原因
    public static String[]wranning = {"您的身体状态较差,诸如感冒发言等引起了发烧的症状","您的身体状态较差、心态起伏或是疾病困扰,详细情况请咨询专业医生","您的身体状态较差,诸如产生了头疼、四肢乏力、头痛等症状","您的身体状态较差,诸如产生了头疼、四肢乏力、头痛等症状","您的身体状态较差,或是使用了高热量食品,超出正常值建议及时就医","您的身体状态较差,饮食不规律或生活习惯有问题,也可能是疾病如糖尿病所致,详细请咨询医生"};
    //日期的限制
    public static String[]datelimit = {"date('now','-1 month')","date('now','-7 day')"};
    //允许显示数据的最少条目
    public static int[]showlimit = {15,4};
    //制图并完成数据分析工作
    public static void intallView(final int code, int option, final int index, LinearLayout linear, Context activity, final ImageView img,final TextView c1)throws Exception{
        double temp1,temp2;
        //标准的读取
        String target1 = target[index];
        String ch = chs[index];
        String fstd1 = fstd[index][0];
        String fstd2 = fstd[index][1];
        double std1 = std[index];
        double dslope = ds[index];
        double mv1 = mv[index][0];//小
        double mv2 = mv[index][1];//大
        double sv1 = sv[index];
        double maxk = smax[index];
        double range1 = range[index][0];//小
        double range2 = range[index][1];//大
        double da = darea[index];
        if(option==0){
            Cursor c = null;
            List<BrokenLine> bl1 = new ArrayList<>();
            BrokenLine b = null;
            double max11 = 0;
            int hour11 = 0;
            double min11 = 500.0;
            int hour12 = 0;
            double max12 = 0;
            double min12 = 500.0;
            int hour13 = 0;
            int hour14 = 0;
            double maxk11 = 0;
            double maxk12 = 0;
            int qhour11 = 0;
            int qhour12 = 0;
            int qhour13 = 0;
            int qhour14 = 0;
            double ek11 = 0;
            double dk11 = 0;
            double ek12 = 0;
            double dk12 = 0;
            double area1 = 0;
            int flag1 = 0;
            int change1 = 0;
            StringBuffer sb1 = new StringBuffer();
            for(int i = 0;i<=24;i++){
                String sql = "select avg("+target1+") from health where rhour ="+i+" and date between "+datelimit[code]+" and date('now','-1 day')";
                c = RandomService.rawQuery(sql,null);
                if(c.moveToNext()){
                    temp1 = Double.parseDouble(String.format(fstd1,c.getDouble(c.getColumnIndex("avg("+target1+")"))));
                    if(!(temp1>=maxrange[index][0]&&temp1<=maxrange[index][1]))flag1+=1;
                }
                else {
                    flag1+=1;
                    temp1 = std1;
                }
                sql = "select "+target1+" from health where rhour = "+i+" and date = date('now')";
                c = RandomService.rawQuery(sql,null);
                if(c.moveToNext())temp2 = Double.parseDouble(String.format(fstd1,c.getDouble(c.getColumnIndex(target1))));
                else temp2 = std1;
                b = new BrokenLine(i+"点",temp1,temp2);
                if(temp1>max11){
                    max11 = temp1;
                    hour11 = i;
                }
                if(temp1<min11){
                    min11 = temp1;
                    hour12 = i;
                }
                if(temp2>max12){
                    max12 = temp2;
                    hour13 = i;
                }
                if(temp2<min12){
                    min12 = temp2;
                    hour14 = i;
                }
                bl1.add(b);
            }
            c.close();
            System.out.println(flag1+"这个值是多少?");
            if(flag1<=23){
                flag1 = 0;
                BrokenLineView blw1 = new BrokenLineView(activity);  //**这里有参数的改动
                blw1.setMdata(bl1);
                linear.addView(blw1);   //**这里有参数的改动

                for(int i = 1;i<=24;i++){
                    temp1 = bl1.get(i).getData()-bl1.get(i-1).getData();
                    temp2 = bl1.get(i).getData2()-bl1.get(i-1).getData2();
                    ek11+=temp1;
                    ek12+=temp2;
                    if(abs(temp1-temp2)<=dslope)flag1+=1;
                    if(abs(temp1)>maxk11){
                        maxk11 = abs(temp1);
                        qhour11 = i-1;
                        qhour12 = i;
                    }
                    if(abs(temp2)>maxk12){
                        maxk12 = abs(temp2);
                        qhour13 = i-1;
                        qhour14 = i;
                    }
                    area1+=((bl1.get(i).getData()+bl1.get(i-1).getData()-bl1.get(i).getData2()-bl1.get(i-1).getData2())/2);
                }
                ek11/=24;ek12/=24;
                ek11 = Double.parseDouble(String.format(fstd1,ek11));
                ek12= Double.parseDouble(String.format(fstd1,ek12));

                for(int i = 1;i<=24;i++){
                    temp1 = bl1.get(i).getData()-bl1.get(i-1).getData();
                    temp2 = bl1.get(i).getData2()-bl1.get(i-1).getData2();
                    dk11+=((temp1-ek11)*(temp1-ek11));
                    dk12+=((temp2-ek12)*(temp2-ek12));
                }
                dk11/=23;dk12/=23;
                dk11 = Double.parseDouble(String.format(fstd2,dk11));
                dk12= Double.parseDouble(String.format(fstd2,dk12));


                sb1.append("此表用来显示近30天内和当天每个时段"+ch+"的平均值,蓝色折线代表近30天的结果,红色代表当天的结果,部分缺少数据的时段为了不影响整体趋势的分析用正常数值替代。\n");
                sb1.append("近30天的日平均"+ch);if(ek11>=mv1&&ek11<=mv2) sb1.append("总体呈上升趋势,");else if(ek11<=-1*mv1&&ek11>=-1*mv2)sb1.append("总体呈下降趋势,");
                else if(ek11<mv1&&ek11>-1*mv1)sb1.append("基本不变");else sb1.append("变化幅度较大");sb1.append(",");
                if(dk11>sv1)sb1.append("前后时段变动幅度较大");else sb1.append("前后时段变化幅度较小");
                sb1.append(",其中变化幅度最大的时段集中在"+qhour11+"时到"+qhour12+"时");
                if(maxk11>=maxk)sb1.append(",这个变化比较剧烈,在该时段及其邻近时段可能"+symptom[index]);
                sb1.append(",平均峰值集中在"+hour11+"时和"+hour12+"时两个时段");
                if(max11>=range1&&max11<=range2&&min11>=range1&&min11<=range2){
                    sb1.append(",数值正常,说明近30天内"+ch+"处在一个正常的范围内。\n");
                    change1 = 2;
                }
                else{
                    sb1.append(",数值较正常人"+ch+"标准为异常,可能是某天某些时段身体状态不佳导致历史平均值异常。\n");
                    change1 = -1;
                }

                sb1.append("当天的"+ch);if(ek12>=mv1&&ek12<=mv2) sb1.append("总体呈上升趋势");else if(ek12<=-1*mv1&&ek12>=-1*mv2)sb1.append("总体呈下降趋势");
                else if(ek12<mv1&&ek12>-1*mv1)sb1.append("基本不变");else sb1.append("变化幅度较大");sb1.append(",");
                if(dk12>sv1)sb1.append("前后时段变动幅度较大");else sb1.append("前后时段变化幅度较小");
                sb1.append(",其中变化幅度最大的时段集中在"+qhour13+"时到"+qhour14+"时");
                if(maxk12>=maxk)sb1.append(",这个变化比较剧烈,在该时段及其邻近时段可能"+symptom[index]);
                sb1.append(",平均峰值集中在"+hour13+"时和"+hour14+"时两个时段");
                if(max12>=range1&&max12<=range2&&min12>=range1&&min12<=range2){
                    sb1.append(",数值正常,说明当天"+ch+"处在一个正常的范围内。\n");
                    change1+=1;
                }
                else {
                    sb1.append(",数值较正常人"+ch+"标准为异常,可能是某些时段身体状态不佳。\n");
                    change1-=1;
                }
                sb1.append("与前近30天的平均情况相比,");
                int f1 = 0;
                if(flag1>=21&&flag1<=24){
                    f1+=1;
                    sb1.append("走势基本相同,可见状态保持稳定,");
                }else if(flag1>=16&&flag1<=20){
                    f1+=2;
                    sb1.append("趋势大致相同,可见状态保持稳定,");
                }else{
                    f1+=3;
                    sb1.append("趋势不大相同,区别较大,身体状态可能需要调整,");
                }
                if(area1>-da&&area1<da){
                    f1+=4;
                    sb1.append(ch+"情况基本上相差不大");
                }else{
                    f1+=5;
                    sb1.append(ch+"情况差异较大");
                }
                if(f1<=6)sb1.append("。"+ch+"整体对比当天继续维持了近一个月的稳定情况。    ");
                else sb1.append("。"+ch+"整体对比当天的变化较历史平均值差异较大,属于异常,请您多注意自身身体情况。    ");
                if(change1==3)outline[index][code] = "当天"+ch+"情况与历史情况相比差异不大,情况良好。    ";
                else if(change1==1)outline[index][code] = "当天"+ch+"情况与历史情况相比情况有所恶化,情况不太理想。    ";
                else if(change1==0)outline[index][code] = "当天"+ch+"情况与历史情况相比情况有所好转,情况良好。     ";
                else if(change1==-2)outline[index][code] = "当天"+ch+"情况与历史情况相比情况差异不大,但是情况不理想。     ";
                canalysis[index][code] = sb1.toString();


                //测试
                Drawable down = activity.getResources().getDrawable(R.drawable.down);
                down.setBounds(0,0,50,50);
                Drawable up = activity.getResources().getDrawable(R.drawable.up);
                up.setBounds(0,0,50,50);
                ImageSpan imageSpan1 = new ImageSpan(down);
                ImageSpan imageSpan2 = new ImageSpan(up);

                final SpannableString spannableString1 = new SpannableString(outline[index][code]);
                spannableString1.setSpan(imageSpan1,outline[index][code].length()-2,outline[index][code].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                final SpannableString spannableString2 = new SpannableString(canalysis[index][code]);
                spannableString2.setSpan(imageSpan2,canalysis[index][code].length()-2,canalysis[index][code].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                c1.setText(spannableString1);

                c1.setOnClickListener(new View.OnClickListener() {
                    int flag = 0;
                    @Override
                    public void onClick(View v) {
                        if(flag==0){
                            flag = 1;
                            c1.setText(spannableString2);
                        }else{
                            flag = 0;
                            c1.setText(spannableString1);
                        }
                    }
                });
                img.setOnClickListener(new View.OnClickListener() {
                    int flag = 0;
                    @Override
                    public void onClick(View v) {
                        if(flag==0){
                            flag = 1;
                            RandomService.sound(canalysis[index][code]);
                        }else{
                            flag = 0;
                            RandomService.soundStop();
                        }
                    }
                });
            }
            else {
                sb1.append("鉴于您最近驾车时间较少,获取到的数据有限,无法提供较为准确的健康和趋势分析!");
                c1.setText(sb1.toString());
                img.setVisibility(View.GONE);
            }
        }
        if(option==1){
            Cursor c = null;
            List<BrokenLine>bl2 = new ArrayList<BrokenLine>();
            BrokenLine b = null;
            double max21 = 0;
            double min21 = 500.0;
            double maxk21 = 0;
            String qhour21 = null;
            String qhour22 = null;
            double ek21 = 0;
            double dk21 = 0;
            int f2 = 0;
            StringBuffer sb2 = new StringBuffer();
            String sql2 = "select avg("+target1+"),date from health group by date having date between "+datelimit[code]+" and date('now','-1 day')";
            c = RandomService.rawQuery(sql2,null);
            while(c.moveToNext()){
                String str = c.getString(c.getColumnIndex("date"));
                temp1 = Double.parseDouble(String.format(fstd1,c.getDouble(c.getColumnIndex("avg("+target1+")"))));
                String[]arr = str.split("-");
                if(temp1>max21)max21 = temp1;
                if(temp1<min21)min21 = temp1;
                b = new BrokenLine(arr[1]+"."+arr[2],temp1,0);
                bl2.add(b);
            }
            c.close();
            if(bl2.size()>showlimit[code]) {
                BrokenLineView blw2 = new BrokenLineView(activity);
                blw2.setMdata(bl2);
                linear.addView(blw2);
                for(int i = 1;i<bl2.size();i++){
                    temp1 = bl2.get(i).getData()-bl2.get(i-1).getData();
                    ek21+=temp1;
                    if(abs(temp1)>maxk21){
                        maxk21 = abs(temp1);
                        qhour21 = bl2.get(i-1).getDate();
                        qhour22 = bl2.get(i).getDate();
                    }
                }
                ek21/=(bl2.size()-1);
                ek21 = Double.parseDouble(String.format(fstd1,ek21));
                for(int i = 1;i<bl2.size();i++){
                    temp1 = bl2.get(i).getData()-bl2.get(i-1).getData()-ek21;
                    dk21+=(temp1*temp1);
                }
                dk21/=(bl2.size()-2);
                dk21 = Double.parseDouble(String.format(fstd2,dk21));
                sb2.append("对整体趋势进行分析：");
                //权值分支树
                if(ek21>=mv1&&ek21<=mv2){
                    f2+=1;
                    sb2.append("总体呈上升趋势,");
                }else if(ek21<=-1*mv1&&ek21>=-1*mv2){
                    f2+=1;
                    sb2.append("总体呈下降趋势,");
                }
                else if(ek21<mv1&&ek21>-1*mv1){
                    f2+=0;
                    sb2.append("基本不变,");
                }else {
                    f2+=3;
                    sb2.append("变化幅度较大,");
                }
                if(dk21>sv1){
                    f2+=4;
                    sb2.append("趋势不够稳定,前后变动较大,表明您的身体状况在这段时间可能内有较大起伏");
                }else {
                    f2+=0;
                    sb2.append("前后变化稳定,");
                }
                if(max21>=range1&&max21<=range2&&min21>=range1&&min21<=range2){
                    f2+=0;
                    sb2.append(ch+"维持在一个正常范围内");
                }else{
                    f2+=5;
                    sb2.append(ch+"边界异常,表明您的身体状态在某段时间内存在较大问题");
                }
                if(maxk21>=maxk){
                    f2+=6;
                    sb2.append(",其中在"+qhour21+"到"+qhour22+"这两天变化剧烈,表明您在邻近几天内有可能"+symptom[index]);
                }
                if(f2<=2)sb2.append(",总体上讲您最近的"+ch+"情况处于正常。");
                else sb2.append(",总体上讲您最近的"+ch+"情况不正常,建议您注意身体情况和生活习惯。");
                final String canalysistt = sb2.toString();
                c1.setText(canalysistt);
                img.setOnClickListener(new View.OnClickListener() {
                    int flag = 0;
                    @Override
                    public void onClick(View v) {
                        if(flag==0){
                            flag = 1;
                            RandomService.sound(canalysistt);
                        }else{
                            flag = 0;
                            RandomService.soundStop();
                        }
                    }
                });
            }else{
                c1.setText("鉴于您最近驾车时间较少,获取到的数据有限,无法提供较为准确的健康和趋势分析!");
                img.setVisibility(View.GONE);
            }
        }
        if(option==2){
            int flag1 = 0;
            TextView norc = null;
            Cursor c = null;
            String sql5 = "select * from normal where stand = '"+target1+"' and date between date('now','-7 day') and date('now','-1 day')";
            c = RandomService.rawQuery(sql5,null);
            while(c.moveToNext()){
                flag1 = 1;
                norc = new TextView(activity);
                norc.setText(c.getString(c.getColumnIndex("date"))+" "+c.getInt(c.getColumnIndex("hour"))+":"+c.getInt(c.getColumnIndex("minute"))+" "+target1+"异常 "+c.getDouble(c.getColumnIndex("value"))+unit[index]);
                linear.addView(norc);
            }
            c.close();
            if(flag1==1)c1.setText("引起"+target1+"异常的可能原因:1、对于部分零星的记录,车内传感器检测"+ch+"有误差；2、对于相对集中的异常记录,原因可能是因为"+wranning[index]+"。");
            else c1.setText("最近没有任何异常数据的记录,您的"+ch+"情况良好!");
        }

    }
    //数据报告文字的编辑
    public static String[] getReport(int index,int kind,Context context){
        //index代表指标索引，kind代表报告的时间范围
        StringBuffer sb1 = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        String str1 = null;
        String str2 = null;
        Cursor c = null;
        double temp = 0;  //临时变量
        String dateTmp = null; //临时变量
        double max,min,dx,ex;//最值范围，方差
        max = 0;min = 500;dx = 0;ex=0;
        String maxDate = null,minDate = null;
        int judge = 0;  //综评值
        List<Msg>healthList = new ArrayList<Msg>();
        Msg msg = null;
        String sql1 = "select avg("+target[index]+"),date from health group by date having date between "+datelimit[kind]+" and date('now','-1 day')";
        try {
            DBManager db = new DBManager(context);
            c = db.rawQuery(sql1,null);
            while(c.moveToNext()){
                msg = new Msg();
                temp = Double.parseDouble(String.format(fstd[index][0],c.getDouble(c.getColumnIndex("avg("+target[index]+")"))));
                dateTmp = c.getString(c.getColumnIndex("date"));
                String[]arr = dateTmp.split("-");
                msg.setDate(arr[1]+"-"+arr[2]);
                msg.setData(temp);
                if(temp>max){
                    max = temp;
                    maxDate = arr[1]+"-"+arr[2];
                }
                if(temp<min){
                    min = temp;
                    minDate = arr[1]+"-"+arr[2];
                }
                healthList.add(msg);
            }
            if(healthList.size()>showlimit[kind]){
                for(Msg m:healthList)ex+=m.getData();
                ex/=healthList.size();ex = Double.parseDouble(String.format(fstd[index][0],ex));
                for(Msg m:healthList)dx+=(m.getData()-ex)*(m.getData()-ex);
                dx/=(healthList.size()-1);dx = Double.parseDouble(String.format(fstd[index][1],dx));
                sb1.append(min+unit[index]+"-"+max+unit[index]);
                sb2.append("平均最高"+chs[index]+"出现在"+maxDate+"这天,为"+max+unit[index]+","+"平均最低"+chs[index]+"出现在"+minDate+"这天,为"+min+unit[index]+"。");
                if(max>=range[index][0]&&max<=range[index][1]&&min>=range[index][0]&&min<=range[index][1]){
                    judge+=2;
                    sb2.append(chs[index]+"在正常范围内变动,");
                }else{
                    judge+=4;
                    sb2.append(chs[index]+"在变动范围不正常,");
                }
                if(dx>sv[index]){
                    judge+=2;
                    sb2.append("前后几天变动幅度较大,不够稳定。");
                }else{
                    judge+=1;
                    sb2.append("前后几天变动幅度较为稳定。");
                }
                if(judge<=4)sb2.append("总体上"+chs[index]+"情况良好,希望您继续保持。\n");
                else sb2.append("总体上"+chs[index]+"情况不太好,最近几天可能"+symptom[index]+",建议及时就医。\n");
            }else{
                str1 = "未知范围";
                str2 = "健康数据较少或数据读取异常，无法为您提供准确的分析。";
            }
            str1 = sb1.toString();
            str2 = sb2.toString();
        } catch (Exception e) {
            e.printStackTrace();
            str1 = "未知范围";
            str2 = "健康数据较少或数据读取异常，无法为您提供准确的分析。";
        }finally{
            if(c!=null)c.close();
        }
        return new String[]{str1,str2};
    }
    public static void setCITY1(String city1){CITY1 = city1;}
    public static void setCITY2(String city2){CITY2 = city2;}
    public static void setCITY3(String city3){CITY3 = city3;}
    public static void setPLATE(String PLATE1){
        PLATE = PLATE1;
    }
    public static void setDRIVERL(String DRIVERL1){
        DRIVERL = DRIVERL;
    }
    public static void setSpeed(double speed1){
        SPEED = speed1;
    }
    public static void setCartmp(double cartmp1){
        CARTMP = cartmp1;
    }
    public static void setTR(int t){TR = t;}

}
