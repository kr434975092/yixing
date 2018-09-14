package zhucheng.com.itest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * Created by 罗爽 on 2018/4/23.
 */

public class BrokenLineView extends View {
    private int width;  //图的宽度
    private int heigh;  //图的高度

    //网格的宽度和高度
    private int grid_w;
    private int grid_h;

    /**
     *
     * 颜色的修改到时候再说
     *
     *
     * */

    //底部空白的高度
    private int brokenline_bottom;
    //灰色背景的画笔
    private Paint mPaint_bg;
    //灰色网格的画笔
    private Paint mPaint_gridline;
    //文本数据的画笔
    private Paint mPaint_text;
    //折线圆点的蓝色背景
    private Paint mPaint_point_bg;
    //折线圆点的白色表面
    private Paint mPaint_point_sur;
    //折线上数据的画笔
    private Paint mPaint_dataT;

    private Paint mPaint_dataT1;
    /*
    //阴影路径的画笔
    private Paint mPaint_path;
    */
    //折线1的画笔
    private Paint mPaint_brokenline;
    //折线2的画笔
    private Paint mPaint_brokenline2;

    private double max = 0;
    private double min = 5000;

    /*
   //路径
    private Path mpath = new Path();
    */
    private List<BrokenLine>mdata;

    public BrokenLineView(Context context) {

        super(context);
        init(context);
    }

    public BrokenLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void Max(){
        double max1 = 0;
        for(BrokenLine h:mdata){
            double tmp1 = h.getData();
            double tmp2 = h.getData2();
            max1 = max1>=tmp1?max1:tmp1;
            max1 = max1>=tmp2?max1:tmp2;
        }
        max = Double.parseDouble(String.format("%.2f",max1*1.2));
    }

    public void Min(){
        double min1 = 5000;
        for(BrokenLine h:mdata){
            double tmp1 = h.getData();
            double tmp2 = h.getData2();
            if(tmp1!=0)min1 = min1<tmp1?min1:tmp1;
            if(tmp2!=0)min1 = min1<tmp2?min1:tmp2;
        }
        min = Double.parseDouble(String.format("%.2f",min1*0.5));
    }

    public void init(Context context){
        //先是画笔的初始化和颜色选择
        mPaint_bg = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_bg.setColor(Color.TRANSPARENT);

        mPaint_gridline = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_gridline.setColor(Color.argb(0xff,0xce,0xCB,0xce));

        mPaint_brokenline = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_brokenline.setColor(Color.BLUE);
        mPaint_brokenline.setTextSize(10);
        mPaint_brokenline.setStrokeWidth(10);
        mPaint_brokenline.setTextAlign(Paint.Align.CENTER);

        mPaint_brokenline2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_brokenline2.setColor(Color.RED);
        mPaint_brokenline2.setTextSize(10);
        mPaint_brokenline2.setStrokeWidth(10);
        mPaint_brokenline2.setTextAlign(Paint.Align.CENTER);

        mPaint_point_bg=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_point_bg.setColor(Color.argb(0xff, 0x91, 0xC8, 0xD6));

        /*
         //注意path的画笔的透明度已经改变了
        mPaint_path=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_path.setColor(Color.argb(0x33,0x91,0xC8,0xD6));
        */

        mPaint_point_sur = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_point_sur.setColor(Color.WHITE);

        mPaint_text=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_text.setColor(Color.BLACK);
        //mPaint_text.setTextAlign(Paint.Align.CENTER);
        mPaint_text.setTextSize(35);

        mPaint_dataT=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_dataT.setColor(Color.RED);
        //mPaint_dataT.setTextAlign(Paint.Align.CENTER);
        mPaint_dataT.setTextSize(35);

        mPaint_dataT1=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_dataT1.setColor(Color.BLUE);
        //mPaint_dataT.setTextAlign(Paint.Align.CENTER);
        mPaint_dataT1.setTextSize(35);

        invalidate();  //这个方法干嘛用的？
    }

    public List<BrokenLine>getMdata(){
        return mdata;
    }

    public void setMdata(List<BrokenLine>mdata){
        this.mdata = mdata;
        Max();
        Min();
        requestLayout();   //请求布局的意思么？不清楚
        invalidate();      //又调用了一次
    }

