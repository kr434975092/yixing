package zhucheng.com.itest;

import cn.bmob.v3.BmobObject;

/**
 * Created by 罗爽 on 2018/4/18.
 */

public class Health extends BmobObject{
    private double tmp;
    //private double weight;
    private double hi;
    private double hbp;
    private double lbp;
    private double tc;
    private double tg;
    private String date;
    private int hour;
    private int rhour;
    private double mins;
    private String driverl;
    public double getTmp() {
        return tmp;
    }

    public void setTmp(double tmp) {
        this.tmp = tmp;
    }
    
    public double getHi() {
        return hi;
    }

    public void setHi(double hi) {
        this.hi = hi;
    }

    public double getHbp() {
        return hbp;
    }

    public void setHbp(double hbp) {
        this.hbp = hbp;
    }

    public double getLbp() {
        return lbp;
    }

    public void setLbp(double lbp) {
        this.lbp = lbp;
    }

    public double getTc() {
        return tc;
    }

    public void setTc(double tc) {
        this.tc = tc;
    }

    public double getTg() {
        return tg;
    }

    public void setTg(double tg) {
        this.tg = tg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getRhour() {
        return rhour;
    }

    public void setRhour(int rhour) {
        this.rhour = rhour;
    }

    public double getMins() {
        return mins;
    }

    public void setMins(double mins) {
        this.mins = mins;
    }

    public String getDriverl() {
        return driverl;
    }

    public void setDriverl(String driverl) {
        this.driverl = driverl;
    }
}
