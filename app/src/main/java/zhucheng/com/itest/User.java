package zhucheng.com.itest;

import cn.bmob.v3.BmobObject;

/**
 * Created by 罗爽 on 2018/5/17.
 */

public class User extends BmobObject {
    private String plate;
    private String driverl;
    private Integer height;
    private Double weight;
    private String sex;
    private Integer age;

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getDriverl() {
        return driverl;
    }

    public void setDriverl(String driverl) {
        this.driverl = driverl;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
