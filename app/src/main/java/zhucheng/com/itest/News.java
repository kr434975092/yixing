package zhucheng.com.itest;

import android.graphics.Bitmap;

/**
 * Created by 罗爽 on 2018/5/10.
 */

public class News {

    Bitmap logo;
    String title;
    String date;


    public Bitmap getLogo() {
        return logo;
    }

    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
