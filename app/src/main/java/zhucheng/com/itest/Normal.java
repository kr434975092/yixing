package zhucheng.com.itest;

import cn.bmob.v3.BmobObject;

/**
 * Created by 罗爽 on 2018/5/23.
 */

public class Normal extends BmobObject {
    private String driverl;
    private Double value;
    private String stand;
    private String date;
    private Integer hour;
    private Integer minute;

    public String getDriverl() {
        return driverl;
    }

    public void setDriverl(String driverl) {
        this.driverl = driverl;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getStand() {
        return stand;
    }

    public void setStand(String stand) {
        this.stand = stand;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }
}