    protected void onDraw(Canvas canvas){
        //白色背景+灰色矩形+网格+圆点+数据文本

        super.onDraw(canvas);
        //绘制白色背景

        canvas.drawColor(Color.TRANSPARENT);
        //画灰色矩形区域
        canvas.drawRect(10,0,width,heigh-brokenline_bottom,mPaint_bg);

        //绘制网格线、横向的、Y轴不变 X轴绘制直线
        for(int j = 0;j<4;j++)  //这个j的值看看怎么调整
            canvas.drawLine(10,grid_h*(j+1),width,grid_h*(j+1),mPaint_gridline);

        for(int i = 0;i<mdata.size();++i){

            /*
            //开始时需要将path移动到要开始绘制的位置，否则默认从（0,0）开始，绘制path路径
            //if(i==0)mpath.moveTo(grid_w * i + 10, (float) (heigh - brokenline_bottom - (heigh - brokenline_bottom) * mdata.get(i).getData() / max));
            */

            //绘制纵向网格线
            canvas.drawLine(grid_w * i + 10, 0, grid_w * i + 10, heigh - brokenline_bottom, mPaint_gridline);

            if (i != mdata.size() - 1)
                //根据圆点位置绘制折线
                canvas.drawLine(grid_w * i + 10, (float) (heigh - brokenline_bottom - (heigh - brokenline_bottom) * (mdata.get(i).getData()-min) / (max-min)), grid_w * (i + 1) + 10, (float) (heigh - brokenline_bottom - (heigh - brokenline_bottom) * (mdata.get(i + 1).getData()-min) / (max-min)), mPaint_brokenline);
            //path的路径跟绘制的线的路径是相同的，因此path的起止点与线的起止点相同
            //mpath.quadTo(grid_w * i + 10,(float)(heigh - brokenline_bottom - (heigh - brokenline_bottom) * mdata.get(i).getData() / max),grid_w * (i + 1) + 10, (float)(heigh - brokenline_bottom - (heigh - brokenline_bottom) * mdata.get(i + 1).getData() / max));


            //绘制圆点，圆点位置根据网格线的位置确定
            canvas.drawCircle(grid_w * i + 10, (float) (heigh - brokenline_bottom - (heigh - brokenline_bottom) * (mdata.get(i).getData() -min)/(max-min)), 10, mPaint_point_bg);
            canvas.drawCircle(grid_w * i + 10, (float) (heigh - brokenline_bottom - (heigh - brokenline_bottom) * (mdata.get(i).getData()-min) / (max-min)), 5, mPaint_point_sur);

            if(mdata.get(i).getData2()!=0) {
                if (i != mdata.size() - 1)canvas.drawLine(grid_w * i + 10, (float) (heigh - brokenline_bottom - (heigh - brokenline_bottom) * (mdata.get(i).getData2()-min )/(max-min)), grid_w * (i + 1) + 10, (float) (heigh - brokenline_bottom - (heigh - brokenline_bottom) * (mdata.get(i + 1).getData2() -min)/ (max-min)), mPaint_brokenline2);
                canvas.drawCircle(grid_w * i + 10, (float) (heigh - brokenline_bottom - (heigh - brokenline_bottom) *( mdata.get(i).getData2()-min )/ (max-min)), 10, mPaint_point_bg);
                canvas.drawCircle(grid_w * i + 10, (float) (heigh - brokenline_bottom - (heigh - brokenline_bottom) * (mdata.get(i).getData2()-min )/ (max-min)), 5, mPaint_point_sur);
                String data1 = mdata.get(i).getData2() + "";

                String data = mdata.get(i).getData() + "";

                if(mdata.get(i).getData()>=mdata.get(i).getData2()){
                    canvas.drawText(data1, grid_w * i + 10, (float) (heigh - brokenline_bottom - (heigh - brokenline_bottom) * (mdata.get(i).getData2()-min)/(max-min) + 2*mPaint_brokenline.measureText(data1)), mPaint_dataT);
                    canvas.drawText(data, grid_w * i + 10, (float) (heigh - brokenline_bottom - (heigh - brokenline_bottom) * (mdata.get(i).getData()-min )/ (max-min) - mPaint_brokenline.measureText(data)), mPaint_dataT1);
                }else{
                    canvas.drawText(data1, grid_w * i + 10, (float) (heigh - brokenline_bottom - (heigh - brokenline_bottom) * (mdata.get(i).getData2()-min) /(max-min) - mPaint_brokenline.measureText(data1)), mPaint_dataT);
                    canvas.drawText(data, grid_w * i + 10, (float) (heigh - brokenline_bottom - (heigh - brokenline_bottom) * (mdata.get(i).getData()-min) / (max-min) + 2*mPaint_brokenline.measureText(data)), mPaint_dataT1);
                }
               //
               // Log.d("数据大小", (heigh - brokenline_bottom - (heigh - brokenline_bottom) * (mdata.get(i).getData()-min) / (max-min))+" "+ (heigh - brokenline_bottom - (heigh - brokenline_bottom) * (mdata.get(i).getData2()-min) / (max-min)));
            }else {
                //绘制数据的数量
                String data = mdata.get(i).getData() + "";
                canvas.drawText(data, grid_w * i + 10, (float) (heigh - brokenline_bottom - (heigh - brokenline_bottom) * (mdata.get(i).getData()-min) / (max-min)- mPaint_brokenline.measureText(data)), mPaint_dataT1);
            }
            //绘制底部空白处：数据的日期
            String date = mdata.get(i).getDate();
            canvas.drawText(date, grid_w * i + 10, heigh - brokenline_bottom / 2, mPaint_text);

        }

    }

    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){  //这里可以改为传入两个参数grid_h,grid_w函数的内层
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        grid_w = 100;
        if(mdata.size()==0)width = getDefaultSize(getSuggestedMinimumWidth(),widthMeasureSpec);
        else width = grid_w*mdata.size()+10;  //打出富裕,根据数据的条目设置宽度
        heigh = 450;
        brokenline_bottom = 50;   //可是适度调整
        grid_h = (heigh - brokenline_bottom)/4;
        setMeasuredDimension(width,heigh);
    }

}

