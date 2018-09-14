package zhucheng.com.itest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 罗爽 on 2018/5/10.
 */

public class NewsListAdapter extends BaseAdapter {
    private List<News> newsList;
    private LayoutInflater layoutInflater;

    public NewsListAdapter(Context context, List<News>list){
        this.newsList = list;

        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    @Override
    public View getView(int position, View View, ViewGroup parent) {
        NewsHolder viewHolder = null;
        if(null==View){
            viewHolder = new NewsHolder();
            View = layoutInflater.inflate(R.layout.news,null);
            viewHolder.title = (TextView)View.findViewById(R.id.newstitle);
            viewHolder.date = (TextView)View.findViewById(R.id.newsdate);
            viewHolder.logo = (ImageView) View.findViewById(R.id.newslogo);
            View.setTag(viewHolder);
        }else viewHolder = (NewsHolder)View.getTag();
        News news = newsList.get(position);
        viewHolder.logo.setImageBitmap(news.logo);
        viewHolder.title.setText(news.title);
        viewHolder.date.setText(news.date);
        return View;
    }

    public final class NewsHolder{
        public ImageView logo;
        public TextView title;
        public TextView date;
    }

}
