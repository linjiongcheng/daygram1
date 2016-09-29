package com.example.john.daygram;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChoosingAdapter extends BaseAdapter {
    private static final int TYPE_A = 0;            //代表该月
    private static final int TYPE_B = 1;            //代表不是该月

    private Context context;

    //整合数据
    private List<List<Data>> yearData = new ArrayList<>();
    private List<Data> monthData = new ArrayList<>();


    public ChoosingAdapter(Context context, ArrayList<List<Data>> a, ArrayList<Data> b) {
        this.context = context;
        //把数据装载同一个list里面
        yearData.addAll(a);
        monthData.addAll(b);
    }


    @Override
    public int getItemViewType(int position) {
        int result = 0;
        if (yearData.get(position).get(0).getMonth().equals(monthData.get(0).getMonth())) {
            result = TYPE_A;
        } else{
            result = TYPE_B;
        }
        return result;
    }

    //获得有多少中view type
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return yearData.size();
    }

    @Override
    public List<Data> getItem(int position) {
        return yearData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //创建两种不同种类的viewHolder变量
        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;
        //根据position获得View的type
        int type = getItemViewType(position);
        if (convertView == null) {
            //实例化
            holder1 = new ViewHolder1();
            holder2 = new ViewHolder2();
            //根据不同的type 来inflate不同的item layout
            //然后设置不同的tag
            //这里的tag设置是用的资源ID作为Key
            switch (type) {
                case TYPE_A:
                    convertView = View.inflate(context, R.layout.chosen_month, null);
                    holder1.month = (TextView) convertView.findViewById(R.id.chosen);
                    convertView.setTag(R.id.tag_chosen, holder1);
                    break;
                case TYPE_B:
                    convertView = View.inflate(context, R.layout.not_chosen_month, null);
//                    AbsListView.LayoutParams param1 = new AbsListView.LayoutParams(-1,100);
//                    convertView.setLayoutParams(param1);
                    holder2.month = (TextView) convertView.findViewById(R.id.not_chosen);
                    convertView.setTag(R.id.tag_not_chosen, holder2);
                    break;
            }
        } else {
            //根据不同的type来获得tag
            switch (type) {
                case TYPE_A:
                    holder1 = (ViewHolder1) convertView.getTag(R.id.tag_chosen);
                    break;
                case TYPE_B:
                    holder2 = (ViewHolder2) convertView.getTag(R.id.tag_not_chosen);
                    break;
            }
        }

        List<Data> o = yearData.get(position);
        //根据不同的type设置数据
        switch (type) {
            case TYPE_A:
                String A = o.get(0).getMonth().substring(0,3).toUpperCase();
                holder1.month.setText(A);
                break;

            case TYPE_B:
                String B = o.get(0).getMonth().substring(0,3).toUpperCase();
                holder2.month.setText(B);
                break;
        }
        return convertView;
    }

    private static class ViewHolder1 {
        TextView month;
    }

    private static class ViewHolder2 {
        TextView month;
    }
}