package zhucheng.com.itest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 罗爽 on 2018/6/18.
 */

public class BreakRuleListAdapter extends BaseAdapter {
    private List<BR>list;
    private LayoutInflater layoutInflater;

    public BreakRuleListAdapter(Context context,List<BR>list){
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    public int getCount(){
        return list.size();
    }

    public BR getItem(int pos){
        return list.get(pos);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View View, ViewGroup parent){
        BRHolder brHolder = null;
        if(null==View){
            brHolder = new BRHolder();
            View = layoutInflater.inflate(R.layout.br,null);
            brHolder.time = (TextView)View.findViewById(R.id.time);
            brHolder.place = (TextView)View.findViewById(R.id.place);
            brHolder.accdient = (TextView)View.findViewById(R.id.accdient);
            brHolder.mark = (TextView)View.findViewById(R.id.mark);
            brHolder.money = (TextView)View.findViewById(R.id.money);
            View.setTag(brHolder);
        }else brHolder = (BRHolder)View.getTag();
        BR br = list.get(position);
        brHolder.time.setText(br.getTime());
        brHolder.place.setText(br.getPlace());
        brHolder.accdient.setText(br.getPlace());
        brHolder.mark.setText(br.getMark());
        brHolder.money.setText(br.getMoney());
        return View;
    }

    public final class BRHolder{
        public TextView time;
        public TextView place;
        public TextView accdient;
        public TextView mark;
        public TextView money;
    }

}
