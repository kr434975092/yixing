package zhucheng.com.itest;

/**
 * Created by 罗爽 on 2018/4/23.
 */

public class BrokenLine {
    //拜访日期？到时候再改不迟
    private String date;
    //包含的数据一
    private double data;
    //包含的数据二
    private double data2;


    public BrokenLine(String date, double data,double data2) {
        this.date = date;
        this.data = data;
        this.data2 = data2;  //怎么就他妈搁了两个点
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }

    public double getData2() {
        return data2;
    }

    public void setData2(double data2) {
        this.data2 = data2;
    }


}
